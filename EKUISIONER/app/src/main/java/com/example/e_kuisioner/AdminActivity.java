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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        myDb = new DatabaseHelper(this);
        userListView = findViewById(R.id.user_list_view);
        approveButton = findViewById(R.id.approve_button);

        userIdList = new ArrayList<>();
        userDisplayList = new ArrayList<>();

        loadAllNonAdminUsers();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        int userNumber = 1;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_1));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_4));
            @SuppressLint("Range") int isApproved = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_8));

            String approvalStatus = isApproved == 1 ? "(Approved)" : "(Not Approved)";
            userDisplayList.add(userNumber + ". " + email + " " + approvalStatus);
            userIdList.add(userId);
            userNumber++;
        }
        cursor.close();

        userAdapter = new ArrayAdapter<>(this, R.layout.list_view, R.id.text_view_item, userDisplayList);
        userListView.setAdapter(userAdapter);
    }


    private void showOverallPercentages() {
        Cursor cursor = myDb.getAllQuestionnaireData();

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

        Intent intent = new Intent(AdminActivity.this, PercentagesActivity.class);
        intent.putExtra("TOKOPEDIA_VALUE", totalTokopediaValue);
        intent.putExtra("TOKOPEDIA_PERCENTAGE", totalTokopediaPercentage);
        intent.putExtra("SHOPEE_VALUE", totalShopeeValue);
        intent.putExtra("SHOPEE_PERCENTAGE", totalShopeePercentage);
        startActivity(intent);
    }

}
