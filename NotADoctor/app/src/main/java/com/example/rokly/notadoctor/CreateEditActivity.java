package com.example.rokly.notadoctor;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.ViewModel.CreateEditViewModel;
import com.example.rokly.notadoctor.ViewModel.CreateEditViewModelFactory;

import java.util.Locale;

import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_INT_ABSENT;
import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_INT_PRESENT;


public class CreateEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_USER_ID = "userExtraId";
    public static final String INSTANCE_USER_ID = "userInstanceId";
    private static final String SEX_TYPE_M = "male";
    private static final String SEX_TYPE_W = "female";
    private static final String SEX_TYPE_D = "diverse";
    private static final int DEFAULT_USER_ID = -1;
    private int UserId = DEFAULT_USER_ID;


    private EditText nameEditField;
    private Spinner sexSpinner;
    private String sex;

    private EditText ageEditField;
    private EditText heightEditField;
    private EditText weightEditField;

    private Button abortButton;
    private Button saveButton;

    private AppDatabase notADoctorDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit);

        findViews();

        notADoctorDB = AppDatabase.getInstance(getApplicationContext());

        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_USER_ID)){
            UserId = savedInstanceState.getInt(INSTANCE_USER_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_USER_ID)) {
            saveButton.setText(R.string.button_update);
            if (UserId == DEFAULT_USER_ID) {
                UserId = intent.getIntExtra(EXTRA_USER_ID, DEFAULT_USER_ID);

                CreateEditViewModelFactory factory = new CreateEditViewModelFactory(notADoctorDB, UserId);

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
        nameEditField.setText(user.getName());
        setSpinnerSelection(user.getSex());
        ageEditField.setText(String.format(Locale.getDefault(),"%d", user.getAge()));
        heightEditField.setText(String.format(Locale.getDefault(),"%d", user.getHeight()));
        weightEditField.setText(String.format(Locale.getDefault(),"%d", user.getWeight()));

        setRadioGroup(user.getHypertension(), R.id.rg_hypertension);
        setRadioGroup(user.getSmoking(), R.id.rg_smoking);
    }

    private void findViews(){
        nameEditField = findViewById(R.id.et_name);
        sexSpinner = findViewById(R.id.sp_sex);
        setSpinner();
        ageEditField = findViewById(R.id.et_age);
        heightEditField = findViewById(R.id.et_height);
        weightEditField = findViewById(R.id.et_weight);

        abortButton = findViewById(R.id.bt_abort);
        abortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveButton = findViewById(R.id.bt_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClicked();
            }
        });
    }

    private void saveButtonClicked(){
        String name = nameEditField.getText().toString();
        int age = getInt(ageEditField.getText().toString());
        int height = getInt(heightEditField.getText().toString());
        int weight = getInt(weightEditField.getText().toString());
        int bmiOver30 = bmiOver30(height,weight);
        int bmiUnder19 = bmiUnder19(height, weight);
        int hypertension = getRadioGroupResult(R.id.rg_hypertension);
        int smoking = getRadioGroupResult(R.id.rg_smoking);

        if(checkEntrys(name, age, height, weight)){
            final UserEntry user = new UserEntry(name, sex, age, bmiOver30, bmiUnder19, hypertension, smoking, height, weight);
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(UserId == DEFAULT_USER_ID){
                        notADoctorDB.databaseDao().insertUser(user);
                    }else{
                        user.setId(UserId);
                        notADoctorDB.databaseDao().updateUser(user);
                    }
                    finish();
                }
            });
        }else{
            Toast.makeText(this, R.string.error_some_entrys_missing, Toast.LENGTH_SHORT).show();
        }
    }

    private int getInt(String value){
        int i;

        if(value.equals("")){
            i = 0;
        }else{
            i = Integer.parseInt(value);
        }

        return i;
    }

    private boolean checkEntrys(String name, int age, int height, int weight){
        boolean checked = true;

        if(name.equals("")){
            checked = false;
            nameEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.error_background));
        }else{
            nameEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
        }

        if(age == 0){
            checked = false;
            ageEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.error_background));
        }else{
            ageEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
        }

        if(height == 0){
            checked = false;
            heightEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.error_background));
        }else{
            heightEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
        }

        if(weight == 0){
            checked = false;
            weightEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.error_background));
        }else{
            weightEditField.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
        }

        return checked;
    }

    private void setSpinnerSelection(String sex){
        switch (sex){
            case SEX_TYPE_M:
                sexSpinner.setSelection(0,true);
                break;
            case SEX_TYPE_W:
                sexSpinner.setSelection(1, true);
                break;
            case SEX_TYPE_D:
                sexSpinner.setSelection(2, true);
                break;
            default:
                sexSpinner.setSelection(0,true);
        }
    }

    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(adapter);
        sexSpinner.setOnItemSelectedListener(this);
    }

    public int getRadioGroupResult(int resourceId) {
        int resultRG;
        int checkedId = ((RadioGroup) findViewById(resourceId)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.rb_1:
                resultRG = CHOICEID_INT_PRESENT;
                break;
            case R.id.rb_2:
                resultRG = CHOICEID_INT_ABSENT;
                break;
            default:
                resultRG = CHOICEID_INT_PRESENT;
        }
        return resultRG;
    }

    public void setRadioGroup(int resultRG, int resourceId) {
        switch (resultRG) {
            case CHOICEID_INT_PRESENT:
                ((RadioGroup)findViewById(resourceId)).check(R.id.rb_1);
                break;
            case CHOICEID_INT_ABSENT:
                ((RadioGroup) findViewById(resourceId)).check(R.id.rb_2);
                break;
            default:
                ((RadioGroup)findViewById(resourceId)).check(R.id.rb_1);
        }
    }

    private int bmiOver30(int height, int weight){
        double heightInMeter = (double)height / 100;
        double bmi = weight /(heightInMeter * heightInMeter);

        if(bmi > 30){
            return CHOICEID_INT_PRESENT;
        }else{
            return CHOICEID_INT_ABSENT;
        }
    }

    private int bmiUnder19(int height, int weight){
        double heightInMeter = (double)height / 100;
        double bmi = weight /(heightInMeter * heightInMeter);

        if(bmi < 19){
            return CHOICEID_INT_PRESENT;
        }else{
            return CHOICEID_INT_ABSENT;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sex = adapterView.getItemAtPosition(i).toString();
        if(sex.equals("diverse")){
            sex = "male";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        sex = "male";
    }
}
