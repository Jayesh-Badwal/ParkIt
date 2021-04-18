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

public class BookSlots extends AppCompatActivity {
    private String user;
    private String location, source, phoneNo;
    private Car car;

    private ArrayList<String> garageList;
    private ArrayList<String> garageAddress;
    private ListView listView;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slots);
        ///////////data///////////////

        Intent intent = getIntent();

        this.user= intent.getStringExtra("NAME");
        this.location = intent.getStringExtra("destination");
        this.source = intent.getStringExtra("source");
        this.phoneNo = intent.getStringExtra("phoneNo");
        car=new Car();
        car.setModel("model");
        car.setRegNo("licensePlate");
        listView=findViewById(R.id.listView1);
        loadCar();
        loadGarages();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("garage", garageList.get(i));
                intent.putExtra("car", car);
                intent.putExtra("destination", garageAddress.get(i));
                intent.putExtra("source", source);
                intent.putExtra("phoneNo", phoneNo);
                startActivity(intent);


            }
        });
    }

    private void loadCar() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users").child(phoneNo);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                car.setModel(snapshot.child("model").getValue().toString());
                car.setRegNo(snapshot.child("licenseP").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGarages() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("City").child(location);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProgressBar progressBar = findViewById(R.id.progressBar4);
                progressBar.setVisibility(View.GONE);
                garageList=new ArrayList<>();
                garageAddress=new ArrayList<>();
                for(DataSnapshot d: snapshot.getChildren()){
                    garageAddress.add(d.getValue(String.class));
                    garageList.add(d.getKey());
                }

                arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, garageList);
                listView.setAdapter(arrayAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}