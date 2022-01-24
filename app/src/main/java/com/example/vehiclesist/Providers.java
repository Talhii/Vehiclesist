package com.example.vehiclesist;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class Providers extends AppCompatActivity {

    RecyclerView recyclerView;
    ViewAdapter viewAdapter;

    private FirebaseDatabase database;
    private DatabaseReference ref, requestsRef;

    private FirebaseAuth firebaseAuth;

    String recipientLatitude,recipientLongitude,recipientAddress;

    private SharedPreferences prefs;

    LatLng recipientLatLng ;
    LatLng providerLatLng ;
    Double distance;

    ArrayList<String> firstName, lastName, email, address, rating, latitude, longitude,number ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers);

        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerView);


        prefs = getSharedPreferences("RecipientLocation", MODE_PRIVATE);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("ServiceProvider");
        requestsRef = database.getReference().child("Request");

        firebaseAuth = FirebaseAuth.getInstance();

        firstName = new ArrayList<String>();
        lastName = new ArrayList<String>();
        email = new ArrayList<String>();
        address = new ArrayList<String>();
        rating = new ArrayList<String>();
        latitude = new ArrayList<String>();
        longitude = new ArrayList<String>();
        number = new ArrayList<String>();
    }

    private void getListView() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recipientLatitude = prefs.getString("latitude", "Not defined");
                recipientLongitude = prefs.getString("longitude", "Not defined");

                recipientLatLng = new LatLng(Double.valueOf(recipientLatitude), Double.valueOf(recipientLongitude));


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    providerLatLng = new LatLng(Double.valueOf(ds.child("Latitude").getValue().toString()), Double.valueOf(ds.child("Longitude").getValue().toString()));
                    
                    distance = SphericalUtil.computeDistanceBetween(recipientLatLng, providerLatLng)/1000;

                    if (getIntent().getStringExtra("type").equals(ds.child("Type").getValue().toString()) && distance <= 5.0) {

                        firstName.add(ds.child("FirstName").getValue().toString());
                        lastName.add(ds.child("LastName").getValue().toString());
                        email.add(ds.child("Email").getValue().toString());
                        address.add(ds.child("Address").getValue().toString());
                        rating.add(ds.child("Rating").getValue().toString());
                        latitude.add(ds.child("Latitude").getValue().toString());
                        longitude.add(ds.child("Longitude").getValue().toString());
                        number.add(ds.child("Number").getValue().toString());



                        setRecyclerView();


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAdapter = new ViewAdapter(this, getList());
        recyclerView.setAdapter(viewAdapter);
    }

    private List<ViewModel> getList() {
        List<ViewModel> viewList = new ArrayList<>();

        viewList.clear();

        for (int i = 0; i < firstName.size() ; i++) {

            viewList.add(new ViewModel(firstName.get(i), lastName.get(i), email.get(i), address.get(i), rating.get(i), latitude.get(i), longitude.get(i),number.get(i) ));
        }

        return viewList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstName.clear();
        lastName.clear();
        email.clear();
        address.clear();
        rating.clear();
        latitude.clear();
        longitude.clear();
        number.clear();
        getListView();
    }
}