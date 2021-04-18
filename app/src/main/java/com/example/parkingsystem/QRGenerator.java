package com.example.parkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;



public class QRGenerator extends AppCompatActivity {

    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);

        Intent intent = getIntent();
        this.s=intent.getStringExtra("NAME");//replace with intent
        drawQRCode();
    }
    void drawQRCode(){

        String url="https://chart.apis.google.com/chart?cht=qr&chs=500x500&chl="+this.s;
        final ImageView imageView = findViewById(R.id.imageView);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>(){
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 500, 500, ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(imageRequest);
    }

    public void retry(View view) {
        drawQRCode();
    }
}