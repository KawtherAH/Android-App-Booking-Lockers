package com.example.lockersproject;

import static com.example.lockersproject.DBHelper.KEY_AVAILABILITY;
import static com.example.lockersproject.DBHelper.KEY_LockerNO;
import static com.example.lockersproject.DBHelper.TABLE_LOCKERS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Lockers {

    private int LockerNo;
    private int IsAvailable;

    public Lockers(int lockerNo, int isAvailable) {
        this.LockerNo = lockerNo;
        this.IsAvailable = isAvailable;
    }
    public Lockers(int isAvailable) {
        this.IsAvailable = isAvailable;
    }
    public int getLockerNo() {
        return LockerNo;
    }

    public void setLockerNo(int lockerNo) {
        LockerNo = lockerNo;
    }

    public int getIsAvailable() {
        return IsAvailable;
    }

    public void setIsAvailable(int isAvailable) {
        IsAvailable = isAvailable;
    }

    public static ArrayList getAllLockers(Context context){
        ArrayList<Lockers> LockersArrayList = new ArrayList();

        //Query to get all Lockers
        DBHelper dbHelper = new DBHelper(context.getApplicationContext() );
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query( TABLE_LOCKERS, new String[]{"*"}, null, null, null, null, null );
        while (cursor.moveToNext()) {

            int NO = cursor.getInt(cursor.getColumnIndexOrThrow( KEY_LockerNO ));
            int isAvailable = cursor.getInt( cursor.getColumnIndexOrThrow( KEY_AVAILABILITY ) );

            LockersArrayList.add(new Lockers(NO, isAvailable));
        }
        cursor.close();

        return LockersArrayList;
    }
    public static Lockers getLockerInfo(int id, Context context){
        Lockers locker = null;

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query( TABLE_LOCKERS, new String[]{"*"}, KEY_LockerNO + " =? ", new String[]{id + ""}, null, null, null );
        cursor.moveToNext();

        locker = new Lockers(
                cursor.getInt(
                        cursor.getColumnIndexOrThrow( KEY_AVAILABILITY )
                ));
        cursor.close();

        return locker;
    }
}














