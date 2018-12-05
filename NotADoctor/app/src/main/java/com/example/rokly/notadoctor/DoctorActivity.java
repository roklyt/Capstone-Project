package com.example.rokly.notadoctor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Model.Places.Places;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.PlacesApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientPlaces;
import com.example.rokly.notadoctor.helper.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.ConditionActivity.getCategories;


public class DoctorActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    public final static String EXTRA_CONDITION_DETAIL = "conditionDetail";
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location currentLocation;
    private ConditionDetail conditionDetail;
    private LatLng currentLocationLatLng;
    private final static long LOCATION_REFRESH_TIME = 3000;
    private final static float LOCATION_REFRESH_DISTANCE = 10;

    boolean isPermissionGranted;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(DoctorActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_CONDITION_DETAIL)){
            conditionDetail = intent.getParcelableExtra(EXTRA_CONDITION_DETAIL);

            callInfermedica();

        }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(DoctorActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLng));
        mMap.setMinZoomPreference(20000);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(DoctorActivity.this, "Location changed: Lat: " + location.getLatitude() + " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLng));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void callInfermedica(){
        String categories = getCategories(conditionDetail);
        String queryText = categories + ","  + conditionDetail.getCommonName();
        String location = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        long radius = 1000;


        PlacesApi placesApi = RetrofitClientPlaces.getRetrofitInstance().create(PlacesApi.class);
        Call<Places> call = placesApi.getPlaces(queryText,"doctor", location,  radius, getResources().getString(R.string.google_maps_key).toString());
        call.enqueue(new Callback<Places>() {

            @Override
            public void onResponse(@NonNull Call<Places> call, @NonNull Response<Places> response) {
                response.body();
            }

            @Override
            public void onFailure(@NonNull Call<Places> call, @NonNull Throwable t) {
                Log.e("DiagnoseActivity","Counter :" +  t);

            }
        });

    }

/*    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=52.523479,%2013.344122&radius=500&type=doctor&key=AIzaSyDnjocYCZLzb8eNDBtRTJ5pUgKb5eXoQDw

    https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=mongolian%20grill&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating&locationbias=circle:2000@47.6918452,-122.2226413&key=AIzaSyDnjocYCZLzb8eNDBtRTJ5pUgKb5eXoQDw


    https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Meningitis,Neurology&inputtype=textquery&type=doctor&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyDnjocYCZLzb8eNDBtRTJ5pUgKb5eXoQDw
    https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=abion&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating&key=AIzaSyDnjocYCZLzb8eNDBtRTJ5pUgKb5eXoQDw
    https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=abion&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating&location=52.523479,%2013.344122&radius=500&key=AIzaSyDnjocYCZLzb8eNDBtRTJ5pUgKb5eXoQDw

    https://maps.googleapis.com/maps/api/place/textsearch/json?query=Food$20poisoning,Gastroenterology&location=52.523479,13.344122&radius=100&key=AIzaSyDnjocYCZLzb8eNDBtRTJ5pUgKb5eXoQDw*/
}
