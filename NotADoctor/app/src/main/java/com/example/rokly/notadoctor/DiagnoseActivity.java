package com.example.rokly.notadoctor;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.transition.Slide;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.rokly.notadoctor.helper.CheckNetwork;
import com.example.rokly.notadoctor.helper.ToolBarHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.ConditionActivity.EXTRA_CONDITIONS;
import static com.example.rokly.notadoctor.helper.ChoiceId.getChoiceIdInt;

public class DiagnoseActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {
    public static final String EXTRA_DIAGNOSE = "extraDiagnose";
    public static final String EXTRA_USER = "extraUser";
    private static final String EXTRA_DIAGNOSE_RESP = "extraDiagnoseResp";
    private DiagnoseReq currentDiagnose;
    private UserEntry currentUser;
    private Diagnose diagnose;
    private static final double MINIMUM_PERCENTAGE = 0.85;
    private static int counter = 0;
    private final static int maxCounter = 3;
    private QuestionFragment questionFragment;
    private ProgressBar progressBar;
    private int initialValue = 0;
    private TextView progressPercentageTextView;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);

        setToolBarNavigation();

        activity = this;
        progressBar = findViewById(R.id.pb_question);
        progressPercentageTextView = findViewById(R.id.tv_progress_percentage);

        Intent intent = getIntent();

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            questionFragment = (QuestionFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            currentUser = savedInstanceState.getParcelable(EXTRA_USER);
            currentDiagnose = savedInstanceState.getParcelable(EXTRA_DIAGNOSE);
            diagnose = savedInstanceState.getParcelable(EXTRA_DIAGNOSE_RESP);

            showDiagnose();
        }else if(intent != null && intent.hasExtra(EXTRA_DIAGNOSE)) {
            currentUser = intent.getParcelableExtra(EXTRA_USER);
            currentDiagnose = intent.getParcelableExtra(EXTRA_DIAGNOSE);
            callInfermedica();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(getSupportFragmentManager() != null){
            getSupportFragmentManager().putFragment(outState, "myFragmentName", questionFragment);
        }
        outState.putParcelable(EXTRA_DIAGNOSE, currentDiagnose);
        outState.putParcelable(EXTRA_USER, currentUser);
        outState.putParcelable(EXTRA_DIAGNOSE_RESP, diagnose);
    }

    @Override
    public void onFragmentInteraction(List<Evidence> evidences) {
        CheckNetwork checkNetwork = new CheckNetwork();
        if(checkNetwork.isNetworkConnected(activity)){
            for(Evidence evidence:evidences){
                currentDiagnose.getEvidence().add(evidence);
            }

            writeEvidenceIntoDatabse(evidences);
            callInfermedica();
        }else{
            Toast.makeText(getApplicationContext(), R.string.error_no_network, Toast.LENGTH_LONG).show();
        }
    }

    private void callInfermedica(){
            progressBar.setVisibility(View.VISIBLE);
            InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance(DiagnoseActivity.this).create(InfermedicaApi.class);
            Call<Diagnose> call = infermedicaApi.getQuestion(currentDiagnose);
            call.enqueue(new Callback<Diagnose>() {

                @Override
                public void onResponse(@NonNull Call<Diagnose> call, @NonNull Response<Diagnose> response) {
                    if(response.body() != null){
                        diagnose = response.body();
                        showDiagnose();
                        counter++;
                    } else{
                    Toast.makeText(getApplicationContext(), R.string.error_something, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Diagnose> call, @NonNull Throwable t) {
                    Log.e("DiagnoseActivity","Counter : " + counter + ":" +  t);
                    Toast.makeText(getApplicationContext(), R.string.error_something, Toast.LENGTH_LONG).show();
                }
            });
    }

    private void showDiagnose(){
        progressBar.setVisibility(View.GONE);
        Double myDouble = diagnose.getConditions().get(0).getProbability() * 100;
        int percentage = myDouble.intValue();
        animateTextView(initialValue, percentage, progressPercentageTextView);
        initialValue = percentage;

        if(counter < maxCounter && !isMinimumPercentage()){
            if(counter == 0){
                addFragment();
            }else{
                replaceFragment();
            }
        }else{
            writeFinalConditionsIntoDb(diagnose.getConditions());
            Intent conditionActivity = new Intent(DiagnoseActivity.this, ConditionActivity.class);
            conditionActivity.putExtra(EXTRA_CONDITIONS, diagnose);
            conditionActivity.putExtra(EXTRA_USER, currentUser);
            startActivity(conditionActivity, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        }
    }

    private void replaceFragment(){
        questionFragment = new QuestionFragment();
        questionFragment.setQuestion(diagnose.getQuestion());
        questionFragment.setEnterTransition(new Slide(Gravity.RIGHT));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.question_container, questionFragment)
                .commit();
    }

    private void addFragment(){
        questionFragment = new QuestionFragment();
        questionFragment.setQuestion(diagnose.getQuestion());
        questionFragment.setEnterTransition(new Slide(Gravity.RIGHT));
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

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString() + "%");

            }
        });
        valueAnimator.start();
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
                    Intent intent = new Intent(DiagnoseActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    supportFinishAfterTransition();
                }
            });
        }
    }
}
