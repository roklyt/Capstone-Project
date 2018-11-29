package com.example.rokly.notadoctor.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "user")
public class UserEntry implements Parcelable {

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<UserEntry> CREATOR = new Parcelable.Creator<UserEntry>() {
        @Override
        public UserEntry createFromParcel(Parcel parcel) {
            return new UserEntry(parcel);
        }

        @Override
        public UserEntry[] newArray(int i) {
            return new UserEntry[i];
        }

    };

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String sex;
    private int age;
    private int bmiOver30;
    private int bmiUnder19;
    private int hypertension;
    private int smoking;
    private int height;
    private int weight;


    @Ignore
    public UserEntry(String name, String sex, int age, int bmiOver30, int bmiUnder19, int hypertension, int smoking, int height, int weight) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.bmiOver30 = bmiOver30;
        this.bmiUnder19 = bmiUnder19;
        this.hypertension = hypertension;
        this.smoking = smoking;
        this.height = height;
        this.weight = weight;
    }

    public UserEntry(int id, String name, String sex, int age, int bmiOver30, int bmiUnder19, int hypertension, int smoking, int height, int weight) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.bmiOver30 = bmiOver30;
        this.bmiUnder19 = bmiUnder19;
        this.hypertension = hypertension;
        this.smoking = smoking;
        this.height = height;
        this.weight = weight;
    }

    private UserEntry(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sex = in.readString();
        age = in.readInt();
        bmiOver30 = in.readInt();
        bmiUnder19 = in.readInt();
        hypertension = in.readInt();
        smoking = in.readInt();
        height = in.readInt();
        weight = in.readInt();
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
        parcel.writeInt(age);
        parcel.writeInt(bmiOver30);
        parcel.writeInt(bmiUnder19);
        parcel.writeInt(hypertension);
        parcel.writeInt(smoking);
        parcel.writeInt(height);
        parcel.writeInt(weight);

    }
}
