package com.example.rokly.notadoctor.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "condition",
        foreignKeys = @ForeignKey(entity = DiagnoseEntry.class,
                parentColumns = "id",
                childColumns = "diagnoseId",
                onDelete = CASCADE),
        indices = {@Index("diagnoseId")})
public class ConditionEntry implements Parcelable {

    public final static Parcelable.Creator<ConditionEntry> CREATOR = new Parcelable.Creator<ConditionEntry>() {
        @Override
        public ConditionEntry createFromParcel(Parcel parcel) {
            return new ConditionEntry(parcel);
        }

        @Override
        public ConditionEntry[] newArray(int i) {
            return new ConditionEntry[i];
        }

    };

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "diagnoseId")
    private int diagnoseId;
    private String conditionId;
    private String name;
    private double probability;


    @Ignore
    public ConditionEntry(int diagnoseId, String conditionId, String name, double probability) {
        this.diagnoseId = diagnoseId;
        this.conditionId = conditionId;
        this.name = name;
        this.probability = probability;
    }

    public ConditionEntry(int id, int diagnoseId, String conditionId, String name, double probability) {
        this.id = id;
        this.diagnoseId = diagnoseId;
        this.conditionId = conditionId;
        this.name = name;
        this.probability = probability;
    }

    private ConditionEntry(Parcel in) {
        id = in.readInt();
        diagnoseId = in.readInt();
        name = in.readString();
        probability = in.readDouble();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiagnoseId() {
        return diagnoseId;
    }

    public void setDiagnoseId(int diagnoseId) {
        this.diagnoseId = diagnoseId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(diagnoseId);
        parcel.writeString(conditionId);
        parcel.writeString(name);
        parcel.writeDouble(probability);

    }
}