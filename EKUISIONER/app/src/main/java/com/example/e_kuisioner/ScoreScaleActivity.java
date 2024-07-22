package com.example.e_kuisioner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreScaleActivity extends AppCompatActivity {

    Button nextButton;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_scale);

        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "";
        }

        nextButton = findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreScaleActivity.this, QuestionnaireActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        });
    }
}
