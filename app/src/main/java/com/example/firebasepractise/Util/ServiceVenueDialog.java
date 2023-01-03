package com.example.firebasepractise.Util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.firebasepractise.R;
import com.example.firebasepractise.model.Booked;
import com.example.firebasepractise.model.ServicePlanner;
import com.example.firebasepractise.model.Venue;

public class ServiceVenueDialog extends Dialog implements View.OnClickListener {

    private ServicePlanner servicePlanner;
    private Venue venue;
    private String type;

    //    service text views
    TextView serviceNameTextView, serviceDescriptionTextView, servicePriceTextView, serviceDateTextView, serviceParentTextView, serviceChildTextView;

    //    venue text views
    TextView venueNameTextView, addressTextView, sizeTextView, perHourTextView, noOfGuestsTextView, attachedRoomsTextView, washroomsTextView;

    public ServiceVenueDialog(@NonNull Context context, String type, Object object) {
        super(context);

        this.type = type;
        switch (this.type) {
            case "service":
                this.servicePlanner = (ServicePlanner) object;
                break;
            case "venue":
                this.venue = (Venue) object;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch (type) {
            case "service":

//                getting view ids
                setContentView(R.layout.custom_dialog_service_layout);
                serviceNameTextView = findViewById(R.id.serviceNameTextView);
                serviceDescriptionTextView = findViewById(R.id.serviceDescriptionTextView);
                serviceDateTextView = findViewById(R.id.serviceDateTextView);
                servicePriceTextView = findViewById(R.id.servicePriceTextView);
                serviceParentTextView = findViewById(R.id.serviceParentCategory);
                serviceChildTextView = findViewById(R.id.serviceChildCategory);

//                setting views
                serviceNameTextView.setText(servicePlanner.getServiceName());
                serviceDescriptionTextView.setText(servicePlanner.getServiceDescription());
                Log.d("myCheck", "service: " + servicePlanner.getDate());
                serviceDateTextView.setText(servicePlanner.getDate());
                servicePriceTextView.setText(String.valueOf(servicePlanner.getServicePrice()));
                serviceParentTextView.setText(servicePlanner.getServiceParentCategory());
                serviceChildTextView.setText(servicePlanner.getServiceChildCategory());

                break;

            case "venue":
                setContentView(R.layout.custom_dialog_venue_layout);
                Log.d("myCheck", "venue: " + venue.getDate());
//                getting view ids
                venueNameTextView = findViewById(R.id.venueNameTextView);
                addressTextView = findViewById(R.id.addressTextView);
                serviceDateTextView = findViewById(R.id.dateTextView);
                sizeTextView = findViewById(R.id.sizeTextView);
                perHourTextView = findViewById(R.id.perHourRentTextView);
                noOfGuestsTextView = findViewById(R.id.noOfGeustsTextView);
                attachedRoomsTextView = findViewById(R.id.attachedRoomsTextView);
                washroomsTextView = findViewById(R.id.washRoomsTextView);

//                washrooms
                venueNameTextView.setText(venue.getName());
                addressTextView.setText(venue.getAddress());
                sizeTextView.setText(venue.getSize());
                serviceDateTextView.setText(venue.getDate());
                perHourTextView.setText(venue.getPerHourRent());
                noOfGuestsTextView.setText(venue.getNoGuests());
                attachedRoomsTextView.setText(venue.getAttachedRooms());
                washroomsTextView.setText(venue.getWashRooms());

                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
