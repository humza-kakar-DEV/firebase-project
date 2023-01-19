package com.example.firebasepractise.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.firebasepractise.AuthType;
import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.model.Booked;
import com.google.android.gms.auth.api.Auth;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

public class BottomSheetAdapter extends BottomSheetDialogFragment {

    private final Booked booked;
    private BottomSheetDialog bottomSheetDialog;
    private View rootView;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private String rating;

    public BottomSheetAdapter (Booked booked) {
        this.booked = booked;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return bottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_dialog_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        TextInputLayout feedBackEditText = (TextInputLayout) rootView.findViewById(R.id.text_input_feedBack);
        Spinner spinner = rootView.findViewById(R.id.spinnerRating);
        Button feedBackButton = rootView.findViewById(R.id.feedBackButton);

        spinner.setAdapter(new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Utils.rating));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rating = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        feedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedbackText = feedBackEditText.getEditText().getText().toString();
                getContext().sendBroadcast(new Intent("com.example.firebasepractise.feedBack").putExtra("feedBackText", feedbackText).putExtra("ratingText", rating).putExtra("serializable", (Serializable) booked));
                dismiss();
            }
        });

    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }
}
