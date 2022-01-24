package com.example.vehiclesist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText etNumber;
    ImageButton btnContinue;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    String isMatched;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        etNumber = findViewById(R.id.etNumber);


        editor = getSharedPreferences("User", MODE_PRIVATE).edit();

        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isMatched = "false";

                if (TextUtils.isEmpty(etNumber.getText().toString()) || etNumber.getText().toString().length() < 10) {
                    etNumber.setError("Enter valid number");
                    etNumber.requestFocus();
                    return;
                }

                myRef.child("ServiceRecipient").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (("+92" + etNumber.getText().toString()).equals(ds.child("Number").getValue().toString())) {
                                editor.putString("user", "ServiceRecipient");
                                editor.apply();
                                loginUser();
                                isMatched = "true";
                            }
                        }

                        if (isMatched.equals("true")) {
                            Toast.makeText(Login.this, "User Found ,Sending code", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                myRef.child("ServiceProvider").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (("+92" + etNumber.getText().toString()).equals(ds.child("Number").getValue().toString())) {
                                editor.putString("user", "ServiceProvider");
                                editor.apply();
                                loginUser();
                                isMatched = "true";
                            }
                        }

                        if (isMatched.equals("true")) {
                            Toast.makeText(Login.this, "User Found ,Sending code", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }


    private void loginUser() {
        Intent intent = new Intent(Login.this, LogInVerifyPhone.class);
        intent.putExtra("number", etNumber.getText().toString());
        startActivity(intent);
    }

}