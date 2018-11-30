package com.example.rokly.notadoctor.Model.Diagnose.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.rokly.notadoctor.Model.Diagnose.Request.Extras;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Question implements Parcelable {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("extras")
    @Expose
    private Extras extras;
    public final static Parcelable.Creator<Question> CREATOR = new Creator<Question>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return (new Question[size]);
        }

    }
            ;

    protected Question(Parcel in) {
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.items = new ArrayList<>();
        in.readList(this.items, (Item.class.getClassLoader()));
        this.extras = ((Extras) in.readValue((Extras.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Question() {
    }

    /**
     *
     * @param text
     * @param items
     * @param type
     * @param extras
     */
    public Question(String type, String text, List<Item> items, Extras extras) {
        super();
        this.type = type;
        this.text = text;
        this.items = items;
        this.extras = extras;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Extras getExtras() {
        return extras;
    }

    public void setExtras(Extras extras) {
        this.extras = extras;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeValue(text);
        dest.writeList(items);
        dest.writeValue(extras);
    }

    public int describeContents() {
        return 0;
    }

}