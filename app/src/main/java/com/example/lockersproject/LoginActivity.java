package com.example.lockersproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText etID, etPW;
    private Button loginBtn;
    private TextView toRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etID =  (EditText) findViewById(R.id.EditUserID);
        etPW = (EditText) findViewById(R.id.EditUserPassword);

        loginBtn = (Button) findViewById(R.id.LoginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        toRegister =  (TextView) findViewById(R.id.toRegister);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    private void Login() {
        String UserIDstr = etID.getText().toString();
        String Password = etPW.getText().toString();

        if (UserIDstr.isEmpty() && Password.isEmpty()) {

            Toast.makeText(LoginActivity.this, "ID and Password are required!", Toast.LENGTH_LONG).show();
            etID.setError("Required");
            etPW.setError("Required");

        }
        else if(UserIDstr.isEmpty()){
            etID.setError("ID is required");
            etID.requestFocus();
            return;
        }
        else if(Password.isEmpty()){
            etPW.setError("Password is required");
            etPW.requestFocus();
            return;
        }
        else{
            int ID = Integer.parseInt(UserIDstr);
            Users user = new Users(ID, Password);
            boolean checkUser = user.checkUser(ID, Password, this);

            if(checkUser == true){
                ArrayList<Users> CurrentUser = Users.getUserInfo(ID, this);
                Toast.makeText(LoginActivity.this, "Welcome Back "+ CurrentUser.get(0).getUserName() , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("UserID", UserIDstr);

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }else{
                Toast.makeText(LoginActivity.this, "ID / Password is Wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}