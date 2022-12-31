package com.example.firebasepractise.Util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.firebasepractise.R;
import com.example.firebasepractise.model.Booked;

public class CustomAlertDialog extends Dialog implements View.OnClickListener {

    private final Booked booked;

//    service text views
    TextView serviceNameTextView, serviceDescriptionTextView, servicePriceTextView, serviceDateTextView, serviceParentTextView, serviceChildTextView;

//    venue text views
    TextView venueNameTextView, addressTextView, sizeTextView, perHourTextView, noOfGuestsTextView, attachedRoomsTextView, washroomsTextView;

    public CustomAlertDialog(@NonNull Context context, Booked booked) {
        super(context);

        this.booked = booked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        switch (booked.getType()) {
            case "service":

//                getting view ids
                setContentView(R.layout.custom_dialog_service_layout);
                serviceNameTextView = findViewById(R.id.serviceNameTextView);
                serviceDescriptionTextView = findViewById(R.id.serviceDescriptionTextView);
                servicePriceTextView = findViewById(R.id.servicePriceTextView);
                serviceParentTextView = findViewById(R.id.serviceParentCategory);
                serviceChildTextView = findViewById(R.id.serviceChildCategory);

//                setting views
                serviceNameTextView.setText(booked.getServiceName());
                serviceDescriptionTextView.setText(booked.getServiceDescription());
                servicePriceTextView.setText(booked.getServicePrice());
                serviceParentTextView.setText(booked.getServiceParentCategory());
                serviceChildTextView.setText(booked.getServiceChildCategory());

                break;
            case "venue":
                setContentView(R.layout.custom_dialog_venue_layout);

//                getting view ids
                venueNameTextView = findViewById(R.id.venueNameTextView);
                addressTextView = findViewById(R.id.addressTextView);
                sizeTextView = findViewById(R.id.sizeTextView);
                perHourTextView = findViewById(R.id.perHourRentTextView);
                noOfGuestsTextView = findViewById(R.id.noOfGeustsTextView);
                attachedRoomsTextView = findViewById(R.id.attachedRoomsTextView);
                washroomsTextView = findViewById(R.id.washRoomsTextView);

//                washrooms
                venueNameTextView.setText(booked.getVenueName());
                addressTextView.setText(booked.getVenueAddress());
                sizeTextView.setText(booked.getVenueSize());
                perHourTextView.setText(booked.getVenueRent());
                noOfGuestsTextView.setText(booked.getVenueGuests());
                attachedRoomsTextView.setText(booked.getVenueRooms());
                washroomsTextView.setText(booked.getVenueWashrooms());

                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bookButton:
                break;
            case R.id.addressTextView:
                break;
            default:
                break;
        }
        dismiss();
    }
}
