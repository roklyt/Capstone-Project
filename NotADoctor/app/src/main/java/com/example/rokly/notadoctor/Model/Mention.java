package com.example.rokly.notadoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mention {

    @SerializedName("mentions")
    @Expose
    private List<Mentions> mentions = null;
    @SerializedName("tokens")
    @Expose
    private List<String> tokens = null;
    @SerializedName("obvious")
    @Expose
    private Boolean obvious;

    public List<Mentions> getMentions() {
        return mentions;
    }

    public void setMentions(List<Mentions> mentions) {
        this.mentions = mentions;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public Boolean getObvious() {
        return obvious;
    }

    public void setObvious(Boolean obvious) {
        this.obvious = obvious;
    }

}
