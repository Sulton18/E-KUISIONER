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
    private DatabaseHelper myDb;
    private List<UserPercentage> userPercentages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentages);

        totalBarChartView = findViewById(R.id.total_bar_chart);
        myDb = new DatabaseHelper(this);
        userPercentages = new ArrayList<>();

        loadAllUserPercentages();

        // Set data to BarChartView
        totalBarChartView.setUserPercentages(userPercentages);
    }

    private void loadAllUserPercentages() {
        Cursor cursor = myDb.getAllQuestionnaireData(); // Retrieve data for all users

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No questionnaire data found", Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_2));
            @SuppressLint("Range") int tokopediaPercentage = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
            @SuppressLint("Range") int shopeePercentage = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));

            // Retrieve user name from user_table based on userId
            Cursor userCursor = myDb.getDataById(userId);
            String userName = "";
            if (userCursor.moveToFirst()) {
                @SuppressLint("Range") String name = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COL_4));
                userName = name;
            }
            userCursor.close();

            userPercentages.add(new UserPercentage(userId, userName, tokopediaPercentage, shopeePercentage));
        }
        cursor.close();
    }
}


class UserPercentage {
    String userId;
    String userName; // Added field
    int tokopediaPercentage;
    int shopeePercentage;

    UserPercentage(String userId, String userName, int tokopediaPercentage, int shopeePercentage) {
        this.userId = userId;
        this.userName = userName; // Initialize userName
        this.tokopediaPercentage = tokopediaPercentage;
        this.shopeePercentage = shopeePercentage;
    }
}

