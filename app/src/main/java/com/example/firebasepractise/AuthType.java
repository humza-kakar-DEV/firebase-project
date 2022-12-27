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
import com.example.firebasepractise.fragment.UserContentContainerFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class AuthType extends AppCompatActivity {

    Button plannerButton, adminButton, userButton;
    private ActivityAuthTypeBinding binding;
    private FrameLayout frameLayout;
    private UserContentContainerFragment venueUserFragment;
    private CommunicationInterface communicationInterface;

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

//        plannerButton = (Button) findViewById(R.id.plannerButton);
//        adminButton = (Button) findViewById(R.id.adminButton);
//        userButton = (Button) findViewById(R.id.userButton);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        venueUserFragment = UserContentContainerFragment.newInstance(null, null);

        getSupportFragmentManager()
                .beginTransaction()
                .add(frameLayout.getId(), venueUserFragment.newInstance(null, null))
                .commit();

//        plannerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AuthType.this, loginActivity.class)
//                        .putExtra(Constant.AUTH_TYPE, Constant.PLANNER_ROLE));
//            }
//        });
//
//        adminButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AuthType.this, loginActivity.class)
//                        .putExtra(Constant.AUTH_TYPE, Constant.ADMIN_ROLE));
//            }
//        });
//
//        userButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AuthType.this, loginActivity.class)
//                        .putExtra(Constant.AUTH_TYPE, Constant.USER_ROLE));
//            }
//        });
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

    public interface AuthTypeInterface {
        void updateAdapterListener (String value);
    }
}