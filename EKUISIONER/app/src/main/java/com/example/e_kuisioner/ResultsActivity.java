package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    private TextView userDetailTextView;
    private TextView tokopediaValueTextViewWarna;
    private TextView tokopediaValueTextViewNavigasi;
    private TextView shopeeValueTextViewWarna;
    private TextView shopeeValueTextViewNavigasi;
    private TextView allResultValueTokopedia;
    private TextView allResultValueShopee;
    private TextView allResults;
    BarChartView3 barChartView;
    BarChartView barChartViewAll;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        myDb = new DatabaseHelper(this);

        userDetailTextView = findViewById(R.id.user_detail_text_view);
        tokopediaValueTextViewWarna = findViewById(R.id.tokopedia_value_text_view_warna);
        tokopediaValueTextViewNavigasi = findViewById(R.id.tokopedia_value_text_view_navigasi);
        shopeeValueTextViewWarna = findViewById(R.id.shopee_value_text_view_warna);
        shopeeValueTextViewNavigasi = findViewById(R.id.shopee_value_text_view_navigasi);
        allResultValueTokopedia = findViewById(R.id.all_result_value_tokopedia);
        allResultValueShopee = findViewById(R.id.all_result_value_shopee);
        allResults = findViewById(R.id.all_results);
        barChartView = findViewById(R.id.bar_chart);
        barChartViewAll = findViewById(R.id.bar_chart_all);

        userId = getIntent().getStringExtra("USER_ID");

        Cursor userCursor = myDb.getDataById(userId);
        if (userCursor != null && userCursor.moveToFirst()) {
            @SuppressLint("Range") String name = userCursor.getString(userCursor.getColumnIndex("NAME"));
            @SuppressLint("Range") int age = userCursor.getInt(userCursor.getColumnIndex("AGE"));
            @SuppressLint("Range") String job = userCursor.getString(userCursor.getColumnIndex("JOB"));
            @SuppressLint("Range") String email = userCursor.getString(userCursor.getColumnIndex("EMAIL"));

            StringBuilder userDetails = new StringBuilder();
            userDetails.append("Nama        : ").append(name).append("\n");
            userDetails.append("Umur         : ").append(age).append("\n");
            userDetails.append("Pekerjaan : ").append(job).append("\n");
            userDetails.append("Email         : ").append(email).append("\n");

            userDetailTextView.setText(userDetails.toString());

        } else {
            Toast.makeText(this, "No user data available", Toast.LENGTH_LONG).show();
        }

        Cursor data = myDb.getQuestionnaireData(userId);

        if (data != null && data.moveToFirst()) {
            @SuppressLint("Range") int tokopediaWarnaValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_3));
            @SuppressLint("Range") int tokopediaNavigasiValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_4));
            @SuppressLint("Range") int shopeeWarnaValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
            @SuppressLint("Range") int shopeeNavigasiValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));
            @SuppressLint("Range") int tokopediaWarnaPercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_7));
            @SuppressLint("Range") int tokopediaNavigasiPercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_8));
            @SuppressLint("Range") int shopeeWarnaPercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_9));
            @SuppressLint("Range") int shopeeNavigasiPercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_10));
            @SuppressLint("Range") int tokopediaValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_11));
            @SuppressLint("Range") int shopeeValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_12));
            @SuppressLint("Range") int tokopediaPercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_13));
            @SuppressLint("Range") int shopeePercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_14));
            

            tokopediaValueTextViewWarna.setText("Tokopedia Warna Value: " + tokopediaWarnaValue + " (" + tokopediaWarnaPercentage + "%)");
            tokopediaValueTextViewNavigasi.setText("Tokopedia Navigasi Value: " + tokopediaNavigasiValue + " (" + tokopediaNavigasiPercentage + "%)");
            shopeeValueTextViewWarna.setText("Shopee Warna Value: " + shopeeWarnaValue + " (" + shopeeWarnaPercentage + "%)");
            shopeeValueTextViewNavigasi.setText("Shopee Navigasi Value: " + shopeeNavigasiValue + " (" + shopeeNavigasiPercentage + "%)");

            allResultValueTokopedia.setText("Tokopedia All Value: " + tokopediaValue + " (" + tokopediaPercentage + "%)");
            allResultValueShopee.setText("Shopee All Value: " + shopeeValue + " (" + shopeePercentage + "%)");

            if(tokopediaValue > shopeeValue){
                allResults.setText("Tokopedia Lebih Baik Dari Pada Shopee");
            }else if(tokopediaValue < shopeeValue){
                allResults.setText("Shopee Lebih Baik Dari Pada Tokopedia");
            }else if(tokopediaValue == shopeeValue) {
                allResults.setText("Tokopedia & Shopee Setara");
            }

            barChartViewAll.setPercentages(tokopediaValue, tokopediaPercentage, shopeeValue, shopeePercentage);
            barChartView.setPercentages(tokopediaWarnaValue, tokopediaNavigasiValue, tokopediaWarnaPercentage, tokopediaNavigasiPercentage,shopeeWarnaValue,shopeeNavigasiValue,shopeeWarnaPercentage,shopeeNavigasiPercentage);

            data.close();
        }
    }
}
