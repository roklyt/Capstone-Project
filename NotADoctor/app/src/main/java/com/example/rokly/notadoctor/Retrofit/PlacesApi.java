package com.example.rokly.notadoctor.Retrofit;

import com.example.rokly.notadoctor.Model.PlaceDetail.PlaceDetail;
import com.example.rokly.notadoctor.Model.Places.Places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi{

    @GET("textsearch/json")
    Call<Places> getPlaces(
            @Query("query") String query,
            @Query("type") String type,
            @Query("location") String location,
            @Query("radius") long radius,
            @Query("key") String key);

    @GET("details/json")
    Call<PlaceDetail> getPlaceDetail(
                    @Query("placeid") String placeid,
                    @Query("fields") String fields,
                    @Query("key") String key);
}
