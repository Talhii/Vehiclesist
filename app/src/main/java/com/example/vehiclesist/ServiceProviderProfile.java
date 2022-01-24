package com.example.vehiclesist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class ServiceProviderProfile extends AppCompatActivity {

    private TextView tvFirstName, tvLastName, tvNumber, tvEmail, tvType;
    private ImageView tvImage;

    StorageReference storageReference;

    public Uri image_uri;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);


        getSupportActionBar().hide();

        tvImage = findViewById(R.id.tvImage);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvNumber = findViewById(R.id.tvNumber);
        tvEmail = findViewById(R.id.tvEmail);
        tvType = findViewById(R.id.tvType);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getReference();


        tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void editFirstName(View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_name, null);
        final EditText etDialogFirstName = alertLayout.findViewById(R.id.etDialogFirstName);
        etDialogFirstName.setText(tvFirstName.getText().toString());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Edit First Name");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstName = etDialogFirstName.getText().toString();

                databaseReference.child("ServiceProvider").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            if ((firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("Number").getValue().toString()))) {
                                DatabaseReference firstNameReference = ds.getRef().child("FirstName");
                                firstNameReference.setValue(firstName);
                                onResume();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                etDialogFirstName.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void editLastName(View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_last_name, null);
        final EditText etDialogLastName = alertLayout.findViewById(R.id.etDialogLastName);
        etDialogLastName.setText(tvLastName.getText().toString());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit Last Name");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String lastName = etDialogLastName.getText().toString();

                databaseReference.child("ServiceProvider").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            if ((firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("Number").getValue().toString()))) {
                                DatabaseReference lastNameReference = ds.getRef().child("LastName");
                                lastNameReference.setValue(lastName);
                                onResume();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                etDialogLastName.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void editEmail(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_email, null);
        final EditText etDialogEmail = alertLayout.findViewById(R.id.etDialogEmail);
        etDialogEmail.setText(tvEmail.getText().toString());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit Email");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = etDialogEmail.getText().toString();

                databaseReference.child("ServiceProvider").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            if ((firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("Number").getValue().toString()))) {
                                DatabaseReference emailReference = ds.getRef().child("Email");
                                emailReference.setValue(email);
                                onResume();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                etDialogEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceProviderProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            image_uri = data.getData();
            tvImage.setImageURI(image_uri);

            uploadData();

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "data", null);
        return Uri.parse(path);
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void uploadData() {

        if (image_uri != null) {

            StorageReference storageReference2nd = storageReference.child("ServiceProviderProfile/" + firebaseAuth.getCurrentUser().getPhoneNumber());

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.show();

            storageReference2nd.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String photoLink = uri.toString();
                            databaseReference.child("ServiceProvider").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        if ((firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("Number").getValue().toString()))) {
                                            DatabaseReference imageUrlReference = ds.getRef().child("ImageUrl");
                                            imageUrlReference.setValue(photoLink);
                                            onResume();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ServiceProviderProfile.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress_percentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded Percentage: " + (int) progress_percentage);
                }
            });

        } else {
            Toast.makeText(this, "Image must be selected", Toast.LENGTH_SHORT).show();
        }
    }


    public void navigateLogOut(View v) {
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference.child("ServiceProvider").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if ((firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("Number").getValue().toString()))) {
                        tvFirstName.setText(ds.child("FirstName").getValue().toString());
                        tvLastName.setText(ds.child("LastName").getValue().toString());
                        tvEmail.setText(ds.child("Email").getValue().toString());
                        tvNumber.setText(ds.child("Number").getValue().toString());
                        tvType.setText(ds.child("Type").getValue().toString());

                        if (!ds.child("ImageUrl").getValue().toString().equals("default")) {
                            Glide.with(ServiceProviderProfile.this).load(ds.child("ImageUrl").getValue().toString()).centerInside().into(tvImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}