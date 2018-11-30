package com.example.rokly.notadoctor.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "evidence")
public class EvidenceEntry implements Parcelable {

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<EvidenceEntry> CREATOR = new Parcelable.Creator<EvidenceEntry>() {
        @Override
        public EvidenceEntry createFromParcel(Parcel parcel) {
            return new EvidenceEntry(parcel);
        }

        @Override
        public EvidenceEntry[] newArray(int i) {
            return new EvidenceEntry[i];
        }

    };

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int diagnoseId;
    private String evidenceId;
    private int choiceId;


    @Ignore
    public EvidenceEntry(int diagnoseId, String evidenceId, int choiceId) {
        this.diagnoseId = diagnoseId;
        this.evidenceId = evidenceId;
        this.choiceId =choiceId;
    }

    public EvidenceEntry(int id, int diagnoseId, String evidenceId, int choiceId) {
        this.id = id;
        this.diagnoseId = diagnoseId;
        this.evidenceId = evidenceId;
        this.choiceId =choiceId;
    }

    private EvidenceEntry(Parcel in) {
        id = in.readInt();
        this.diagnoseId = diagnoseId;
        this.evidenceId = evidenceId;
        this.choiceId =choiceId;
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

    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(diagnoseId);
        parcel.writeString(evidenceId);
        parcel.writeInt(choiceId);

    }
}