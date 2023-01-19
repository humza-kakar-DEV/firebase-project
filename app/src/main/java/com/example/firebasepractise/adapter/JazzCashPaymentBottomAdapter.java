package com.example.firebasepractise.adapter;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebasepractise.R;
import com.example.firebasepractise.Util.interfaces.JazzCashPaymentBottomSheetResponse;
import com.example.firebasepractise.Util.jazzcash.JazzCashPayment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class JazzCashPaymentBottomAdapter extends BottomSheetDialogFragment {

    private View rootView;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private JazzCashPaymentBottomSheetResponse jazzCashPaymentBottomSheetResponse;

    public JazzCashPaymentBottomAdapter (JazzCashPaymentBottomSheetResponse jazzCashPaymentBottomSheetResponse) {
        this.jazzCashPaymentBottomSheetResponse = jazzCashPaymentBottomSheetResponse;
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
        rootView = inflater.inflate(R.layout.jazz_cash_bottom_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        WebView webView = (WebView) rootView.findViewById(R.id.webView);

//        launching jazz cash payment
        new JazzCashPayment(getContext()).launch(webView);

//      listening for broadcast receiver
        intentFilter = new IntentFilter("com.example.firebasepractise.myCustom");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.example.firebasepractise.myCustom")) {
                    jazzCashPaymentBottomSheetResponse.response();
                    dismiss();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetWhiteDialog;
    }
}
