package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasepractise.R;
import com.example.firebasepractise.fragment.ServiceUserFragment;
import com.example.firebasepractise.fragment.VenueUserFragment;
import com.example.firebasepractise.model.Venue;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewChipList extends RecyclerView.Adapter<RecyclerViewChipList.MyRecyclerViewHolder> {

    private List<String> planList = new ArrayList<>();
    private Context context;
    private String listType;
    private FragmentActivity fragmentActivity;
    private VenueUserFragment venueUserFragment;
    private ServiceUserFragment serviceUserFragment;

    public RecyclerViewChipList(Context context, List<String> planList, FragmentActivity fragmentActivity, Fragment fragment, String listType) {
        this.context = context;
        this.planList = planList;
        this.listType = listType;
        this.fragmentActivity = fragmentActivity;
        this.serviceUserFragment = serviceUserFragment;
        switch (listType) {
            case "venue":
                venueUserFragment = (VenueUserFragment) fragment;
                break;
            case "service":
                serviceUserFragment = (ServiceUserFragment) fragment;
                break;
        }
//        recyclerViewSwitchListener = (RecyclerViewSwitchListener) fragmentActivity;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recyclerview_chip_background, parent, false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        String plan = planList.get(position);
        holder.chip.setText(plan);
        holder.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (listType) {
                    case "venue":
                        venueUserFragment.chipTextVenue(planList.get(position));
                        break;
                    case "service":
                        serviceUserFragment.chipTextService(planList.get(position));
                        break;
                }
            }
        });
    }

    public void updateAdapter (List<String> planList) {
        this.planList.clear();
        this.planList.addAll(planList);
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

        private Chip chip;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            chip = itemView.findViewById(R.id.chip);
        }
    }

    public interface RecyclerChipViewServiceListener {
        void chipTextService (String value);
    }

    public interface RecyclerChipVenueListener {
        void chipTextVenue (String value);
    }
}

