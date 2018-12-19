package com.example.rokly.notadoctor.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.rokly.notadoctor.Model.Places.Location;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "doctor",
        foreignKeys = @ForeignKey(entity = DiagnoseEntry.class,
                parentColumns = "id",
                childColumns = "diagnoseId",
                onDelete = CASCADE),
        indices = {@Index("diagnoseId")})
public class DoctorEntry implements Parcelable {

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<DoctorEntry> CREATOR = new Parcelable.Creator<DoctorEntry>() {
        @Override
        public DoctorEntry createFromParcel(Parcel parcel) {
            return new DoctorEntry(parcel);
        }

        @Override
        public DoctorEntry[] newArray(int i) {
            return new DoctorEntry[i];
        }

    };

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "diagnoseId")
    private int diagnoseId;
    private String doctorName;
    private String doctorAddress;
    private Double lat;
    private Double lng;
    private String doctorPhoneNumber;
    private String placeId;


    @Ignore
    public DoctorEntry(int diagnoseId, String doctorName, String doctorAddress,Double lat, Double lng, String doctorPhoneNumber, String placeId) {
        this.diagnoseId = diagnoseId;
        this.doctorName = doctorName;
        this.doctorAddress = doctorAddress;
        this.lat = lat;
        this.lng = lng;
        this.doctorPhoneNumber = doctorPhoneNumber;
        this.placeId = placeId;
    }

    public DoctorEntry(int id, int diagnoseId, String doctorName, String doctorAddress, Double lat, Double lng, String doctorPhoneNumber, String placeId) {
        this.id = id;
        this.diagnoseId = diagnoseId;
        this.doctorName = doctorName;
        this.doctorAddress = doctorAddress;
        this.lat = lat;
        this.lng = lng;
        this.doctorPhoneNumber = doctorPhoneNumber;
        this.placeId = placeId;
    }

    private DoctorEntry(Parcel in) {
        id = in.readInt();
        diagnoseId = in.readInt();
        doctorName = in.readString();
        doctorAddress = in.readString();
        this.lat = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lng = ((Double) in.readValue((Double.class.getClassLoader())));
        doctorPhoneNumber = in.readString();
        placeId = in.readString();
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDoctorPhoneNumber() {
        return doctorPhoneNumber;
    }

    public void setDoctorPhoneNumber(String doctorPhoneNumber) {
        this.doctorPhoneNumber = doctorPhoneNumber;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(diagnoseId);
        parcel.writeString(doctorName);
        parcel.writeString(doctorAddress);
        parcel.writeValue(lat);
        parcel.writeValue(lng);
        parcel.writeString(doctorPhoneNumber);
        parcel.writeString(placeId);
    }
}