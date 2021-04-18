package com.example.parkingsystem;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class Route extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionsResponse>, PermissionsListener {

    private MapView mapView;
    MapboxMap mapboxMap;
//    Point origin = Point.fromLngLat(73.515365, 19.55632);
//    Point destination = Point.fromLngLat(73.998665, 19.0032);
    Point origin, destination;
    double latS, lngS, latD, lngD;
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private MapboxDirections client;
    int startTime;
    int endTime;
    int hours;
    int slotNo;
    PermissionsManager permissionsManager;
    String user;
    String garage, phoneNo, source, dest, bookingID;

    @Override
    synchronized protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        garage=intent.getStringExtra("garage");
        bookingID=intent.getStringExtra("bookingID");
        startTime=intent.getIntExtra("startTime", 0);
        endTime=intent.getIntExtra("endTime", 0);
        hours=intent.getIntExtra("hours", 0);
        slotNo=intent.getIntExtra("slotNo", 0);
        phoneNo=intent.getStringExtra("phoneNo");
        source=intent.getStringExtra("source");
        dest=intent.getStringExtra("destination");

        Log.d("test", dest);

        loadDataS(source);
        loadDataD(dest);

        Log.d("testing", "here");

        permissionsManager = new PermissionsManager(this);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_route);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    synchronized void loadDataS(String c) {
        Request req = new Request.Builder().url("https://api.mapbox.com/geocoding/v5/mapbox.places/" + c + ".json?access_token=pk.eyJ1IjoiamF5ZXNoYmFkd2FsIiwiYSI6ImNraTIwNGZnYTJuMG0yenA1dXljNWQ1bXAifQ.swnZBtWw-tcslkN5sDF0hA&country=IN").get().build();
        OkHttpClient clientOKHttp = new OkHttpClient();
        clientOKHttp.newCall(req).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error in request", Toast.LENGTH_SHORT).show();
                }
                String respFromAPI = response.body().string();
                Log.d("RawData", respFromAPI);
                try {
                    JSONObject obj = new JSONObject(respFromAPI);
                    JSONArray array = obj.getJSONArray("features");
                    JSONObject jsonObject = array.getJSONObject(0);
                    JSONArray arr = jsonObject.getJSONArray("center");
                    double lng = arr.getDouble(0);
                    double lat = arr.getDouble(1);
                    lngS = lng;
                    latS = lat;
//                    origin = Point.fromLngLat(lng, lat);
                } catch (Exception e) {
                    Log.d("Error", "Not in json format");
                }
            }
        });
    }

    synchronized void loadDataD(String c) {
        Request req = new Request.Builder().url("https://api.mapbox.com/geocoding/v5/mapbox.places/" + c + ".json?access_token=pk.eyJ1IjoiamF5ZXNoYmFkd2FsIiwiYSI6ImNraTIwNGZnYTJuMG0yenA1dXljNWQ1bXAifQ.swnZBtWw-tcslkN5sDF0hA&country=IN").get().build();
        OkHttpClient clientOKHttp = new OkHttpClient();
        clientOKHttp.newCall(req).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error in request", Toast.LENGTH_SHORT).show();
                }
                String respFromAPI = response.body().string();
                Log.d("RawData", respFromAPI);
                try {
                    JSONObject obj = new JSONObject(respFromAPI);
                    JSONArray array = obj.getJSONArray("features");
                    JSONObject jsonObject = array.getJSONObject(0);
                    JSONArray arr = jsonObject.getJSONArray("center");
                    double lng = arr.getDouble(0);
                    double lat = arr.getDouble(1);
                    lngD = lng;
                    latD = lat;
                    Log.d("jayesh", String.valueOf(lngD));
                    Log.d("jayesh", String.valueOf(latD));
//                    destination = Point.fromLngLat(lng, lat);
                } catch (Exception e) {
                    Log.d("Error", "Not in json format");
                }
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

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
// Set the origin location to the Alhambra landmark in Granada, Spain.
                enableLocation(style);

                origin = Point.fromLngLat(lngS, latS);
                destination = Point.fromLngLat(lngD,latD);

// Set the destination location to the Plaza del Triunfo in Granada, Spain.
                initSource(style);

                initLayers(style);
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(origin.latitude(), origin.longitude()))
                        .zoom(18)
                        .tilt(13)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
// Get the directions route from the Mapbox Directions API
                getRoute(mapboxMap, origin, destination);
            }
        });
    }

    private void enableLocation(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

// Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.marker)));

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconOffset(new Float[] {0f, -9f})));
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

        if (response.body() == null) {
            Toast.makeText(this, "No routes found, make sure you set the right user and access token.", Toast.LENGTH_LONG);
            return;
        } else if (response.body().routes().size() < 1) {
            Toast.makeText(this, "No routes found", Toast.LENGTH_LONG);
            return;
        }

// Get the directions route
        DirectionsRoute currentRoute = response.body().routes().get(0);

        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
// Retrieve and update the source designated for showing the directions route
                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                    if (source != null) {
                        source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                    }
                }
            });
        }
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

    }

    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(Mapbox.getAccessToken())
                .build();

        client.enqueueCall(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocation(style);
                }
            });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public void callScanner(View view) {

        FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Bookings").child(bookingID).child("currentlyAt").setValue("scan1");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        startActivity(intent);
    }
}