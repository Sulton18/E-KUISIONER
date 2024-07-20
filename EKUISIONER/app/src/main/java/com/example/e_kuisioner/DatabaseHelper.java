package com.example.e_kuisioner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and table names
    public static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "EMAIL";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "NAME";
    public static final String COL_5 = "AGE";
    public static final String COL_6 = "JOB";
    public static final String COL_7 = "USER_TYPE"; // New column for user type (e.g., 'admin' or 'user')
    public static final String COL_8 = "IS_APPROVED";

    public static final String QUESTIONNAIRE_TABLE_NAME = "questionnaire_table";
    public static final String QUESTIONNAIRE_COL_1 = "ID";
    public static final String QUESTIONNAIRE_COL_2 = "USER_ID";
    public static final String QUESTIONNAIRE_COL_3 = "TOKOPEDIA_VALUE";
    public static final String QUESTIONNAIRE_COL_4 = "SHOPEE_VALUE";
    public static final String QUESTIONNAIRE_COL_5 = "TOKOPEDIA_PERCENTAGE";
    public static final String QUESTIONNAIRE_COL_6 = "SHOPEE_PERCENTAGE";

    private static final int DATABASE_VERSION = 4; // Incremented version

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user_table with the new USER_TYPE column
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "EMAIL TEXT, PASSWORD TEXT, NAME TEXT, AGE INTEGER, JOB TEXT, USER_TYPE TEXT, IS_APPROVED INTEGER)");

        // Create questionnaire_table
        db.execSQL("CREATE TABLE " + QUESTIONNAIRE_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USER_ID TEXT, TOKOPEDIA_VALUE INTEGER, SHOPEE_VALUE INTEGER, TOKOPEDIA_PERCENTAGE INTEGER, SHOPEE_PERCENTAGE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            // Add USER_TYPE column if it does not exist
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN USER_TYPE TEXT");
        }
    }

    public boolean insertData(String email, String password, String name, int age, String job, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, email);
        contentValues.put(COL_3, password);
        contentValues.put(COL_4, name);
        contentValues.put(COL_5, age);
        contentValues.put(COL_6, job);
        contentValues.put(COL_7, userType); // 'user' or 'admin'

        // Set IS_APPROVED to 1 for admin users, otherwise default to 0
        if ("admin".equals(userType)) {
            contentValues.put(COL_8, 1);
        } else {
            contentValues.put(COL_8, 0);
        }

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean approveUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8, 1); // Set as approved
        long result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{userId});
        return result > 0;
    }

    public Cursor getAllNonAdminUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USER_TYPE != 'admin'", null);
    }


    public Cursor getData(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE EMAIL = ? AND PASSWORD = ?", new String[]{email, password});
    }

    public boolean insertQuestionnaireData(String userId, int tokopediaValue, int shopeeValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        float tokopediaPercentage = calculatePercentage(tokopediaValue);
        float shopeePercentage = calculatePercentage(shopeeValue);

        contentValues.put(QUESTIONNAIRE_COL_2, userId);
        contentValues.put(QUESTIONNAIRE_COL_3, tokopediaValue);
        contentValues.put(QUESTIONNAIRE_COL_4, shopeeValue);
        contentValues.put(QUESTIONNAIRE_COL_5, tokopediaPercentage);
        contentValues.put(QUESTIONNAIRE_COL_6, shopeePercentage);

        long result = db.insert(QUESTIONNAIRE_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    private float calculatePercentage(int value) {
        return ((value / 50.0f) * 100);
    }

    public Cursor getQuestionnaireData(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + QUESTIONNAIRE_TABLE_NAME + " WHERE USER_ID = ?", new String[]{userId});
    }

    public Cursor getDataById(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
    }

}
