package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetailActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private TextView userDetailTextView;
    private TextView tokopediaValueTextView;
    private TextView shopeeValueTextView;
    private Button approveButton;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        myDb = new DatabaseHelper(this);
        userDetailTextView = findViewById(R.id.user_detail_text_view);
        tokopediaValueTextView = findViewById(R.id.tokopedia_value_text_view);
        shopeeValueTextView = findViewById(R.id.shopee_value_text_view);
        approveButton = findViewById(R.id.approve_button);

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
    }

    private void displayUserDetails(String userId) {
        Cursor userCursor = myDb.getDataById(userId);
        if (userCursor.getCount() == 0) {
            Toast.makeText(this, "No user data available", Toast.LENGTH_LONG).show();
            userCursor.close();
            return;
        }

        if (userCursor.moveToFirst()) {
            @SuppressLint("Range") String name = userCursor.getString(userCursor.getColumnIndex("NAME"));
            @SuppressLint("Range") int age = userCursor.getInt(userCursor.getColumnIndex("AGE"));
            @SuppressLint("Range") String job = userCursor.getString(userCursor.getColumnIndex("JOB"));
            @SuppressLint("Range") String email = userCursor.getString(userCursor.getColumnIndex("EMAIL"));

            StringBuilder userDetails = new StringBuilder();
            userDetails.append("Name: ").append(name).append("\n");
            userDetails.append("Age: ").append(age).append("\n");
            userDetails.append("Job: ").append(job).append("\n");
            userDetails.append("Email: ").append(email).append("\n");

            userDetailTextView.setText(userDetails.toString());
        }
        userCursor.close();

        Cursor questionnaireCursor = myDb.getQuestionnaireData(userId);
        if (questionnaireCursor == null || questionnaireCursor.getCount() == 0) {
            Toast.makeText(this, "No questionnaire data available for this user", Toast.LENGTH_LONG).show();
            if (questionnaireCursor != null) {
                questionnaireCursor.close();
            }
            return;
        }

        if (questionnaireCursor.moveToFirst()) {
            @SuppressLint("Range") int tokopediaValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_3));
            @SuppressLint("Range") int shopeeValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_4));
            @SuppressLint("Range") int tokopediaPercentage = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_5));
            @SuppressLint("Range") int shopeePercentage = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex(DatabaseHelper.QUESTIONNAIRE_COL_6));

            Log.d("UserDetailActivity", "Tokopedia Value: " + tokopediaValue);
            Log.d("UserDetailActivity", "Shopee Value: " + shopeeValue);
            Log.d("UserDetailActivity", "Tokopedia Percentage: " + tokopediaPercentage);
            Log.d("UserDetailActivity", "Shopee Percentage: " + shopeePercentage);

            tokopediaValueTextView.setText("Tokopedia Value: " + tokopediaValue + " (" + tokopediaPercentage + "%)");
            shopeeValueTextView.setText("Shopee Value: " + shopeeValue + " (" + shopeePercentage + "%)");

            questionnaireCursor.close();
        }
    }

    private void approveUser() {
        boolean isApproved = myDb.approveUser(userId);
        if (isApproved) {
            Toast.makeText(this, "User approved", Toast.LENGTH_LONG).show();
            approveButton.setEnabled(false); // Disable button after approval
        } else {
            Toast.makeText(this, "Approval failed", Toast.LENGTH_LONG).show();
        }
    }
}
