package com.example.rokly.notadoctor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.rokly.notadoctor.Adapter.PlacesAdapter;
import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.PlaceDetail.PlaceDetail;
import com.example.rokly.notadoctor.Model.Places.Places;
import com.example.rokly.notadoctor.Model.Places.Result;
import com.example.rokly.notadoctor.Retrofit.PlacesApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientPlaces;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.ConditionActivity.getCategories;

public class FindADoctor extends AppCompatActivity implements OnMapReadyCallback, LocationListener, PlacesAdapter.ItemClickListener{

    public final static String EXTRA_CONDITION_DETAIL = "conditionDetail";

    private PlacesAdapter userAdapter;
    private GoogleMap map;
    private RecyclerView userRecyclerView;
    private ConditionDetail conditionDetail;
    private LocationManager locationManager;
    private Location currentLocation;
    private final static long LOCATION_REFRESH_TIME = 3000;
    private final static long LOCATION_RADIUS = 1000;
    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_adoctor);

        userRecyclerView = findViewById(R.id.rv_places);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new PlacesAdapter(this, this);
        userRecyclerView.setAdapter(userAdapter);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_CONDITION_DETAIL)){
            conditionDetail = intent.getParcelableExtra(EXTRA_CONDITION_DETAIL);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        currentLocation = getLocation();

    }

    public Location getLocation() {
        Location location = null;


        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(FindADoctor.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return location;
            }

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,  LOCATION_REFRESH_TIME,  10, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }
                }
                //get the location by gps
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,LOCATION_REFRESH_TIME,10, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
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
                    Toast.makeText(FindADoctor.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void callPlaces(){
        String categories = getCategories(conditionDetail);
        //String queryText = categories + ","  + conditionDetail.getCommonName();
        if(categories.contains("Other")){
            categories = "family doctor";
        }
        String queryText = categories;
        String location = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        long radius = LOCATION_RADIUS;


        PlacesApi placesApi = RetrofitClientPlaces.getRetrofitInstance().create(PlacesApi.class);
        Call<Places> call = placesApi.getPlaces(queryText,getResources().getString(R.string.google_places_type), location,  radius, getResources().getString(R.string.google_maps_key));
        call.enqueue(new Callback<Places>() {

            @Override
            public void onResponse(@NonNull Call<Places> call, @NonNull Response<Places> response) {
                setMapsMarker(response.body());
                setPlacesDetail(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Places> call, @NonNull Throwable t) {
                Log.e("DoctorActivity","Counter :" +  t);

            }
        });

    }

    private void setPlacesDetail(final Places places){

        final Places getDetailPlaces = places;
        for(int i = 0; i < places.getResults().size(); i++){
            PlacesApi placesApi = RetrofitClientPlaces.getRetrofitInstance().create(PlacesApi.class);
            Call<PlaceDetail> call = placesApi.getPlaceDetail(places.getResults().get(i).getPlaceId(), "name,rating,formatted_phone_number", getResources().getString(R.string.google_maps_key));
            final int x = i;
            call.enqueue(new Callback<PlaceDetail>() {
                @Override
                public void onResponse(@NonNull Call<PlaceDetail> call, @NonNull Response<PlaceDetail> response) {
                    getDetailPlaces.getResults().get(x).setDetailResult(response.body().getResult());
                    if(x  == places.getResults().size() - 1){
                        userAdapter.setPlacesData(getDetailPlaces.getResults());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaceDetail> call, @NonNull Throwable t) {
                    Log.e("ConditionActivity","" +  t);

                }
            });
        }

    }

    private void setMapsMarker(Places places){

        List<Result> results = places.getResults();

        for(Result result:results){
            LatLng latLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
            String markerTitle = result.getName();

            map.addMarker(new MarkerOptions().position(latLng)
                    .title(markerTitle));
        }
    }

    private void setUserMarker(){

        map.addMarker(new MarkerOptions().position(getCurrentLatLng())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title(getResources().getString(R.string.marker_your_position)));


        map.animateCamera(CameraUpdateFactory.newLatLngZoom(getCurrentLatLng(), getResources().getInteger(R.integer.zoom_level)), getResources().getInteger(R.integer.zoom_level), null);
    }

    private LatLng getCurrentLatLng(){
        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    @Override
    public void onItemClickListener(Result currentResult) {

    }

    @Override
    public void onCallClickListener(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(FindADoctor.this, "Location changed: Lat: " + location.getLatitude() + " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        currentLocation = location;
        setUserMarker();
        callPlaces();
        locationManager.removeUpdates(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}