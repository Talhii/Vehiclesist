package com.example.vehiclesist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SignUpVerifyPhone extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    private SharedPreferences prefs;


    String code, user, type, firstName, lastName, email, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verify_phone);

        getSupportActionBar().hide();
        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        editTextCode = findViewById(R.id.editTextCode);


        prefs = getSharedPreferences("User", MODE_PRIVATE);
        //getting mobile number from the previous activity
        //and sending the verification code to the number
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        number = getIntent().getStringExtra("number");
        type = getIntent().getStringExtra("type");
        code = "+92";

        sendVerificationCode((code + number));


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String number) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(SignUpVerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpVerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            final String randomKey = UUID.randomUUID().toString();
                            String num = code + number;

                            user = prefs.getString("user", "Not defined");

                            if (user.equals("ServiceRecipient")) {

                                myRef.child(user).child(randomKey).child("FirstName").setValue(firstName);
                                myRef.child(user).child(randomKey).child("LastName").setValue(lastName);
                                myRef.child(user).child(randomKey).child("Email").setValue(email);
                                myRef.child(user).child(randomKey).child("Number").setValue(num);
                                myRef.child(user).child(randomKey).child("Latitude").setValue("default");
                                myRef.child(user).child(randomKey).child("Longitude").setValue("default");
                                myRef.child(user).child(randomKey).child("Address").setValue("default");

                                Intent intent = new Intent(SignUpVerifyPhone.this, ServiceRecipient.class);
                                startActivity(intent);
                                finish();

                            } else if (user.equals("ServiceProvider")) {

                                myRef.child(user).child(randomKey).child("FirstName").setValue(firstName);
                                myRef.child(user).child(randomKey).child("LastName").setValue(lastName);
                                myRef.child(user).child(randomKey).child("Email").setValue(email);
                                myRef.child(user).child(randomKey).child("Number").setValue(num);
                                myRef.child(user).child(randomKey).child("Type").setValue(type);
                                myRef.child(user).child(randomKey).child("Latitude").setValue("default");
                                myRef.child(user).child(randomKey).child("Longitude").setValue("default");
                                myRef.child(user).child(randomKey).child("Address").setValue("default");
                                myRef.child(user).child(randomKey).child("ImageUrl").setValue("default");
                                myRef.child(user).child(randomKey).child("Rating").setValue("default");

                                Intent intent = new Intent(SignUpVerifyPhone.this, ServiceProvider.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Something went wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

}