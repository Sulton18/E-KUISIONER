package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TextView nama,age,job,tokopediaResults, shopeeResults,allResults;
    BarChartView barChartView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        myDb = new DatabaseHelper(this);

        tokopediaResults = findViewById(R.id.tokopedia_results);
        shopeeResults = findViewById(R.id.shopee_results);
        allResults = findViewById(R.id.all_results);
        nama = findViewById(R.id.nama);
        age = findViewById(R.id.age);
        job = findViewById(R.id.job);
        barChartView = findViewById(R.id.bar_chart);

        userId = getIntent().getStringExtra("USER_ID");

        Cursor userCursor = myDb.getDataById(userId);
        if (userCursor != null && userCursor.moveToFirst()) {
            @SuppressLint("Range") String name = userCursor.getString(userCursor.getColumnIndex("NAME"));
            @SuppressLint("Range") int age2 = userCursor.getInt(userCursor.getColumnIndex("AGE"));
            @SuppressLint("Range") String job2 = userCursor.getString(userCursor.getColumnIndex("JOB"));
            @SuppressLint("Range") String email = userCursor.getString(userCursor.getColumnIndex("EMAIL"));

            nama.setText("Nama        : " + name);
            age.setText("Umur         : " + age2);
            job.setText("Pekerjaan : " + job2);

        } else {
            Toast.makeText(this, "No user data available", Toast.LENGTH_LONG).show();
        }

        Cursor data = myDb.getQuestionnaireData(userId);

        if (data != null && data.moveToFirst()) {
            @SuppressLint("Range") int tokopediaValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_3));
            @SuppressLint("Range") int shopeeValue = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_4));
            @SuppressLint("Range") int tokopediaPercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
            @SuppressLint("Range") int shopeePercentage = data.getInt(data.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));


            tokopediaResults.setText("Tokopedia: " + tokopediaValue +"/50 " + "("+tokopediaPercentage + "%)");
            shopeeResults.setText("Shopee   : " + shopeeValue+"/50 " +"("+shopeePercentage + "%)");
            if(tokopediaValue > shopeeValue){
                allResults.setText("Tokopedia Lebih Baik Dari Pada Shopee");
            }else if(tokopediaValue < shopeeValue){
                allResults.setText("Tokopedia Lebih Baik Dari Pada Shopee");
            }else if(tokopediaValue == shopeeValue){
                allResults.setText("Tokopedia & Shopee Setara");
            }

            barChartView.setPercentages(tokopediaValue, tokopediaPercentage, shopeeValue, shopeePercentage);

            data.close();
        }
    }
}
