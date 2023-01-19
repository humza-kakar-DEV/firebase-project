package com.example.firebasepractise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasepractise.R;
import com.example.firebasepractise.fragment.AdminFragment;
import com.example.firebasepractise.model.FeedBack;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;

import java.util.ArrayList;
import java.util.List;

public class FeedBackRecyclerView extends RecyclerView.Adapter<FeedBackRecyclerView.MyRecyclerViewHolder> {

    private final Context context;
    private List<FeedBack> feedBackList = new ArrayList<>();

    public FeedBackRecyclerView(Context context, List<FeedBack> feedBackList) {
        this.context = context;
        this.feedBackList = feedBackList;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.admin_feed_back_recycler_view, parent, false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        FeedBack feedBack = feedBackList.get(position);
        holder.plannerTextView.setText(feedBack.getPlannerEmail());
        holder.userTextView.setText(feedBack.getUserEmail());
        holder.typeTextView.setText(feedBack.getText());
        holder.ratingTextView.setText(feedBack.getRating() + " / " + "4");
    }

    @Override
    public int getItemCount() {
        return feedBackList.size();
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

        private TextView plannerTextView;
        private TextView userTextView;
        private TextView typeTextView;
        private TextView ratingTextView;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            plannerTextView = itemView.findViewById(R.id.plannerEmail);
            userTextView = itemView.findViewById(R.id.userEmail);
            ratingTextView = itemView.findViewById(R.id.ratingText);
            typeTextView = itemView.findViewById(R.id.typeTextView);
        }
    }
}
