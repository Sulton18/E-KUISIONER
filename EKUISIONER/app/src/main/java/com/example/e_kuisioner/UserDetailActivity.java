package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetailActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private TextView userDetailTextView;
    private TextView tokopediaValueTextView;
    private TextView shopeeValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        myDb = new DatabaseHelper(this);
        userDetailTextView = findViewById(R.id.user_detail_text_view);
        tokopediaValueTextView = findViewById(R.id.tokopedia_value_text_view);
        shopeeValueTextView = findViewById(R.id.shopee_value_text_view);

        String userId = getIntent().getStringExtra("USER_ID");
        if (userId != null) {
            displayUserDetails(userId);
        } else {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_LONG).show();
        }
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
        if (questionnaireCursor.getCount() == 0) {
            Toast.makeText(this, "No questionnaire data available for this user", Toast.LENGTH_LONG).show();
            questionnaireCursor.close();
            return;
        }

        StringBuilder questionnaireData = new StringBuilder();
        while (questionnaireCursor.moveToNext()) {
            @SuppressLint("Range") int tokopediaValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex("TOKOPEDIA_VALUE"));
            @SuppressLint("Range") int shopeeValue = questionnaireCursor.getInt(questionnaireCursor.getColumnIndex("SHOPEE_VALUE"));
            questionnaireData.append("Tokopedia Value: ").append(tokopediaValue).append("\n");
            questionnaireData.append("Shopee Value: ").append(shopeeValue).append("\n");
        }
        questionnaireCursor.close();

        tokopediaValueTextView.setText(questionnaireData.toString());
        shopeeValueTextView.setText(questionnaireData.toString());
    }
}
