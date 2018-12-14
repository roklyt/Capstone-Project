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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rokly.notadoctor.Adapter.PlacesAdapter;
import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.DiagnoseEntry;
import com.example.rokly.notadoctor.Database.DoctorEntry;
import com.example.rokly.notadoctor.Database.EvidenceEntry;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.Diagnose.Request.Evidence;
import com.example.rokly.notadoctor.Model.PlaceDetail.DetailResult;
import com.example.rokly.notadoctor.Model.PlaceDetail.PlaceDetail;
import com.example.rokly.notadoctor.Model.Places.Geometry;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.ConditionActivity.getCategories;
import static com.example.rokly.notadoctor.helper.ChoiceId.getChoiceIdInt;

public class FindADoctor extends AppCompatActivity implements OnMapReadyCallback, LocationListener, PlacesAdapter.ItemClickListener{

    public final static String EXTRA_CONDITION_DETAIL = "conditionDetail";
    public final static String EXTRA_IS_WIDGET = "isWidget";

    private final static String SAVE_PLACES = "savePlaces";
    private final static String SAVE_LOCATION = "saveLocation";

    private PlacesAdapter userAdapter;
    private GoogleMap map;
    private RecyclerView userRecyclerView;
    private ConditionDetail conditionDetail;
    private LocationManager locationManager;
    private Location currentLocation;
    private Places allPlaces;
    private ProgressBar progressBar;
    private final static long LOCATION_REFRESH_TIME = 3000;
    private final static long LOCATION_RADIUS = 1000;
    private boolean onSavedInstanceState = false;
    private UserEntry currentUser;
    private boolean isWidget;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_adoctor);

        userRecyclerView = findViewById(R.id.rv_places);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new PlacesAdapter(this, this);
        userRecyclerView.setAdapter(userAdapter);
        progressBar = findViewById(R.id.pb_find_a_doctor);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if(savedInstanceState != null){
            allPlaces = savedInstanceState.getParcelable(SAVE_PLACES);
            currentLocation = savedInstanceState.getParcelable(SAVE_LOCATION);
            currentUser = savedInstanceState.getParcelable(DiagnoseActivity.EXTRA_USER);
            onSavedInstanceState = true;
            progressBar.setVisibility(View.GONE);
            userAdapter.setPlacesData(allPlaces.getResults());
        }else{
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = getIntent();


            if(!intent.hasExtra(EXTRA_IS_WIDGET)){
                counter = intent.getIntExtra(ListWidgetService.EXTRA_DIAGNOSE, 1);
                if(intent.hasExtra(EXTRA_CONDITION_DETAIL)) {
                    conditionDetail = intent.getParcelableExtra(EXTRA_CONDITION_DETAIL);
                    currentUser = intent.getParcelableExtra(DiagnoseActivity.EXTRA_USER);
                }
            }

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            currentLocation = getLocation();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_PLACES, allPlaces);
        outState.putParcelable(SAVE_LOCATION, currentLocation);
        outState.putParcelable(DiagnoseActivity.EXTRA_USER, currentUser);
    }

    public Location getLocation() {
        Location location = null;


        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(FindADoctor.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

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

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentLocation = getLocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(FindADoctor.this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    private void callPlaces(){
        String categories = getCategories(conditionDetail);

        if(categories.contains("Other")){
            categories = "family doctor";
        }

        String queryText = categories;
        String location = currentLocation.getLatitude() + "," + currentLocation.getLongitude();

        PlacesApi placesApi = RetrofitClientPlaces.getRetrofitInstance().create(PlacesApi.class);
        Call<Places> call = placesApi.getPlaces(queryText,getResources().getString(R.string.google_places_type), location, LOCATION_RADIUS, getResources().getString(R.string.google_maps_key));
        call.enqueue(new Callback<Places>() {

            @Override
            public void onResponse(@NonNull Call<Places> call, @NonNull Response<Places> response) {
                if(response.body() != null){
                    setMapsMarker(response.body());
                    setPlacesDetail(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Places> call, @NonNull Throwable t) {
                Log.e("DoctorActivity","Counter :" +  t);

            }
        });

    }

    private void setPlacesDetail(final Places places){

        for(int i = 0; i < places.getResults().size(); i++){
            PlacesApi placesApi = RetrofitClientPlaces.getRetrofitInstance().create(PlacesApi.class);
            Call<PlaceDetail> call = placesApi.getPlaceDetail(places.getResults().get(i).getPlaceId(), getResources().getString(R.string.place_search_critera), getResources().getString(R.string.google_maps_key));
            final int x = i;
            call.enqueue(new Callback<PlaceDetail>() {
                @Override
                public void onResponse(@NonNull Call<PlaceDetail> call, @NonNull Response<PlaceDetail> response) {
                    if(response.body() != null){
                        places.getResults().get(x).setDetailResult(response.body().getResult());
                        if(x  == places.getResults().size() - 1){
                            userAdapter.setPlacesData(places.getResults());
                            progressBar.setVisibility(View.GONE);
                            allPlaces = places;
                            writeDoctorsToDB();
                        }
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

    private void getDoctorsFromDatabse(){
        final AppDatabase NotADoctor = AppDatabase.getInstance(this);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Result> result = new ArrayList<>();
                List<DoctorEntry> doctorEntrys = NotADoctor.databaseDao().loadDoctorsByDiagnoseId(counter);
                for(DoctorEntry doctorEntry:doctorEntrys){
                    DetailResult detailResult = new DetailResult();
                    detailResult.setFormattedPhoneNumber(doctorEntry.getDoctorPhoneNumber());


                    com.example.rokly.notadoctor.Model.Places.Location location = new com.example.rokly.notadoctor.Model.Places.Location();
                    location.setLng(doctorEntry.getLng());
                    location.setLat(doctorEntry.getLat());

                    Geometry geometry = new Geometry();
                    geometry.setLocation(location);

                    Result doctor = new Result();
                    doctor.setDetailResult(detailResult);
                    doctor.setGeometry(geometry);
                    doctor.setFormattedAddress(doctorEntry.getDoctorAddress());
                    doctor.setName(doctorEntry.getDoctorName());
                    doctor.setPlaceId(doctorEntry.getPlaceId());
                    result.add(doctor);
                }

                Places allPlaces = new Places();
                allPlaces.setResults(result);

                setMapsMarker(allPlaces);
                userAdapter.setPlacesData(result);

            }
        });
    }

    private void setUserMarker(){

        map.addMarker(new MarkerOptions().position(getCurrentLatLng())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title(getResources().getString(R.string.marker_your_position)));


        map.animateCamera(CameraUpdateFactory.newLatLngZoom(getCurrentLatLng(), getResources().getInteger(R.integer.zoom_level)), getResources().getInteger(R.integer.zoom_level), null);
    }

    private void writeDoctorsToDB(){
        final AppDatabase NotADoctor = AppDatabase.getInstance(this);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                DiagnoseEntry diagnoseEntry =  NotADoctor.databaseDao().loadDiagnoseByUserId(currentUser.getId());

                for(Result currentDoctor:allPlaces.getResults()){
                    String formattedPhoneNumber = "";
                    if(currentDoctor.getDetailResult().getFormattedPhoneNumber() != null){
                        formattedPhoneNumber = currentDoctor.getDetailResult().getFormattedPhoneNumber();
                    }

                    final DoctorEntry doctorEntry= new DoctorEntry(diagnoseEntry.getId(), currentDoctor.getName(), currentDoctor.getFormattedAddress(), currentDoctor.getGeometry().getLocation().getLat(), currentDoctor.getGeometry().getLocation().getLng(), formattedPhoneNumber, currentDoctor.getPlaceId());
                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            NotADoctor.databaseDao().insertDoctor(doctorEntry);
                        }
                    });
                }

                DoctorAppWidgetService.startActionUpdateDoctorWidget(FindADoctor.this, diagnoseEntry);
            }
        });

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
        currentLocation = location;
        setUserMarker();
        if(!isWidget){
            callPlaces();
        }else{

        }

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
        if(onSavedInstanceState){
            setUserMarker();
            setMapsMarker(allPlaces);
        }
    }
}