package com.example.vehiclesist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton btnServiceRecipient,btnServiceProvider;
    TextView tvLogin;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        btnServiceRecipient = findViewById(R.id.btnServiceRecipient);
        btnServiceProvider = findViewById(R.id.btnServiceProvider);

        tvLogin = findViewById(R.id.tvLogin);


        editor = getSharedPreferences("User", MODE_PRIVATE).edit();

        btnServiceRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceRecipientSignUp.class);
                editor.putString("user", "ServiceRecipient");
                editor.apply();
                startActivity(intent);

            }
        });

        btnServiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceProviderSignUp.class);
                editor.putString("user", "ServiceProvider");
                editor.apply();
                startActivity(intent);
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}