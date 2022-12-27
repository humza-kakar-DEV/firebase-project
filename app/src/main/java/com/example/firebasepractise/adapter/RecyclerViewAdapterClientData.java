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

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterClientData extends RecyclerView.Adapter<RecyclerViewAdapterClientData.MyRecyclerViewHolder> {

    private List<ServicePlanner> planList = new ArrayList<>();
    private Context context;
    private FragmentActivity fragmentActivity;

    public RecyclerViewAdapterClientData(Context context, List<ServicePlanner> planList, FragmentActivity fragmentActivity) {
        this.context = context;
        this.planList = planList;
        this.fragmentActivity = fragmentActivity;
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
        ServicePlanner servicePlanner = planList.get(position);
        holder.titleTextView.setText(servicePlanner.getServiceChildCategory());
        holder.descriptionTextView.setText(servicePlanner.getServiceDescription());
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServicePlanner servicePlannerClicked = planList.get(position);
            }
        });
    }

    public void updateAdapter (List<ServicePlanner> planList) {
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

    public interface RecyclerViewListener {
        void onListen (String value);
    }
}
