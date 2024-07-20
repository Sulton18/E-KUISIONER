package com.example.e_kuisioner;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private ListView userListView;
    private Button approveButton;
    private ArrayAdapter<String> userAdapter;
    private List<String> userIdList;
    private List<String> userDisplayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        myDb = new DatabaseHelper(this);
        userListView = findViewById(R.id.user_list_view);
        approveButton = findViewById(R.id.approve_button);

        userIdList = new ArrayList<>();
        userDisplayList = new ArrayList<>();

        // Load unapproved users into the ListView
        loadUnapprovedUsers();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click to view user details or questionnaires
                String userId = userIdList.get(position);
                Intent intent = new Intent(AdminActivity.this, UserDetailActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveSelectedUser();
            }
        });
    }

    private void loadUnapprovedUsers() {
        Cursor cursor = myDb.getUnapprovedUsers();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No unapproved users", Toast.LENGTH_LONG).show();
            return;
        }

        userDisplayList.clear();
        userIdList.clear();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
            userDisplayList.add(email);
            userIdList.add(userId);
        }
        cursor.close();

        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDisplayList);
        userListView.setAdapter(userAdapter);
    }

    private void approveSelectedUser() {
        int position = userListView.getCheckedItemPosition();
        if (position == ListView.INVALID_POSITION) {
            Toast.makeText(this, "Please select a user to approve", Toast.LENGTH_LONG).show();
            return;
        }

        String userId = userIdList.get(position);
        boolean isApproved = myDb.approveUser(userId);
        if (isApproved) {
            Toast.makeText(this, "User approved", Toast.LENGTH_LONG).show();
            loadUnapprovedUsers(); // Refresh the list
        } else {
            Toast.makeText(this, "Approval failed", Toast.LENGTH_LONG).show();
        }
    }
}

