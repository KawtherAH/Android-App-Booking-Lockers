package com.example.lockersproject;

import static com.example.lockersproject.DBHelper.KEY_AVAILABILITY;
import static com.example.lockersproject.DBHelper.KEY_EndDate;
import static com.example.lockersproject.DBHelper.KEY_ID;
import static com.example.lockersproject.DBHelper.KEY_Locker;
import static com.example.lockersproject.DBHelper.KEY_NAME;
import static com.example.lockersproject.DBHelper.KEY_PHONE;
import static com.example.lockersproject.DBHelper.KEY_PW;
import static com.example.lockersproject.DBHelper.TABLE_USERS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Users {

    private int UserID, Phone, LockerNo;
    private String Password, UserName, EndDate;

    public Users() {
    }

    //For Register
    public Users(int userID, int phone, String password, String userName) {
        this.UserID = userID;
        this.Phone = phone;
        this.Password = password;
        this.UserName = userName;
    }// all user info
    //For Login
    public Users(int userID, String password) {
        UserID = userID;
        Password = password;
    }// id + pw
    //For Profile info
    public Users(int phone) {
        this.Phone = phone;
    }
    public Users(String userName) {
        UserName = userName;
    }


    public boolean addUser(Users user, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getUserID());
        values.put(KEY_PW, user.getPassword());
        values.put(KEY_NAME, user.getUserName());
        values.put(KEY_PHONE, user.getPhone());

        long result = database.insert(TABLE_USERS, null, values);
        database.close();

        if(result == -1) {
            return false;
        }else {
            return true;
        }
    }
    public boolean checkUser(int UID, String PW, Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor =  database.query(TABLE_USERS, new String[]{"*"}, KEY_ID + " = ?" + " AND " + KEY_PW + " = ?", new String[]{UID+"", PW},null,null,null );
        int cursorCount = cursor.getCount();
        cursor.close();

        if(cursorCount > 0) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean checkUserBooking(int UID, Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor =  database.query(TABLE_USERS, new String[]{KEY_Locker}, KEY_ID + " = ?" + " AND " + KEY_Locker + " != ?", new String[]{UID+"", 0+""},null,null,null );
        int cursorCount = cursor.getCount();
        cursor.close();

        if(cursorCount > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static ArrayList getUserInfo(int UID, Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(TABLE_USERS, new String[]{"*"}, KEY_ID +" =?", new String[]{UID+""},null,null,null );
        cursor.moveToNext();

        Users user_phone = new Users(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PHONE)));
        Users user_name = new Users(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));

        ArrayList<Users> UserInfo = new ArrayList();
        UserInfo.add(user_name);
        UserInfo.add(user_phone);

        cursor.close();
        return UserInfo;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
    public void setPhone(int phone) {
        Phone = phone;
    }
    public void setLockerNo(int lockerNo) {
        LockerNo = lockerNo;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getUserID() {
        return UserID;
    }
    public int getPhone() {
        return Phone;
    }
    public int getLockerNo() {
        return LockerNo;
    }
    public String getPassword() {
        return Password;
    }
    public String getUserName() {
        return UserName;
    }
    public String getEndDate() {
        return EndDate;
    }

}
