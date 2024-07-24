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
    private TextView tokopediaValueTextViewWarna2;
    private TextView tokopediaValueTextViewNavigasi2;
    private TextView shopeeValueTextViewWarna2;
    private TextView shopeeValueTextViewNavigasi2;
    private TextView allResults;
    private TextView allResultsUser;
    private TextView totalUser;
    BarChartView3 barChartView;
    BarChartView3 barChartViewAll;
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
        tokopediaValueTextViewWarna2 = findViewById(R.id.tokopedia_value_text_view_warna2);
        tokopediaValueTextViewNavigasi2 = findViewById(R.id.tokopedia_value_text_view_navigasi2);
        shopeeValueTextViewWarna2 = findViewById(R.id.shopee_value_text_view_warna2);
        shopeeValueTextViewNavigasi2 = findViewById(R.id.shopee_value_text_view_navigasi2);
        allResults = findViewById(R.id.all_results);
        allResultsUser = findViewById(R.id.all_results_user);
        totalUser = findViewById(R.id.total_user);
        barChartView = findViewById(R.id.bar_chart);
        barChartViewAll = findViewById(R.id.bar_chart_all);

        userId = getIntent().getStringExtra("USER_ID");

        loadAllUserPercentages();

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
            

            tokopediaValueTextViewWarna.setText("Tokopedia Warna Value: " + tokopediaWarnaValue + " (" + tokopediaWarnaPercentage + "%)");
            tokopediaValueTextViewNavigasi.setText("Tokopedia Navigasi Value: " + tokopediaNavigasiValue + " (" + tokopediaNavigasiPercentage + "%)");
            shopeeValueTextViewWarna.setText("Shopee Warna Value: " + shopeeWarnaValue + " (" + shopeeWarnaPercentage + "%)");
            shopeeValueTextViewNavigasi.setText("Shopee Navigasi Value: " + shopeeNavigasiValue + " (" + shopeeNavigasiPercentage + "%)");

            if((tokopediaWarnaValue > shopeeWarnaValue)&&(tokopediaNavigasiValue > shopeeNavigasiValue)){
                allResultsUser.setText("Tokopedia Lebih Unggul Dari Pada Shopee Dari Segi Warna Maupun Navigasi");
            }else if((tokopediaWarnaValue < shopeeWarnaValue)&&(tokopediaNavigasiValue < shopeeNavigasiValue)){
                allResultsUser.setText("Shopee Lebih Unggul Dari Pada Tokopedia Dari Segi Warna Maupun Navigasi");
            }else if((tokopediaWarnaValue > shopeeWarnaValue)&&(tokopediaNavigasiValue < shopeeNavigasiValue)) {
                allResultsUser.setText("Tokopedia Lebih Unggul Dari Pada Shopee Dari Segi Warna Sedangkan Pada Segi Navigasi Shopee Lebih Unggul Dari Pada Tokopedia");
            }else if((tokopediaWarnaValue < shopeeWarnaValue)&&(tokopediaNavigasiValue > shopeeNavigasiValue)) {
                allResultsUser.setText("Shopee Lebih Unggul Dari Pada Tokopedia Dari Segi Warna Sedangkan Pada Segi Navigasi Tokopedia Lebih Unggul Dari Pada Shopee");
            }else if((tokopediaWarnaValue == shopeeWarnaValue)&&(tokopediaNavigasiValue > shopeeNavigasiValue)) {
                allResultsUser.setText("Shopee & Tokopedia Dari Segi Warna Setara Sedangkan Pada Segi Navigasi Tokopedia Lebih Unggul Dari Pada Shopee");
            }else if((tokopediaWarnaValue == shopeeWarnaValue)&&(tokopediaNavigasiValue < shopeeNavigasiValue)) {
                allResultsUser.setText("Shopee & Tokopedia Dari Segi Warna Setara Sedangkan Pada Segi Navigasi Shopee Lebih Unggul Dari Pada Tokopedia");
            }else if((tokopediaWarnaValue > shopeeWarnaValue)&&(tokopediaNavigasiValue == shopeeNavigasiValue)) {
                allResultsUser.setText("Shopee & Tokopedia Dari Segi Navigasi Setara Sedangkan Pada Segi Warna Tokopedia Lebih Unggul Dari Pada Shopee");
            }else if((tokopediaWarnaValue < shopeeWarnaValue)&&(tokopediaNavigasiValue == shopeeNavigasiValue)) {
                allResultsUser.setText("Shopee & Tokopedia Dari Segi Navigasi Setara Sedangkan Pada Segi Warna Shopee Lebih Unggul Dari Pada Tokopedia");
            }else if((tokopediaWarnaValue == shopeeWarnaValue)&&(tokopediaNavigasiValue == shopeeNavigasiValue)) {
                allResultsUser.setText("Shopee & Tokopedia Setara Dari Segi Warna Maupun Navigasi");
            }

            barChartView.setPercentages(tokopediaWarnaValue, tokopediaNavigasiValue, tokopediaWarnaPercentage, tokopediaNavigasiPercentage,shopeeWarnaValue,shopeeNavigasiValue,shopeeWarnaPercentage,shopeeNavigasiPercentage);

            data.close();
        }
    }

    private void loadAllUserPercentages() {
        Cursor cursor = myDb.getAllQuestionnaireData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No questionnaire data found", Toast.LENGTH_LONG).show();
            return;
        }

        int totalUsers = 0;
        int totalTokopediaWarnaValue = 0;
        int totalTokopediaNavigasiValue = 0;
        int totalShopeeWarnaValue = 0;
        int totalShopeeNavigasiValue = 0;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int tokopediaWarnaValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_3));
            @SuppressLint("Range") int tokopediaNavigasiValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_4));
            @SuppressLint("Range") int shopeeWarnaValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
            @SuppressLint("Range") int shopeeNavigasiValue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));

            totalUsers++;
            totalTokopediaWarnaValue += tokopediaWarnaValue;
            totalTokopediaNavigasiValue += tokopediaNavigasiValue;
            totalShopeeWarnaValue += shopeeWarnaValue;
            totalShopeeNavigasiValue += shopeeNavigasiValue;
        }

        if (totalUsers > 0) {
            int totalTokopediaWarnaPersen = (totalTokopediaWarnaValue / totalUsers) * 100 / 25;
            int totalTokopediaNavigasiPersen = (totalTokopediaNavigasiValue / totalUsers) * 100 / 25;
            int totalShopeeWarnaPersen = (totalShopeeWarnaValue / totalUsers) * 100 / 25;
            int totalShopeeNavigasiPersen = (totalShopeeNavigasiValue / totalUsers) * 100 / 25;

            if((totalTokopediaWarnaValue > totalShopeeWarnaValue)&&(totalTokopediaNavigasiValue > totalShopeeNavigasiValue)){
                allResults.setText("Tokopedia Lebih Unggul Dari Pada Shopee Dari Segi Warna Maupun Navigasi");
            }else if((totalTokopediaWarnaValue < totalShopeeWarnaValue)&&(totalTokopediaNavigasiValue < totalShopeeNavigasiValue)){
                allResults.setText("Shopee Lebih Unggul Dari Pada Tokopedia Dari Segi Warna Maupun Navigasi");
            }else if((totalTokopediaWarnaValue > totalShopeeWarnaValue)&&(totalTokopediaNavigasiValue < totalShopeeNavigasiValue)) {
                allResults.setText("Tokopedia Lebih Unggul Dari Pada Shopee Dari Segi Warna Sedangkan Pada Segi Navigasi Shopee Lebih Unggul Dari Pada Tokopedia");
            }else if((totalTokopediaWarnaValue < totalShopeeWarnaValue)&&(totalTokopediaNavigasiValue > totalShopeeNavigasiValue)) {
                allResults.setText("Shopee Lebih Unggul Dari Pada Tokopedia Dari Segi Warna Sedangkan Pada Segi Navigasi Tokopedia Lebih Unggul Dari Pada Shopee");
            }else if((totalTokopediaWarnaValue == totalShopeeWarnaValue)&&(totalTokopediaNavigasiValue > totalShopeeNavigasiValue)) {
                allResults.setText("Shopee & Tokopedia Dari Segi Warna Setara Sedangkan Pada Segi Navigasi Tokopedia Lebih Unggul Dari Pada Shopee");
            }else if((totalTokopediaWarnaValue == totalShopeeWarnaValue)&&(totalTokopediaNavigasiValue < totalShopeeNavigasiValue)) {
                allResults.setText("Shopee & Tokopedia Dari Segi Warna Setara Sedangkan Pada Segi Navigasi Shopee Lebih Unggul Dari Pada Tokopedia");
            }else if((totalTokopediaWarnaValue > totalShopeeWarnaValue)&&(totalTokopediaNavigasiPersen == totalShopeeNavigasiPersen)) {
                allResults.setText("Shopee & Tokopedia Dari Segi Navigasi Setara Sedangkan Pada Segi Warna Tokopedia Lebih Unggul Dari Pada Shopee");
            }else if((totalTokopediaWarnaValue < totalShopeeWarnaValue)&&(totalTokopediaNavigasiPersen == totalShopeeNavigasiPersen)) {
                allResults.setText("Shopee & Tokopedia Dari Segi Navigasi Setara Sedangkan Pada Segi Warna Shopee Lebih Unggul Dari Pada Tokopedia");
            }else if((totalTokopediaWarnaValue == totalShopeeWarnaValue)&&(totalTokopediaNavigasiPersen == totalShopeeNavigasiPersen)) {
                allResults.setText("Shopee & Tokopedia Setara Dari Segi Warna Maupun Navigasi");
            }

            totalUser.setText("Total User : "+totalUsers);
            tokopediaValueTextViewWarna2.setText("Tokopedia Warna Value: " + totalTokopediaWarnaValue + " (" + totalTokopediaWarnaPersen + "%)");
            tokopediaValueTextViewNavigasi2.setText("Tokopedia Navigasi Value: " + totalTokopediaNavigasiValue + " (" + totalTokopediaNavigasiPersen + "%)");
            shopeeValueTextViewWarna2.setText("Shopee Warna Value: " + totalShopeeWarnaValue + " (" + totalShopeeWarnaPersen + "%)");
            shopeeValueTextViewNavigasi2.setText("Shopee Navigasi Value: " + totalShopeeNavigasiValue + " (" + totalShopeeNavigasiPersen + "%)");

            barChartViewAll.setPercentages(totalTokopediaWarnaValue, totalTokopediaNavigasiValue, totalTokopediaWarnaPersen, totalTokopediaNavigasiPersen, totalShopeeWarnaValue, totalShopeeNavigasiValue, totalShopeeWarnaPersen, totalShopeeNavigasiPersen);
        } else {
            Toast.makeText(this, "No valid questionnaire data found", Toast.LENGTH_LONG).show();
        }

        cursor.close();
    }
}
