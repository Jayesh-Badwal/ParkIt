package com.example.parkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TimeActivity extends AppCompatActivity {

    String user;
    String garage, phoneNo, source, destination;
    Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        String[] arraySpinner = new String[24];
        for(int i=0; i<24; i++){
            if(i<10)
                arraySpinner[i]="0"+i+":00";
            else
                arraySpinner[i]=i+":00";
        }
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        setSpinner2();

        //this updates the hour counter
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                setSpinner2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Intent intent = getIntent();
        this.user=intent.getStringExtra("user");
        this.garage=intent.getStringExtra("garage");
        this.phoneNo=intent.getStringExtra("phoneNo");
        this.source=intent.getStringExtra("source");
        this.destination=intent.getStringExtra("destination");
        this.car=(Car)intent.getSerializableExtra("car");
        getGarageTimings();

    }



    void setSpinner2(){
        Log.d("TAG", "updated spinner 2");
        Spinner s1= findViewById(R.id.spinner);

        int n = Integer.parseInt(((String) s1.getSelectedItem()).substring(0, 2));
        n = 24 - n;
        String[] arraySpinner = new String[n];
        for (int i = 0; i < n; i++){
            if(i==0)
                arraySpinner[i] = "01 hour";
            else if(i<9)
                arraySpinner[i] = "0"+(i+1) + " hours";
            else
                arraySpinner[i] = (i+1) + " hours";
        }
        Spinner s=findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);


    }


    int garageStartTime;
    int garageEndTime;




    public void openBookSlot(View view) {
        Spinner s1 = findViewById(R.id.spinner);
        Spinner s2 = findViewById(R.id.spinner2);
        int startTime=Integer.parseInt(((String)s1.getSelectedItem()).substring(0,2));
        int hours=Integer.parseInt(((String)s2.getSelectedItem()).substring(0,2));;
        int endTime=startTime+hours;

        if(startTime<garageStartTime||endTime>garageEndTime)
            Toast.makeText(this, "Garage is not open for these timings", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this, ConfirmSlot.class);
            intent.putExtra("user", user);
            intent.putExtra("garage", garage);
            ConfirmSlot.car = car;//find a way to pass objects between activities
            intent.putExtra("startTime", startTime);
            intent.putExtra("hours", hours);
            intent.putExtra("endTime", endTime);
            intent.putExtra("destination", destination);
            intent.putExtra("source", source);
            intent.putExtra("phoneNo", phoneNo);
            startActivity(intent);
        }
    }

    private void getGarageTimings() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Parking Garages").child(garage);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                garageStartTime=snapshot.child("startTime").getValue(int.class);
                garageEndTime=snapshot.child("endTime").getValue(int.class);
                ProgressBar progressBar=findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                Button button = findViewById(R.id.button);
                button.setEnabled(true);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}