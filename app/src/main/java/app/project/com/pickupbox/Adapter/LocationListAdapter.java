package app.project.com.pickupbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.project.com.pickupbox.Data.LocationExample;
import app.project.com.pickupbox.Main_Page.PickupDetail;
import app.project.com.pickupbox.R;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationViewHolder>{
    private Context context;
    private ArrayList<LocationExample> locationList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public LocationListAdapter(Context context, ArrayList<LocationExample> locationList){
        this.context = context;
        this.locationList = locationList;
    }


    @NonNull
    @Override
    public LocationListAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_location,parent,false);
        LocationViewHolder holder = new LocationViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationListAdapter.LocationViewHolder holder, final int position) {
        holder.tvName.setText(locationList.get(position).getName());
        holder.tvLatitude.setText(locationList.get(position).getLatitude());
        holder.tvLongitude.setText(locationList.get(position).getLongitude());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PickupDetail.class);
                intent.putExtra("name",locationList.get(position).getName());
                intent.putExtra("latitude",locationList.get(position).getLatitude());
                intent.putExtra("longitude",locationList.get(position).getLongitude());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return (locationList != null ? locationList.size() : 0);
    }



    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvLatitude,tvLongitude;

        Button magnifier;


        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tvName = itemView.findViewById(R.id.name);
            this.tvLatitude = itemView.findViewById(R.id.myLatitude);
            this.tvLongitude = itemView.findViewById(R.id.myLongitude);




        }
    }
}
