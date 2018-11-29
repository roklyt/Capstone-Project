package com.example.rokly.notadoctor.helper;

public class ChoiceId {
    private static final String CHOICEID_PRESENT = "present";
    private static final String CHOICEID_ABSENT = "absent";
    private static final String CHOICEID_UNKNOWN = "unknown";
    private static final int CHOICEID_INT_PRESENT = 1;
    private static final int CHOICEID_INT_ABSENT = 0;
    private static final int CHOICEID_INT_UNKNOWN = -1;

    public static int getChoiceIdInt(String choiceId){
        int choiceInt;

        switch(choiceId){
            case CHOICEID_PRESENT:
                choiceInt = CHOICEID_INT_PRESENT;
                break;
            case CHOICEID_ABSENT:
                choiceInt = CHOICEID_INT_PRESENT;
                break;
            case CHOICEID_UNKNOWN:
                choiceInt = CHOICEID_INT_UNKNOWN;
                break;
            default:
                choiceInt = CHOICEID_INT_UNKNOWN;
        }

        return choiceInt;
    }

    public static String getChoiceIdString(int choiceId){
        String choiceString;

        switch(choiceId){
            case CHOICEID_INT_PRESENT:
                choiceString = CHOICEID_PRESENT;
                break;
            case CHOICEID_INT_ABSENT:
                choiceString = CHOICEID_ABSENT;
                break;
            case CHOICEID_INT_UNKNOWN:
                choiceString = CHOICEID_UNKNOWN;
                break;
             default:
                 choiceString = CHOICEID_UNKNOWN;
        }

        return choiceString;
    }
}
