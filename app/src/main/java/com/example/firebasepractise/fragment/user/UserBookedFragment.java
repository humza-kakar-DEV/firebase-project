package com.example.firebasepractise.fragment.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasepractise.R;
import com.example.firebasepractise.adapter.UserBookedRecyclerViewAdapter;
import com.example.firebasepractise.databinding.FragmentUserBookedBinding;
import com.example.firebasepractise.model.Booked;
import com.example.firebasepractise.model.FeedBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserBookedFragment extends Fragment implements UserBookedRecyclerViewAdapter.FeekBackInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentUserBookedBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private List<Booked> bookedList = new ArrayList<>();
    private UserBookedFragment userBookedFragment;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadCastReceiver;

    public static UserBookedFragment newInstance(String param1, String param2) {
        UserBookedFragment fragment = new UserBookedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserBookedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBookedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        intentFilter = new IntentFilter("com.example.firebasepractise.feedBack");
        broadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.example.firebasepractise.feedBack")) {
                    String feedBack = intent.getStringExtra("feedBackText");
                    String ratingText = intent.getStringExtra("ratingText");
                    Booked booked = (Booked) intent.getSerializableExtra("serializable");
                    writeFeedBack(feedBack, ratingText, booked);
                }
            }
        };

        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof UserBookedRecyclerViewAdapter.FeekBackInterface) {
                userBookedFragment = (UserBookedFragment) fragment;
            }
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        defaultConfiguration(userBookedFragment);

        return view;
    }

    public void writeFeedBack(String feedBackText, String rating, Booked booked) {
        FeedBack feedBack = new FeedBack(booked.getPlannerEmail(), booked.getUserEmail(), feedBackText, rating);
        firebaseFirestore.collection("FeedBack").document().set(feedBack)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "complete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(broadCastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadCastReceiver);
    }

    public void defaultConfiguration(UserBookedFragment userBookedFragment) {
        firebaseFirestore.collection("Booked").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    bookedList.add(queryDocumentSnapshot.toObject(Booked.class));
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    bookedList.size();
                    UserBookedRecyclerViewAdapter userBookedRecyclerViewAdapter = new UserBookedRecyclerViewAdapter(bookedList, getContext(), getActivity(), "user");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.recyclerView.setLayoutManager(linearLayoutManager);
                    binding.recyclerView.setAdapter(userBookedRecyclerViewAdapter);
                }
            }
        });
    }

    @Override
    public void feedBack() {

    }
}