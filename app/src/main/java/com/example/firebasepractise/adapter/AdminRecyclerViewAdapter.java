package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasepractise.R;
import com.example.firebasepractise.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyRecyclerViewHolder> {

    private List<Plan> planList = new ArrayList<>();
    private Context context;
    private RecyclerViewSwitchListener recyclerViewSwitchListener;
    private FragmentActivity fragmentActivity;

    public RecyclerViewAdapter(Context context, List<Plan> planList, FragmentActivity fragmentActivity) {
        this.context = context;
        this.planList = planList;
        this.fragmentActivity = fragmentActivity;
        recyclerViewSwitchListener = (RecyclerViewSwitchListener) fragmentActivity;
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
        Plan plan = planList.get(position);
        holder.textView.setText(plan.getTitle());
        if (plan.isApproved()) {
            holder.aSwitch.setChecked(true);
        } else {
            holder.aSwitch.setChecked(false);
        }
        holder.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Plan clickedPlan = planList.get(position);
                if (holder.aSwitch.isChecked()) {
                    clickedPlan.setApproved(true);
                    recyclerViewSwitchListener.onListen(clickedPlan);
                } else {
                    clickedPlan.setApproved(false);
                    recyclerViewSwitchListener.onListen(clickedPlan);
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

        TextView textView;
        Switch aSwitch;
        Button submitButton;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            aSwitch = itemView.findViewById(R.id.switchView);
            submitButton = itemView.findViewById(R.id.submitButton);
        }
    }

    public interface RecyclerViewSwitchListener {
        void onListen (Plan plan);
    }
}
