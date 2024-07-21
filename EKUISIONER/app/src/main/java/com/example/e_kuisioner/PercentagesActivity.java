package com.example.e_kuisioner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PercentagesActivity extends AppCompatActivity {

    private TextView totalTokopediaPercentageTextView;
    private TextView totalShopeePercentageTextView;
    private BarChartView totalBarChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentages);

        totalTokopediaPercentageTextView = findViewById(R.id.total_tokopedia_percentage);
        totalShopeePercentageTextView = findViewById(R.id.total_shopee_percentage);
        totalBarChartView = findViewById(R.id.total_bar_chart);

        // Retrieve data from Intent
        Intent intent = getIntent();
        int tokopediaValue = intent.getIntExtra("TOKOPEDIA_VALUE", 0);
        int tokopediaPercentage = intent.getIntExtra("TOKOPEDIA_PERCENTAGE", 0);
        int shopeeValue = intent.getIntExtra("SHOPEE_VALUE", 0);
        int shopeePercentage = intent.getIntExtra("SHOPEE_PERCENTAGE", 0);

        // Set data to views
        totalTokopediaPercentageTextView.setText("Tokopedia Average Percentage: " + tokopediaPercentage + "%");
        totalShopeePercentageTextView.setText("Shopee Average Percentage: " + shopeePercentage + "%");
        totalBarChartView.setPercentages(tokopediaValue, tokopediaPercentage, shopeeValue, shopeePercentage);
    }
}
