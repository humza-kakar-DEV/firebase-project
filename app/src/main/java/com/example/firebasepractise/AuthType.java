package com.example.firebasepractise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.firebasepractise.Util.interfaces.CommunicationInterface;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.adapter.LoadingAlertDialog;
import com.example.firebasepractise.databinding.ActivityAuthTypeBinding;
import com.example.firebasepractise.fragment.AdminFragment;
import com.example.firebasepractise.fragment.LoginFragment;
import com.example.firebasepractise.fragment.RegisterFragment;
import com.example.firebasepractise.fragment.SelectRoleFragment;
import com.example.firebasepractise.fragment.UserContentContainerFragment;
import com.example.firebasepractise.fragment.admin.AdminFragmentContainer;
import com.example.firebasepractise.fragment.planner.PlannerFragmentContainer;
import com.example.firebasepractise.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AuthType extends AppCompatActivity implements LoginFragment.LoginFragmentInterface
        , RegisterFragment.RegisterFragmentListener {

    private static final int RC_SIGN_IN = 111;
    Button plannerButton, adminButton, userButton;
    private ActivityAuthTypeBinding binding;
    public FrameLayout frameLayout;
    private UserContentContainerFragment venueUserFragment;
    private String role;
    private CommunicationInterface communicationInterface;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageUriInterface imageUriInterface;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount googleSignInAccount;
    private boolean optionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthTypeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//!        disabling night mode of activity this will impact on fragments as well
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Initialize sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        Intent intent = new Intent("com.example.firebasepractise.fragment.LoginFragment");
        intent.putExtra("val", "abc");
        sendBroadcast(intent);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        firebaseAuth.signOut();
                        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AuthType.this, "logged out", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        homeFragment();
                        break;
                }
                //This is for closing the drawer after acting on it
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        venueUserFragment = UserContentContainerFragment.newInstance(null, null);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            checkCurrentUserRoleWithUid(0);
        } else if (googleSignInAccount != null) {
//            Toast.makeText(this, "firebase auth user null", Toast.LENGTH_SHORT).show();
            checkGoogleLoggedInUserInFirestoreWithEmail(googleSignInAccount, 0);
        } else {
            homeFragment();
        }
    }


    //    home fragment
    public void homeFragment() {
        getSupportActionBar().setTitle(R.string.app_name);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(Constant.ROOT_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .add(frameLayout.getId(), LoginFragment.newInstance(null, null), Constant.ROOT_FRAGMENT)
                .addToBackStack(Constant.ROOT_FRAGMENT)
                .commit();
    }

    //    creating options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tab_menu_options, menu);
        return true;
    }

    //?    this method is invoked from fragments whether to
//?    enable menu or not
    public void invokeOptions(boolean optionState) {
        this.optionState = optionState;
        invalidateOptionsMenu(); //! this method calls onPrepareOptionMenu callback
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (optionState) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }
        return true;
    }


    //    no usage of this options while filtering
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                invalidateMenu();
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
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((getSupportFragmentManager().getBackStackEntryCount() == 1)) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void interfaceReference(CommunicationInterface communicationInterface) {
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
        checkCurrentUserRoleWithUid(0);
    }

    //    getting data recursive
    public void checkCurrentUserRoleWithUid(int increment) {
        if (increment > 2) {
            return;
        }
        firebaseFirestore.collection(Utils.roles.get(increment)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    User user = queryDocumentSnapshot.toObject(User.class);
                    if (user.getUserId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        checkingRoleOfUser(user.getType());
                        return;
                    }
                }
            }
        });
        checkCurrentUserRoleWithUid(++increment);
    }

    @Override
    public void fromLoginFragmentCheckUser() {
        checkCurrentUserRoleWithUid(0);
    }

    public interface AuthTypeInterface {
        void updateAdapterListener(String value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
//!                  Signed in successfully, show authenticated UI.
//!                Toast.makeText(this, "email: " + account.getEmail(), Toast.LENGTH_SHORT).show();
                checkGoogleLoggedInUserInFirestoreWithEmail(account, 0);
            } catch (ApiException e) {
                // Signed in unsuccessful update UI
            }
        } else if (requestCode == Constant.UPLOAD_IMAGE) {
            imageUriInterface.imageUri(data.getData());
        }
    }

    public void checkGoogleLoggedInUserInFirestoreWithEmail(GoogleSignInAccount account, int increment) {
        if (increment > 2) {
            Log.d("myStart", "runned");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(frameLayout.getId(), SelectRoleFragment.newInstance(null, null))
                    .commit();
            return;
        }
        firebaseFirestore.collection(Utils.roles.get(increment)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    User user = queryDocumentSnapshot.toObject(User.class);
                    if (user.getEmail().equals(account.getEmail())) {
                        checkingRoleOfUser(user.getType());
                        return;
                    }
                }
            }
        });
        checkGoogleLoggedInUserInFirestoreWithEmail(account, ++increment);
    }

    public void checkingRoleOfUser(String role) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (role) {
            case "PlannerRole":
                if (!fragmentManager.isDestroyed()) {
                    fragmentManager
                            .beginTransaction()
                            .replace(frameLayout.getId(), PlannerFragmentContainer.newInstance(null, null))
                            .commit();
                }
                break;
            case "AdminRole":
                if (!fragmentManager.isDestroyed()) {
                    fragmentManager
                            .beginTransaction()
                            .replace(frameLayout.getId(), AdminFragmentContainer.newInstance(null, null))
                            .commit();
                }
                break;
            case "UserRole":
                if (!fragmentManager.isDestroyed()) {
                    fragmentManager
                            .beginTransaction()
                            .replace(frameLayout.getId(), UserContentContainerFragment.newInstance(null, null))
                            .commit();
                }
                break;
        }
    }

    public void imageUriInterfaceReference(ImageUriInterface imageUriInterface) {
        this.imageUriInterface = imageUriInterface;
    }

    public interface ImageUriInterface {
        void imageUri(Uri imageUri);
    }
}
