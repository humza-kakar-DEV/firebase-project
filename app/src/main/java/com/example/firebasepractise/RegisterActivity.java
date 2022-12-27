package com.example.firebasepractise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.databinding.ActivityLoginBinding;
import com.example.firebasepractise.databinding.ActivityRegisterBinding;
import com.example.firebasepractise.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String authType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.textField1.getEditText().getText().toString().trim();
                String email = binding.textField2.getEditText().getText().toString().trim();
                String password = binding.textField3.getEditText().getText().toString().trim();
                String phoneNumber = binding.textField4.getEditText().getText().toString().trim();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User user = null;
                        Intent intent = new Intent(RegisterActivity.this, MainLayout.class);
                        if (task.isSuccessful()) {
                            switch (getIntent().getStringExtra(Constant.AUTH_TYPE)) {
                                case Constant.PLANNER_ROLE:
                                    user = new User(firebaseAuth.getCurrentUser().getUid(), name, Constant.PLANNER_ROLE, phoneNumber);
                                    intent.putExtra(Constant.AUTH_TYPE, Constant.PLANNER_ROLE);
                                    firebaseFirestore.collection(Constant.PLANNER_ROLE).document(user.getUserId()).set(user);
                                    break;
                                case Constant.ADMIN_ROLE:
                                    user = new User(firebaseAuth.getCurrentUser().getUid(), name, Constant.ADMIN_ROLE, phoneNumber);
                                    intent.putExtra(Constant.AUTH_TYPE, Constant.ADMIN_ROLE);
                                    firebaseFirestore.collection(Constant.ADMIN_ROLE).document(user.getUserId()).set(user);
                                    break;
                                case Constant.USER_ROLE:
                                    user = new User(firebaseAuth.getCurrentUser().getUid(), name, Constant.USER_ROLE, phoneNumber);
                                    intent.putExtra(Constant.AUTH_TYPE, Constant.USER_ROLE);
                                    firebaseFirestore.collection(Constant.USER_ROLE).document(user.getUserId()).set(user);
                                    break;
                            }
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}