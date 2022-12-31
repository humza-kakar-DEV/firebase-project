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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.CustomAlertDialog;
import com.example.firebasepractise.fragment.ServiceUserFragment;
import com.example.firebasepractise.fragment.VenueUserFragment;
import com.example.firebasepractise.model.Booked;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class UserBookedRecyclerViewAdapter extends RecyclerView.Adapter<UserBookedRecyclerViewAdapter.MyRecyclerViewHolder> {

    private List<Booked> bookedList = new ArrayList<>();
    private Context context;

    public UserBookedRecyclerViewAdapter(List<Booked> bookedList, Context context) {
        this.context = context;
        this.bookedList = bookedList;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_booked_layout, parent, false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        Booked booked = bookedList.get(position);
        switch (booked.getType()) {
            case "service":
                holder.nameTextView.setText(booked.getServiceName());
                holder.emailTextView.setText(booked.getPlannerEmail());
                holder.dateTextView.setText("22 - 22 - 22");
                holder.typeTextView.setText(booked.getType());
                break;
            case "venue":
                holder.nameTextView.setText(booked.getVenueName());
                holder.emailTextView.setText(booked.getPlannerEmail());
                holder.dateTextView.setText("22 - 22 - 22");
                holder.typeTextView.setText(booked.getType());
                break;
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booked clickedBook = bookedList.get(position);
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(context, clickedBook);
                customAlertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookedList.size();
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

        TextView nameTextView, emailTextView, dateTextView, typeTextView;
        LinearLayout linearLayout;
        CardView cardView;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            cardView = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public interface RecyclerViewClientService {
        void onBookedService (ServicePlanner servicePlanner);
    }

    public interface RecyclerViewClientVenue {
        void onBookedVenue (Venue venue);
    }
}

