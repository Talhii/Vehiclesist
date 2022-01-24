package com.example.vehiclesist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ServiceRecipientShareLocation extends AppCompatActivity {


    FusedLocationProviderClient mFusedLocationClient;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    TextView latitudeTextView, longitudeTextView, address;

    Button btnShareLocation,btnProceed;


    private SharedPreferences.Editor editor;

    int PERMISSION_ID = 44;

    String isShared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_recipient_share_location);

        getSupportActionBar().hide();

        latitudeTextView = findViewById(R.id.latTextView);
        longitudeTextView = findViewById(R.id.lonTextView);
        address = findViewById(R.id.address);


        editor = getSharedPreferences("RecipientLocation", MODE_PRIVATE).edit();

        btnShareLocation = findViewById(R.id.btnReloadAndShare);
        btnProceed = findViewById(R.id.btnProceed);

        firebaseAuth = FirebaseAuth.getInstance();
        isShared = "false";

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ServiceRecipient");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ServiceRecipientShareLocation.this, "Sometimes it may take time , restart app to share again if it does not works", Toast.LENGTH_SHORT).show();
                requestNewLocationData();
            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isShared.equals("true")){
                    Intent intent = new Intent(ServiceRecipientShareLocation.this,ServiceRecipient.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ServiceRecipientShareLocation.this, "Please update your CURRENT location First", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        try {
            // check if permissions are given
            if (checkPermissions()) {

                // check if location is enabled
                if (isLocationEnabled()) {

                    mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                             } else {

                                try {


                                    final Double latitude = location.getLatitude();
                                    final Double longitude = location.getLongitude();
                                    latitudeTextView.setText("Latitude: " + latitude + "");
                                    longitudeTextView.setText("Longitude: " + longitude + "");


                                    Geocoder geocoder = new Geocoder(ServiceRecipientShareLocation.this, Locale.getDefault());
                                    List<Address> addresses = null;

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {

                                    }


                                    final String cityName = addresses.get(0).getAddressLine(0);
                                    address.setText(cityName);

                                    mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                if ((firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("Number").getValue().toString()))) {
                                                    DatabaseReference latitudeRef = ds.getRef().child("Latitude");
                                                    DatabaseReference longitudeRef = ds.getRef().child("Longitude");
                                                    DatabaseReference addressRef = ds.getRef().child("Address");

                                                    latitudeRef.setValue(latitude.toString());
                                                    longitudeRef.setValue(longitude.toString());
                                                    addressRef.setValue(cityName);

                                                    editor.putString("latitude", latitude.toString());
                                                    editor.putString("longitude", longitude.toString());
                                                    editor.putString("address", cityName);
                                                    editor.apply();

                                                }

                                            }

                                            Toast.makeText(ServiceRecipientShareLocation.this, "Your current location is shared", Toast.LENGTH_SHORT).show();
                                            isShared = "true";

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Please turn on" + " your location first", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                // request for permissions
                requestPermissions();
            }
        } catch (Exception e) {

        }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1);
        mLocationRequest.setFastestInterval(1);
        mLocationRequest.setNumUpdates(1);


        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            try {
                if (mLastLocation == null) {
                    requestNewLocationData();
                } else {
                    try {

                        final Double latitude = mLastLocation.getLatitude();
                        final Double longitude = mLastLocation.getLongitude();
                        latitudeTextView.setText("Latitude: " + latitude + "");
                        longitudeTextView.setText("Longitude: " + longitude + "");


                        Geocoder geocoder = new Geocoder(ServiceRecipientShareLocation.this, Locale.getDefault());
                        List<Address> addresses = null;

                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        final String cityName = addresses.get(0).getAddressLine(0);
                        address.setText(cityName);

                        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    if ((firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("Number").getValue().toString()))) {
                                        DatabaseReference latitudeRef = ds.getRef().child("Latitude");
                                        DatabaseReference longitudeRef = ds.getRef().child("Longitude");
                                        DatabaseReference addressRef = ds.getRef().child("Address");

                                        latitudeRef.setValue(latitude.toString());
                                        longitudeRef.setValue(longitude.toString());
                                        addressRef.setValue(cityName);

                                        editor.putString("latitude", latitude.toString());
                                        editor.putString("longitude", longitude.toString());
                                        editor.putString("address", cityName);
                                        editor.apply();

                                       }
                                }

                                Toast.makeText(ServiceRecipientShareLocation.this, "Your Location is shared", Toast.LENGTH_SHORT).show();
                                isShared = "true";
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {

            }

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == PERMISSION_ID) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getLastLocation();
    }
}
