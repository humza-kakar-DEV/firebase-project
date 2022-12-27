package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasepractise.R;
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
    private FragmentActivity fragmentActivity;

    public RecyclerViewAdapterClientData(Context context, List<?> planList, String listType, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.listType = listType;
        this.planList = planList;
        switch (listType) {
            case "service":
                serviceList = (List<ServicePlanner>) planList;
                break;
            case "venue":
                venueList = (List<Venue>) planList;
                break;
        }
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_recycler_view_layout, parent, false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        switch (listType) {
            case "service":
                ServicePlanner servicePlanner = (ServicePlanner) serviceList.get(position);
                holder.titleTextView.setText(servicePlanner.getServiceChildCategory());
                holder.descriptionTextView.setText(servicePlanner.getServiceDescription());
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ServicePlanner servicePlannerClicked = serviceList.get(position);
                    }
                });
                break;
            case "venue":
                Venue venue = (Venue) venueList.get(position);
                holder.titleTextView.setText(venue.getName());
//                holder.descriptionTextView.setText(venueList.getServiceDescription());
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Venue venueClicked = venueList.get(position);
                    }
                });
                break;
        }
    }

    public void updateAdapter (List<?> planList, String listType) {
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

        TextView emailTextView, titleTextView, dateTextView, descriptionTextView;
        Button book;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            emailTextView = itemView.findViewById(R.id.emailEditText);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            book = itemView.findViewById(R.id.bookButton);
        }
    }

    public interface RecyclerViewClientService {
        void onListenService (String value);
    }

    public interface RecyclerViewClientVenue {
        void onListenVenue (String value);
    }
}
