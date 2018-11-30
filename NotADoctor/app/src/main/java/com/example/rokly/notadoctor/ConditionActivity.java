package com.example.rokly.notadoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.rokly.notadoctor.Adapter.ConditionsAdapter;
import com.example.rokly.notadoctor.Adapter.MentionAdpater;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Diagnose;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;

import java.util.List;

public class ConditionActivity extends AppCompatActivity implements ConditionsAdapter.ItemClickListener{
    public static final String EXTRA_CONDITIONS = "extraConditions";
    private List<Condition> conditionList;
    private Diagnose diagnose;
    private RecyclerView recyclerView;
    private ConditionsAdapter conditionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);


        Intent intent = getIntent();

        diagnose = intent.getParcelableExtra(EXTRA_CONDITIONS);

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
    public void onItemClickListener(Condition condition) {
        Toast.makeText(this, condition.getName().toString(), Toast.LENGTH_SHORT).show();
    }
}
