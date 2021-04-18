package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpSuccessfull extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_successfull);
    }
    public void backToLogin(View view) {

        Intent intent = new Intent(SignUpSuccessfull.this, UserLogin.class);
        startActivity(intent);

    }
}