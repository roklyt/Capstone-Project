package com.example.rokly.notadoctor.Model.Parse.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Parse {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("include_tokens")
    @Expose
    private Boolean includeTokens;
    @SerializedName("correct_spelling")
    @Expose
    private Boolean correctSpelling;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIncludeTokens() {
        return includeTokens;
    }

    public void setIncludeTokens(Boolean includeTokens) {
        this.includeTokens = includeTokens;
    }

    public Boolean getCorrectSpelling() {
        return correctSpelling;
    }

    public void setCorrectSpelling(Boolean correctSpelling) {
        this.correctSpelling = correctSpelling;
    }


}