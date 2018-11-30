package com.example.rokly.notadoctor.Model.Diagnose.Response;

import android.os.Parcel;
import android.os.Parcelable;

public class Extras implements Parcelable {

    public final static Parcelable.Creator<Extras> CREATOR = new Creator<Extras>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Extras createFromParcel(Parcel in) {
            return new Extras(in);
        }

        public Extras[] newArray(int size) {
            return (new Extras[size]);
        }

    }
            ;

    protected Extras(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public int describeContents() {
        return 0;
    }
}