package com.example.rokly.notadoctor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.rokly.notadoctor.Model.Diagnose.Request.DiagnoseReq;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Model.Parse.Request.Parse;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiagnoseActivity extends AppCompatActivity {
    public static final String EXTRA_DIAGNOSE = "extrEvidences";
    private DiagnoseReq currentDiagnose;
    private Diagnose diagnose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);

        Intent intent = getIntent();



        if (intent != null && intent.hasExtra(EXTRA_DIAGNOSE)) {

            currentDiagnose = intent.getParcelableExtra(EXTRA_DIAGNOSE);



            InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance().create(InfermedicaApi.class);
            Call<Diagnose> call = infermedicaApi.getQuestion(currentDiagnose);
            call.enqueue(new Callback<Diagnose>() {

                @Override
                public void onResponse(@NonNull Call<Diagnose> call, @NonNull Response<Diagnose> response) {
                   diagnose = response.body();
                }

                @Override
                public void onFailure(@NonNull Call<Diagnose> call, @NonNull Throwable t) {
                    Log.e("MentionActivity","" +  t);

                }
            });

        }


    }
}
