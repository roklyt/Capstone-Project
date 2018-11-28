package com.example.rokly.notadoctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.rokly.notadoctor.Adapter.MentionAdpater;
import com.example.rokly.notadoctor.Model.Mention;
import com.example.rokly.notadoctor.Model.Mentions;
import com.example.rokly.notadoctor.Model.Parse;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;

import java.util.List;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentionActivity extends AppCompatActivity implements MentionAdpater.ItemClickListener{
    private MentionAdpater MentionAdpater;
    private RecyclerView recyclerView;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mention);
        findViews();
        showProgress();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(SymptomActivity.EXTRA_USER)) {

            Parse parse = new Parse();
            parse.setText(intent.getStringExtra(SymptomActivity.EXTRA_USER_SYMPTOME_TEXT));
            parse.setCorrectSpelling(false);
            parse.setIncludeTokens(false);

            InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance().create(InfermedicaApi.class);
            Call<Mention> call = infermedicaApi.getMention(parse);
            call.enqueue(new Callback<Mention>() {

                @Override
                public void onResponse(Call<Mention> call, Response<Mention> response) {
                    showRecyclerView();
                    setMentionAdpater(response.body());
                }

                @Override
                public void onFailure(Call<Mention> call, Throwable t) {
                    Log.e("MentionActivity","" +  t);

                }
            });
        }



    }

    private void findViews(){
        ProgressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rv_mentions);

        MentionAdpater = new MentionAdpater(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(MentionAdpater);
    }

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        ProgressBar.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        ProgressBar.setVisibility(View.GONE);
    }

    private void setMentionAdpater(Mention mention){

        MentionAdpater.setUserData(mention.getMentions());
    }

    @Override
    public void onItemClickListener(Mentions mention) {

    }
}
