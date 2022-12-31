package com.example.firebasepractise.fragment.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasepractise.R;
import com.example.firebasepractise.databinding.FragmentAdminBinding;
import com.example.firebasepractise.databinding.FragmentAdminContainerBinding;
import com.example.firebasepractise.fragment.AdminFragment;
import com.example.firebasepractise.fragment.planner.ServiceFragment;
import com.example.firebasepractise.fragment.planner.VenueFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminFragmentContainer extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String listType;
    private FragmentAdminContainerBinding binding;

    public AdminFragmentContainer() {
    }

    public static AdminFragmentContainer newInstance(String param1, String param2) {
        AdminFragmentContainer fragment = new AdminFragmentContainer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        binding = FragmentAdminContainerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, AdminFragment.newInstance("Service")).commit();
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.servicePlanner:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, AdminFragment.newInstance("Service")).commit();
                        break;
                    case R.id.venue:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, AdminFragment.newInstance("Venue")).commit();
                        break;
                    case R.id.userBooked:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, AdminFragment.newInstance("Booked")).commit();
                        break;
                }
                return false;
            }
        });

        return view;
    }
}