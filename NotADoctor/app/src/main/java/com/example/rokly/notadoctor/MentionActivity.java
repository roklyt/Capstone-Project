package com.example.rokly.notadoctor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.rokly.notadoctor.Adapter.MentionAdpater;
import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.DiagnoseEntry;
import com.example.rokly.notadoctor.Database.EvidenceEntry;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.Model.Diagnose.Request.DiagnoseReq;
import com.example.rokly.notadoctor.Model.Diagnose.Request.Evidence;
import com.example.rokly.notadoctor.Model.Diagnose.Request.Extras;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;
import com.example.rokly.notadoctor.Model.Parse.Response.Mentions;
import com.example.rokly.notadoctor.Model.Parse.Request.Parse;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.helper.ChoiceId.getChoiceIdInt;
import static com.example.rokly.notadoctor.helper.ChoiceId.getChoiceIdString;

public class MentionActivity extends AppCompatActivity implements MentionAdpater.ItemClickListener{
    private MentionAdpater mentionAdpater;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    private List<Mentions> mentionsList;
    Button reEnterButton;
    Button startDiagnoseButton;
    private DiagnoseReq currentDiagnose;

    private AppDatabase notADoctorDB;
    private static final int DEFAULT_DIAGNOSE_ID = -1;
    private int diagnoseId = DEFAULT_DIAGNOSE_ID;

    private UserEntry currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mention);
        findViews();
        showProgress();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(SymptomActivity.EXTRA_USER)) {

            currentUser = intent.getParcelableExtra(SymptomActivity.EXTRA_USER);

            Parse parse = new Parse();
            parse.setText(intent.getStringExtra(SymptomActivity.EXTRA_USER_SYMPTOME_TEXT));
            parse.setCorrectSpelling(false);
            parse.setIncludeTokens(false);

            InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance().create(InfermedicaApi.class);
            Call<Mention> call = infermedicaApi.getMention(parse);
            call.enqueue(new Callback<Mention>() {

                @Override
                public void onResponse(@NonNull Call<Mention> call, @NonNull Response<Mention> response) {
                    showRecyclerView();
                    setMentionAdpater(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<Mention> call, @NonNull Throwable t) {
                    Log.e("MentionActivity","" +  t);

                }
            });

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    int position = viewHolder.getAdapterPosition();
                    mentionAdpater.removeItem(position);
                }
            }).attachToRecyclerView(recyclerView);

        }

        notADoctorDB = AppDatabase.getInstance(getApplicationContext());

    }

    private void findViews(){
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rv_mentions);

        reEnterButton = findViewById(R.id.bt_back_to_symptome);
        reEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        startDiagnoseButton = findViewById(R.id.bt_start_diagnose);
        startDiagnoseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DiagnoseEntry diagnose = new DiagnoseEntry(currentUser.getId(), System.currentTimeMillis() / 1000L);
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(diagnoseId == DEFAULT_DIAGNOSE_ID){
                            notADoctorDB.databaseDao().insertDiagnose(diagnose);
                        }else{
                            diagnose.setId(diagnoseId);
                            notADoctorDB.databaseDao().updateDiagnose(diagnose);
                        }
                    }
                });



                for(Mentions oneMention: mentionsList){
                    final EvidenceEntry evidenceEntry = new EvidenceEntry(currentUser.getId(), oneMention.getId(), getChoiceIdInt(oneMention.getChoiceId()));

                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            notADoctorDB.databaseDao().insertEvidence(evidenceEntry);
                        }
                    });
                }

                createDiagnose();

                Intent startDiagnose = new Intent(MentionActivity.this, DiagnoseActivity.class);

                startDiagnose.putExtra(DiagnoseActivity.EXTRA_DIAGNOSE, currentDiagnose);
                startActivity(startDiagnose);



            }
        });

        mentionAdpater = new MentionAdpater(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mentionAdpater);
    }

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void setMentionAdpater(Mention mention){
        mentionsList = mention.getMentions();
        if(mention.getMentions().size() == 0){
            Toast.makeText(getApplicationContext(), R.string.error_no_mentions, Toast.LENGTH_LONG).show();
            finish();
        }else{
            mentionAdpater.setUserData(mentionsList);
        }
    }

    private void createDiagnose(){


        Evidence bmiOver30 = new Evidence("p_7", getChoiceIdString(currentUser.getBmiOver30()));
        Evidence bmiUnder19 = new Evidence("p_6", getChoiceIdString(currentUser.getBmiUnder19()));
        Evidence hypertension = new Evidence("p_9", getChoiceIdString(currentUser.getHypertension()));
        Evidence smoking = new Evidence("p_28", getChoiceIdString(currentUser.getSmoking()));


        List<Evidence> evidenceList = new ArrayList<>();
        evidenceList.add(bmiOver30);
        evidenceList.add(bmiUnder19);
        evidenceList.add(hypertension);
        evidenceList.add(smoking);

        for(Mentions oneMention: mentionsList){
            Evidence evidence = new Evidence(oneMention.getId(), oneMention.getChoiceId());
            evidenceList.add(evidence);

        }

        //TODO set extra to false to get all questions
        Extras extra = new Extras();
        extra.setDisableGroups(true);

        currentDiagnose = new DiagnoseReq(currentUser.getSex(), currentUser.getAge(), evidenceList, extra);
    }

    @Override
    public void onItemClickListener(Mentions mention) {

    }
}
