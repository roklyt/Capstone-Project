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

@Entity(tableName = "diagnose",
        foreignKeys = @ForeignKey(entity = UserEntry.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE),
        indices = {@Index("userId")})
public class DiagnoseEntry implements Parcelable {

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<DiagnoseEntry> CREATOR = new Parcelable.Creator<DiagnoseEntry>() {
        @Override
        public DiagnoseEntry createFromParcel(Parcel parcel) {
            return new DiagnoseEntry(parcel);
        }

        @Override
        public DiagnoseEntry[] newArray(int i) {
            return new DiagnoseEntry[i];
        }

    };



    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="userId")
    private int userId;
    private long createdAt;


    @Ignore
    public DiagnoseEntry(int userId, long createdAt) {
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public DiagnoseEntry(int id, int userId, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    private DiagnoseEntry(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        createdAt = in.readLong();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeLong(createdAt);

    }
}