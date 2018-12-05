package com.example.rokly.notadoctor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.ConditionEntry;
import com.example.rokly.notadoctor.Database.DiagnoseEntry;
import com.example.rokly.notadoctor.Database.EvidenceEntry;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.Model.Diagnose.Request.DiagnoseReq;
import com.example.rokly.notadoctor.Model.Diagnose.Request.Evidence;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.ConditionActivity.EXTRA_CONDITIONS;
import static com.example.rokly.notadoctor.helper.ChoiceId.getChoiceIdInt;

public class DiagnoseActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {
    public static final String EXTRA_DIAGNOSE = "extraDiagnose";
    public static final String EXTRA_USER = "extraUser";
    private DiagnoseReq currentDiagnose;
    private UserEntry currentUser;
    private Diagnose diagnose;
    private static final double MINIMUM_PERCENTAGE = 0.5;
    private static int counter = 0;
    private final static int maxCounter = 1;
    private QuestionFragment questionFragment;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);

        progressBar = findViewById(R.id.pb_question);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            questionFragment = (QuestionFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_DIAGNOSE)) {
            currentUser = intent.getParcelableExtra(EXTRA_USER);
            currentDiagnose = intent.getParcelableExtra(EXTRA_DIAGNOSE);
            callInfermedica();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "myFragmentName", questionFragment);
    }

    @Override
    public void onFragmentInteraction(List<Evidence> evidences) {
        for(Evidence evidence:evidences){
            currentDiagnose.getEvidence().add(evidence);
        }

        writeEvidenceIntoDatabse(evidences);

        //TODO check why solution is always null
        if(counter < maxCounter && !isMinimumPercentage()){
            callInfermedica();
            counter ++;
        }else{
            writeFinalConditionsIntoDb(diagnose.getConditions());
            Intent conditionActivity = new Intent(DiagnoseActivity.this, ConditionActivity.class);
            conditionActivity.putExtra(EXTRA_CONDITIONS, diagnose);
            startActivity(conditionActivity);
        }

    }

    private void callInfermedica(){
            progressBar.setVisibility(View.VISIBLE);
            InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance().create(InfermedicaApi.class);
            Call<Diagnose> call = infermedicaApi.getQuestion(currentDiagnose);
            call.enqueue(new Callback<Diagnose>() {

                @Override
                public void onResponse(@NonNull Call<Diagnose> call, @NonNull Response<Diagnose> response) {
                    progressBar.setVisibility(View.GONE);
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

    private boolean isMinimumPercentage(){
        boolean isMinimumPercentage = false;

        List<Condition> conditions = diagnose.getConditions();

        for(Condition condition: conditions){
            if(condition.getProbability() > MINIMUM_PERCENTAGE){
                isMinimumPercentage = true;
            }
        }

        return isMinimumPercentage;
    }

    private void writeEvidenceIntoDatabse(final List<Evidence> evicdences){
        final AppDatabase NotADoctor = AppDatabase.getInstance(this);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                DiagnoseEntry diagnoseEntry =  NotADoctor.databaseDao().loadDiagnoseByUserId(currentUser.getId());

                for(Evidence evidence:evicdences){
                    final EvidenceEntry evidenceEntry= new EvidenceEntry(diagnoseEntry.getId(), evidence.getId(), getChoiceIdInt(evidence.getChoiceId()));
                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            NotADoctor.databaseDao().insertEvidence(evidenceEntry);
                        }
                    });
                }
            }
        });
    }

    private void writeFinalConditionsIntoDb(final List<Condition> conditions){
        final AppDatabase NotADoctor = AppDatabase.getInstance(this);
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    DiagnoseEntry diagnoseEntry =  NotADoctor.databaseDao().loadDiagnoseByUserId(currentUser.getId());

                    for(Condition condition:conditions){
                        final ConditionEntry conditionEntry = new ConditionEntry(diagnoseEntry.getId(), condition.getId(), condition.getName(), condition.getProbability());
                        AppExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                NotADoctor.databaseDao().insertCondition(conditionEntry);
                            }
                        });
                    }

                }
            });
        }
}
