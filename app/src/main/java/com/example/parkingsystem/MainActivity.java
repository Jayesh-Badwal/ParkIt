package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    int startTime;
    int endTime;
    int hours;
    int slotNo;
    String user;
    String garage, phoneNo, source, dest, bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        garage=intent.getStringExtra("garage");
        bookingID=intent.getStringExtra("bookingID");
        startTime=intent.getIntExtra("startTime", 0);
        endTime=intent.getIntExtra("endTime", 0);
        hours=intent.getIntExtra("hours", 0);
        slotNo=intent.getIntExtra("slotNo", 0);
        this.phoneNo=intent.getStringExtra("phoneNo");
        this.source=intent.getStringExtra("source");
        this.dest=intent.getStringExtra("destination");
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), UserHome.class);
//        intent.putExtra("NAME", user);
//        intent.putExtra("phoneNo", phoneNo);
//        startActivity(intent);
//    }

    public void openScanner(View view) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, ReminderBroadcast.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,14);
        calendar.set(Calendar.MINUTE,57);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.AM_PM,Calendar.AM);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);

        Intent intent = new Intent(getApplicationContext(), QRScanner.class);
        intent.putExtra("user", user);
        intent.putExtra("garage", garage);
        intent.putExtra("startTime", startTime);
        intent.putExtra("hours", hours);
        intent.putExtra("slotNo", slotNo);
        intent.putExtra("endTime", endTime);
        intent.putExtra("destination", dest);
        intent.putExtra("source", source);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("bookingID", bookingID);
        intent.putExtra("whatNext", "Toentry");
        intent.putExtra("NAME","Not null");
        startActivity(intent);
    }
}