package com.example.parkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ConfirmSlot extends AppCompatActivity {

    int startTime;
    int endTime;
    int hours;

    String user;
    String garage, phoneNo, source, destination;
    static Car car;

    String bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_slot);
        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        garage=intent.getStringExtra("garage");
        startTime=intent.getIntExtra("startTime", 0);
        endTime=intent.getIntExtra("endTime", 0);
        hours=intent.getIntExtra("hours", 0);
        this.phoneNo=intent.getStringExtra("phoneNo");
        this.source=intent.getStringExtra("source");
        this.destination=intent.getStringExtra("destination");
        TextView textView=findViewById(R.id.textViewBookingDetails);
        String s="User: "+user+"\nGarage: "+garage+"\nCar: "+"\n  Model: "+car.getModel()+"\n  Registration: "+car.getRegNo()+"\nStart Time: "+startTime+"\nEnd Time: "+endTime+"\nHours: "+hours;
        textView.setText(s);

        Date date=new Date();//To be added here
        bookingID=date.toString();//for timing to get unique each time we will use current date


    }

    int slotFound=0;
    String oldS;

    public void checkAvailability(View view) {
        final ProgressBar progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        final Button button=findViewById(R.id.check);
        button.setEnabled(false);
        final Button button1=findViewById(R.id.confirm);
        button1.setEnabled(false);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Parking Garages").child(garage).child("slots");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                button.setEnabled(true);
                button1.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                label: for(int i=1; i<=snapshot.getChildrenCount(); i++){
                    oldS = snapshot.child(String.valueOf(i)).child("occupiedState").getValue(String.class);
                    for(int j=startTime; j<endTime; j++){
                        if(oldS.charAt(j)!='a')
                            continue label;
                    }
                    Toast.makeText(getApplicationContext(), "Slots are available for these timings", Toast.LENGTH_SHORT).show();
                    //slotFound=i;
                    //updateString('b');
                    //TextView textView = findViewById(R.id.textViewSlot);
                    //                   textView.setText("Booking ID: "+bookingID+"\nYour slot no is "+i);
//                    textView.setText("Slots are available for these timings");
                    //button.setVisibility(View.GONE);
//                    Button confirm = findViewById(R.id.confirm);
//                    confirm.setVisibility(View.VISIBLE);
                    return;
                }
                Toast.makeText(getApplicationContext(), "Slot not available for these timings", Toast.LENGTH_SHORT).show();
//                TextView textView=findViewById(R.id.textViewSlot);
//                textView.setText("Slots are not available for these timings");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateString(char c) {
        String newS="";
        for(int i=0; i<startTime; i++)
            newS+=oldS.charAt(i);
        for(int i=startTime; i<endTime; i++)
            newS+=c;
        for(int i=endTime; i<24; i++)
            newS+=oldS.charAt(i);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Parking Garages").child(garage).child("slots").child(String.valueOf(slotFound)).child("occupiedState");
        reference.setValue(newS);

    }

    public void confirm(View view) {
//        updateString('c');
//        Booking booking = new Booking();
//        booking.setBookingID(bookingID);
//        booking.setUser(user);
//        booking.setGarage(garage);
//        booking.setCar(car);
//        booking.setStartTime(startTime);
//        booking.setEndTime(endTime);
//        booking.setHours(hours);
//        booking.setSlotNo(slotFound);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Parking Garages").child(garage).child("slots").child(String.valueOf(slotFound)).child("Bookings").child(bookingID);
//        reference.setValue(booking);
//        super.finish();
        final ProgressBar progressBar=findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        final Button button=findViewById(R.id.check);
        button.setEnabled(false);
        final Button button1=findViewById(R.id.confirm);
        button1.setEnabled(false);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Parking Garages").child(garage).child("slots");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                button.setEnabled(true);
                button1.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                label: for(int i=1; i<=snapshot.getChildrenCount(); i++){
                    oldS = snapshot.child(String.valueOf(i)).child("occupiedState").getValue(String.class);
                    for(int j=startTime; j<endTime; j++){
                        if(oldS.charAt(j)!='a')
                            continue label;
                    }
                    slotFound=i;
                    updateString('b');
                    //TextView textView = findViewById(R.id.textViewSlot);
                    //                   textView.setText("Booking ID: "+bookingID+"\nYour slot no is "+i);
                    Toast.makeText(getApplicationContext(), ("Booking ID: "+bookingID+"\nYour slot no is "+i), Toast.LENGTH_SHORT).show();
                    //textView.setText("Slots are available for these timings");
                    //button.setVisibility(View.GONE);
//                    Button confirm = findViewById(R.id.confirm);
//                    confirm.setVisibility(View.VISIBLE);
                    update();
                    return;


                }
                Toast.makeText(getApplicationContext(), "Slot not available for these timings", Toast.LENGTH_SHORT).show();
//                TextView textView=findViewById(R.id.textViewSlot);
//                textView.setText("Slots are not available for these timings");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void update(){
        Booking booking = new Booking();
        booking.setBookingID(bookingID);
        booking.setUser(user);
        booking.setGarage(garage);
        booking.setCar(car);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setHours(hours);
        booking.setSlotNo(slotFound);
        booking.setSource(source);
        booking.setDestination(destination);
        booking.setCurrentlyAt("Route");
        String startTimeString=String.valueOf(startTime), endTimeString=String.valueOf(endTime);
        if(startTime<10)
            startTimeString="0"+startTimeString;
        if(endTime<10)
            endTimeString="0"+endTimeString;
        String s = "From "+ startTimeString+":00 to "+endTimeString+":00";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Parking Garages").child(garage).child("slots").child(String.valueOf(slotFound)).child("Booking").child(s);
        reference.setValue(booking);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(phoneNo).child("Bookings").child(bookingID);
        reference.setValue(booking);

        Intent intent = new Intent(getApplicationContext(), Route.class);
        intent.putExtra("user", user);
        intent.putExtra("garage", garage);
        intent.putExtra("startTime", startTime);
        intent.putExtra("hours", hours);
        intent.putExtra("endTime", endTime);
        intent.putExtra("destination", destination);
        intent.putExtra("slotNo", slotFound);
        intent.putExtra("source", source);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("bookingID", bookingID);
        startActivity(intent);
    }


}