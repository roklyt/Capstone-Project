package com.example.rokly.notadoctor.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class UserEntry {

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

    public UserEntry(int id, String name, String sex, int age, int bmiOver30, int bmiUnder19, int hypertension, int smoking) {
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
}
