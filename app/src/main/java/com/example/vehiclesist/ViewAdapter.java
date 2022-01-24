package com.example.vehiclesist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    Context context;
    List<ViewModel> viewList;


    private FirebaseDatabase database;
    private DatabaseReference ref;

    private FirebaseAuth firebaseAuth;




    public ViewAdapter(Context context, List<ViewModel> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_providers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        if (viewList != null && viewList.size() > 0) {

            ViewModel viewModel = viewList.get(position);

            holder.providerName.setText(viewModel.getfirstName() + " " + viewModel.getlastName());
            holder.providerEmail.setText(viewModel.getemail());
            holder.providerAddress.setText(viewModel.getaddress());
            holder.providerRating.setText(viewModel.getrating());

            holder.btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String randomKey = UUID.randomUUID().toString();


                    ref.child("Request").child(randomKey).child("RecipientNumber").setValue(firebaseAuth.getCurrentUser().getPhoneNumber());
                    ref.child("Request").child(randomKey).child("ProviderNumber").setValue(viewModel.getNumber());
                    ref.child("Request").child(randomKey).child("Status").setValue("Requested");


                }
            });


            holder.btnManageStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ManageStatus.class);
                    intent.putExtra("providerNumber",viewModel.getNumber());
                    intent.putExtra("recipientNumber",firebaseAuth.getCurrentUser().getPhoneNumber());

                    context.startActivity(intent);
                }
            });

            holder.btnShowLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, MapsActivity.class);

                    double latitude = Double.parseDouble(viewModel.getlatitude());
                    double longitude = Double.parseDouble(viewModel.getlongitude());

                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude );


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

        TextView providerEmail,providerName,providerAddress,providerRating;
        Button btnShowLocation,btnRequest,btnManageStatus ;

        public ViewHolder(View itemView) {
            super(itemView);

            providerName = itemView.findViewById(R.id.providerName);
            providerEmail = itemView.findViewById(R.id.providerEmail);
            providerAddress = itemView.findViewById(R.id.providerAddress);
            providerRating = itemView.findViewById(R.id.providerRating);

            btnShowLocation = itemView.findViewById(R.id.btnShowLocation);
            btnRequest = itemView.findViewById(R.id.btnRequest);
            btnManageStatus = itemView.findViewById(R.id.btnManageStatus);

        }


    }


}
