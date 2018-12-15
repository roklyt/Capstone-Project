package com.example.rokly.notadoctor.Model.Parse.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mention  implements Parcelable{

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<Mention> CREATOR = new Parcelable.Creator<Mention>() {
        @Override
        public Mention createFromParcel(Parcel parcel) {
            return new Mention(parcel);
        }

        @Override
        public Mention[] newArray(int i) {
            return new Mention[i];
        }

    };

    @SerializedName("mentions")
    @Expose
    private List<Mentions> mentions = null;
    @SerializedName("tokens")
    @Expose
    private List<String> tokens = null;
    @SerializedName("obvious")
    @Expose
    private Boolean obvious;

    public Mention(List<Mentions> mentions, List<String> tokens, Boolean obvious) {
        this.mentions = mentions;
        this.tokens = tokens;
        this.obvious = obvious;
    }

    private Mention(Parcel in) {
        in.readList(this.mentions, (java.lang.Object.class.getClassLoader()));
        in.readList(this.tokens, (java.lang.Object.class.getClassLoader()));
        obvious = in.readByte() != 0;
    }



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

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mentions);
        dest.writeList(tokens);
        dest.writeByte((byte) (obvious ? 1 : 0));
    }

}
