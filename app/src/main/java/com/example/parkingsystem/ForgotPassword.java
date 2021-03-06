package com.example.parkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgotPassword extends AppCompatActivity {

    EditText phoneNumber;
    CountryCodePicker countryCodePicker;
    String whereFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Intent intent = getIntent();
        whereFrom = intent.getStringExtra("whereFrom");

        phoneNumber = findViewById(R.id.forget_password_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);
        countryCodePicker.registerCarrierNumberEditText(phoneNumber);
    }

    public void verifyPhoneNumber(View view) {

        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }

        if(!validatePhoneNumber())
            return;

        final String _completePhoneNumber = countryCodePicker.getFullNumberWithPlus() ;

        if(whereFrom.equals("Users")) {
            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        phoneNumber.setError(null);

                        Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
                        intent.putExtra("phoneNumber", _completePhoneNumber);
                        intent.putExtra("whereToGo", "toSetNewPassword");
                        startActivity(intent);
                        finish();

                    } else {
                        phoneNumber.setError("No such user exists!");
                        phoneNumber.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ForgotPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if(whereFrom.equals("Parking Garages")) {
            Query checkUser = FirebaseDatabase.getInstance().getReference("Parking").orderByChild("phoneNo").equalTo(_completePhoneNumber);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        phoneNumber.setError(null);

                        Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
                        intent.putExtra("phoneNumber", _completePhoneNumber);
                        intent.putExtra("whereToGo", "toSetNewPassword");
                        startActivity(intent);
                        finish();

                    } else {
                        phoneNumber.setError("No such user exists!");
                        phoneNumber.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ForgotPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }

    private boolean validatePhoneNumber() {
        String val = phoneNumber.getText().toString().trim();

        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        }
        if (val.contains(" ")) {
            phoneNumber.setError("No White spaces are allowed!");
            return false;
        }
        if (!(val.charAt(0) == '7' || val.charAt(0) == '8' || val.charAt(0) == '9')) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        }
        phoneNumber.setError(null);
        return true;
    }
}