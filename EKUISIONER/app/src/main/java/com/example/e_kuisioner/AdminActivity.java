package com.example.e_kuisioner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private ListView userListView;
    private ArrayAdapter<String> userAdapter;
    private List<String> userIdList;
    private List<String> userDisplayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        myDb = new DatabaseHelper(this);
        userListView = findViewById(R.id.user_list_view);

        userIdList = new ArrayList<>();
        userDisplayList = new ArrayList<>();

        // Load all non-admin users into the ListView
        loadAllNonAdminUsers();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click to view user details
                String userId = userIdList.get(position);
                Intent intent = new Intent(AdminActivity.this, UserDetailActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
    }

    private void loadAllNonAdminUsers() {
        Cursor cursor = myDb.getAllNonAdminUsers();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No users found", Toast.LENGTH_LONG).show();
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
}
