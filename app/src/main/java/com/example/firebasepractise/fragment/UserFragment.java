package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.adapter.UserRecyclerViewAdapter;
import com.example.firebasepractise.databinding.FragmentPlannerBinding;
import com.example.firebasepractise.databinding.FragmentUserBinding;
import com.example.firebasepractise.model.Plan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.grpc.NameResolver;

public class UserFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List<Plan> planList = new ArrayList<>();
    public UserRecyclerViewAdapter userRecyclerViewAdapter;
    private RecyclerView recyclerView;

    private FragmentUserBinding binding;
    private Context context;

    public UserFragment() {
    }

    public static UserFragment newInstance(List<Plan> planList) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) planList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            planList = (List<Plan>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        ShowEventFragment showEventFragment = ShowEventFragment.newInstance(planList);
        BookedEventFragment bookedEventFragment = new BookedEventFragment();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, showEventFragment).commit();
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.servicePlanner:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, showEventFragment).commit();
                        break;
                    case R.id.venue:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, bookedEventFragment).commit();
                        break;
                    case R.id.booked:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, bookedEventFragment).commit();
                        break;
                }
                return false;
            }
        });

        return view;
    }
}