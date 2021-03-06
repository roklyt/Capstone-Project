package com.example.rokly.notadoctor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Diagnose.Request.Evidence;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Question;
import com.example.rokly.notadoctor.helper.ButtonAnimator;

import java.util.ArrayList;
import java.util.List;

import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_ABSENT;
import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_PRESENT;
import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_UNKNOWN;

public class QuestionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String QUESTION_KIND_SINGLE = "single";
    private static final String QUESTION_KIND_GROUP_SINGLE = "group_single";
    private static final String QUESTION_KIND_GROUP_MULTIPLE = "group_multiple";
    private Question question;
    private TextView questionTextView;
    private ImageButton proceedButton;
    private RadioGroup radioGroup;
    private ImageButton passButton;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public void setQuestion(Question question){
        this.question = question;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;

        if (savedInstanceState != null) {
            question = savedInstanceState.getParcelable(ARG_PARAM1);
        }

        assert question != null;
        switch (question.getType()) {
                case QUESTION_KIND_SINGLE:
                    rootView = inflater.inflate(R.layout.fragment_question, container, false);
                    findSingleView(rootView);

                    proceedButton.setOnClickListener(view -> {
                        List<Evidence> evidences = new ArrayList<>();
                        Evidence evidence = new Evidence(question.getItems().get(0).getId(), getRadioGroupResult());
                        evidences.add(evidence);
                        onButtonPressed(evidences);
                    });
                    break;
                case QUESTION_KIND_GROUP_SINGLE:
                    rootView = inflater.inflate(R.layout.fragment_question_group_single, container, false);
                    findGroupSingleView(rootView);

                    proceedButton.setOnClickListener(view -> onButtonPressed(getRadioGroupSingleResult()));

                    passButton.setOnClickListener(view -> onButtonPressed(getRadioGroupUnknown()));
                    break;
                case QUESTION_KIND_GROUP_MULTIPLE:
                    rootView = inflater.inflate(R.layout.fragment_question_group_multiple, container, false);
                    findGroupMulitpleView(rootView);

                    proceedButton.setOnClickListener(view -> onButtonPressed(getRadioGroupMultipleResult(rootView)));

                    passButton.setOnClickListener(view -> onButtonPressed(getRadioGroupUnknown()));
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_question, container, false);
                    break;
            }

        ButtonAnimator.imageButtonAnimator(proceedButton);


        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_PARAM1, question);
    }

    private void findSingleView(View rootView){
        questionTextView = rootView.findViewById(R.id.tv_question);
        RadioButton radioButtonOne = rootView.findViewById(R.id.rb_1);
        RadioButton radioButtonTwo = rootView.findViewById(R.id.rb_2);
        RadioButton radioButtonThree = rootView.findViewById(R.id.rb_3);
        proceedButton = rootView.findViewById(R.id.bt_procced);
        radioGroup = rootView.findViewById(R.id.rg_question);

        questionTextView.setText(question.getText());
        radioButtonOne.setText(question.getItems().get(0).getChoices().get(0).getLabel());
        radioButtonTwo.setText(question.getItems().get(0).getChoices().get(1).getLabel());
        radioButtonThree.setText(question.getItems().get(0).getChoices().get(2).getLabel());
    }

    private void findGroupSingleView(View rootView){
        proceedButton = rootView.findViewById(R.id.bt_procced);
        radioGroup = rootView.findViewById(R.id.rg_question);
        questionTextView = rootView.findViewById(R.id.tv_question);
        passButton = rootView.findViewById(R.id.bt_pass);

        questionTextView.setText(question.getText());

        for (int i = 0; i < question.getItems().size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i + 1000);
            radioButton.setText(question.getItems().get(i).getName());
            radioGroup.addView(radioButton);
        }
        ButtonAnimator.imageButtonAnimator(passButton);
    }

    private void findGroupMulitpleView(View rootView){
        proceedButton = rootView.findViewById(R.id.bt_procced);
        radioGroup = rootView.findViewById(R.id.rg_question);
        questionTextView = rootView.findViewById(R.id.tv_question);
        passButton = rootView.findViewById(R.id.bt_pass);
        LinearLayout radioButtonLinearLayout = rootView.findViewById(R.id.ll_question);
        questionTextView.setText(question.getText());

        for (int i = 0; i < question.getItems().size(); i++) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(i + 1000);
            checkBox.setText(question.getItems().get(i).getName());
            radioButtonLinearLayout.addView(checkBox);
        }
        ButtonAnimator.imageButtonAnimator(passButton);
    }


    private List<Evidence> getRadioGroupMultipleResult(View rootView) {
        List<Evidence> evidences = new ArrayList<>();


        for (int i = 0; i <  question.getItems().size(); i++) {
            Evidence evidence = new Evidence(null,null);
            evidence.setId(question.getItems().get(i).getId());

            CheckBox checkBox = rootView.findViewById(i + 1000);

            if(checkBox.isChecked()){
                evidence.setChoiceId(CHOICEID_PRESENT);
            }else{
                evidence.setChoiceId(CHOICEID_ABSENT);
            }


            evidences.add(evidence);
        }

        return evidences;
    }

    private List<Evidence> getRadioGroupSingleResult() {
        List<Evidence> evidences = new ArrayList<>();

        int checkedId = radioGroup.getCheckedRadioButtonId();

        for (int i = 0; i <  question.getItems().size(); i++) {
            Evidence evidence = new Evidence(null,null);
            evidence.setId(question.getItems().get(i).getId());

            if(checkedId == i + 1000){
                evidence.setChoiceId(CHOICEID_PRESENT);
            }else{
                evidence.setChoiceId(CHOICEID_ABSENT);
            }

            evidences.add(evidence);
        }

        return evidences;
    }

    private List<Evidence> getRadioGroupUnknown(){
        List<Evidence> evidences = new ArrayList<>();

        for (int i = 0; i < question.getItems().size(); i++) {
            Evidence evidence = new Evidence(null,null);
            evidence.setId(question.getItems().get(i).getId());
            evidence.setChoiceId(CHOICEID_UNKNOWN);
            evidences.add(evidence);
        }

        return evidences;
    }

    private String getRadioGroupResult() {
        String resultRG;
        int checkedId = radioGroup.getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.rb_1:
                resultRG = question.getItems().get(0).getChoices().get(0).getId();
                break;
            case R.id.rb_2:
                resultRG = question.getItems().get(0).getChoices().get(1).getId();
                break;
            case R.id.rb_3:
                resultRG = question.getItems().get(0).getChoices().get(2).getId();
                break;

            default:
                resultRG =  CHOICEID_UNKNOWN;
        }
        return resultRG;
    }

    public void onButtonPressed(List<Evidence> evidences) {
        if (mListener != null) {
            mListener.onFragmentInteraction(evidences);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(List<Evidence> evidences);
    }
}
