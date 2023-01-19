package com.example.firebasepractise.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.firebasepractise.AuthType;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.adapter.LoadingAlertDialog;
import com.example.firebasepractise.databinding.FragmentSelectRoleBinding;
import com.example.firebasepractise.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SelectRoleFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentSelectRoleBinding binding;
    private String role;
    private String usernameInput;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String textInputPhoneNumber;
    private GoogleSignInAccount googleSignInAccount;

    public SelectRoleFragment() {
    }

    public static SelectRoleFragment newInstance(String param1, String param2) {
        SelectRoleFragment fragment = new SelectRoleFragment();
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
        binding = FragmentSelectRoleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
//      checking google auth currently sign in user
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        binding.spinnerRole.setAdapter(new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Utils.roles));
        binding.spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = binding.spinnerRole.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername()) {
                    return;
                }
                if (role.equals("AdminRole")) {
                    firebaseFirestore.collection("AdminRole").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.size() == 0) {
                                //                create user in fire store database
                                User user = new User(googleSignInAccount.getId(), googleSignInAccount.getDisplayName(), role, textInputPhoneNumber, googleSignInAccount.getEmail());
                                firebaseFirestore.collection(role).document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((AuthType) getActivity()).checkGoogleLoggedInUserInFirestoreWithEmail(googleSignInAccount, 0);
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Admin already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    //                create user in fire store database
                    User user = new User(googleSignInAccount.getId(), googleSignInAccount.getDisplayName(), role, textInputPhoneNumber, googleSignInAccount.getEmail());
                    firebaseFirestore.collection(role).document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            ((AuthType) getActivity()).checkGoogleLoggedInUserInFirestoreWithEmail(googleSignInAccount, 0);
                        }
                    });
                }

//                create user in fire store database

            }
        });

        return view;
    }

    private boolean validateUsername() {
        textInputPhoneNumber = binding.textInputPhoneNumber.getEditText().getText().toString().trim();

        if (textInputPhoneNumber.isEmpty()) {
            binding.textInputPhoneNumber.setError("Field can't be empty");
            return false;
        } else {
            binding.textInputPhoneNumber.setError(null);
            return true;
        }
    }

}