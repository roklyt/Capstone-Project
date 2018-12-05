package com.example.rokly.notadoctor.helper;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

public class LocationHelper implements LocationListener {
    private Context context;

    public LocationHelper(Context context){
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location loc) {
        Toast.makeText(context, "Location changed: Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
    }


}
