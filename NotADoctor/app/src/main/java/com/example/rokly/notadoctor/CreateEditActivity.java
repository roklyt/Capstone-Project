package com.example.rokly.notadoctor;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.ViewModel.CreateEditViewModel;
import com.example.rokly.notadoctor.ViewModel.CreateEditViewModelFactory;

import java.util.Locale;


public class CreateEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_USER_ID = "userExtraId";
    public static final String INSTANCE_USER_ID = "userInstanceId";
    private static final String SEX_TYPE_M = "male";
    private static final String SEX_TYPE_W = "female";
    private static final String SEX_TYPE_D = "diverse";

    public static final int PRESENT_INT = 1;
    public static final int ABSENT_INT = 0;


    private EditText NameEditField;
    private Spinner SexSpinner;
    private String Sex;

    private EditText AgeEditField;
    private EditText HeightEditField;
    private EditText WeightEditField;

    private Button AbortButton;
    private Button SaveButton;

    private AppDatabase UserDb;

    private static final int DEFAULT_USER_ID = -1;

    private int UserId = DEFAULT_USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        findViews();

        UserDb = AppDatabase.getInstance(getApplicationContext());

        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_USER_ID)){
            UserId = savedInstanceState.getInt(INSTANCE_USER_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_USER_ID)) {
            SaveButton.setText(R.string.button_update);
            if (UserId == DEFAULT_USER_ID) {
                UserId = intent.getIntExtra(EXTRA_USER_ID, DEFAULT_USER_ID);

                CreateEditViewModelFactory factory = new CreateEditViewModelFactory(UserDb, UserId);

                final CreateEditViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(CreateEditViewModel.class);

                viewModel.getUser().observe(this, new Observer<UserEntry>() {
                    @Override
                    public void onChanged(@Nullable UserEntry userEntry) {
                        viewModel.getUser().removeObserver(this);
                        assert userEntry != null;
                        showUserValues(userEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_USER_ID, UserId);
        super.onSaveInstanceState(outState);
    }

    private void showUserValues(UserEntry user){
        if(user == null){return;}
        NameEditField.setText(user.getName());
        setSpinnerSelection(user.getSex());
        AgeEditField.setText(String.format(Locale.getDefault(),"%d", user.getAge()));
        HeightEditField.setText(String.format(Locale.getDefault(),"%d", user.getHeight()));
        WeightEditField.setText(String.format(Locale.getDefault(),"%d", user.getWeight()));

        setRadioGroup(user.getHypertension(), R.id.rg_hypertension);
        setRadioGroup(user.getSmoking(), R.id.rg_smoking);
    }

    private void findViews(){
        NameEditField = findViewById(R.id.et_name);
        SexSpinner = findViewById(R.id.sp_sex);
        setSpinner();
        AgeEditField = findViewById(R.id.et_age);
        HeightEditField = findViewById(R.id.et_height);
        WeightEditField = findViewById(R.id.et_weight);

        AbortButton = findViewById(R.id.bt_abort);
        AbortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SaveButton = findViewById(R.id.bt_save);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClicked();
            }
        });
    }

    private void saveButtonClicked(){
        String name = NameEditField.getText().toString();
        int age = Integer.parseInt(AgeEditField.getText().toString());
        int height = Integer.parseInt(HeightEditField.getText().toString());
        int weight = Integer.parseInt(WeightEditField.getText().toString());
        int bmiOver30 = bmiOver30(height,weight);
        int bmiUnder19 = bmiUnder19(height, weight);
        int hypertension = getRadioGroupResult(R.id.rg_hypertension);
        int smoking = getRadioGroupResult(R.id.rg_smoking);

        final UserEntry user = new UserEntry(name, Sex, age, bmiOver30, bmiUnder19, hypertension, smoking, height, weight);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(UserId == DEFAULT_USER_ID){
                    UserDb.databaseDao().insertUser(user);
                }else{
                    user.setId(UserId);
                    UserDb.databaseDao().updateUser(user);
                }
                finish();
            }
        });
    }

    private void setSpinnerSelection(String sex){
        switch (sex){
            case SEX_TYPE_M:
                SexSpinner.setSelection(0,true);
                break;
            case SEX_TYPE_W:
                SexSpinner.setSelection(1, true);
                break;
            case SEX_TYPE_D:
                SexSpinner.setSelection(2, true);
                break;
            default:
                SexSpinner.setSelection(0,true);
        }
    }

    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SexSpinner.setAdapter(adapter);
        SexSpinner.setOnItemSelectedListener(this);
    }

    public int getRadioGroupResult(int resourceId) {
        int resultRG;
        int checkedId = ((RadioGroup) findViewById(resourceId)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.rb_1:
                resultRG = PRESENT_INT;
                break;
            case R.id.rb_2:
                resultRG = ABSENT_INT;
                break;
            default:
                resultRG = PRESENT_INT;
        }
        return resultRG;
    }

    public void setRadioGroup(int resultRG, int resourceId) {
        switch (resultRG) {
            case PRESENT_INT:
                ((RadioGroup)findViewById(resourceId)).check(R.id.rb_1);
                break;
            case ABSENT_INT:
                ((RadioGroup) findViewById(resourceId)).check(R.id.rb_2);
                break;
            default:
                ((RadioGroup)findViewById(resourceId)).check(R.id.rb_1);
        }
    }

    private int bmiOver30(int height, int weight){
        double heightInMeter = height / 100;
        double bmi = weight /(heightInMeter * heightInMeter);

        if(bmi > 30){
            return PRESENT_INT;
        }else{
            return ABSENT_INT;
        }
    }

    private int bmiUnder19(int height, int weight){
        double heightInMeter = height / 100;
        double bmi = weight /(heightInMeter * heightInMeter);

        if(bmi < 19){
            return PRESENT_INT;
        }else{
            return ABSENT_INT;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Sex = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Sex = "male";
    }
}
