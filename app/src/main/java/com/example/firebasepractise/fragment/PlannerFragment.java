package com.example.firebasepractise.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.databinding.FragmentPlannerBinding;
import com.example.firebasepractise.model.Plan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class PlannerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String date;
    private Uri imageUri;
    private String documentUniqueId;
    private String imageUrl;
    private Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private FragmentPlannerBinding binding;

    public PlannerFragment() {

    }

    public static PlannerFragment newInstance(String param1, String param2) {
        PlannerFragment fragment = new PlannerFragment();
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
        
        binding = FragmentPlannerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

//        date picker code
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        binding.selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        date = materialDatePicker.getHeaderText();
                        binding.selectDate.setText(date);
                    }
                });

//        this button will run in end it will check
//        all the input fields value exits
        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              download image
                String title = binding.titleEditText.getEditText().getText().toString();
                String description = binding.descriptionEditText.getEditText().getText().toString();
                Plan plan = new Plan(title, description, date, false, firebaseAuth.getCurrentUser().getUid(), documentUniqueId, imageUrl);
                firebaseFirestore.collection(Constant.PLANS_TAG).document(plan.getDocumentId()).set(plan);
                Toast.makeText(context, "data saved", Toast.LENGTH_SHORT).show();
            }
        });

//        code for image picker
        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                requireActivity().startActivityForResult(intent, 112);
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.full_screen_image_layout);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.imageViewFull);
                imageView.setImageURI(imageUri);
                dialog.show();
            }
        });

        return view;
    }

    //    storing image file to cloud storage callback from activity
    public void uploadImage(Uri imageUri) {
        this.imageUri = imageUri;
        documentUniqueId = firebaseFirestore.collection(Constant.PLANS_TAG).document().toString();
        Glide
                .with(context)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_cloud_upload_24)
                .into(binding.imageView);
//                upload image
        firebaseStorage.getReference("uploads").child(System.currentTimeMillis() + "." + Utils.getMimeType(context, imageUri)).putFile(imageUri)
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
                });
    }
}