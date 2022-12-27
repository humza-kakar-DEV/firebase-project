package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasepractise.R;
import com.example.firebasepractise.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.MyRecyclerViewHolder> {

    private List<Plan> planList = new ArrayList<>();
    private Context context;
    private RecyclerViewSwitchListener recyclerViewSwitchListener;
    private FragmentActivity fragmentActivity;

    public UserRecyclerViewAdapter(Context context, List<Plan> planList, FragmentActivity fragmentActivity) {
        this.context = context;
        this.planList = planList;
        this.fragmentActivity = fragmentActivity;
        recyclerViewSwitchListener = (RecyclerViewSwitchListener) fragmentActivity;
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
        Plan plan = planList.get(position);
        holder.titleTextView.setText(plan.getTitle());
        holder.dateTextView.setText(plan.getDate());
        holder.descriptionTextView.setText(plan.getDescription());
    }

    public void updateAdapter (List<Plan> planList) {
//        this.planList.clear();
//        planList.addAll(planList);
//        notify();
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

        TextView titleTextView, descriptionTextView, dateTextView;
        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

    public interface RecyclerViewSwitchListener {
        void favourites (Plan plan);
    }
}
