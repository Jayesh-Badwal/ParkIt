package com.example.parkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Calendar;

public class ParkingStatus extends AppCompatActivity {
    String parkingGarageID;
    int currentHr;
    ArrayList<String> slotsState;
    ListView listView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_status);

        Intent intent = getIntent();
        this.parkingGarageID = intent.getStringExtra("NAME");
        Calendar calendar = Calendar.getInstance();
        currentHr = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("HOUR_OF_DAY", String.valueOf(Calendar.HOUR_OF_DAY));
        slotsState=new ArrayList<>();
        listView=findViewById(R.id.listView);
        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), BookingList.class);
                intent.putExtra("garage", parkingGarageID);
                intent.putExtra("slot", String.valueOf(i+1));
                startActivity(intent);

            }
        });

    }

    private void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Parking Garages").child(parkingGarageID).child("slots");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProgressBar progressBar =findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                for(DataSnapshot d: snapshot.getChildren()){
                    String s=d.child("occupiedState").getValue(String.class);
                    Log.d("tag", s);
                    String data="Slot: "+d.getKey()+", Current Status: ";
                    if(s.charAt(currentHr)=='a'){
                        data+="Available";
                    }else if(s.charAt(currentHr)=='b'){
                        data+="Booked";
                    }else{
                        data+="Currently Occupied";
                    }
                    slotsState.add(data);
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, slotsState);

                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void callQRGenerator(View view) {
        Intent intent = new Intent(getApplicationContext(), QRGenerator.class);
        intent.putExtra("NAME", parkingGarageID);
        startActivity(intent);
    }

    public void callShowFeedback(View view) {
        Intent intent = new Intent(getApplicationContext(), ParkingFeedback.class);
        intent.putExtra("NAME", parkingGarageID);
        startActivity(intent);
    }
}