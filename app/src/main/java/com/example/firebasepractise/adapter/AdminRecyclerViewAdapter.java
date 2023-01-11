package com.example.firebasepractise.adapter;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasepractise.R;
import com.example.firebasepractise.fragment.AdminFragment;
import com.example.firebasepractise.model.Plan;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;

import java.util.ArrayList;
import java.util.List;

public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<AdminRecyclerViewAdapter.MyRecyclerViewHolder> {

    private List<ServicePlanner> servicePlannerList = new ArrayList<>();
    private List<Venue> venueList = new ArrayList<>();
    private Context context;
    private String listType;
    private List<?> planList = new ArrayList<>();
    private AdminFragment adminFragment;
    private FragmentActivity fragmentActivity;

    public AdminRecyclerViewAdapter(Context context, List<?> planList, String listType, AdminFragment adminFragment) {
        this.context = context;
        this.listType = listType;
        this.planList = planList;
        this.adminFragment = adminFragment;
        switch (listType) {
            case "Service":
                servicePlannerList = (List<ServicePlanner>) planList;
                break;
            case "Venue":
                venueList = (List<Venue>) planList;
                break;
        }
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.admin_recycler_view_layout, parent, false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        switch (listType) {
            case "Service":
                ServicePlanner servicePlanner = servicePlannerList.get(position);
                Glide
                        .with(context)
                        .load(servicePlanner.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_cloud_upload_24)
                        .into(holder.imageView);
                holder.titleTextView.setText(servicePlanner.getServiceName());
                holder.descriptionTextView.setText(servicePlanner.getServiceDescription());
                holder.dateTextView.setText(servicePlanner.getDate());
                holder.priceTextView.setText(String.valueOf(servicePlanner.getServicePrice()));
                if (servicePlanner.isApproved()) {
                    holder.yes.setChecked(true);
                    holder.no.setChecked(false);
                } else {
                    holder.yes.setChecked(false);
                    holder.no.setChecked(true);
                }
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "Venue":
                Venue venue = venueList.get(position);
                Glide
                        .with(context)
                        .load(venue.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_cloud_upload_24)
                        .into(holder.imageView);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.titleTextView.setText(venue.getName());
                holder.descriptionTextView.setText(venue.getAddress());
                holder.dateTextView.setText(venue.getDate());
                holder.priceTextView.setText(venue.getPerHourRent());
                if (venue.getApproved()) {
                    holder.yes.setChecked(true);
                    holder.no.setChecked(false);
                } else {
                    holder.yes.setChecked(false);
                    holder.no.setChecked(true);
                }
                break;
        }

//        button clicked
        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (listType) {
                    case "Service":
                        ServicePlanner servicePlanner = servicePlannerList.get(position);
                        if (holder.yes.isChecked()) {
                            servicePlanner.setApproved(true);
                            adminFragment.servicePlannerListener(servicePlanner);
                        }
                        break;
                    case "Venue":
                        Venue venue = venueList.get(position);
                        if (holder.yes.isChecked()) {
                            venue.setApproved(true);
                            adminFragment.venueListener(venue);
                        }
                        break;
                }
            }
        });

        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (listType) {
                    case "Service":
                        ServicePlanner servicePlanner = servicePlannerList.get(position);
                        if (holder.no.isChecked()) {
                            servicePlanner.setApproved(false);
                            adminFragment.servicePlannerListener(servicePlanner);
                        }
                        break;
                    case "Venue":
                        Venue venue = venueList.get(position);
                        if (holder.no.isChecked()) {
                            venue.setApproved(false);
                            adminFragment.venueListener(venue);
                        }
                        break;
                }
            }
        });
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

        TextView titleTextView, descriptionTextView, dateTextView, priceTextView;
        RadioGroup radioGroup;
        RadioButton yes;
        RadioButton no;
        ImageView imageView;
        LinearLayout cardView;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            titleTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            radioGroup = itemView.findViewById(R.id.toggle);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface RecyclerViewAdminListener {
        void servicePlannerListener(ServicePlanner servicePlanner);

        void venueListener(Venue venue);
    }
}
