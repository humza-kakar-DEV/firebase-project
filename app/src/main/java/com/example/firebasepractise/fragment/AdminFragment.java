package com.example.firebasepractise.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasepractise.R;
import com.example.firebasepractise.adapter.AdminRecyclerViewAdapter;
import com.example.firebasepractise.model.Plan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Plan> planList = new ArrayList<>();

    private RecyclerView recyclerView;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(List<Plan> planList) {
        AdminFragment fragment = new AdminFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        AdminRecyclerViewAdapter adminRecyclerViewAdapter = new AdminRecyclerViewAdapter(view.getContext(), planList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adminRecyclerViewAdapter);

        return view;
    }
}