package com.example.parkingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParkingFeedback extends AppCompatActivity {

    private static double rating;
    private String garage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_feedback);
        Intent intent=getIntent();
//        Pass garage name
        garage = intent.getStringExtra("NAME");
        getData();

    }

    private void getData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FeedbackCall").child(garage).child("Feedback");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int n = (int) snapshot.getChildrenCount();

                double rating = 0;

                //////////Temporary to be changed
//                TextView textView = findViewById(R.id.Review);
//                textView.setText("");

                /////////////////////Till here
                ArrayList<String> arrayList = new ArrayList<>();
                for(DataSnapshot d : snapshot.getChildren()) {

                    rating+= d.child("rating").getValue(double.class);
                    Log.d("TAG", String.valueOf(rating));
                      ////////Temporary to be changed
                    String review=d.child("review").getValue(String.class);
                    if(!review.equals("")) {
                        String s = d.getKey() + " " + d.child("rating").getValue(int.class).toString() + "/5\n\n" + review;
                        //textView.append(s);
                        arrayList.add(s);
                    }

                }
                ListView listView = findViewById(R.id.listView);
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);

                listView.setAdapter(arrayAdapter);

                rating/=n;
                Log.d("TAG", String.valueOf(rating));
                if(n!=0){
                    TextView textView1 = findViewById(R.id.rating);
                    textView1.setText(String.format("%.1f", rating));
                }

                Math.round(rating);
                ParkingFeedback.rating=rating;
                displayStars();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void displayStars() {

        ImageView star1 = findViewById(R.id.star1);
        star1.setImageResource(R.drawable.btn_star_big_off);
        ImageView star2 = findViewById(R.id.star2);
        star2.setImageResource(R.drawable.btn_star_big_off);
        ImageView star3 = findViewById(R.id.star3);
        star3.setImageResource(R.drawable.btn_star_big_off);
        ImageView star4 = findViewById(R.id.star4);
        star4.setImageResource(R.drawable.btn_star_big_off);
        ImageView star5 = findViewById(R.id.star5);
        star5.setImageResource(R.drawable.btn_star_big_off);
        if(rating>=1) {
            star1.setImageResource(R.drawable.btn_star_big_on);
        }
        if(rating>=2) {
            star2.setImageResource(R.drawable.btn_star_big_on);
        }
        if(rating>=3) {
            star3.setImageResource(R.drawable.btn_star_big_on);
        }
        if(rating>=4) {
            star4.setImageResource(R.drawable.btn_star_big_on);
        }
        if(rating>=5) {
            star5.setImageResource(R.drawable.btn_star_big_on);
        }
    }
}
