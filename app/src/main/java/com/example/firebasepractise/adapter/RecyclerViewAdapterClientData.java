package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.CustomAlertDialog;
import com.example.firebasepractise.Util.ServiceVenueDialog;
import com.example.firebasepractise.fragment.ServiceUserFragment;
import com.example.firebasepractise.fragment.VenueUserFragment;
import com.example.firebasepractise.model.Booked;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterClientData extends RecyclerView.Adapter<RecyclerViewAdapterClientData.MyRecyclerViewHolder> {

    private List<ServicePlanner> serviceList = new ArrayList<>();
    private List<Venue> venueList = new ArrayList<>();
    private List<?> planList = new ArrayList<>();
    private Context context;
    private String listType;
    private ServiceUserFragment serviceUserFragment;
    private VenueUserFragment venueUserFragment;
    private FragmentActivity fragmentActivity;

    public RecyclerViewAdapterClientData(Context context, List<?> planList, String listType, Fragment fragment, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.listType = listType;
        this.planList = planList;
        switch (listType) {
            case "service":
                this.serviceUserFragment = (ServiceUserFragment) fragment;
                serviceList = (List<ServicePlanner>) planList;
                break;
            case "venue":
                this.venueUserFragment = (VenueUserFragment) fragment;
                venueList = (List<Venue>) planList;
                break;
        }
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = null;
        switch (listType) {
            case "service":
                view = layoutInflater.inflate(R.layout.user_recycler_view_layout, parent, false);
                break;
            case "venue":
                view = layoutInflater.inflate(R.layout.venue_user_recyclerview_layout, parent, false);
                break;
        }
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        switch (listType) {
            case "service":
                ServicePlanner servicePlanner = (ServicePlanner) serviceList.get(position);
                Glide
                        .with(context)
                        .load(servicePlanner.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_cloud_upload_24)
                        .into(holder.imageView);
                holder.nameTextView.setText(servicePlanner.getServiceName());
                holder.dateTextView.setText(servicePlanner.getDate());
                holder.descriptionTextView.setText(servicePlanner.getServiceDescription());
                holder.priceTextView.setText("Rs - " + String.valueOf(servicePlanner.getServicePrice()));
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ServicePlanner servicePlannerClicked = serviceList.get(position);
                        serviceUserFragment.onBookedService(servicePlannerClicked);
                    }
                });
                holder.serviceLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ServicePlanner clickedService = serviceList.get(position);
                        ServiceVenueDialog serviceVenueDialog = new ServiceVenueDialog(context, "service", clickedService);
                        serviceVenueDialog.show();
                    }
                });
                break;
            case "venue":
                Venue venue = (Venue) venueList.get(position);
                Glide
                        .with(context)
                        .load(venue.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_cloud_upload_24)
                        .into(holder.imageView);
                holder.venueNameTextView.setText(venue.getName());
                holder.dateTextView.setText(venue.getDate());
                holder.addressTextView.setText(venue.getAddress());
                holder.perHourTextView.setText("Rs - " + venue.getPerHourRent());
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Venue venueClicked = venueList.get(position);
                        venueUserFragment.onBookedVenue(venueClicked);
                    }
                });
                holder.venueLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Venue clickedVenue = venueList.get(position);
                        ServiceVenueDialog serviceVenueDialog = new ServiceVenueDialog(context, "venue", clickedVenue);
                        serviceVenueDialog.show();
                    }
                });
                break;
        }
    }

    public void updateAdapter(List<?> planList, String listType) {
        switch (listType) {
            case "service":
                this.serviceList.clear();
                this.serviceList.addAll((List<ServicePlanner>) planList);
                break;
            case "venue":
                this.venueList.clear();
                this.venueList.addAll((List<Venue>) planList);
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        //        service views
        TextView nameTextView, descriptionTextView, priceTextView, parentTextView, childTextView;

        //        venue views;
        TextView venueNameTextView, addressTextView, sizeTextView, perHourTextView, noOfGuestsTextView, attachedRoomsTextView, washroomsTextView;

        //        booking button same for both data types
        Button book;
        RoundedImageView imageView;
        TextView dateTextView;

        LinearLayout serviceLinearLayout, venueLinearLayout;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            switch (listType) {
                case "service":
                    nameTextView = itemView.findViewById(R.id.nameTextView);
                    descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                    priceTextView = itemView.findViewById(R.id.priceTextView);
                    serviceLinearLayout = itemView.findViewById(R.id.cardView);
                    break;
                case "venue":
                    venueNameTextView = itemView.findViewById(R.id.venueNameTextView);
                    addressTextView = itemView.findViewById(R.id.addressTextView);
                    perHourTextView = itemView.findViewById(R.id.perHourRentTextView);
                    venueLinearLayout = itemView.findViewById(R.id.cardView);
                    break;
            }
            book = itemView.findViewById(R.id.bookButton);
            imageView = itemView.findViewById(R.id.imageView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }

    public interface RecyclerViewClientService {
        void onBookedService(ServicePlanner servicePlanner);
    }

    public interface RecyclerViewClientVenue {
        void onBookedVenue(Venue venue);
    }
}
