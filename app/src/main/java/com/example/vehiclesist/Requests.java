package com.example.vehiclesist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Requests extends AppCompatActivity {
    RecyclerView recyclerView;
    RequestsAdapter viewAdapter;

    private FirebaseDatabase database;
    private DatabaseReference ref, serviceRecipientRef;

    private FirebaseAuth firebaseAuth;

    private SharedPreferences prefs;


    ArrayList<String> firstName, lastName, email, address, latitude, longitude, number;

    String recipientNumber, requestStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerView);

        prefs = getSharedPreferences("ProviderLocation", MODE_PRIVATE);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Request");

        serviceRecipientRef = database.getReference().child("ServiceRecipient");

        firebaseAuth = FirebaseAuth.getInstance();

        firstName = new ArrayList<String>();
        lastName = new ArrayList<String>();
        email = new ArrayList<String>();
        address = new ArrayList<String>();
        latitude = new ArrayList<String>();
        longitude = new ArrayList<String>();
        number = new ArrayList<String>();

    }

    private void getListView() {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                    if (firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds1.child("ProviderNumber").getValue().toString())) {

                        requestStatus = ds1.child("Status").getValue().toString();

                        serviceRecipientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds2 : dataSnapshot.getChildren()) {

                                    if (ds1.child("RecipientNumber").getValue().toString().equals(ds2.child("Number").getValue().toString())) {

                                        firstName.add(ds2.child("FirstName").getValue().toString());
                                        lastName.add(ds2.child("LastName").getValue().toString());
                                        email.add(ds2.child("Email").getValue().toString());
                                        address.add(ds2.child("Address").getValue().toString());
                                        latitude.add(ds2.child("Latitude").getValue().toString());
                                        longitude.add(ds2.child("Longitude").getValue().toString());
                                        number.add(ds2.child("Number").getValue().toString());

                                        setRecyclerView();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
        viewAdapter = new RequestsAdapter(this, getList());
        recyclerView.setAdapter(viewAdapter);
    }

    private List<RequestsModel> getList() {
        List<RequestsModel> viewList = new ArrayList<>();

        viewList.clear();

        for (int i = 0; i < firstName.size(); i++) {

            viewList.add(new RequestsModel(firstName.get(i), lastName.get(i), email.get(i), address.get(i), latitude.get(i), longitude.get(i), number.get(i)));
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
        latitude.clear();
        longitude.clear();
        number.clear();
        getListView();
    }
}