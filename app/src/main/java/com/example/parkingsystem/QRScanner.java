package com.example.parkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class QRScanner extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "",str, oldS;
    boolean isEmail = false;
    int startTime;
    int endTime;
    int hours;
    int slotNo;
    String user;
    String garage, phoneNo, source, dest, bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        initViews();
        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        garage=intent.getStringExtra("garage");
        bookingID=intent.getStringExtra("bookingID");
        str=intent.getStringExtra("whatNext");
        slotNo=intent.getIntExtra("slotNo", 0);
        startTime = intent.getIntExtra("startTime", 0);
        endTime = intent.getIntExtra("endTime", 0);
        this.phoneNo = intent.getStringExtra("phoneNo");
        if(str.equals("Toentry")) {
            this.source = intent.getStringExtra("source");
            this.dest = intent.getStringExtra("destination");
            hours = intent.getIntExtra("hours", 0);
        }

    }
    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }
    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QRScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QRScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email!=null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                            } else {
                                isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);

                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void goToNext(View view) {
        if(str.equals("Toentry")){
            updateString("c");

            FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Bookings").child(bookingID).child("currentlyAt").setValue("scan2");

            Intent intent=new Intent(getApplicationContext(),EntrySuccessful.class);
            intent.putExtra("NAME",user);
            intent.putExtra("garage",garage);
            intent.putExtra("startTime", startTime);
            intent.putExtra("slotNo", slotNo);
            intent.putExtra("endTime", endTime);
            intent.putExtra("bookingID", bookingID);
            intent.putExtra("phoneNo", phoneNo);
            startActivity(intent);
        }
        else if(str.equals("feedback")){
            updateString("a");

            FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Bookings").child(bookingID).child("currentlyAt").setValue("Feedback");

            Intent intent=new Intent(getApplicationContext(),ShowFeedback.class);
            intent.putExtra("NAME",user);
            intent.putExtra("garage",garage);
            intent.putExtra("phoneNo", phoneNo);
            intent.putExtra("bookingID", bookingID);
            startActivity(intent);
        }
    }

    private void updateString(String c) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Parking Garages").child(garage).child("slots").child(String.valueOf(slotNo)).child("occupiedState");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String oldS = snapshot.getValue(String.class);
                String newS="";
                for(int i=0; i<startTime; i++)
                    newS+=oldS.charAt(i);
                for(int i=startTime; i<endTime; i++)
                    newS+=c;
                for(int i=endTime; i<24; i++)
                    newS+=oldS.charAt(i);
                reference.setValue(newS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}