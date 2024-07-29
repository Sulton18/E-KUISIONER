package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PercentagesActivity extends AppCompatActivity {

    private BarChartView3 totalBarChartViewAll;
    private DatabaseHelper myDb;

    private TextView tokopediaValueTextViewWarna;
    private TextView tokopediaValueTextViewNavigasi;
    private TextView shopeeValueTextViewWarna;
    private TextView shopeeValueTextViewNavigasi;
    private TextView allResults;
    private TextView totalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentages);

        tokopediaValueTextViewWarna = findViewById(R.id.tokopedia_value_text_view_warna);
        tokopediaValueTextViewNavigasi = findViewById(R.id.tokopedia_value_text_view_navigasi);
        shopeeValueTextViewWarna = findViewById(R.id.shopee_value_text_view_warna);
        shopeeValueTextViewNavigasi = findViewById(R.id.shopee_value_text_view_navigasi);

        allResults = findViewById(R.id.all_results);
        totalUser = findViewById(R.id.all_peruser);

        totalBarChartViewAll = findViewById(R.id.total_bar_chart);
        myDb = new DatabaseHelper(this);

        loadAllUserPercentages();
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
            int totalTokopediaWarnaPersen = (int) ((float) totalTokopediaWarnaValue / totalUsers * 100 / 25);
            int totalTokopediaNavigasiPersen = (int) ((float) totalTokopediaNavigasiValue / totalUsers * 100 / 25);
            int totalShopeeWarnaPersen = (int) ((float) totalShopeeWarnaValue / totalUsers * 100 / 25);
            int totalShopeeNavigasiPersen = (int) ((float) totalShopeeNavigasiValue / totalUsers * 100 / 25);

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
            tokopediaValueTextViewWarna.setText("Tokopedia Warna Value: " + totalTokopediaWarnaValue + " (" + totalTokopediaWarnaPersen + "%)");
            tokopediaValueTextViewNavigasi.setText("Tokopedia Navigasi Value: " + totalTokopediaNavigasiValue + " (" + totalTokopediaNavigasiPersen + "%)");
            shopeeValueTextViewWarna.setText("Shopee Warna Value: " + totalShopeeWarnaValue + " (" + totalShopeeWarnaPersen + "%)");
            shopeeValueTextViewNavigasi.setText("Shopee Navigasi Value: " + totalShopeeNavigasiValue + " (" + totalShopeeNavigasiPersen + "%)");

            totalBarChartViewAll.setPercentages(totalTokopediaWarnaValue, totalTokopediaNavigasiValue, totalTokopediaWarnaPersen, totalTokopediaNavigasiPersen, totalShopeeWarnaValue, totalShopeeNavigasiValue, totalShopeeWarnaPersen, totalShopeeNavigasiPersen);
        } else {
            Toast.makeText(this, "No valid questionnaire data found", Toast.LENGTH_LONG).show();
        }

        cursor.close();
    }
}
