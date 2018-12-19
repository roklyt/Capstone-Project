package com.example.rokly.notadoctor;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rokly.notadoctor.Adapter.ConditionsAdapter;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;
import com.example.rokly.notadoctor.helper.ButtonAnimator;
import com.example.rokly.notadoctor.helper.CheckNetwork;
import com.example.rokly.notadoctor.helper.ToolBarHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConditionActivity extends AppCompatActivity implements ConditionsAdapter.ItemClickListener{
    public static final String EXTRA_CONDITIONS = "extraConditions";
    public static final String EXTRA_CONDITIONS_DETAIL = "extraConditionDetail";
    public static final String EXTRA_CONDITIONS_POSITION = "extraConditionPosition";
    private Diagnose diagnose;
    private UserEntry currentUser;
    private ConditionsAdapter conditionsAdapter;
    private ConditionDetail conditionDetail;
    private int currentItemPosition;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        setToolBarNavigation();

        activity = this;
        if(savedInstanceState != null){
            diagnose = savedInstanceState.getParcelable(EXTRA_CONDITIONS);
            findViews();
            conditionDetail = savedInstanceState.getParcelable(EXTRA_CONDITIONS_DETAIL);
            currentItemPosition = savedInstanceState.getInt(EXTRA_CONDITIONS_POSITION);
            currentUser = savedInstanceState.getParcelable(DiagnoseActivity.EXTRA_USER);

        }else{
            Intent intent = getIntent();
            if(intent.hasExtra(EXTRA_CONDITIONS)){
                diagnose = intent.getParcelableExtra(EXTRA_CONDITIONS);
                currentUser = intent.getParcelableExtra(DiagnoseActivity.EXTRA_USER);
                findViews();
            }
        }

    }

    private void findViews(){
        RecyclerView recyclerView = findViewById(R.id.rv_conditions);
        conditionsAdapter = new ConditionsAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(conditionsAdapter);
        setConditionsAdpater(diagnose);
    }

    private void setConditionsAdpater(Diagnose diagnose){
        List<Condition> conditionList = diagnose.getConditions();
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
        outState.putParcelable(DiagnoseActivity.EXTRA_USER, currentUser);
    }

    @Override
    public void onItemClickListener(Condition condition) {
        Toast.makeText(this, condition.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemExpandChecklist(final View view, Condition condition, final int position) {
        CheckNetwork checkNetwork = new CheckNetwork();
        if(checkNetwork.isNetworkConnected(activity)){
            InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance(ConditionActivity.this).create(InfermedicaApi.class);
            currentItemPosition = position;
            Call<ConditionDetail> call = infermedicaApi.getConditionById(condition.getId());
            call.enqueue(new Callback<ConditionDetail>() {

                @Override
                public void onResponse(@NonNull Call<ConditionDetail> call, @NonNull Response<ConditionDetail> response) {
                    if(response.body() != null){
                        conditionDetail = response.body();
                        setDetailText(view, response.body());
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.error_something, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ConditionDetail> call, @NonNull Throwable t) {
                    Log.e("ConditionActivity","" +  t);
                    Toast.makeText(getApplicationContext(), R.string.error_something, Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), R.string.error_no_network, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onButtonClicked(Condition condition) {
        Intent startMaps = new Intent(ConditionActivity.this, FindADoctor.class);
        startMaps.putExtra(FindADoctor.EXTRA_CONDITION_DETAIL, conditionDetail);
        startMaps.putExtra(DiagnoseActivity.EXTRA_USER, currentUser);
        startActivity(startMaps, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
    }

    @Override
    public void onResetScreen(View view) {
        setDetailText(view, conditionDetail);
    }

    private void setDetailText(View view, ConditionDetail conditionDetail){

        if(conditionDetail != null){

            TextView conditionDetailsNameTextView = view.findViewById(R.id.tv_name_value);
            TextView conditionDetailsCategoriesTextView = view.findViewById(R.id.tv_categories_value);
            TextView conditionDetailsPrevalenceTextView = view.findViewById(R.id.tv_prevalence_value);
            TextView conditionDetailsAcutenessTextView = view.findViewById(R.id.tv_acuteness_value);
            TextView conditionDetailsNameSeverityView = view.findViewById(R.id.tv_severity_value);
            TextView conditionDetailsHintTextView = view.findViewById(R.id.tv_hint);
            ImageButton conditionDetailSearchButton = view.findViewById(R.id.bt_find_a_doctor);

            ButtonAnimator.imageButtonAnimator(conditionDetailSearchButton);

            conditionDetailsNameTextView.setText(conditionDetail.getCommonName());
            conditionDetailsCategoriesTextView.setText(getCategories(conditionDetail).replace("_", " "));
            conditionDetailsPrevalenceTextView.setText(conditionDetail.getPrevalence().replace("_", " "));
            conditionDetailsAcutenessTextView.setText(conditionDetail.getAcuteness().replace("_", " "));
            conditionDetailsNameSeverityView.setText(conditionDetail.getSeverity().replace("_", " "));
            conditionDetailsHintTextView.setText(conditionDetail.getExtras().getHint());
        }
    }

    public static String getCategories(ConditionDetail conditionDetail){
        StringBuilder categories = new StringBuilder();
        for(int i = 0;i < conditionDetail.getCategories().size();i++){
            if(i == 0){
                categories = new StringBuilder(conditionDetail.getCategories().get(i));
            }else{
                categories.append(", ").append(conditionDetail.getCategories().get(i));
            }
        }

        return categories.toString();
    }

    private void setToolBarNavigation(){
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.drawable.not_a_docotor_icon);
        setSupportActionBar(myToolbar);

        View logoView = ToolBarHelper.getToolbarLogoView(myToolbar);
        if (logoView != null) {
            logoView.setOnClickListener(v -> {
                Intent intent = new Intent(ConditionActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                supportFinishAfterTransition();
            });
        }
    }
}
