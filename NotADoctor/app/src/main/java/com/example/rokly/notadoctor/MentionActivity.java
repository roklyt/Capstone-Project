package com.example.rokly.notadoctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.rokly.notadoctor.Adapter.MentionAdpater;
import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.DiagnoseEntry;
import com.example.rokly.notadoctor.Database.EvidenceEntry;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.Model.Diagnose;
import com.example.rokly.notadoctor.Model.Evidence;
import com.example.rokly.notadoctor.Model.Mention;
import com.example.rokly.notadoctor.Model.Mentions;
import com.example.rokly.notadoctor.Model.Parse;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;
import com.example.rokly.notadoctor.helper.ChoiceId;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rokly.notadoctor.helper.ChoiceId.getChoiceIdInt;
import static com.example.rokly.notadoctor.helper.ChoiceId.getChoiceIdString;

public class MentionActivity extends AppCompatActivity implements MentionAdpater.ItemClickListener{
    private MentionAdpater MentionAdpater;
    private RecyclerView recyclerView;
    ProgressBar ProgressBar;
    private List<Mentions> MentionsList;
    Button ReEnterButton;
    Button StartDiagnoseButton;
    private Diagnose CurrentDiagnose;

    private AppDatabase NotADoctorDB;
    private static final int DEFAULT_DIAGNOSE_ID = -1;
    private int DiagnoseId = DEFAULT_DIAGNOSE_ID;

    private UserEntry CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mention);
        findViews();
        showProgress();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(SymptomActivity.EXTRA_USER)) {

            CurrentUser = intent.getParcelableExtra(SymptomActivity.EXTRA_USER);

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
                    MentionAdpater.removeItem(position);
                }
            }).attachToRecyclerView(recyclerView);

        }

        NotADoctorDB = AppDatabase.getInstance(getApplicationContext());

    }

    private void findViews(){
        ProgressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rv_mentions);

        ReEnterButton = findViewById(R.id.bt_back_to_symptome);
        ReEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        StartDiagnoseButton = findViewById(R.id.bt_start_diagnose);
        StartDiagnoseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DiagnoseEntry diagnose = new DiagnoseEntry(CurrentUser.getId(), System.currentTimeMillis() / 1000L);
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(DiagnoseId == DEFAULT_DIAGNOSE_ID){
                            NotADoctorDB.databaseDao().insertDiagnose(diagnose);
                        }else{
                            diagnose.setId(DiagnoseId);
                            NotADoctorDB.databaseDao().updateDiagnose(diagnose);
                        }
                    }
                });



                for(Mentions oneMention: MentionsList){
                    final EvidenceEntry evidenceEntry = new EvidenceEntry(CurrentUser.getId(), oneMention.getId(), getChoiceIdInt(oneMention.getChoiceId()));

                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            NotADoctorDB.databaseDao().insertEvidence(evidenceEntry);
                        }
                    });
                }

                createDiagnose();

                Intent startDiagnose = new Intent(MentionActivity.this, DiagnoseActivity.class);
                startDiagnose.putExtra(DiagnoseActivity.EXTRA_DIAGNOSE, )



            }
        });

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
        MentionsList = mention.getMentions();
        if(mention.getMentions().size() == 0){
            Toast.makeText(getApplicationContext(), R.string.error_no_mentions, Toast.LENGTH_LONG).show();
            finish();
        }else{
            MentionAdpater.setUserData(MentionsList);
        }
    }

    private void createDiagnose(){


        Evidence bmiOver30 = new Evidence();
        bmiOver30.setChoiceId(getChoiceIdString(CurrentUser.getBmiOver30()));
        bmiOver30.setId("p_7");

        Evidence bmiUnder19 = new Evidence();
        bmiUnder19.setChoiceId(getChoiceIdString(CurrentUser.getBmiUnder19()));
        bmiUnder19.setId("p_6");

        Evidence hypertension = new Evidence();
        hypertension.setChoiceId(getChoiceIdString(CurrentUser.getHypertension()));
        hypertension.setId("p_9");

        Evidence smoking = new Evidence();
        smoking.setChoiceId(getChoiceIdString(CurrentUser.getSmoking()));
        smoking.setId("p_28");



        List<Evidence> evidenceList = null;
        evidenceList.add(bmiOver30);
        evidenceList.add(bmiUnder19);
        evidenceList.add(hypertension);
        evidenceList.add(smoking);

        for(Mentions oneMention: MentionsList){
            Evidence evidence = new Evidence();
            evidence.setChoiceId(oneMention.getChoiceId());
            evidence.setId(oneMention.getId());
            evidenceList.add(evidence);

        }

        CurrentDiagnose = new Diagnose();
        CurrentDiagnose.setAge(CurrentUser.getAge());
        CurrentDiagnose.setSex(CurrentUser.getSex());
        CurrentDiagnose.setEvidence(evidenceList);

    }

    @Override
    public void onItemClickListener(Mentions mention) {

    }
}
