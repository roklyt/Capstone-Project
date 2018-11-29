package com.example.rokly.notadoctor;

import android.content.Intent;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.UserEntry;

public class SymptomActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "currentUser";
    public static final String EXTRA_USER_SYMPTOME_TEXT = "symptomeText";

    private TextView AskForSymptomeTextView;
    private EditText EnterSymptomeEditText;
    private Button SubmitSymptomeButton;
    private UserEntry currentUser;

    AppDatabase UserDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        findViews();


        final Resources res = getResources();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_USER)) {
            currentUser = intent.getParcelableExtra(EXTRA_USER);
            AskForSymptomeTextView.setText(res.getString(R.string.welcome_to_the_symptomes, currentUser.getName()));

            SubmitSymptomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkEntry()){
                        Intent submitSymptomes = new Intent(SymptomActivity.this, MentionActivity.class);
                        submitSymptomes.putExtra(EXTRA_USER, currentUser);
                        submitSymptomes.putExtra(EXTRA_USER_SYMPTOME_TEXT, EnterSymptomeEditText.getText().toString());
                        startActivity(submitSymptomes);
                    }else{
                        Toast.makeText(SymptomActivity.this, getResources().getString(R.string.error_some_entrys_missing), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }else{
            Toast.makeText(getApplicationContext(), R.string.error_no_user, Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkEntry(){
        boolean checked = true;

        if(EnterSymptomeEditText.getText().toString().equals("")){
            checked = false;
            EnterSymptomeEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.error_background));
        }else{
            EnterSymptomeEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
        }

        return checked;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void findViews(){
        AskForSymptomeTextView = findViewById(R.id.tv_ask_for_symptome);
        EnterSymptomeEditText = findViewById(R.id.et_enter_symptome);
        SubmitSymptomeButton = findViewById(R.id.bt_submit);
    }
}
