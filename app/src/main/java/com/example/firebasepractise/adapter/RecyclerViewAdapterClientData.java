package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasepractise.R;
import com.example.firebasepractise.fragment.ServiceUserFragment;
import com.example.firebasepractise.fragment.VenueUserFragment;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;

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
                holder.nameTextView.setText(servicePlanner.getServiceName());
                holder.descriptionTextView.setText(servicePlanner.getServiceDescription());
                holder.priceTextView.setText(String.valueOf(servicePlanner.getServicePrice()));
                holder.parentTextView.setText(servicePlanner.getServiceParentCategory());
                holder.childTextView.setText(servicePlanner.getServiceChildCategory());
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ServicePlanner servicePlannerClicked = serviceList.get(position);
                        serviceUserFragment.onBookedService(servicePlannerClicked);
                    }
                });
                break;
            case "venue":
                Venue venue = (Venue) venueList.get(position);
                holder.venueNameTextView.setText(venue.getName());
                holder.addressTextView.setText(venue.getAddress());
                holder.sizeTextView.setText(venue.getSize());
                holder.perHourTextView.setText(venue.getPerHourRent());
                holder.noOfGuestsTextView.setText(venue.getNoGuests());
                holder.attachedRoomsTextView.setText(venue.getAttachedRooms());
                holder.washroomsTextView.setText(venue.getAttachedRooms());
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Venue venueClicked = venueList.get(position);
                        venueUserFragment.onBookedVenue(venueClicked);
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

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            switch (listType) {
                case "service":
                    nameTextView = itemView.findViewById(R.id.nameTextView);
                    descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                    priceTextView = itemView.findViewById(R.id.priceTextView);
                    parentTextView = itemView.findViewById(R.id.parentCategoryTextView);
                    childTextView = itemView.findViewById(R.id.childCategoryTextView);
                    break;
                case "venue":
                    venueNameTextView = itemView.findViewById(R.id.venueNameTextView);
                    addressTextView = itemView.findViewById(R.id.addressTextView);
                    sizeTextView = itemView.findViewById(R.id.sizeTextView);
                    perHourTextView = itemView.findViewById(R.id.perHourRentTextView);
                    noOfGuestsTextView = itemView.findViewById(R.id.noOfGeustsTextView);
                    attachedRoomsTextView = itemView.findViewById(R.id.attachedRoomsTextView);
                    washroomsTextView = itemView.findViewById(R.id.washRoomsTextView);
                    break;
            }
            book = itemView.findViewById(R.id.bookButton);
        }
    }

    public interface RecyclerViewClientService {
        void onBookedService(ServicePlanner servicePlanner);
    }

    public interface RecyclerViewClientVenue {
        void onBookedVenue(Venue venue);
    }
}
