package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasepractise.R;
import com.example.firebasepractise.databinding.FragmentUserBinding;
import com.example.firebasepractise.databinding.FragmentVenueBinding;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class VenueFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FragmentVenueBinding binding;

    public VenueFragment() {
    }

    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
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
        binding = FragmentVenueBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String perHourRent = binding.perHourRent.getText().toString();
                String address = binding.address.getText().toString();
                String size = binding.size.getText().toString();
                String noGuests = binding.noOfGuest.getText().toString();
                String attachRooms = binding.attachedRoom.getText().toString();
                String washRooms = binding.washRoom.getText().toString();
                Venue venue = new Venue(name, perHourRent, address, size, noGuests, attachRooms, washRooms);
                firebaseFirestore.collection("Venue").document().set(venue).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "data saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}