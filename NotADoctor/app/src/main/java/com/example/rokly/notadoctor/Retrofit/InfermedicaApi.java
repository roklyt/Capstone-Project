package com.example.rokly.notadoctor.Retrofit;

import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.Diagnose.Request.DiagnoseReq;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;
import com.example.rokly.notadoctor.Model.Parse.Request.Parse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InfermedicaApi {
    String appId = "App_Id";
    String appKey = "App_Key";

    @Headers({
            "Content-Type: application/json",
            "App-Id: " + appId,
            "App-Key: " + appKey
    })
    @POST("parse")
    Call<Mention> getMention(@Body Parse parse);

    @Headers({
            "Content-Type: application/json",
            "App-Id: " + appId,
            "App-Key: " + appKey
    })
    @POST("diagnosis")
    Call<Diagnose> getQuestion(@Body DiagnoseReq diagnoseReq);

    @Headers({
            "Content-Type: application/json",
            "App-Id: " + appId,
            "App-Key: " + appKey
    })
    @GET("conditions/{id}")
    Call<ConditionDetail> getConditionById(@Path("id") String id);

}
