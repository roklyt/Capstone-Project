package com.example.rokly.notadoctor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rokly.notadoctor.Adapter.ConditionsAdapter;
import com.example.rokly.notadoctor.Adapter.MentionAdpater;
import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConditionActivity extends AppCompatActivity implements ConditionsAdapter.ItemClickListener{
    public static final String EXTRA_CONDITIONS = "extraConditions";
    public static final String EXTRA_CONDITIONS_DETAIL = "extraConditionDetail";
    public static final String EXTRA_CONDITIONS_POSITION = "extraConditionPosition";
    private List<Condition> conditionList;
    private Diagnose diagnose;
    private RecyclerView recyclerView;
    private ConditionsAdapter conditionsAdapter;
    private ConditionDetail conditionDetail;
    private int currentItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        if(savedInstanceState != null){
            diagnose = savedInstanceState.getParcelable(EXTRA_CONDITIONS);
            findViews();
            conditionDetail = savedInstanceState.getParcelable(EXTRA_CONDITIONS_DETAIL);
            currentItemPosition = savedInstanceState.getInt(EXTRA_CONDITIONS_POSITION);

            setDetailText(recyclerView.findViewHolderForAdapterPosition(currentItemPosition).itemView, conditionDetail);
        }else{
            Intent intent = getIntent();
            if(intent.hasExtra(EXTRA_CONDITIONS)){
                diagnose = intent.getParcelableExtra(EXTRA_CONDITIONS);
                findViews();
            }
        }
    }

    private void findViews(){
        recyclerView = findViewById(R.id.rv_conditions);
        conditionsAdapter = new ConditionsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(conditionsAdapter);
        setConditionsAdpater(diagnose);
    }

    private void setConditionsAdpater(Diagnose diagnose){
        conditionList = diagnose.getConditions();
        if(diagnose.getConditions().size() == 0){
            Toast.makeText(getApplicationContext(), R.string.error_no_conditions, Toast.LENGTH_LONG).show();
            finish();
        }else{
            conditionsAdapter.setUserData(conditionList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_CONDITIONS, diagnose);
        outState.putParcelable(EXTRA_CONDITIONS_DETAIL, conditionDetail );
        outState.putInt(EXTRA_CONDITIONS_POSITION, currentItemPosition);
    }

    @Override
    public void onItemClickListener(Condition condition) {
        Toast.makeText(this, condition.getName().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemExpandChecklist(final View view, Condition condition, int position) {
        InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance().create(InfermedicaApi.class);
        currentItemPosition = position;
        Call<ConditionDetail> call = infermedicaApi.getConditionById(condition.getId());
        call.enqueue(new Callback<ConditionDetail>() {

            @Override
            public void onResponse(@NonNull Call<ConditionDetail> call, @NonNull Response<ConditionDetail> response) {
                //TODO check for bad response
                conditionDetail = response.body();
                setDetailText(view, response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ConditionDetail> call, @NonNull Throwable t) {
                Log.e("ConditionActivity","" +  t);

            }
        });
    }

    @Override
    public void onButtonClicked(Condition condition) {
/*        Intent startMaps = new Intent(ConditionActivity.this, DoctorActivity.class);
        startMaps.putExtra(DoctorActivity.EXTRA_CONDITION_DETAIL, conditionDetail);*/
        Intent startMaps = new Intent(ConditionActivity.this, FindADoctor.class);
        startMaps.putExtra(FindADoctor.EXTRA_CONDITION_DETAIL, conditionDetail);
        startActivity(startMaps);
    }

    private void setDetailText(View view, ConditionDetail conditionDetail){
        if(conditionDetail != null){

            TextView conditionDetailsNameTextView = view.findViewById(R.id.tv_name_value);
            TextView conditionDetailsCategoriesTextView = view.findViewById(R.id.tv_categories_value);
            TextView conditionDetailsPrevalenceTextView = view.findViewById(R.id.tv_prevalence_value);
            TextView conditionDetailsAcutenessTextView = view.findViewById(R.id.tv_acuteness_value);
            TextView conditionDetailsNameSeverityView = view.findViewById(R.id.tv_severity_value);
            TextView conditionDetailsHintTextView = view.findViewById(R.id.tv_hint);

            conditionDetailsNameTextView.setText(conditionDetail.getCommonName());
            conditionDetailsCategoriesTextView.setText(getCategories(conditionDetail));
            conditionDetailsPrevalenceTextView.setText(conditionDetail.getPrevalence());
            conditionDetailsAcutenessTextView.setText(conditionDetail.getAcuteness());
            conditionDetailsNameSeverityView.setText(conditionDetail.getSeverity());
            conditionDetailsHintTextView.setText(conditionDetail.getExtras().getHint());
        }
    }

    public static String getCategories(ConditionDetail conditionDetail){
        String categories = "";
        for(int i = 0;i < conditionDetail.getCategories().size();i++){
            if(i == 0){
                categories = conditionDetail.getCategories().get(i);
            }else{
                categories = categories + ", "+ conditionDetail.getCategories().get(i);
            }
        }

        return categories;
    }
}
