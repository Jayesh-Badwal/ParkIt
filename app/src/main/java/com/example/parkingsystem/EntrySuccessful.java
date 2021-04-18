package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EntrySuccessful extends AppCompatActivity {

    String fullName, garage, phoneNo, bookingID;
    int slotNo, startTime, endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_successful);

        Intent intent=getIntent();
        fullName = intent.getStringExtra("NAME");
        garage=intent.getStringExtra("garage");
        phoneNo=intent.getStringExtra("phoneNo");
        bookingID=intent.getStringExtra("bookingID");
        slotNo=intent.getIntExtra("slotNo", 0);
        startTime = intent.getIntExtra("startTime", 0);
        endTime = intent.getIntExtra("endTime", 0);
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), UserHome.class);
//        intent.putExtra("NAME", fullName);
//        intent.putExtra("phoneNo", phoneNo);
//        startActivity(intent);
//    }

    public void goToexit(View view) {
        Intent intent=new Intent(getApplicationContext(),QRScanner.class);
        intent.putExtra("whatNext","feedback");
        intent.putExtra("user",fullName);
        intent.putExtra("garage",garage);
        intent.putExtra("startTime", startTime);
        intent.putExtra("slotNo", slotNo);
        intent.putExtra("endTime", endTime);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("bookingID", bookingID);
        startActivity(intent);
    }
}