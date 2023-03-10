package com.example.firebasepractise.fragment.planner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasepractise.AuthType;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.adapter.LoadingAlertDialog;
import com.example.firebasepractise.databinding.FragmentServiceBinding;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.User;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.UnknownServiceException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ServiceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentServiceBinding binding;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    private String parentCategory = "Travel";
    private String childCategory = "Affordable";
    private String nameInput;
    private String descriptionInput;
    private int priceInput;
    private FirebaseAuth firebaseAuth;
    String email;
    String phoneNumber;
    private String date;
    private Uri fragmentImageUri;
    private ServicePlanner servicePlanner;
    private FirebaseStorage firebaseStorage;
    private String imageUrl;
    private GoogleSignInAccount googleSignInAccount;

    public ServiceFragment() {
    }

    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentServiceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(getContext());

        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(Arrays.asList("select category"))));
        binding.spinnerParentCategory.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Utils.parentCategory));

        binding.spinnerParentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (binding.spinnerParentCategory.getSelectedItem().toString()) {
                    case "Travel":
                        parentCategory = "Travel";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.travelItems));
                        break;
                    case "Catering":
                        parentCategory = "Catering";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.cateringItems));
                        break;
                    case "Decor":
                        parentCategory = "Decor";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.decorItems));
                        break;
                    case "Entertainment":
                        parentCategory = "Entertainment";
                        binding.spinnerChildCategory.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Utils.djItems));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.spinnerChildCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                childCategory = binding.spinnerChildCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

//!     disabling date button
        binding.selectDate.setVisibility(View.GONE);

//!     get current date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date calender = new Date();
        date = dateFormat.format(calender).toString();

//        Toast.makeText(context, "date: " + date, Toast.LENGTH_SHORT).show();

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
                if (!validateNameField() | !validateDescriptionField() | !validatePriceField()) {
                    return;
                }
                loadingAlertDialog.show();
                loadingAlertDialog.setCancelable(false);

//                uploading image
                StorageReference storageReference = null;
                if (fragmentImageUri != null) {
                    storageReference = firebaseStorage.getReference("uploads").child(System.currentTimeMillis() + "." + Utils.getMimeType(context, fragmentImageUri));
                } else {
                    fragmentImageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.black_background);
                    Glide
                            .with(context)
                            .load(fragmentImageUri)
                            .centerCrop()
                            .into(binding.imageView);
                    storageReference = firebaseStorage.getReference("uploads").child(System.currentTimeMillis() + "." + "png");
                }

                storageReference.putFile(fragmentImageUri)
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
                                                servicePlanner = new ServicePlanner(descriptionInput, nameInput, parentCategory, childCategory, priceInput, false, firebaseFirestore.collection("Service").document().getId(), email, phoneNumber, date, imageUrl);
                                                firebaseFirestore.collection("Service").document(servicePlanner.getDocumentId()).set(servicePlanner).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public static final Uri getUriToResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        Uri resUri = getUriToResource(context,resId);
        return resUri;
    }

    private boolean validateNameField() {
        nameInput = binding.textInputName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            binding.textInputName.setError("Field can't be empty");
            return false;
        }
        if (binding.textInputName.isErrorEnabled()) {
            binding.textInputName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescriptionField() {
        descriptionInput = binding.textInputDescription.getEditText().getText().toString().trim();
        if (descriptionInput.isEmpty()) {
            binding.textInputDescription.setError("Field can't be empty");
            return false;
        }
        if (binding.textInputDescription.isErrorEnabled()) {
            binding.textInputDescription.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validatePriceField() {
        String priceCheck = binding.textInputPrice.getEditText().getText().toString().trim();
        if (priceCheck.isEmpty()) {
            binding.textInputPrice.setError("Field can't be empty");
            return false;
        }
        if (binding.textInputPrice.isErrorEnabled()) {
            binding.textInputPrice.setErrorEnabled(false);
        }
        priceInput = Integer.parseInt(priceCheck);
        return true;
    }
}