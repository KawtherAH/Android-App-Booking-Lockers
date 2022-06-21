package com.example.lockersproject;

import static com.example.lockersproject.DBHelper.KEY_EndDate;
import static com.example.lockersproject.DBHelper.KEY_ID;
import static com.example.lockersproject.DBHelper.KEY_Locker;
import static com.example.lockersproject.DBHelper.KEY_NAME;
import static com.example.lockersproject.DBHelper.KEY_PHONE;
import static com.example.lockersproject.DBHelper.TABLE_USERS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UserLocker {

    private int UserID, LockerNo;
    private String UserName, EndDate;

    public UserLocker() {
    }
    public UserLocker(int userID, int lockerNo, String userName, String endDate) {
        this.UserID = userID;
        this.LockerNo = lockerNo;
        this.UserName = userName;
        this.EndDate = endDate;
    }

    public UserLocker(int lockerNo) {
        this.LockerNo = lockerNo;
    }
    public UserLocker(String endDate) {
        this.EndDate = endDate;
    }

    public static ArrayList getLockerInfo(int UID, Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(TABLE_USERS, new String[]{"*"}, KEY_ID +" =?", new String[]{UID+""},null,null,null );
        cursor.moveToNext();

        UserLocker user_locker = new UserLocker(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_Locker)));
        UserLocker end_date = new UserLocker(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EndDate)));

        ArrayList<UserLocker> LockerInfo = new ArrayList();
        LockerInfo.add(user_locker);
        LockerInfo.add(end_date);

        cursor.close();
        return LockerInfo;
    }

    public int getUserID() {
        return UserID;
    }
    public void setUserID(int userID) {
        UserID = userID;
    }
    public int getLockerNo() {
        return LockerNo;
    }
    public void setLockerNo(int lockerNo) {
        LockerNo = lockerNo;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public String getEndDate() {
        return EndDate;
    }
    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
