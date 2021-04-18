package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Log.d("tag", "Display loaded");
        Intent intent = getIntent();
        Booking booking = (Booking)intent.getSerializableExtra("booking");
        String s="";
        Car car=booking.getCar();

        s+="\nUser: "+booking.getUser();
        s+="\nCar Details:- Model: "+car.getModel()+", Registration Number: "+car.getRegNo();
        s+="\nStart Time: "+booking.getStartTime()+":00";
        s+="\nEnd Time: "+booking.getEndTime()+":00";
        s+="\nHours: "+booking.getHours();
        s+="\nBooking ID: "+booking.getBookingID();
        s+="\nGarage: "+booking.getGarage();
        s+="\nSlot Number: "+booking.getSlotNo();

        TextView textView = findViewById(R.id.textView);
        textView.setText(s);

    }
}