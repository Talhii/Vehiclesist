package com.example.vehiclesist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();


        prefs = getSharedPreferences("User", MODE_PRIVATE);

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser == null) {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);

            }
            if (firebaseUser != null) {


                String user = prefs.getString("user", "Not defined");
                new Handler().postDelayed(new Runnable() {


                    @Override
                    public void run() {

                        if(user.equals("ServiceProvider")){
                            Intent intent = new Intent(Splash.this, ServiceProvider.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (user.equals("ServiceRecipient")){
                            Intent intent = new Intent(Splash.this, ServiceRecipientShareLocation.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 2000);
            }
        }
    };
}