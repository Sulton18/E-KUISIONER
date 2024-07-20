package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText email, password;
    Button loginButton, registerPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerPageButton = findViewById(R.id.register_page_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Email dan Password tidak boleh kosong", Toast.LENGTH_LONG).show();
                    return;
                }

                Cursor res = myDb.getData(emailStr, passwordStr);
                if (res.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Login Gagal", Toast.LENGTH_LONG).show();
                } else {
                    res.moveToFirst();
                    @SuppressLint("Range") String userType = res.getString(res.getColumnIndex("USER_TYPE"));
                    @SuppressLint("Range") int isApproved = res.getInt(res.getColumnIndex("IS_APPROVED"));
                    @SuppressLint("Range") String userId = res.getString(res.getColumnIndex("ID")); // Retrieve user ID

                    Cursor userCursor = myDb.getQuestionnaireData(userId);
                    Log.d("UserDetailActivity", "Questionnaire data found for user ID: " + userCursor.getCount());
                    if (isApproved == 0) {
                        Toast.makeText(MainActivity.this, "Akun belum disetujui", Toast.LENGTH_LONG).show();
                    } else if (userType.equals("admin")) {
                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (userCursor == null || userCursor.getCount() == 0){
                        Intent intent = new Intent(MainActivity.this, QuestionnaireActivity.class);
                        intent.putExtra("USER_ID", userId);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(MainActivity.this,ResultsActivity.class);
                        intent.putExtra("USER_ID", userId);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        registerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
