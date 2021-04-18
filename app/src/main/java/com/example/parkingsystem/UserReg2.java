package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class UserReg2 extends AppCompatActivity {

    EditText phoneNumber,address,licensep,model;
    String fullName, email, password, phoneNumberS,addressS,LicenseS,ModelS;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg2);

        Intent intent = getIntent();

        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        phoneNumber = findViewById(R.id.phone);
        countryCodePicker = findViewById(R.id.country_code_picker);
        address = findViewById(R.id.address);
        licensep=findViewById(R.id.license_plate);
        model=findViewById(R.id.car_model);

        countryCodePicker.registerCarrierNumberEditText(phoneNumber);
    }

    public void callVerifyOTPScreen(View view) {

        if (!validatePhoneNumber())
            return;

        phoneNumberS = countryCodePicker.getFullNumberWithPlus();
        addressS=address.getText().toString();
        LicenseS=licensep.getText().toString();
        ModelS=model.getText().toString();

        Intent intent = new Intent(UserReg2.this, VerifyOtp.class);
        intent.putExtra("fullName", fullName);
        intent.putExtra("LicensePlate",LicenseS);
        intent.putExtra("Model",ModelS);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("Address",addressS);
        intent.putExtra("phoneNumber", phoneNumberS);
        intent.putExtra("whereToGo", "toSignUpSuccessfull");
        intent.putExtra("whereFrom","user");
        startActivity(intent);

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
