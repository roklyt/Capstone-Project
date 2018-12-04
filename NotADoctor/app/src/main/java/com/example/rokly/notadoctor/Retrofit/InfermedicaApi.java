package com.example.rokly.notadoctor.Retrofit;



import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.Diagnose.Request.DiagnoseReq;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;
import com.example.rokly.notadoctor.Model.Parse.Response.Mentions;
import com.example.rokly.notadoctor.Model.Parse.Request.Parse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InfermedicaApi {
    String appId = "app_id";
    String appKey = "app_key";

    @Headers({
            "Content-Type: application/json",
            "App-Id: " + appId,
            "App-Key: " + appKey
    })
    @POST("parse")
    Call<List<Mentions>> getMentions(@Body Parse parse);


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
