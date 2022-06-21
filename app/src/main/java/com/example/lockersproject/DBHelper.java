package com.example.lockersproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper {

    public static final String TABLE_USERS = "Users";
    public static final String KEY_ID = "UserID";
    public static final String KEY_PW = "Password";
    public static final String KEY_NAME = "UserName";
    public static final String KEY_PHONE = "Phone";
    public static final String KEY_Locker = "LockerNo";
    public static final String KEY_EndDate = "EndDate";

    public static final String TABLE_LOCKERS = "Lockers";
    public static final String KEY_LockerNO = "LockerNo";
    public static final String KEY_AVAILABILITY = "IsAvailable";

    public DBHelper(Context context) {
        super(context, "KFULockersDB.db", null, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
