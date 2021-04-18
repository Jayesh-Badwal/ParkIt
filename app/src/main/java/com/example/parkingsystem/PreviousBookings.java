package com.example.parkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreviousBookings extends AppCompatActivity {
    private String user;
    private String phone;

    ListView listView;
    private ArrayList<String> arrayList;
    private ArrayList<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_bookings);

        Intent intent = getIntent();
        this.user=intent.getStringExtra("NAME");///////get Intent
        this.phone=intent.getStringExtra("phoneNo");///////get Intent
        listView = findViewById(R.id.listViewOrders);
        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(bookingList.get(i).getCurrentlyAt().equals("Route")){
                    Intent intent = new Intent(getApplicationContext(), Route.class);
                    intent.putExtra("user", user);
                    intent.putExtra("garage", bookingList.get(i).getGarage());
                    intent.putExtra("startTime", bookingList.get(i).getStartTime());
                    intent.putExtra("hours", bookingList.get(i).getHours());
                    intent.putExtra("endTime", bookingList.get(i).getEndTime());
                    intent.putExtra("destination", bookingList.get(i).getDestination());
                    intent.putExtra("slotNo", bookingList.get(i).getSlotNo());
                    intent.putExtra("source", bookingList.get(i).getSource());
                    intent.putExtra("bookingID", bookingList.get(i).getBookingID());
                    intent.putExtra("phoneNo", phone);
                    startActivity(intent);
                } else if(bookingList.get(i).getCurrentlyAt().equals("scan1")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("garage", bookingList.get(i).getGarage());
                    intent.putExtra("startTime", bookingList.get(i).getStartTime());
                    intent.putExtra("hours", bookingList.get(i).getHours());
                    intent.putExtra("endTime", bookingList.get(i).getEndTime());
                    intent.putExtra("destination", bookingList.get(i).getDestination());
                    intent.putExtra("slotNo", bookingList.get(i).getSlotNo());
                    intent.putExtra("source", bookingList.get(i).getSource());
                    intent.putExtra("bookingID", bookingList.get(i).getBookingID());
                    intent.putExtra("phoneNo", phone);
                    startActivity(intent);
                } else if(bookingList.get(i).getCurrentlyAt().equals("scan2")){
                    Intent intent = new Intent(getApplicationContext(), EntrySuccessful.class);
                    intent.putExtra("NAME", user);
                    intent.putExtra("garage", bookingList.get(i).getGarage());
                    intent.putExtra("startTime", bookingList.get(i).getStartTime());
                    intent.putExtra("endTime", bookingList.get(i).getEndTime());
                    intent.putExtra("slotNo", bookingList.get(i).getSlotNo());
                    intent.putExtra("bookingID", bookingList.get(i).getBookingID());
                    startActivity(intent);
                } else if(bookingList.get(i).getCurrentlyAt().equals("Feedback")){
                    Intent intent = new Intent(getApplicationContext(), ShowFeedback.class);
                    intent.putExtra("NAME", user);
                    intent.putExtra("garage", bookingList.get(i).getGarage());
                    intent.putExtra("bookingID", bookingList.get(i).getBookingID());
                    intent.putExtra("phoneNo", phone);
                    startActivity(intent);
                }
            }
        });

    }

    private void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users").child(phone).child("Bookings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProgressBar progressBar = findViewById(R.id.progressBarDisplayOrders);
                progressBar.setVisibility(View.GONE);
                arrayList=new ArrayList<>();
                bookingList=new ArrayList<>();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String  s=snapshot1.child("garage").getValue(String.class)+" From "+ snapshot1.child("startTime").getValue(Integer.class)+":00 to "+snapshot1.child("endTime").getValue(Integer.class)+":00";
                    arrayList.add(s);
                    bookingList.add(snapshot1.getValue(Booking.class));
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}