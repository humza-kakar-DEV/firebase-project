package com.example.firebasepractise.fragment.planner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasepractise.AuthType;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.adapter.LoadingAlertDialog;
import com.example.firebasepractise.databinding.FragmentVenueBinding;
import com.example.firebasepractise.model.User;
import com.example.firebasepractise.model.Venue;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class VenueFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context context;
    private FragmentVenueBinding binding;
    private String email;
    private String phoneNumber;
    private String date;
    private Uri fragmentImageUri;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private String imageUrl;
    private GoogleSignInAccount googleSignInAccount;

    public VenueFragment() {
    }

    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
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
        binding = FragmentVenueBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(getContext());

        binding.selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
                materialDateBuilder.setTitleText("SELECT A DATE");
                final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

                materialDatePicker.addOnPositiveButtonClickListener(
                        new MaterialPickerOnPositiveButtonClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                date = materialDatePicker.getHeaderText();
                                binding.selectDate.setText(date);
                            }
                        });
            }
        });

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                requireActivity().startActivityForResult(intent, Constant.UPLOAD_IMAGE);
            }
        });

        ((AuthType) getActivity()).imageUriInterfaceReference(new AuthType.ImageUriInterface() {
            @Override
            public void imageUri(Uri imageUri) {
                fragmentImageUri = imageUri;
                Glide
                        .with(context)
                        .load(imageUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_cloud_upload_24)
                        .into(binding.imageView);
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.full_screen_image_layout);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.imageViewFull);
                imageView.setImageURI(fragmentImageUri);
                dialog.show();
            }
        });

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  uploading image
                loadingAlertDialog.show();
                loadingAlertDialog.setCancelable(false);
                firebaseStorage.getReference("uploads").child(System.currentTimeMillis() + "." + Utils.getMimeType(context, fragmentImageUri)).putFile(fragmentImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        imageUrl = task.getResult().toString();
                                    }
                                });
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
                                    firebaseFirestore.collection("PlannerRole").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                User user = queryDocumentSnapshot.toObject(User.class);
                                                String currentEmail;
                                                if (googleSignInAccount != null) {
                                                    currentEmail = googleSignInAccount.getEmail();
                                                } else {
                                                    currentEmail = firebaseAuth.getCurrentUser().getEmail();
                                                }
                                                if (user.getEmail().equals(currentEmail)) {
                                                    email = user.getEmail();
                                                    phoneNumber = user.getPhoneNumber();
                                                }
                                            }
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                String name = binding.textInputName.getEditText().getText().toString();
                                                String perHourRent = binding.textInputPerHourRent.getEditText().getText().toString();
                                                String address = binding.textInputAddress.getEditText().getText().toString();
                                                String size = binding.textInputSize.getEditText().getText().toString();
                                                String noGuests = binding.textInputNumberOfGuests.getEditText().getText().toString();
                                                String attachRooms = binding.textInputAttachedRooms.getEditText().getText().toString();
                                                String washRooms = binding.textInputWashRooms.getEditText().getText().toString();
                                                Venue venue = new Venue(name, perHourRent, address, size, noGuests, attachRooms, washRooms, false, firebaseFirestore.collection("Venue").document().getId(), email, phoneNumber, imageUrl, date);
                                                firebaseFirestore.collection("Venue").document(venue.getDocumentId()).set(venue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            loadingAlertDialog.dismiss();
                                                            Toast.makeText(context, "data saved", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });

        return view;
    }

//    validating input fields


}