package com.example.firebasepractise;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.adapter.AdminRecyclerViewAdapter;
import com.example.firebasepractise.adapter.UserRecyclerViewAdapter;
import com.example.firebasepractise.fragment.AdminFragment;
import com.example.firebasepractise.fragment.PlannerFragment;
import com.example.firebasepractise.fragment.UserFragment;
import com.example.firebasepractise.model.Plan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainLayout extends AppCompatActivity implements UserRecyclerViewAdapter.RecyclerViewSwitchListener {

    FrameLayout frameLayout;
    List<Plan> planList = new ArrayList<>();
    FirebaseFirestore firebaseFirestore;
    UserFragment userfragment;
    PlannerFragment plannerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Utils.StatusBarColour(this, R.color.custom_dark_background);

        frameLayout = findViewById(R.id.frameLayout);

        firebaseFirestore = FirebaseFirestore.getInstance();
        plannerFragment = PlannerFragment.newInstance(null, null);

        switch (getIntent().getStringExtra(Constant.AUTH_TYPE)) {
            case Constant.PLANNER_ROLE:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(frameLayout.getId(), plannerFragment)
                        .commit();
                break;
            case Constant.ADMIN_ROLE:
                firebaseFirestore.collection(Constant.PLANS_TAG).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            planList.add(queryDocumentSnapshot.toObject(Plan.class));
                        }
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(frameLayout.getId(), AdminFragment.newInstance())
//                                .commit();
                    }
                });
                break;
            case Constant.USER_ROLE:
                firebaseFirestore.collection(Constant.PLANS_TAG).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (Objects.requireNonNull(documentSnapshot.toObject(Plan.class)).isApproved()) {
                                planList.add(documentSnapshot.toObject(Plan.class));
                            }
                        }
                        userfragment = UserFragment.newInstance(planList);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(frameLayout.getId(), UserFragment.newInstance(planList))
                                .commit();
                    }
                });
                break;
        }
    }

//    @Override
//    public void onListen(Plan plan) {
//        firebaseFirestore.collection(Constant.PLANS_TAG).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
//                    if (plan.getDocumentId().equals(queryDocumentSnapshot.getId())) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("approved", plan.isApproved());
//                        firebaseFirestore.collection(Constant.PLANS_TAG).document(plan.getDocumentId()).update(map);
//                    }
//                }
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        plannerFragment.uploadImage(data.getData());
    }

//    creating options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tab_menu_options, menu);
        return true;
    }

    @Override
    public void favourites(Plan plan) {

    }

}