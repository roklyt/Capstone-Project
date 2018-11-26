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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBmiOver30() {
        return bmiOver30;
    }

    public void setBmiOver30(int bmiOver30) {
        this.bmiOver30 = bmiOver30;
    }

    public int getBmiUnder19() {
        return bmiUnder19;
    }

    public void setBmiUnder19(int bmiUnder19) {
        this.bmiUnder19 = bmiUnder19;
    }

    public int getHypertension() {
        return hypertension;
    }

    public void setHypertension(int hypertension) {
        this.hypertension = hypertension;
    }

    public int getSmoking() {
        return smoking;
    }

    public void setSmoking(int smoking) {
        this.smoking = smoking;
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
