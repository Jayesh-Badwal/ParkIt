package com.example.parkingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

public class ParkingReg3 extends AppCompatActivity {

    CardForm cardForm;
    String fullName, email, password, phoneNumber,address,open,close,rate,slotno,whereFrom, whereToGo;
    Button reg;
    AlertDialog.Builder alertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_reg3);

        Intent intent = getIntent();
        fullName = intent.getStringExtra("fullName");
        rate = intent.getStringExtra("Rate");
        open = intent.getStringExtra("Openhr");
        close = intent.getStringExtra("Closehr");
        email = intent.getStringExtra("email");
        slotno = intent.getStringExtra("Slotno");
        password = intent.getStringExtra("password");
        address = intent.getStringExtra("Address");
        phoneNumber = intent.getStringExtra("phoneNumber");
        whereToGo = intent.getStringExtra("whereToGo");
        whereFrom = intent.getStringExtra("whereFrom");

        cardForm=findViewById(R.id.card_form);
        reg=findViewById(R.id.btnReg);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(ParkingReg3.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardForm.isValid()){
                    alertBuilder=new AlertDialog.Builder(getApplicationContext());
                    alertBuilder.setTitle("Confirm before proceeding..");
                    alertBuilder.setMessage("Card number: "+cardForm.getCardNumber()+"\n"+
                            "Card expiry date: "+cardForm.getExpirationDateEditText().getText().toString()+"\n"+
                            "Card CVV: "+cardForm.getCvv()+"\n"+
                            "Phone number: "+cardForm.getMobileNumber());
                    Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("Rate",rate);
                    intent.putExtra("Openhr",open);
                    intent.putExtra("Closehr",close);
                    intent.putExtra("email", email);
                    intent.putExtra("Slotno",slotno);
                    intent.putExtra("password", password);
                    intent.putExtra("Address",address);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("whereToGo", "toSignUpSuccessfull");
                    intent.putExtra("whereFrom","Parking Garages");
                    intent.putExtra("CardNumber", cardForm.getCardNumber());
                    intent.putExtra("Card expiry date", cardForm.getExpirationDateEditText().getText().toString());
                    intent.putExtra("Card CVV", cardForm.getCvv());
                    startActivity(intent);
                }
            }
        });
    }
}