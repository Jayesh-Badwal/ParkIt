package com.example.parkingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserHome extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Button search;
    String fullName, phoneNo;
    TextView destination, source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Intent intent=getIntent();
        fullName=intent.getStringExtra("NAME");
        phoneNo=intent.getStringExtra("phoneNo");

        search=findViewById(R.id.search);
        destination = findViewById(R.id.destination);
        source = findViewById(R.id.source);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), BookSlots.class);
                intent.putExtra("whatNext","Toentry");
                intent.putExtra("NAME", fullName);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("destination", destination.getText().toString());
                intent.putExtra("source", source.getText().toString());
                startActivity(intent);
            }
        });
    }


    public void loadPreviousBookings(View view) {
        Intent intent= new Intent(getApplicationContext(), PreviousBookings.class);
        intent.putExtra("NAME", fullName);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
    }
}