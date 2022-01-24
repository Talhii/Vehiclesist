package com.example.vehiclesist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.UUID;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
    Context context;
    List<RequestsModel> viewList;


    private FirebaseDatabase database;
    private DatabaseReference ref;

    private FirebaseAuth firebaseAuth;


    public RequestsAdapter(Context context, List<RequestsModel> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        if (viewList != null && viewList.size() > 0) {

            RequestsModel requestsModel = viewList.get(position);

            holder.tvViewName.setText(requestsModel.getfirstName() + " " + requestsModel.getlastName());
            holder.tvViewEmail.setText(requestsModel.getemail());
            holder.tvViewAddress.setText(requestsModel.getaddress());
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ref.child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                if (firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("ProviderNumber").getValue().toString()) && requestsModel.getNumber().equals(ds.child("RecipientNumber").getValue().toString()) && ds.child("Status").getValue().toString().equals("Requested")) {

                                    DatabaseReference statusReference = ds.getRef().child("Status");
                                    statusReference.setValue("Accepted");

                                    Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });


            holder.btnCheckStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ref.child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                if (firebaseAuth.getCurrentUser().getPhoneNumber().equals(ds.child("ProviderNumber").getValue().toString()) && requestsModel.getNumber().equals(ds.child("RecipientNumber").getValue().toString())) {

                                    Toast.makeText(context, ds.child("Status").getValue().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });


            holder.btnShowLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, MapsActivity.class);

                    double latitude = Double.parseDouble(requestsModel.getlatitude());
                    double longitude = Double.parseDouble(requestsModel.getlongitude());

                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);


                    context.startActivity(intent);
                }
            });
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvViewName, tvViewEmail, tvViewAddress;
        Button btnShowLocation, btnAccept, btnCheckStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            tvViewName = itemView.findViewById(R.id.tvViewName);
            tvViewEmail = itemView.findViewById(R.id.tvViewEmail);
            tvViewAddress = itemView.findViewById(R.id.tvViewAddress);

            btnShowLocation = itemView.findViewById(R.id.btnShowLocation);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnCheckStatus = itemView.findViewById(R.id.btnCheckStatus);

        }
    }
}
