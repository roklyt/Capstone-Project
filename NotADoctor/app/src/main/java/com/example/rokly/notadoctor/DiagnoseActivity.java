package com.example.rokly.notadoctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DiagnoseActivity extends AppCompatActivity {
    public static final String EXTRA_EVIDENCES = "extrEvidences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);
    }
}
