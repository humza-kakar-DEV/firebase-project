package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasepractise.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserContentContainerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentUserContentContainerBinding binding;
    private Context context;

    public UserContentContainerFragment() {
    }

    public static UserContentContainerFragment newInstance(String param1, String param2) {
        UserContentContainerFragment fragment = new UserContentContainerFragment();
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
        binding = FragmentUserContentContainerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, ServiceUserFragment.newInstance(null, null)).commit();
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.servicePlanner:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, ServiceUserFragment.newInstance(null, null)).commit();
                        break;
                    case R.id.venue:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, ServiceUserFragment.newInstance(null, null)).commit();
                        break;
                    case R.id.booked:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, ServiceUserFragment.newInstance(null, null)).commit();
                        break;
                }
                return false;
            }
        });

        return view;
    }
}