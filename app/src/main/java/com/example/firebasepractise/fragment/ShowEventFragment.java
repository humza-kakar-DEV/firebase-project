package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.adapter.UserRecyclerViewAdapter;
import com.example.firebasepractise.databinding.FragmentShowEventBinding;
import com.example.firebasepractise.databinding.FragmentUserBinding;
import com.example.firebasepractise.model.Plan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Plan> planList = new ArrayList<>();
    public UserRecyclerViewAdapter userRecyclerViewAdapter;
    private FragmentShowEventBinding binding;
    private Context context;

    public ShowEventFragment() {
        // Required empty public constructor
    }

    public static ShowEventFragment newInstance(List<Plan> planList) {
        ShowEventFragment fragment = new ShowEventFragment();
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
        binding = FragmentShowEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        planList.size();
        userRecyclerViewAdapter = new UserRecyclerViewAdapter(view.getContext(), planList, getActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recyclerView.setAdapter(userRecyclerViewAdapter);

        return view;
    }
}