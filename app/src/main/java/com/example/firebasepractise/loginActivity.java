package com.example.firebasepractise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String authType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Utils.StatusBarColour(this, R.color.custom_dark_background);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, RegisterActivity.class);
                switch (getIntent().getStringExtra(Constant.AUTH_TYPE)) {
                    case Constant.PLANNER_ROLE:
                        intent.putExtra(Constant.AUTH_TYPE, Constant.PLANNER_ROLE);
                        break;
                    case Constant.ADMIN_ROLE:
                        intent.putExtra(Constant.AUTH_TYPE, Constant.ADMIN_ROLE);
                        break;
                    case Constant.USER_ROLE:
                        intent.putExtra(Constant.AUTH_TYPE, Constant.USER_ROLE);
                        break;
                }
                startActivity(intent);
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailEditText.getEditText().getText().toString().trim();
                String password = binding.passwordEditText.getEditText().getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uidCurrentUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            switch (getIntent().getStringExtra(Constant.AUTH_TYPE)) {
                                case Constant.PLANNER_ROLE:
                                    FirebaseFirestore.getInstance().collection(Constant.PLANNER_ROLE).document(uidCurrentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (Objects.equals(documentSnapshot.getString("type"), Constant.PLANNER_ROLE)) {
                                                Toast.makeText(loginActivity.this, "planner logged in", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(loginActivity.this, MainLayout.class)
                                                        .putExtra(Constant.AUTH_TYPE, Constant.PLANNER_ROLE));
                                            } else {
                                                Toast.makeText(loginActivity.this, "no such user as planner", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                                case Constant.ADMIN_ROLE:
                                    FirebaseFirestore.getInstance().collection(Constant.ADMIN_ROLE).document(uidCurrentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (Objects.equals(documentSnapshot.getString("type"), Constant.ADMIN_ROLE)) {
                                                Toast.makeText(loginActivity.this, "admin logged in", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(loginActivity.this, MainLayout.class)
                                                        .putExtra(Constant.AUTH_TYPE, Constant.ADMIN_ROLE));
                                            } else {
                                                Toast.makeText(loginActivity.this, "no such user as admin", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                                case Constant.USER_ROLE:
                                    FirebaseFirestore.getInstance().collection(Constant.USER_ROLE).document(uidCurrentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (Objects.equals(documentSnapshot.getString("type"), Constant.USER_ROLE)) {
                                                Toast.makeText(loginActivity.this, "user logged in", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(loginActivity.this, MainLayout.class)
                                                        .putExtra(Constant.AUTH_TYPE, Constant.USER_ROLE));
                                            } else {
                                                Toast.makeText(loginActivity.this, "no such user as user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                });
            }
        });
    }
}