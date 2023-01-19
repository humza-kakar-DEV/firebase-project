package com.example.firebasepractise.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebasepractise.AuthType;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentLoginBinding binding;

    private FirebaseAuth firebaseAuth;
    private LoginFragmentInterface loginfragmentInterface;
    private String emailInput;
    private String usernameInput;
    private String passwordInput;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInClient mGoogleSignInClient;
    private BroadcastReceiver broadcastReceiver;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();

//!       setting options menu to disable
        ((AuthType) getActivity()).invokeOptions(false);

//        implementing google sign in
        binding.googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                // Initialize sign in client
                mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

//                checking google sign in
                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
                if (googleSignInAccount != null) {
                    Toast.makeText(getContext(), "email: " + googleSignInAccount.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    requireActivity().startActivityForResult(signInIntent, Constant.RC_SIGN_IN);
                }
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() | !validatePassword()) {
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "user logged in", Toast.LENGTH_SHORT).show();
                        loginfragmentInterface.fromLoginFragmentCheckUser();
                    }
                });
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginfragmentInterface.setRegisterFragment();
            }
        });
        
        binding.forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(((AuthType) getActivity()).frameLayout.getId(), ResetPasswordFragment.newInstance(null, null))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    //    input fields validation
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

    private boolean validatePassword() {
        passwordInput = binding.textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            binding.textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!Utils.PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.textInputPassword.setError("Password too weak");
            return false;
        } else {
            binding.textInputPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginFragmentInterface) {
            loginfragmentInterface = (LoginFragmentInterface) context;
        }
    }

    public interface LoginFragmentInterface {
        void fromLoginFragmentCheckUser();

        void setRegisterFragment();
    }
}