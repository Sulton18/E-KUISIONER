package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PercentagesActivity extends AppCompatActivity {

    private BarChartView2 totalBarChartView;
    private BarChartView totalBarChartViewAll;
    private DatabaseHelper myDb;
    private List<UserPercentage> userPercentages;
    private int totalUsers;
    private int totalTokopediaValue;
    private int totalShopeeValue;
    private int totalTokopediaPercentage;
    private int totalShopeePercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentages);

        totalBarChartView = findViewById(R.id.total_bar_chart);
        totalBarChartViewAll = findViewById(R.id.total_bar_chart_all);
        myDb = new DatabaseHelper(this);
        userPercentages = new ArrayList<>();
        totalUsers = 0;
        totalTokopediaValue = 0;
        totalShopeeValue = 0;
        totalTokopediaPercentage = 0;
        totalShopeePercentage = 0;

        loadAllUserPercentages();

        totalBarChartView.setUserPercentages(userPercentages);
    }

    private void loadAllUserPercentages() {
        Cursor cursor = myDb.getAllQuestionnaireData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No questionnaire data found", Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_2));
            @SuppressLint("Range") int tokopediaValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_11));
            @SuppressLint("Range") int shopeeValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_12));
            @SuppressLint("Range") int tokopediaPercentage = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_13));
            @SuppressLint("Range") int shopeePercentage = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_14));

            Cursor userCursor = myDb.getDataById(userId);
            String userName = "";
            if (userCursor.moveToFirst()) {
                @SuppressLint("Range") String name = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COL_4));
                userName = name;
            }
            userCursor.close();

            userPercentages.add(new UserPercentage(userId, userName, tokopediaPercentage, shopeePercentage));

            totalUsers++;
            totalTokopediaPercentage += tokopediaPercentage;
            totalShopeePercentage += shopeePercentage;
            totalTokopediaValue += tokopediaValue;
            totalShopeeValue += shopeeValue;
        }
        totalBarChartViewAll.setPercentages(totalTokopediaValue, totalTokopediaPercentage/totalUsers, totalShopeeValue, totalShopeePercentage/totalUsers);
        cursor.close();
    }

    private void displayTotals() {
        // Display or use the totalUsers, totalTokopediaPercentage, and totalShopeePercentage as needed.
        // For example:
        Toast.makeText(this, "Total Users: " + totalUsers, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Total Tokopedia Percentage: " + totalTokopediaPercentage, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Total Shopee Percentage: " + totalShopeePercentage, Toast.LENGTH_LONG).show();
    }
}


class UserPercentage {
    String userId;
    String userName;
    int tokopediaPercentage;
    int shopeePercentage;

    UserPercentage(String userId, String userName, int tokopediaPercentage, int shopeePercentage) {
        this.userId = userId;
        this.userName = userName;
        this.tokopediaPercentage = tokopediaPercentage;
        this.shopeePercentage = shopeePercentage;
    }
}
