package com.example.vehiclesist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceProviderSignUp extends AppCompatActivity {

    EditText etFirstName,etLastName,etEmail,etNumber;
    ImageButton btnContinue;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    Spinner spinnerUserType;
    String isAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_sign_up);

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etNumber = findViewById(R.id.etNumber);


        spinnerUserType = new Spinner(this);
        spinnerUserType = findViewById(R.id.spinnerUserType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_type_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);


        btnContinue = findViewById(R.id.btnContinue);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isAllowed = "true";

                if(TextUtils.isEmpty(etFirstName.getText().toString())){
                    etFirstName.setError("Enter first name");
                    etFirstName.requestFocus();
                    return;
                } else if(TextUtils.isEmpty(etLastName.getText().toString())){
                    etLastName.setError("Enter last name");
                    etLastName.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(etEmail.getText().toString())){
                    etEmail.setError("Enter email");
                    etEmail.requestFocus();
                    return;
                }
                else if (!checkEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter valid email");
                    etEmail.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(etNumber.getText().toString()) || etNumber.getText().toString().length()<10 ){
                    etNumber.setError("Enter valid number");
                    etNumber.requestFocus();
                    return;
                }

                else {
                    myRef.child("ServiceProvider").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (("+92"+etNumber.getText().toString()).equals(ds.child("Number").getValue().toString())) {
                                    isAllowed = "false";
                                }
                            }

                            if(isAllowed.equals("true")){
                                registerUser();
                            }
                            else{
                                Toast.makeText(ServiceProviderSignUp.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void registerUser(){

        Intent intent = new Intent(ServiceProviderSignUp.this, SignUpVerifyPhone.class);
        intent.putExtra("firstName",etFirstName.getText().toString());
        intent.putExtra("lastName",etLastName.getText().toString());
        intent.putExtra("email",etEmail.getText().toString());
        intent.putExtra("number", etNumber.getText().toString());
        intent.putExtra("type",spinnerUserType.getSelectedItem().toString());

        startActivity(intent);

    }

    private boolean checkEmail(CharSequence mail)
    {
        return(!TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches());
    }
}