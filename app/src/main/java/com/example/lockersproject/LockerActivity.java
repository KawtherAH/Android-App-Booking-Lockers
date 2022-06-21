package com.example.lockersproject;

import static com.example.lockersproject.DBHelper.KEY_AVAILABILITY;
import static com.example.lockersproject.DBHelper.KEY_EndDate;
import static com.example.lockersproject.DBHelper.KEY_ID;
import static com.example.lockersproject.DBHelper.KEY_Locker;
import static com.example.lockersproject.DBHelper.KEY_LockerNO;
import static com.example.lockersproject.DBHelper.TABLE_LOCKERS;
import static com.example.lockersproject.DBHelper.TABLE_USERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Calendar;

public class LockerActivity extends AppCompatActivity {

    private AlertDialog.Builder alertBuilder;
    private AlertDialog dialog, recyclerAlertDialog;
    private TextView MsgContent;
    private Button confirmBtn, cancelNtn, CloseBtn;
    private RecyclerView recyclerView;
    private int lockerNo, status;

    private EditText et_name, et_id, et_locker_no, et_end_date;
    private Button update, delete;
    private String currID, lockerStr;
    private ArrayList<Users> currentUser;
    private ArrayList<UserLocker> userLocker;

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);
        NavBar();
        currID = getIntent().getStringExtra("UserID");

        et_name = (EditText) findViewById(R.id.editIDLocker);
        et_id = (EditText) findViewById(R.id.editNameLocker);
        et_locker_no = (EditText) findViewById(R.id.editLockerNO);
        et_end_date = (EditText) findViewById(R.id.editEndDate);

        int ui = Integer.parseInt(currID);
        currentUser = Users.getUserInfo(ui, this);
        userLocker = UserLocker.getLockerInfo(ui, this);

        et_id.setText(currID);
        et_name.setText(currentUser.get(0).getUserName());

        lockerStr = String.valueOf(userLocker.get(0).getLockerNo());
        et_locker_no.setText(lockerStr);
        et_end_date.setText(userLocker.get(1).getEndDate());

        update = (Button) findViewById(R.id.updateBooking);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockersList();
            }
        });
        delete = (Button)findViewById(R.id.deleteBooking);// pop up window to confirm deleting locker
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteLockerDialog();
            }
        });
    }

    public void LockersList(){
        alertBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.update_pop_up_locker_no, null);

        CloseBtn = (Button) view.findViewById(R.id.Closebtn) ;
        recyclerView = (RecyclerView) view.findViewById(R.id.Update_lockers_recycler_view);
        alertBuilder.setView(view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LockerAdapter adapter = new LockerAdapter(Lockers.getAllLockers(this), new LockerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Lockers locker) {
                lockerNo = locker.getLockerNo();
                status = locker.getIsAvailable();

                if(status == 0) SorryUnAvailableLocker(); // check if locker UnAvailable
                else UpdateLockerDialog(); // else call BookLockerDialog();

            }
        });
        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerAlertDialog.dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerAlertDialog = alertBuilder.create();
        recyclerAlertDialog.show();
    }
    public void UpdateLockerDialog(){
        CreateAlertDialog();

        MsgContent.setText("Update Your Locker to No# " + lockerNo);
        //MsgContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked, 0, 0, 0);

        dialog = alertBuilder.create();
        dialog.show();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update user locker
                ContentValues cvUser = new ContentValues();
                cvUser.put(KEY_Locker, lockerNo);
                int res = database.update(TABLE_USERS, cvUser, KEY_ID + " = ?", new String[]{currID});

                // Update the new locker availability to 0
                ContentValues newLockerStatus = new ContentValues();
                newLockerStatus.put(KEY_AVAILABILITY, 0);
                int res2 = database.update(TABLE_LOCKERS, newLockerStatus, KEY_LockerNO + " = ?", new String[]{lockerNo+""});

                // Update the old locker availability to 1
                ContentValues oldLockerStatus = new ContentValues();
                oldLockerStatus.put(KEY_AVAILABILITY, 1);
                int res3 = database.update(TABLE_LOCKERS, oldLockerStatus, KEY_LockerNO + " = ?", new String[]{userLocker.get(0).getLockerNo()+""});

                if ((res > 0) && (res2 > 0) && (res3 > 0)){
                    dialog.dismiss();
                    Toast.makeText(LockerActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    recyclerAlertDialog.dismiss();
                }
            }
        });
        cancelNtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public void DeleteLockerDialog(){
        CreateAlertDialog();

        MsgContent.setText("Delete Your Locker No# " + lockerStr);
        //MsgContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked, 0, 0, 0);

        dialog = alertBuilder.create();
        dialog.show();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update user locker to 0 and nul
                ContentValues cvUser = new ContentValues();
                cvUser.put(KEY_Locker, 0);
                cvUser.put(KEY_EndDate, "");
                int res = database.update(TABLE_USERS, cvUser, KEY_ID + " = ?", new String[]{currID});

                // Update locker availability to 1
                ContentValues cvLockers = new ContentValues();
                cvLockers.put(KEY_AVAILABILITY, 1);
                int res2 = database.update(TABLE_LOCKERS, cvLockers, KEY_LockerNO + " = ?", new String[]{userLocker.get(0).getLockerNo()+""});

                if ((res > 0) && (res2 > 0)){
                    dialog.dismiss();
                    Toast.makeText(LockerActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelNtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public void CreateAlertDialog(){
        alertBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.pop_up_confirm_msg, null);

        MsgContent = (TextView) view.findViewById(R.id.MsgContent);
        confirmBtn = (Button) view.findViewById(R.id.confirm);
        cancelNtn = (Button) view.findViewById(R.id.cancel);
        alertBuilder.setView(view);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }
    public void SorryUnAvailableLocker(){
        alertBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.sorry_msg, null);

        MsgContent = (TextView) view.findViewById(R.id.MsgSorry);
        cancelNtn = (Button) view.findViewById(R.id.sorrybtn);
        alertBuilder.setView(view);

        MsgContent.setText("Sorry but locker No# " + lockerNo + " Unavailable");
        //MsgContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked, 0, 0, 0);

        dialog = alertBuilder.create();
        dialog.show();

        cancelNtn.setText("Ok");
        cancelNtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void NavBar(){
        BottomNavigationView bottomNav = findViewById(R.id.BottmBar);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HomePg:
                        Intent m = new Intent(LockerActivity.this, MainActivity.class);
                        m.putExtra("UserID", currID);

                        startActivity(m);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.ProfilePg:
                        Intent p = new Intent(LockerActivity.this, ProfileActivity.class);
                        p.putExtra("UserID", currID);

                        startActivity(p);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.LockerPg:
                        Intent l = new Intent(LockerActivity.this, LockerActivity.class);
                        l.putExtra("UserID", currID);

                        startActivity(l);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_btn_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                finish();
                startActivity(new Intent(LockerActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
        return true;
    }
}