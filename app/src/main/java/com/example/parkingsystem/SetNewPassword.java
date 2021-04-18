package com.example.parkingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    EditText password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        password = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
    }
    public void setNewPasswordBtn(View view) {

        //CheckInternet checkInternet = new CheckInternet();
        //if(!checkInternet.isConnected(this)) {
            //showCustomDialog();
            //return;
        //}

        if(!validatePassword() | !validateConfirmPassword()) {
            return;
        }

        String _newPassword = password.getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNumber).child("password").setValue(_newPassword);

        Intent intent = new Intent(SetNewPassword.this, UserHome.class);
        startActivity(intent);

    }
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPassword.this);
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
    private boolean validatePassword() {
        String val = password.getText().toString().trim();

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }
        if (!(val.contains("@") || val.contains("#") || val.contains("!") || val.contains("~")
                || val.contains("$") || val.contains("%") || val.contains("^") || val.contains("&")
                || val.contains("*") || val.contains("(") || val.contains(")") || val.contains("-")
                || val.contains("+") || val.contains("/") || val.contains(":") || val.contains(".")
                || val.contains(", ") || val.contains("<") || val.contains(">") || val.contains("?")
                || val.contains("|"))) {
            password.setError("Password should contain atleast one character!");
            return false;
        }
        if (!((val.length() >= 8) && (val.length() <= 15))) {
            password.setError("Password should contain 8 characters!");
            return false;
        }
        if (val.contains(" ")) {
            password.setError("Password should not contain spaces!");
            return false;
        }
        if (true) {
            int count = 0;
            for (int i = 90; i <= 122; i++) {
                char c = (char)i;
                String str1 = Character.toString(c);
                if (val.contains(str1)) {
                    count = 1;
                    break;
                }
            }
            if (count == 0) {
                password.setError("Password should contain atleast one lower case letter!");
                return false;
            }
        }
        if (true) {
            int count = 0;
            for (int i = 65; i <= 90; i++) {
                char c = (char)i;
                String str1 = Character.toString(c);
                if (val.contains(str1)) {
                    count = 1;
                    break;
                }
            }
            if (count == 0) {
                password.setError("Password should contain atleast one upper case letter!");
                return false;
            }
        }
        if (true) {
            int count = 0;
            for (int i = 0; i <= 9; i++) {
                String str1 = Integer.toString(i);
                if (val.contains(str1)) {
                    count = 1;
                    break;
                }
            }
            if (count == 0) {
                password.setError("Password should contain atleast one digit!");
                return false;
            }
        }
        password.setError(null);
        return true;
    }

    private boolean validateConfirmPassword() {
        String val = password.getText().toString().trim();
        String val2 = confirmPassword.getText().toString().trim();

        if(val.equals(val2))
            return true;
        else
            return false;
    }

}