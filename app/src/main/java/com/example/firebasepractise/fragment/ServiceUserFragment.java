package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasepractise.AuthType;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.CommunicationInterface;
import com.example.firebasepractise.adapter.RecyclerViewAdapterClientData;
import com.example.firebasepractise.adapter.RecyclerViewChipList;
import com.example.firebasepractise.databinding.FragmentVenueUserBinding;
import com.example.firebasepractise.model.Booked;
import com.example.firebasepractise.model.ServicePlanner;
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
import java.util.Objects;

public class ServiceUserFragment extends Fragment implements RecyclerViewChipList.RecyclerChipViewServiceListener
        , RecyclerViewAdapterClientData.RecyclerViewClientService {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    //    private FragmentVenueUserBinding binding;
    private Context context;
    private FragmentVenueUserBinding binding;
    private RecyclerViewChipList recyclerviewChipListAdapter;
    private List<String> defaultList = new ArrayList<String>(Arrays.asList("Affordable", "economical", "luxury"));
    private ArrayList<ServicePlanner> defaultContentList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerViewAdapterClientData recyclerViewAdapterClientData;
    private Fragment fragment;
    private FirebaseAuth firebaseAuth;

    public static ServiceUserFragment newInstance(String param1, String param2) {
        ServiceUserFragment fragment = new ServiceUserFragment();
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
        binding = FragmentVenueUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

//        default configuration when fragment will load
        defaultConfiguration();

        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof RecyclerViewChipList.RecyclerChipViewServiceListener) {
                this.fragment = (Fragment) fragment;
            }
        }

//      chips fragment  load adapter
        recyclerviewChipListAdapter = new RecyclerViewChipList(view.getContext(), defaultList, getActivity(), fragment, "service");
        LinearLayoutManager layoutManagerChips = new LinearLayoutManager(context);
        layoutManagerChips.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerView.setLayoutManager(layoutManagerChips);
        binding.recyclerView.setAdapter(recyclerviewChipListAdapter);

        ((AuthType) getActivity()).interfaceReference(new CommunicationInterface() {
            @Override
            public void fragmentListen(List<String> itemList) {
                recyclerviewChipListAdapter.updateAdapter(itemList);
            }
        });

        return view;
    }

    public void defaultConfiguration() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore.collection("Service").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    ServicePlanner servicePlanner = documentSnapshot.toObject(ServicePlanner.class);
                    if (servicePlanner.getServiceParentCategory().equals("Travel") && servicePlanner.getServiceChildCategory().equals("Affordable") && servicePlanner.isApproved() == false) {
                        defaultContentList.add(servicePlanner);
                    }
                }
                defaultContentList.size();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                recyclerViewAdapterClientData = new RecyclerViewAdapterClientData(getContext(), defaultContentList, "service", fragment, getActivity());
                LinearLayoutManager layoutManagerContentList = new LinearLayoutManager(context);
                layoutManagerContentList.setOrientation(LinearLayoutManager.VERTICAL);
                binding.listRecyclerView.setLayoutManager(layoutManagerContentList);
                binding.listRecyclerView.setAdapter(recyclerViewAdapterClientData);
            }
        });
    }

    public void getDataFromFirebase(String text) {
        List<ServicePlanner> servicePlanners = new ArrayList<>();
        firebaseFirestore.collection("Service").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    ServicePlanner servicePlanner = queryDocumentSnapshot.toObject(ServicePlanner.class);
                    if (servicePlanner.getServiceChildCategory().equals(text) && servicePlanner.isApproved() == false) {
                        servicePlanners.add(queryDocumentSnapshot.toObject(ServicePlanner.class));
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    recyclerViewAdapterClientData.updateAdapter(servicePlanners, "service");
                }
            }
        });
    }

    @Override
    public void chipTextService(String value) {
        getDataFromFirebase(value);
    }

    @Override
    public void onBookedService(ServicePlanner servicePlanner) {
//        store service planner data
        Toast.makeText(context, "email: " + servicePlanner.getEmail(), Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "phone number: " + servicePlanner.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        firebaseFirestore.collection("Booked").document().set(new Booked(servicePlanner.getEmail(), servicePlanner.getPhoneNumber(), firebaseAuth.getCurrentUser().getEmail(), "service", servicePlanner.getServiceName(), servicePlanner.getServiceDescription(), String.valueOf(servicePlanner.getServicePrice()), servicePlanner.getServiceParentCategory(), servicePlanner.getServiceChildCategory()));
    }
}