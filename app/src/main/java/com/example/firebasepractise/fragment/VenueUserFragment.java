package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasepractise.AuthType;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.CommunicationInterface;
import com.example.firebasepractise.adapter.RecyclerViewAdapterClientData;
import com.example.firebasepractise.adapter.RecyclerViewChipList;
import com.example.firebasepractise.databinding.FragmentVenueBinding;
import com.example.firebasepractise.databinding.FragmentVenueUser2Binding;
import com.example.firebasepractise.databinding.FragmentVenueUserBinding;
import com.example.firebasepractise.model.Booked;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueUserFragment extends Fragment implements RecyclerViewChipList.RecyclerChipVenueListener
        , RecyclerViewAdapterClientData.RecyclerViewClientVenue {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FragmentVenueUser2Binding binding;
    private Context context;
    private RecyclerViewChipList recyclerviewChipListAdapter;
    private List<String> defaultList = new ArrayList<String>();
    private ArrayList<Venue> defaultContentList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerViewAdapterClientData recyclerViewAdapterClientData;
    private VenueUserFragment venueUserFragment;
    private Fragment fragment;
    private FirebaseAuth firebaseAuth;

    public VenueUserFragment() {
    }

    public static VenueUserFragment newInstance(String param1, String param2) {
        VenueUserFragment fragment = new VenueUserFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentVenueUser2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        // default configuration when fragment will load
        defaultConfiguration();

        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof RecyclerViewChipList.RecyclerChipVenueListener) {
                this.fragment = (Fragment) fragment;
            }
        }

        return view;
    }

    public void defaultConfiguration() {
//        firebase fire store
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

//        loading chips data
        firebaseFirestore.collection("Venue").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    defaultList.add(queryDocumentSnapshot.getString("name"));
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                recyclerviewChipListAdapter = new RecyclerViewChipList(getContext(), defaultList, getActivity(), fragment, "venue");
                LinearLayoutManager layoutManagerChips = new LinearLayoutManager(context);
                layoutManagerChips.setOrientation(LinearLayoutManager.HORIZONTAL);
                binding.recyclerView.setLayoutManager(layoutManagerChips);
                binding.recyclerView.setAdapter(recyclerviewChipListAdapter);
            }
        });

//        loading real content data
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Venue").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Venue venue = documentSnapshot.toObject(Venue.class);
                    if (venue.getApproved() == false) {
                        defaultContentList.add(venue);
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    recyclerViewAdapterClientData = new RecyclerViewAdapterClientData(getContext(), defaultContentList, "venue", fragment, getActivity());
                    LinearLayoutManager layoutManagerContentList = new LinearLayoutManager(context);
                    layoutManagerContentList.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.listRecyclerView.setLayoutManager(layoutManagerContentList);
                    binding.listRecyclerView.setAdapter(recyclerViewAdapterClientData);
                }
            }
        });
    }

    public void getDataFromFirebase(String text) {
        List<Venue> venueList = new ArrayList<>();
        firebaseFirestore.collection("Venue").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Venue venue = queryDocumentSnapshot.toObject(Venue.class);
                    if (venue.getName().equals(text) && venue.getApproved() == false) {
                        venueList.add(queryDocumentSnapshot.toObject(Venue.class));
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !venueList.isEmpty()) {
                    recyclerViewAdapterClientData.updateAdapter(venueList, "venue");
                }
            }
        });
    }

    @Override
    public void chipTextVenue(String value) {
        getDataFromFirebase(value);
    }

    @Override
    public void onBookedVenue(Venue venue) {
//        store booking data
        firebaseFirestore.collection("Booked").document().set(new Booked(venue.getEmail(), venue.getPhoneNumber(), firebaseAuth.getCurrentUser().getEmail(), "venue", venue.getName(), venue.getAddress(), venue.getSize(), venue.getPerHourRent(), venue.getNoGuests(), venue.getAttachedRooms(), venue.getWashRooms()));
    }
}