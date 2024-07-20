package com.example.e_kuisioner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionnaireActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    // Initialize RadioGroups for questions
    RadioGroup tokopediaColorGroups[];
    RadioGroup tokopediaNavGroups[];
    RadioGroup shopeeColorGroups[];
    RadioGroup shopeeNavGroups[];

    Button submitButton;
    Button nextButton;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        myDb = new DatabaseHelper(this);

        // Retrieve the user ID from the intent
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize RadioGroups for questions
        tokopediaColorGroups = new RadioGroup[]{
                findViewById(R.id.tokopedia_color_1),
                findViewById(R.id.tokopedia_color_2),
                findViewById(R.id.tokopedia_color_3),
                findViewById(R.id.tokopedia_color_4),
                findViewById(R.id.tokopedia_color_5)
        };

        tokopediaNavGroups = new RadioGroup[]{
                findViewById(R.id.tokopedia_nav_1),
                findViewById(R.id.tokopedia_nav_2),
                findViewById(R.id.tokopedia_nav_3),
                findViewById(R.id.tokopedia_nav_4),
                findViewById(R.id.tokopedia_nav_5)
        };

        shopeeColorGroups = new RadioGroup[]{
                findViewById(R.id.shopee_color_1),
                findViewById(R.id.shopee_color_2),
                findViewById(R.id.shopee_color_3),
                findViewById(R.id.shopee_color_4),
                findViewById(R.id.shopee_color_5)
        };

        shopeeNavGroups = new RadioGroup[]{
                findViewById(R.id.shopee_nav_1),
                findViewById(R.id.shopee_nav_2),
                findViewById(R.id.shopee_nav_3),
                findViewById(R.id.shopee_nav_4),
                findViewById(R.id.shopee_nav_5)
        };

        submitButton = findViewById(R.id.submit_all);
        View tokopediaSection = findViewById(R.id.tokopedia_section);
        View shopeeSection = findViewById(R.id.shopee_section);
        nextButton = findViewById(R.id.next_button);

        // Hide Shopee section initially
        shopeeSection.setVisibility(View.GONE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide Tokopedia section and show Shopee section
                tokopediaSection.setVisibility(View.GONE);
                shopeeSection.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get answers from each section
                int tokopediaColorTotal = getTotalCheckedValue(tokopediaColorGroups);
                int tokopediaNavTotal = getTotalCheckedValue(tokopediaNavGroups);
                int shopeeColorTotal = getTotalCheckedValue(shopeeColorGroups);
                int shopeeNavTotal = getTotalCheckedValue(shopeeNavGroups);

                // Check if all answers are valid
                if (tokopediaColorTotal == -1 || tokopediaNavTotal == -1 || shopeeColorTotal == -1 || shopeeNavTotal == -1) {
                    Toast.makeText(QuestionnaireActivity.this, "Please answer all questions", Toast.LENGTH_LONG).show();
                    return;
                }

                int tokopediaNilai = tokopediaColorTotal + tokopediaNavTotal;
                int shopeeNilai = shopeeColorTotal + shopeeNavTotal;

                // Save data to database
                boolean isInserted = myDb.insertQuestionnaireData(
                        userId, // Use the actual user ID
                        tokopediaNilai,
                        shopeeNilai
                );

                if (isInserted) {
                    Toast.makeText(QuestionnaireActivity.this, "Data saved successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(QuestionnaireActivity.this, ResultsActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(QuestionnaireActivity.this, "Data saving failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private int getTotalCheckedValue(RadioGroup[] groups) {
        int totalValue = 0;

        for (RadioGroup group : groups) {
            int checkedRadioButtonId = group.getCheckedRadioButtonId();
            if (checkedRadioButtonId == -1) {
                return -1; // Indicate error
            }

            View radioButton = group.findViewById(checkedRadioButtonId);
            int idx = group.indexOfChild(radioButton);
            totalValue += idx + 1;
        }

        return totalValue;
    }
}
