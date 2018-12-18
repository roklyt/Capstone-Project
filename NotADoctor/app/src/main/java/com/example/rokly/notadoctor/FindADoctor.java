package com.example.rokly.notadoctor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rokly.notadoctor.Adapter.PlacesAdapter;
import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.DiagnoseEntry;
import com.example.rokly.notadoctor.Database.DoctorEntry;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.PlaceDetail.PlaceDetail;
import com.example.rokly.notadoctor.Model.Places.Places;
import com.example.rokly.notadoctor.Model.Places.Result;
import com.example.rokly.notadoctor.Retrofit.PlacesApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientPlaces;
import com.example.rokly.notadoctor.helper.CheckNetwork;
import com.example.rokly.notadoctor.helper.ConvertDocEntryIntoResult;
import com.example.rokly.notadoctor.helper.LocationHelper;
import com.example.rokly.notadoctor.helper.ToolBarHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.ConditionActivity.getCategories;

public class FindADoctor extends AppCompatActivity implements OnMapReadyCallback, LocationHelper.OnLocationListener, PlacesAdapter.ItemClickListener, GoogleMap.OnMarkerClickListener {

    public final static String EXTRA_CONDITION_DETAIL = "conditionDetail";
    public final static String EXTRA_IS_WIDGET = "isWidget";

    private final static String SAVE_PLACES = "savePlaces";
    private final static String SAVE_LOCATION = "saveLocation";

    private PlacesAdapter userAdapter;
    private GoogleMap map;
    private LocationHelper locationHelper;
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
    private List<Marker> markers = new ArrayList<>();
    private RecyclerView userRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_adoctor);

        setToolBarNavigation();

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

            if(intent.hasExtra(EXTRA_IS_WIDGET)){
                counter = intent.getIntExtra(ListWidgetService.EXTRA_DIAGNOSE, 1);
                isWidget = intent.getBooleanExtra(EXTRA_IS_WIDGET, true);
                getDoctorsFromDatabse();
            }

            if(intent.hasExtra(EXTRA_CONDITION_DETAIL)) {
                conditionDetail = intent.getParcelableExtra(EXTRA_CONDITION_DETAIL);
                currentUser = intent.getParcelableExtra(DiagnoseActivity.EXTRA_USER);
            }

            locationHelper = new LocationHelper(this);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            currentLocation = locationHelper.getLocation(this, locationManager);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_PLACES, allPlaces);
        outState.putParcelable(SAVE_LOCATION, currentLocation);
        outState.putParcelable(DiagnoseActivity.EXTRA_USER, currentUser);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationHelper.getLocation(this, locationManager);
                } else {

                    Toast.makeText(this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void callPlaces(){
        CheckNetwork checkNetwork = new CheckNetwork();
        if(checkNetwork.isNetworkConnected(this)){
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
                        setUserMarker();
                        setPlacesDetail(response.body());
                        progressBar.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.error_something, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Places> call, @NonNull Throwable t) {
                    Log.e("DoctorActivity","Counter :" +  t);
                    Toast.makeText(getApplicationContext(), R.string.error_something, Toast.LENGTH_LONG).show();
                }
            });
        } else{
            Toast.makeText(getApplicationContext(), R.string.error_no_network, Toast.LENGTH_LONG).show();
        }
    }

    private void setPlacesDetail(final Places places){
        CheckNetwork checkNetwork = new CheckNetwork();
        if(checkNetwork.isNetworkConnected(this)){
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
                                allPlaces = places;
                                writeDoctorsToDB();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlaceDetail> call, @NonNull Throwable t) {
                        Log.e("ConditionActivity","" +  t);
                        Toast.makeText(getApplicationContext(), R.string.error_something, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.error_no_network, Toast.LENGTH_LONG).show();
        }
    }

    private void getDoctorsFromDatabse(){
        final AppDatabase NotADoctor = AppDatabase.getInstance(this);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<DoctorEntry> doctorEntrys = NotADoctor.databaseDao().loadDoctorsByDiagnoseId(counter);
                ConvertDocEntryIntoResult convertDocEntryIntoResult = new ConvertDocEntryIntoResult();

                allPlaces = new Places();
                allPlaces.setResults(convertDocEntryIntoResult.convertDocEntryIntoResult(doctorEntrys));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userAdapter.setPlacesData(allPlaces.getResults());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }

    private void writeDoctorsToDB(){
        final AppDatabase NotADoctor = AppDatabase.getInstance(this);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                DiagnoseEntry diagnoseEntry =  NotADoctor.databaseDao().loadDiagnoseByUserId(currentUser.getId());

                for(Result currentDoctor:allPlaces.getResults()){
                    String formattedPhoneNumber = "";
                    if(currentDoctor.getDetailResult() != null){
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

    @Override
    public void onItemClickListener(Result currentResult) {

    }

    @Override
    public void onCallClickListener(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }

    @Override
    public void onExpandClickListener(String name) {
        for (Marker marker : markers) {
            if (marker.getTitle().equals(name)) {
                marker.showInfoWindow();
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        if(onSavedInstanceState && !isWidget){
            setUserMarker();
            setMapsMarker(allPlaces);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try{
            for(int i = 0;i < allPlaces.getResults().size(); i++){
                if(allPlaces.getResults().get(i).getName().equals(marker.getTitle())){
                    userRecyclerView.scrollToPosition(i);
                    Objects.requireNonNull(userRecyclerView.findViewHolderForAdapterPosition(i)).itemView.performClick();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private void setMapsMarker(Places places){
        List<Result> results = places.getResults();

        for(Result result:results){
            LatLng latLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
            String markerTitle = result.getName();

            Marker newMarker = map.addMarker(new MarkerOptions().position(latLng)
                    .title(markerTitle));

            markers.add(newMarker);
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
    public void locationChanged(Location location) {
        currentLocation = location;
        if(!isWidget){
            callPlaces();
        }else{
            setUserMarker();
            setMapsMarker(allPlaces);
        }
    }

    private void setToolBarNavigation(){
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.drawable.not_a_docotor_icon);
        setSupportActionBar(myToolbar);

        View logoView = ToolBarHelper.getToolbarLogoView(myToolbar);
        if (logoView != null) {
            logoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FindADoctor.this, WelcomeActivity.class);
                    startActivity(intent);
                    supportFinishAfterTransition();
                }
            });
        }
    }
}