package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.databinding.FragmentVenueBinding;
import com.example.firebasepractise.model.ServicePlanner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentVenueBinding binding;
    private Context context;
    private List<String> spinnerList = new ArrayList<String>(Arrays.asList("Apple", "Banana", "Orange"));
    private FirebaseFirestore firebaseFirestore;

    private String parentCategory = "Travel";
    private String childCategory = "Affordable";

    public ServiceFragment() {
    }

    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVenueBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();

//        spinner adpater code
//        1. create adapter
//        2. create list
//        3. spinner view set adapter to view

        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(Arrays.asList("select category"))));
        binding.spinnerParentCategory.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Utils.parentCategory));

        binding.spinnerParentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (binding.spinnerParentCategory.getSelectedItem().toString()) {
                    case "Travel":
                        parentCategory = "Travel";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.travelItems));
                        break;
                    case "Catering":
                        parentCategory = "Catering";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.cateringItems));
                        break;
                    case "Decor":
                        parentCategory = "Decor";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.decorItems));
                        break;
                    case "DJ":
                        parentCategory = "DJ";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.djItems));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.spinnerChildCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                childCategory = binding.spinnerChildCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String description = binding.description.getText().toString();
//                int price = Integer.parseInt(binding.price.getText().toString());
                ServicePlanner servicePlanner = new ServicePlanner(description, name, parentCategory, childCategory, 112);
                firebaseFirestore.collection("Service").document().set(servicePlanner).addOnCompleteListener(new OnCompleteListener<Void>() {
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