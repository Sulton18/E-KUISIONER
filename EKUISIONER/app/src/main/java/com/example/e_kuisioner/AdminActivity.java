package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private ListView userListView;
    private ArrayAdapter<String> userAdapter;
    private List<String> userIdList;
    private List<String> userDisplayList;
    private Button approveButton;
    private LinearLayout percentagesContainer;
    private TextView totalTokopediaPercentageTextView;
    private TextView totalShopeePercentageTextView;
    private BarChartView totalBarChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views and DatabaseHelper
        myDb = new DatabaseHelper(this);
        userListView = findViewById(R.id.user_list_view);
        approveButton = findViewById(R.id.approve_button);
        percentagesContainer = findViewById(R.id.percentages_container);
        totalTokopediaPercentageTextView = findViewById(R.id.total_tokopedia_percentage);
        totalShopeePercentageTextView = findViewById(R.id.total_shopee_percentage);
        totalBarChartView = findViewById(R.id.total_bar_chart);

        userIdList = new ArrayList<>();
        userDisplayList = new ArrayList<>();

        // Load all non-admin users into the ListView
        loadAllNonAdminUsers();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click to view user details
                String userId = userIdList.get(position);
                Intent intent = new Intent(AdminActivity.this, UserDetailActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverallPercentages();
            }
        });
    }

    private void loadAllNonAdminUsers() {
        Cursor cursor = myDb.getAllNonAdminUsers();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No users found", Toast.LENGTH_LONG).show();
            return;
        }

        userDisplayList.clear();
        userIdList.clear();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_1));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_2));
            userDisplayList.add(email);
            userIdList.add(userId);
        }
        cursor.close();

        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDisplayList);
        userListView.setAdapter(userAdapter);
    }

    private void showOverallPercentages() {
        Cursor cursor = myDb.getAllQuestionnaireData(); // Retrieve data for all users

        int totalTokopediaValue = 0;
        int totalShopeeValue = 0;
        int totalTokopediaPercentage = 0;
        int totalShopeePercentage = 0;
        int count = 0;

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No questionnaire data found", Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int tokopediaValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_3));
            @SuppressLint("Range") int shopeeValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_4));
            @SuppressLint("Range") int tokopediaPct = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
            @SuppressLint("Range") int shopeePct = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));

            totalTokopediaValue += tokopediaValue;
            totalShopeeValue += shopeeValue;
            totalTokopediaPercentage += tokopediaPct;
            totalShopeePercentage += shopeePct;
            count++;
        }
        cursor.close();

        if (count > 0) {
            totalTokopediaPercentage /= count;
            totalShopeePercentage /= count;
        } else {
            totalTokopediaPercentage = 0;
            totalShopeePercentage = 0;
        }

        // Start PercentagesActivity
        Intent intent = new Intent(AdminActivity.this, PercentagesActivity.class);
        intent.putExtra("TOKOPEDIA_VALUE", totalTokopediaValue);
        intent.putExtra("TOKOPEDIA_PERCENTAGE", totalTokopediaPercentage);
        intent.putExtra("SHOPEE_VALUE", totalShopeeValue);
        intent.putExtra("SHOPEE_PERCENTAGE", totalShopeePercentage);
        startActivity(intent);
    }

}
