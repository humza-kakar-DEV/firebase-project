package com.example.firebasepractise.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.databinding.FragmentRegisterBinding;
import com.example.firebasepractise.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentRegisterBinding binding;
    private String role;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RegisterFragmentListener registerFragmentListener;
    private String emailInput;
    private String passwordInput;
    private String usernameInput;
    private String phoneNumber;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
                if (!validateEmail() | !validatePassword() | !validateUsername() | !validatePhoneNumber()) {
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User(firebaseAuth.getCurrentUser().getUid(), usernameInput, role, phoneNumber, emailInput);
                        firebaseFirestore.collection(role).document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                registerFragmentListener.setMainContentFragments();
//                                Toast.makeText(getContext(), "user created", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });

        return view;
    }

    //    fields validation
    private boolean validateEmail() {
        emailInput = binding.textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            binding.textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            binding.textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        usernameInput = binding.textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            binding.textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            binding.textInputUsername.setError("Username too long");
            return false;
        } else {
            binding.textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        phoneNumber = binding.textInputPhoneNumber.getEditText().getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            binding.textInputPhoneNumber.setError("Field can't be empty");
            return false;
        } else {
            binding.textInputPhoneNumber.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        passwordInput = binding.textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            binding.textInputPassword.setError("Field can't be empty");
            return false;
        }
        return true;
//        else if (!Utils.PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            binding.textInputPassword.setError("Password too weak");
//            return false;
//        } else {
//            binding.textInputPassword.setError(null);
//            return true;
//        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RegisterFragmentListener) {
            registerFragmentListener = (RegisterFragmentListener) context;
        }
    }

    public interface RegisterFragmentListener {
        void setMainContentFragments();
    }
}