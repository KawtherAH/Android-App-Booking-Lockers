package com.example.lockersproject;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity{

    private EditText EditID, EditName, EditPassword, EditPhone;
    private Button RegisterBtn;
    private TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditID = (EditText) findViewById(R.id.EditID);
        EditName = (EditText) findViewById(R.id.EditName);
        EditPassword = (EditText) findViewById(R.id.EditPassword);
        EditPhone = (EditText) findViewById(R.id.EditPhone);

        RegisterBtn = (Button) findViewById(R.id.RegisterBtn);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registration();
            }
        });
        toLogin =  (TextView) findViewById(R.id.toLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void Registration() {
        String IDstr = EditID.getText().toString();
        String username = EditName.getText().toString();
        String password = EditPassword.getText().toString();
        String phonestr = EditPhone.getText().toString();

        if(IDstr.isEmpty() && username.isEmpty() && password.isEmpty() && phonestr.isEmpty()){
            Toast.makeText(RegisterActivity.this, "All Fields are required!", Toast.LENGTH_LONG).show();
            EditID.setError("Required");
            EditName.setError("Required");
            EditPassword.setError("Required");
            EditPhone.setError("Required");
        }
        else if(IDstr.isEmpty()){
            EditID.setError("Your ID is required");
            EditID.requestFocus();
            return;
        }
        else if(username.isEmpty()){
            EditName.setError("Your name is required");
            EditName.requestFocus();
            return;
        }
        else if(password.isEmpty()){
            EditPassword.setError("Password is required");
            EditPassword.requestFocus();
            return;
        }
        else if(phonestr.isEmpty()){
            EditPhone.setError("Your phone is required");
            EditPhone.requestFocus();
            return;
        }
        else {
            int ID = Integer.parseInt(IDstr);
            int phone = Integer.parseInt(phonestr);

            Users user = new Users(ID, phone, password, username);
            boolean checkUser = user.checkUser(ID, password, this);

            if(checkUser == false){

                boolean newUser = user.addUser(user, this);
                if (newUser == true){
                    Toast.makeText(RegisterActivity.this, "Yor are Registered Successfully", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("UserID", IDstr);

                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "User already exits! please sign in", Toast.LENGTH_LONG).show();
            }
        }
    }

}












