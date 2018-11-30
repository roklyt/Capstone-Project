package com.example.rokly.notadoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.rokly.notadoctor.Model.Diagnose.Request.DiagnoseReq;
import com.example.rokly.notadoctor.Model.Diagnose.Request.Evidence;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Model.Parse.Request.Parse;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.ConditionActivity.EXTRA_CONDITIONS;

public class DiagnoseActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {
    public static final String EXTRA_DIAGNOSE = "extrEvidences";
    private DiagnoseReq currentDiagnose;
    private Diagnose diagnose;
    private static final double MINIMUM_PERCENTAGE = 0.7;
    private static int counter = 0;
    private final static int maxCounter = 15;
    private QuestionFragment questionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);

        Intent intent = getIntent();



        if (intent != null && intent.hasExtra(EXTRA_DIAGNOSE)) {

            currentDiagnose = intent.getParcelableExtra(EXTRA_DIAGNOSE);
            callInfermedica();
        }


    }

    @Override
    public void onFragmentInteraction(Evidence evidence) {
        currentDiagnose.getEvidence().add(evidence);
        //TODO write new evidence into the database
        //TODO check thihs implementaiont : implement percentage check when the diagnose is over
        //TODO check why solution is always null
        if(counter < maxCounter && !isMinimumPercentag()){
            callInfermedica();
            counter ++;
        }else{
            //TODO write final conditions into the database
            Intent conditionActivity = new Intent(DiagnoseActivity.this, ConditionActivity.class);
            conditionActivity.putExtra(EXTRA_CONDITIONS, diagnose);
            startActivity(conditionActivity);
        }

    }

    private void callInfermedica(){
            InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance().create(InfermedicaApi.class);
            Call<Diagnose> call = infermedicaApi.getQuestion(currentDiagnose);
            call.enqueue(new Callback<Diagnose>() {

                @Override
                public void onResponse(@NonNull Call<Diagnose> call, @NonNull Response<Diagnose> response) {
                    diagnose = response.body();

                    if(counter == 0){
                        addFragment();
                    }else{
                        replaceFragment();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Diagnose> call, @NonNull Throwable t) {
                    Log.e("DiagnoseActivity","Counter : " + counter + ":" +  t);

                }
            });

    }

    private void replaceFragment(){
        questionFragment = new QuestionFragment();
        questionFragment.setQuestion(diagnose.getQuestion());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.question_container, questionFragment)
                .commit();
    }

    private void addFragment(){
        questionFragment = new QuestionFragment();
        questionFragment.setQuestion(diagnose.getQuestion());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.question_container, questionFragment)
                .commit();
    }

    private boolean isMinimumPercentag(){
        boolean isMinimumPercentage = false;

        List<Condition> conditions = diagnose.getConditions();

        for(Condition condition: conditions){
            if(condition.getProbability() > MINIMUM_PERCENTAGE){
                isMinimumPercentage = true;
            }
        }

        return isMinimumPercentage;
    }
}
