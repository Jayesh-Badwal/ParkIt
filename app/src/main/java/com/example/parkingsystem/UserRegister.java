package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserRegister extends AppCompatActivity {

    private EditText username,pass1,pass2,email;
    Button btnRegister;
    TextView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        Intent intent = getIntent();
        final String whereFrom=intent.getStringExtra("whereFrom");

        btn=findViewById(R.id.alreadyHave);
        username = findViewById(R.id.username);
        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);
        email = findViewById(R.id.email);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials(whereFrom);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserLogin.class));
            }
        });
    }

    private boolean checkemail() {
        String inputemail=email.getText().toString();
        if((inputemail.contains(".com") || inputemail.contains(".in")) && inputemail.contains("@")){
            return true;
        }
        else{
            return false;
        }
    }


    private void checkCredentials(String whereFrom) {
        String inputname = username.getText().toString();
        String inputemail = email.getText().toString();
        String inputpass1 = pass1.getText().toString();
        String inputpass2 = pass2.getText().toString();
        if (inputname.isEmpty()) {
            showError(username, "Your username is invalid.");
        } else if(inputemail.isEmpty() || !checkemail()){
            showError(email, "Email not valid.");
        }
        else if(pass1.length()<7 || !checkpassword()){
            showError(pass1, "Password must be atleast 7 characters, should have 1 Capital letter, 1 digit and 1 special character..");
        }
        else if(inputpass1.isEmpty() || !inputpass2.equals(inputpass1)){
            showError(pass2, "Password did not match!");
        }
        else{
            Intent intent;
            if(whereFrom.equals("Users")) {
                intent=new Intent(UserRegister.this,UserReg2.class);
            }
            else{
                intent=new Intent(UserRegister.this,ParkingReg2.class);
            }
            intent.putExtra("fullName", inputname);
            intent.putExtra("email", inputemail);
            intent.putExtra("password", inputpass1);
            startActivity(intent);
        }

    }

    private boolean checkpassword() {
        int countC=0, countD=0, countS=0;
        String inputpass= pass1.getText().toString();
        for(int i=0; i<inputpass.length();i++){
            if(inputpass.charAt(i)==32){
                showError(pass1, "Password cannot contain spaces!");
                return false;
            }
            else if(inputpass.charAt(i)>=48 && inputpass.charAt(i)<=57) countD++;
            else if(inputpass.charAt(i)>=65 && inputpass.charAt(i)<=90) countC++;
            else if(inputpass.charAt(i)=='!' || inputpass.charAt(i)=='*' || inputpass.charAt(i)=='$' || inputpass.charAt(i)== '@' ||
                    inputpass.charAt(i)=='/' || inputpass.charAt(i)=='^' || inputpass.charAt(i)=='&' || inputpass.charAt(i)=='#') countS++;

        }
        if(countC==0 || countD==0 || countS==0) return false;
        else return true;
    }

    private void showError(EditText username, String s) {
        username.setError(s);
    }
}


