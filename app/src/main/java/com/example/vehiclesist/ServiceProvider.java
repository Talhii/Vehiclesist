package com.example.vehiclesist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ServiceProvider extends AppCompatActivity {

    TextView btnShareLocation,btnProfile, btnRequest,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);


        getSupportActionBar().hide();

        btnShareLocation = findViewById(R.id.btnShareLocation);
        btnProfile = findViewById(R.id.btnProfile);
        btnRequest = findViewById(R.id.btnRequest);

        btnLogout = findViewById(R.id.btnLogout);

        btnShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceProvider.this, ServiceProviderShareLocation.class);
                intent.putExtra("user","serviceProvider");
                startActivity(intent);

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceProvider.this,ServiceProviderProfile.class);
                startActivity(intent);

            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceProvider.this, Requests.class);
                startActivity(intent);
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(ServiceProvider.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });
    }
}