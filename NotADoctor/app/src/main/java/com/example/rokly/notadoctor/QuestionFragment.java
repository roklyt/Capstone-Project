package com.example.rokly.notadoctor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Diagnose.Request.Evidence;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Question;

import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_UNKNOWN;

public class QuestionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String QUESTION_KIND_SINGLE = "single";
    private static final String QUESTION_KIND_GROUP_SINGLE = "group_single";
    private static final String QUESTION_KIND_GROUP_MULTIPLE = "group_multiple";
    private Question question;
    private TextView questionTextView;
    private RadioButton radioButtonOne;
    private RadioButton radioButtonTwo;
    private RadioButton radioButtonThree;
    private Button proceedButton;
    private RadioGroup radioGroup;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }


    public static QuestionFragment newInstance(Question question) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, question);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        if(question.getType().equals(QUESTION_KIND_SINGLE)){
            rootView = inflater.inflate(R.layout.fragment_question, container, false);
            findSingleView(rootView);

            questionTextView.setText(question.getText());
            radioButtonOne.setText(question.getItems().get(0).getChoices().get(0).getLabel());
            radioButtonTwo.setText(question.getItems().get(0).getChoices().get(1).getLabel());
            radioButtonThree.setText(question.getItems().get(0).getChoices().get(2).getLabel());

            proceedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Evidence evidence = new Evidence(question.getItems().get(0).getId(), getRadioGroupResult());
                    onButtonPressed(evidence);
                }
            });
        }else if(question.getType().equals(QUESTION_KIND_GROUP_SINGLE)){
            //TODO implement group single question. Make a new layout and gather this questions
            rootView = inflater.inflate(R.layout.fragment_question, container, false);
        }else if(question.getType().equals(QUESTION_KIND_GROUP_MULTIPLE)){
            //TODO implement group multiple question. Make a new layout and gather this questions
            rootView = inflater.inflate(R.layout.fragment_question, container, false);
        }else{
            //TODO implement an error layout
            rootView = inflater.inflate(R.layout.fragment_question, container, false);
        }

        return rootView;
    }

    private void findSingleView(View rootView){
        questionTextView = rootView.findViewById(R.id.tv_question);
        radioButtonOne = rootView.findViewById(R.id.rb_1);
        radioButtonTwo = rootView.findViewById(R.id.rb_2);
        radioButtonThree = rootView.findViewById(R.id.rb_3);
        proceedButton = rootView.findViewById(R.id.bt_procced);
        radioGroup = rootView.findViewById(R.id.rg_question);
    }

    public String getRadioGroupResult() {
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

    public void onButtonPressed(Evidence evidence) {
        if (mListener != null) {
            mListener.onFragmentInteraction(evidence);
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
        void onFragmentInteraction(Evidence evidence);
    }
}
