package com.example.vehiclesist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.UUID;

public class ManageStatus extends AppCompatActivity {

    TextView tvStatus;
    Button btnMarkAsComplete,btnProceedToPayment;
    private FirebaseDatabase database;
    private DatabaseReference requestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_status);

        btnMarkAsComplete = findViewById(R.id.btnMarkAsComplete);
        btnProceedToPayment = findViewById(R.id.btnProceedToPayment);


        tvStatus = findViewById(R.id.tvStatus);


        database = FirebaseDatabase.getInstance();
        requestsRef = database.getReference().child("Request");



        requestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (getIntent().getStringExtra("recipientNumber").equals(ds.child("RecipientNumber").getValue().toString()) && getIntent().getStringExtra("providerNumber").equals(ds.child("ProviderNumber").getValue().toString())) {

                        tvStatus.setText(ds.child("Status").getValue().toString());
                        if(ds.child("Status").getValue().toString().equals("Requested")){
                            Toast.makeText(ManageStatus.this, "You cannot mark as complete or pay if it is not accepted by provider", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnMarkAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            if (getIntent().getStringExtra("recipientNumber").equals(ds.child("RecipientNumber").getValue().toString()) && getIntent().getStringExtra("providerNumber").equals(ds.child("ProviderNumber").getValue().toString())  && ds.child("Status").getValue().toString().equals("Accepted") ) {
                                DatabaseReference statusReference = ds.getRef().child("Status");
                                statusReference.setValue("Completed");

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        btnProceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvStatus.equals("Completed")){
                    Intent intent = new Intent(ManageStatus.this,GetPaymentData.class);
                    startActivity(intent);
                }
                else if(tvStatus.equals("Requested")){
                    Toast.makeText(ManageStatus.this, "Order is not accepted by provider yet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}