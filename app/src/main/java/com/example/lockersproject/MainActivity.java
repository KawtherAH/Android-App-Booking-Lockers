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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener */{

    private AlertDialog.Builder alertBuilder;
    private AlertDialog dialog;
    private TextView MsgContent;
    private Button confirmBtn, cancelNtn;
    private int lockerNo, status;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String currID, EndDate;
    private SearchView searchView;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavBar();
        currID = getIntent().getStringExtra("UserID");
        /*toolbar = (Toolbar) findViewById(R.id.);
        toolbar.setSubtitle("Test Subtitle");
        toolbar.inflateMenu(R.menu.logout_btn_menu);*/

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lockers_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        LockerAdapter adapter = new LockerAdapter(Lockers.getAllLockers(this), new LockerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Lockers locker) {
                lockerNo = locker.getLockerNo();
                status = locker.getIsAvailable();

                Users user = new Users();
                boolean checkUserBooking = user.checkUserBooking(Integer.parseInt(currID), getBaseContext());

                if(checkUserBooking == true) SorryHaveLocker(); // check if already has locker
                else {
                    if(status == 0) SorryUnAvailableLocker();// check if locker UnAvailable
                    else BookLockerDialog(); // else call BookLockerDialog();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.search1 );
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter( s.toString() );
                return true;
            }
        } );

    }
    public void BookLockerDialog(){
        CreateAlertDialog();

        MsgContent.setText("Booking Locker No# " + lockerNo);
        MsgContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked, 0, 0, 0);

        dialog = alertBuilder.create();
        dialog.show();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                calendar.add(Calendar.YEAR, Calendar.YEAR + 0);
                EndDate = dateFormat.format(calendar.getTime());

                // Update user with locker info
                ContentValues cvUser = new ContentValues();
                cvUser.put(KEY_Locker, lockerNo);
                cvUser.put(KEY_EndDate, EndDate);// EndDate = year from booking day
                int res = database.update(TABLE_USERS, cvUser, KEY_ID + " = ?", new String[]{currID});

                // Update locker availability to 0
                ContentValues cvLockers = new ContentValues();
                cvLockers.put(KEY_AVAILABILITY, 0);
                int res2 = database.update(TABLE_LOCKERS, cvLockers, KEY_LockerNO + " = ?", new String[]{lockerNo+""});

                if ((res > 0) && (res2 > 0)){
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
        CreateSorryAlertDialog();

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
    public void SorryHaveLocker(){
        CreateSorryAlertDialog();

        MsgContent.setText("Sorry, but you have a locker already");
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
    public void CreateSorryAlertDialog(){
        alertBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.sorry_msg, null);

        MsgContent = (TextView) view.findViewById(R.id.MsgSorry);
        cancelNtn = (Button) view.findViewById(R.id.sorrybtn);
        alertBuilder.setView(view);
    }

    public void NavBar(){
        BottomNavigationView bottomNav = findViewById(R.id.BottmBar);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HomePg:
                        Intent m = new Intent(MainActivity.this, MainActivity.class);
                        m.putExtra("UserID", currID);

                        startActivity(m);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.ProfilePg:
                        Intent p = new Intent(MainActivity.this, ProfileActivity.class);
                        p.putExtra("UserID", currID);

                        startActivity(p);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.LockerPg:
                        Intent l = new Intent(MainActivity.this, LockerActivity.class);
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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
        return true;
    }


}