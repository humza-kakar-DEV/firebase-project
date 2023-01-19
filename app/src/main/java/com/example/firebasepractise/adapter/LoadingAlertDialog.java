package com.example.firebasepractise.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebasepractise.R;

public class LoadingAlertDialog extends Dialog implements View.OnClickListener {
    public LoadingAlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_alert_dialog);
    }

    @Override
    public void onClick(View view) {

    }
}
