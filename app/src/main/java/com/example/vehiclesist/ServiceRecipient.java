package com.example.vehiclesist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ServiceRecipient extends AppCompatActivity {


    TextView tvFindMechanic,tvFindTowTruck,tvRequestDelivery;
    ImageButton ibLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_recipient);

        getSupportActionBar().hide();

        tvFindMechanic = findViewById(R.id.tvFindMechanic);
        tvFindTowTruck = findViewById(R.id.tvFindTowTruck);
        tvRequestDelivery = findViewById(R.id.tvRequestDelivery);
        ibLogout = findViewById(R.id.ibLogout);

        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ServiceRecipient.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


        tvFindMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceRecipient.this,Providers.class);
                intent.putExtra("type","Mechanic");
                startActivity(intent);
            }
        });


        tvFindTowTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceRecipient.this,Providers.class);
                intent.putExtra("type","Tow Truck");
                startActivity(intent);
            }
        });


        tvRequestDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceRecipient.this,Providers.class);
                intent.putExtra("type","Petrol Delivery Guy");
                startActivity(intent);
            }
        });
    }
}