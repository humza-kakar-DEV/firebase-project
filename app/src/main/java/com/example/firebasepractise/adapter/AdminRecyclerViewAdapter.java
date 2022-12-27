package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasepractise.R;
import com.example.firebasepractise.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<AdminRecyclerViewAdapter.MyRecyclerViewHolder> {

    private List<Plan> planList = new ArrayList<>();
    private Context context;
    private RecyclerViewSwitchListener recyclerViewSwitchListener;
    private FragmentActivity fragmentActivity;

    public AdminRecyclerViewAdapter(Context context, List<Plan> planList, FragmentActivity fragmentActivity) {
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
        holder.titleTextView.setText(plan.getTitle());
        holder.dateTextView.setText(plan.getDate());
        holder.descriptionTextView.setText(plan.getDescription());
        Glide
                .with(context)
                .load(plan.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_cloud_upload_24)
                .into(holder.imageView);

        if (plan.isApproved()) {
            holder.yes.setChecked(true);
            holder.no.setChecked(false);
        } else {
            holder.yes.setChecked(false);
            holder.no.setChecked(true);
        }
        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Plan clickedPlan = planList.get(position);
                if (holder.yes.isChecked()) {
                    clickedPlan.setApproved(true);
                    recyclerViewSwitchListener.onListen(plan);
                }
            }
        });
        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Plan clickedPlan = planList.get(position);
                if (holder.no.isChecked()) {
                    clickedPlan.setApproved(false);
                    recyclerViewSwitchListener.onListen(plan);
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

        TextView titleTextView, descriptionTextView, dateTextView;
        RadioGroup radioGroup;
        RadioButton yes;
        RadioButton no;
        ImageView imageView;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            radioGroup = itemView.findViewById(R.id.toggle);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);
        }
    }

    public interface RecyclerViewSwitchListener {
        void onListen (Plan plan);
    }
}
