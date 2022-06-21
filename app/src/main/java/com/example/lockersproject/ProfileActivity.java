package com.example.lockersproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private EditText Name, ID, Phone;
    private String UserID;
    private ArrayList<Users> currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        NavBar();

        Name = (EditText) findViewById(R.id.editNameProfile);
        ID = (EditText) findViewById(R.id.editIDProfile);
        Phone = (EditText) findViewById(R.id.editPhoneProfile);

        UserID = getIntent().getStringExtra("UserID");
        ID.setText(UserID);

        int ui = Integer.parseInt(UserID);
        currentUser = Users.getUserInfo(ui, this);

        Name.setText(currentUser.get(0).getUserName());
        String phoneStr = String.valueOf(currentUser.get(1).getPhone());
        Phone.setText(phoneStr);
    }
    public void NavBar(){
        BottomNavigationView bottomNav = findViewById(R.id.BottmBar);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HomePg:
                        Intent m = new Intent(ProfileActivity.this, MainActivity.class);
                        m.putExtra("UserID", UserID);

                        startActivity(m);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.ProfilePg:
                        Intent p = new Intent(ProfileActivity.this, ProfileActivity.class);
                        p.putExtra("UserID", UserID);

                        startActivity(p);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.LockerPg:
                        Intent l = new Intent(ProfileActivity.this, LockerActivity.class);
                        l.putExtra("UserID", UserID);

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
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
        return true;
    }

}