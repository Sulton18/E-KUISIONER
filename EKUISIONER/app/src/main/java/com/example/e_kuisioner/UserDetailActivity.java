package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserDetailActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
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
    private Button approveButton;
    private Button editButton;
    private Button deleteButton;
    private BarChartView3 barChartView;
    private BarChartView3 barChartViewAll;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

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
        allResultsUser = findViewById(R.id.all_results_user);
        allResults = findViewById(R.id.all_results);
        totalUser = findViewById(R.id.total_user);
        approveButton = findViewById(R.id.approve_button);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        barChartView = findViewById(R.id.bar_chart);
        barChartViewAll = findViewById(R.id.bar_chart_all);

        userId = getIntent().getStringExtra("USER_ID");
        if (userId != null) {
            displayUserDetails(userId);
        } else {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_LONG).show();
        }

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveUser();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailActivity.this, EditUserActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteUser();
            }
        });

        loadAllUserPercentages();
    }

    private void displayUserDetails(String userId) {
        Cursor userCursor = null;
        Cursor questionnaireCursor = null;
        try {
            userCursor = myDb.getDataById(userId);
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

                @SuppressLint("Range") int isApproved = userCursor.getInt(userCursor.getColumnIndex("IS_APPROVED"));
                if (isApproved == 1) {
                    approveButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                } else {
                    approveButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "No user data available", Toast.LENGTH_LONG).show();
            }

            questionnaireCursor = myDb.getQuestionnaireData(userId);
            if (questionnaireCursor != null && questionnaireCursor.moveToFirst()) {
                @SuppressLint("Range") int tokopediaWarnaValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_3));
                @SuppressLint("Range") int tokopediaNavigasiValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_4));
                @SuppressLint("Range") int shopeeWarnaValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
                @SuppressLint("Range") int shopeeNavigasiValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));
                @SuppressLint("Range") int tokopediaWarnaPercentage = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_7));
                @SuppressLint("Range") int tokopediaNavigasiPercentage = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_8));
                @SuppressLint("Range") int shopeeWarnaPercentage = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_9));
                @SuppressLint("Range") int shopeeNavigasiPercentage = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_10));
                @SuppressLint("Range") int tokopediaValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_11));
                @SuppressLint("Range") int shopeeValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_12));


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
            } else {
                Toast.makeText(this, "No questionnaire data available for this user", Toast.LENGTH_LONG).show();
            }
        } finally {
            if (userCursor != null && !userCursor.isClosed()) {
                userCursor.close();
            }
            if (questionnaireCursor != null && !questionnaireCursor.isClosed()) {
                questionnaireCursor.close();
            }
        }
    }

    private void approveUser() {
        boolean isApproved = myDb.approveUser(userId);
        if (isApproved) {
            Toast.makeText(this, "User approved", Toast.LENGTH_LONG).show();
            approveButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Approval failed", Toast.LENGTH_LONG).show();
        }
    }

    private void confirmDeleteUser() {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUser() {
        boolean isDeleted = myDb.deleteUser(userId);
        if (isDeleted) {
            Toast.makeText(this, "User deleted", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Deletion failed", Toast.LENGTH_LONG).show();
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
