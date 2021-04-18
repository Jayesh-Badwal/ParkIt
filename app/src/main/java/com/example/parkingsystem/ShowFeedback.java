package com.example.parkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowFeedback extends AppCompatActivity {

    private static double rating;
    public static String user;
    public String garage, phoneNo, bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);

        Intent intent=getIntent();
//        Pass garage name
        phoneNo = intent.getStringExtra("phoneNo");
        bookingID = intent.getStringExtra("bookingID");
        garage = intent.getStringExtra("garage");
        this.user = intent.getStringExtra("NAME");//current user
        getData();
    }
    private void getData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FeedbackCall").child(garage).child("Feedback");

        //final List<Feedback> list = null;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int n = (int) snapshot.getChildrenCount();

                double rating = 0;

                ArrayList<String> arrayList = new ArrayList<>();


                /////////////////////Till here

                for(DataSnapshot d : snapshot.getChildren()) {

                    rating+= d.child("rating").getValue(double.class);
                    Log.d("TAG", String.valueOf(rating));



                    //////////Temporary to be changed
                    String review=d.child("review").getValue(String.class);
                    if(!review.equals("")) {
                        String s = d.getKey() + " " + d.child("rating").getValue(int.class).toString() + "/5\n\n" + review;
                        //textView.append(s);
                        arrayList.add(s);
                    }
                    /////////////////////Till here
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
                ShowFeedback.rating=rating;
                displayStars();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), UserHome.class);
//        intent.putExtra("NAME", user);
//        intent.putExtra("phoneNo", phoneNo);
//        startActivity(intent);
//    }

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

    public void post(View view) {

        Intent intent = new Intent(this, LeaveFeedback.class);
        intent.putExtra("NAME", user);
        intent.putExtra("garage", garage);
        startActivity(intent);

    }

    public void back(View view) {
        super.finish();
    }

    public void callPayment(View view) {
        Intent intent = new Intent(this, Payment.class);
        intent.putExtra("NAME", user);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("bookingID", bookingID);
        startActivity(intent);
    }
}