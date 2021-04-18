package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginAs extends AppCompatActivity {

    Button user, parking;
    TextView showtext;
    String whichAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as);

        Intent intent = getIntent();
        if(intent.getStringExtra("whichAction")!=null){
            whichAction=intent.getStringExtra("whichAction");
        }
        else
            whichAction="";
        user=findViewById(R.id.User);
        parking=findViewById(R.id.parking);
        showtext=findViewById(R.id.textView);

        if(whichAction.equals("Signup")){
            showtext.setText("Whom will you sign up as? ");
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginAs.this, UserRegister.class);
                    intent.putExtra("whereFrom", "Users");
                    startActivity(intent);
                }
            });

            parking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginAs.this, UserRegister.class);
                    intent.putExtra("whereFrom", "Parking Garages");
                    startActivity(intent);
                }
            });
        }
        else{
            showtext.setText("Whom will you login as?");
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginAs.this, UserLogin.class);
                    intent.putExtra("whereFrom", "Users");
                    startActivity(intent);
                }
            });

            parking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginAs.this, UserLogin.class);
                    intent.putExtra("whereFrom", "Parking Garages");
                    startActivity(intent);
                }
            });
        }
    }

}