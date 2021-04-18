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

public class BookingList extends AppCompatActivity {
    private String garage, slot;
    ListView listView;
    ArrayList<String> bookingsTime;
    ArrayList<Booking> bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        Intent intent = getIntent();
        garage=intent.getStringExtra("garage");
        slot=intent.getStringExtra("slot");

        bookingsTime = new ArrayList<>();
        bookings = new ArrayList<>();
        listView=findViewById(R.id.listView2);
        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                intent.putExtra("booking", bookings.get(i));
                startActivity(intent);


            }
        });

    }

    private void loadData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Parking Garages").child(garage).child("slots").child(slot).child("Booking");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProgressBar progressBar = findViewById(R.id.progressBar4);
                progressBar.setVisibility(View.GONE);
                for(DataSnapshot d: snapshot.getChildren()){
                    bookings.add(d.getValue(Booking.class));
                    bookingsTime.add(d.getKey());
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(BookingList.this, android.R.layout.simple_list_item_1, bookingsTime);

                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}