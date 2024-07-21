package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TextView resultsSummary, tokopediaResults, shopeeResults;
    BarChartView barChartView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        myDb = new DatabaseHelper(this);

        resultsSummary = findViewById(R.id.results_summary);
        tokopediaResults = findViewById(R.id.tokopedia_results);
        shopeeResults = findViewById(R.id.shopee_results);
        barChartView = findViewById(R.id.bar_chart);

        // Retrieve data from the database
        userId = getIntent().getStringExtra("USER_ID");
        Cursor data = myDb.getQuestionnaireData(userId);

        if (data != null && data.moveToFirst()) {
            // Get values from cursor
            @SuppressLint("Range") int tokopediaValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_3));
            @SuppressLint("Range") int shopeeValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_4));
            @SuppressLint("Range") int tokopediaPercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
            @SuppressLint("Range") int shopeePercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));

            // Set text views
            tokopediaResults.setText("Tokopedia: " + tokopediaValue + " (" + tokopediaPercentage + "%)");
            shopeeResults.setText("Shopee: " + shopeeValue + " (" + shopeePercentage + "%)");

            // Calculate summary
            resultsSummary.setText("Tokopedia: " + tokopediaPercentage + "%\nShopee: " + shopeePercentage + "%");

            // Set data in bar chart
            barChartView.setPercentages(tokopediaValue, tokopediaPercentage, shopeeValue, shopeePercentage);

            data.close();
        }
    }
}
