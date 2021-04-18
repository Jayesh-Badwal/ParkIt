package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class ParkingReg2 extends AppCompatActivity {

    EditText phoneNumber,address,slotsno,openhr,closehr,rate;
    String fullName, email, password, phoneNumberS,addressS,openS,closeS,rateS,slotnoS,whereFrom;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_reg2);

        Intent intent = getIntent();

        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        phoneNumber = findViewById(R.id.phone);
        countryCodePicker = findViewById(R.id.country_code_picker);
        address = findViewById(R.id.address);
        slotsno=findViewById(R.id.noslots);
        openhr=findViewById(R.id.open);
        closehr=findViewById(R.id.close);
        rate=findViewById(R.id.rate);

        countryCodePicker.registerCarrierNumberEditText(phoneNumber);
    }
    public void callVerifyOTPScreen(View view) {

        if (!validatePhoneNumber())
            return;

        phoneNumberS = countryCodePicker.getFullNumberWithPlus();
        addressS=address.getText().toString();
        rateS=rate.getText().toString();
        openS=openhr.getText().toString();
        closeS=closehr.getText().toString();
        slotnoS=slotsno.getText().toString();

        Intent intent = new Intent(ParkingReg2.this, ParkingReg3.class);
        intent.putExtra("fullName", fullName);
        intent.putExtra("Rate",rateS);
        intent.putExtra("Openhr",openS);
        intent.putExtra("Closehr",closeS);
        intent.putExtra("email", email);
        intent.putExtra("Slotno",slotnoS);
        intent.putExtra("password", password);
        intent.putExtra("Address",addressS);
        intent.putExtra("phoneNumber", phoneNumberS);
        intent.putExtra("whereToGo", "toSignUpSuccessfull");
        intent.putExtra("whereFrom","Parking Garages");
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