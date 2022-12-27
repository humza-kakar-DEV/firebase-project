package com.example.firebasepractise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.firebasepractise.Util.CommunicationInterface;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.databinding.ActivityAuthTypeBinding;
import com.example.firebasepractise.fragment.LoginFragment;
import com.example.firebasepractise.fragment.RegisterFragment;
import com.example.firebasepractise.fragment.UserContentContainerFragment;
import com.example.firebasepractise.fragment.planner.PlannerFragmentContainer;
import com.example.firebasepractise.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AuthType extends AppCompatActivity implements LoginFragment.LoginFragmentInterface
        , RegisterFragment.RegisterFragmentListener {

    Button plannerButton, adminButton, userButton;
    private ActivityAuthTypeBinding binding;
    private FrameLayout frameLayout;
    private UserContentContainerFragment venueUserFragment;
    private String role;
    private CommunicationInterface communicationInterface;
    private int increment = 0;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthTypeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Utils.StatusBarColour(this, R.color.custom_dark_background);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        venueUserFragment = UserContentContainerFragment.newInstance(null, null);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            checkCurrentUserRoleFromDatabase();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(frameLayout.getId(), LoginFragment.newInstance(null, null))
                    .commit();
        }
    }

//    create home fragment
    public void homeFragment () {

    }

//    creating options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tab_menu_options, menu);
        return true;
    }

//    no usage of this options while filtering
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                communicationInterface.fragmentListen(Utils.travelItems);
                return true;
            case R.id.item2:
                communicationInterface.fragmentListen(Utils.cateringItems);
                return true;
            case R.id.item3:
                communicationInterface.fragmentListen(Utils.decorItems);
                return true;
            case R.id.item4:
                communicationInterface.fragmentListen(Utils.djItems);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void interfaceReference (CommunicationInterface communicationInterface) {
        this.communicationInterface = communicationInterface;
    }

    @Override
    public void setRegisterFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), RegisterFragment.newInstance(null, null))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setMainContentFragments() {
        checkCurrentUserRoleFromDatabase ();
    }

    public void checkCurrentUserRoleFromDatabase () {
        firebaseFirestore.collection(Utils.roles.get(increment)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    User user = queryDocumentSnapshot.toObject(User.class);
                    if (user.getUserId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        role = user.getType();
                        switch (role){
                            case "PlannerRole":
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .add(frameLayout.getId(), PlannerFragmentContainer.newInstance(null, null))
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case "AdminRole":
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .add(frameLayout.getId(), UserContentContainerFragment.newInstance(null, null))
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case "UserRole":
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .add(frameLayout.getId(), UserContentContainerFragment.newInstance(null, null))
                                        .addToBackStack(null)
                                        .commit();
                                break;
                        }
                        return;
                    }
                }
                increment++;
                checkCurrentUserRoleFromDatabase();
            }
        });
    }

    @Override
    public void fromLoginFragmentCheckUser() {
        checkCurrentUserRoleFromDatabase();
    }

    public interface AuthTypeInterface {
        void updateAdapterListener (String value);
    }
}