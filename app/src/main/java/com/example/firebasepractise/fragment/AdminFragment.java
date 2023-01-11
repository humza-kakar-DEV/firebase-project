package com.example.firebasepractise.fragment;

import android.app.Service;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.adapter.AdminRecyclerViewAdapter;
import com.example.firebasepractise.adapter.UserBookedRecyclerViewAdapter;
import com.example.firebasepractise.adapter.UserRecyclerViewAdapter;
import com.example.firebasepractise.databinding.FragmentAdminBinding;
import com.example.firebasepractise.databinding.FragmentAdminContainerBinding;
import com.example.firebasepractise.fragment.admin.AdminFragmentContainer;
import com.example.firebasepractise.fragment.admin.AdminServiceFragment;
import com.example.firebasepractise.model.Booked;
import com.example.firebasepractise.model.Plan;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment implements AdminRecyclerViewAdapter.RecyclerViewAdminListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String listType;

    private List<ServicePlanner> servicePlannerList = new ArrayList<>();
    private List<Venue> venueList = new ArrayList<>();
    private List<Booked> bookedList = new ArrayList<>();

    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;
    private FragmentAdminBinding binding;
    private AdminFragment adminFragment;
    private UserBookedRecyclerViewAdapter userBookedRecyclerViewAdapter;

    public AdminFragment() {
    }

    public static AdminFragment newInstance(String param1) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listType = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        firebaseFirestore = FirebaseFirestore.getInstance();

        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof AdminRecyclerViewAdapter.RecyclerViewAdminListener) {
                adminFragment = (AdminFragment) fragment;
            }
        }

        fetchData (listType);

        return view;
    }

    public void fetchData (String listType) {
        firebaseFirestore.collection(listType).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    switch (listType) {
                        case "Service":
                            servicePlannerList.add(queryDocumentSnapshot.toObject(ServicePlanner.class));
                            servicePlannerList.size();
                            break;
                        case "Venue":
                            venueList.add(queryDocumentSnapshot.toObject(Venue.class));
                            break;
                        case "Booked":
                            bookedList.add(queryDocumentSnapshot.toObject(Booked.class));
                            break;
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                AdminRecyclerViewAdapter adminRecyclerViewAdapter = null;
                servicePlannerList.size();
                switch (listType) {
                    case "Service":
                        adminRecyclerViewAdapter = new AdminRecyclerViewAdapter(getContext(), servicePlannerList, listType, adminFragment);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adminRecyclerViewAdapter);
                        break;
                    case "Venue":
                        adminRecyclerViewAdapter = new AdminRecyclerViewAdapter(getContext(), venueList, listType, adminFragment);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adminRecyclerViewAdapter);
                        break;
                    case "Booked":
                        userBookedRecyclerViewAdapter = new UserBookedRecyclerViewAdapter(bookedList, getContext(), getActivity(), "admin");
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(userBookedRecyclerViewAdapter);
                        break;
                }
            }
        });
//        switch (listType) {
//            case "service":
//
//                break;
//            case "venue":
//                firebaseFirestore.collection("Venue").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
//                            switch (listType) {
//                                case "service":
//                                    planList.add(queryDocumentSnapshot.toObject(Venue.class));
//                                    break;
//                                case "venue":
//                                    planList.add(queryDocumentSnapshot.toObject(Venue.class));
//                                    break;
//                            }
//                        }
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        AdminRecyclerViewAdapter adminRecyclerViewAdapter = new AdminRecyclerViewAdapter(getContext(), planList, listType, adminFragment);
//                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                        binding.recyclerView.setAdapter(adminRecyclerViewAdapter);
//                    }
//                });
//                break;
//        }
    }

    @Override
    public void servicePlannerListener(ServicePlanner servicePlanner) {
        Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
        firebaseFirestore.collection("Service").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    if (servicePlanner.getDocumentId().equals(queryDocumentSnapshot.getId())) {
                        Log.d("value00", "document founded");
                        Map<String, Object> map = new HashMap<>();
                        map.put("approved", servicePlanner.isApproved());
                        firebaseFirestore.collection("Service").document(servicePlanner.getDocumentId()).update(map);
                    }
                }
            }
        });
    }

    @Override
    public void venueListener(Venue venue) {
        Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
        firebaseFirestore.collection("Venue").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    if (venue.getDocumentId().equals(queryDocumentSnapshot.getId())) {
                        Log.d("value00", "document founded");
                        Map<String, Object> map = new HashMap<>();
                        map.put("approved", venue.getApproved());
                        firebaseFirestore.collection("Venue").document(venue.getDocumentId()).update(map);
                    }
                }
            }
        });
    }
}