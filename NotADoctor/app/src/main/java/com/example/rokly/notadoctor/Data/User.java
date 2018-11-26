package com.example.rokly.notadoctor.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }

    };

    private int id;
    private String name;
    private String sex;
    private int age;
    private int bmiOver30;
    private int bmiUnder19;
    private int hypertension;
    private int smoking;

    public User(int id, String name, String sex, int age, int bmiOver30, int bmiUnder19, int hypertension, int smoking) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.bmiOver30 = bmiOver30;
        this.bmiUnder19 = bmiUnder19;
        this.hypertension = hypertension;
        this.smoking = smoking;
    }

    private User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sex = in.readString();
        age = in.readInt();
        bmiOver30 = in.readInt();
        bmiUnder19 = in.readInt();
        hypertension = in.readInt();
        smoking = in.readInt();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public int getBmiOver30() {
        return bmiOver30;
    }

    public int getBmiUnder19() {
        return bmiUnder19;
    }

    public int getHypertension() {
        return hypertension;
    }

    public int getSmoking() {
        return smoking;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Sex='" + sex + '\'' +
                ", Age='" + age + '\'' +
                ", BmiOver30='" + bmiOver30 + '\'' +
                ", BmiUnder19='" + bmiUnder19 + '\'' +
                ", Hypertension='" + hypertension + '\'' +
                ", Smoking='" + smoking +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(sex);
        parcel.writeInt(bmiOver30);
        parcel.writeInt(bmiUnder19);
        parcel.writeInt(hypertension);
        parcel.writeInt(smoking);
    }
}
