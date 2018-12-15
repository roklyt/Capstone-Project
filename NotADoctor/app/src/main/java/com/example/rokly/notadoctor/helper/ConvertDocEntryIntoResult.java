package com.example.rokly.notadoctor.helper;

import com.example.rokly.notadoctor.Database.DoctorEntry;
import com.example.rokly.notadoctor.Model.PlaceDetail.DetailResult;
import com.example.rokly.notadoctor.Model.Places.Geometry;
import com.example.rokly.notadoctor.Model.Places.Location;
import com.example.rokly.notadoctor.Model.Places.Result;

import java.util.ArrayList;
import java.util.List;

public class ConvertDocEntryIntoResult {

    public List<Result> convertDocEntryIntoResult(List<DoctorEntry> doctorEntryList){

        List<Result> result = new ArrayList<>();
        for(DoctorEntry doctorEntry:doctorEntryList){
            DetailResult detailResult = new DetailResult();
            detailResult.setFormattedPhoneNumber(doctorEntry.getDoctorPhoneNumber());

            Location location = new Location();
            location.setLng(doctorEntry.getLng());
            location.setLat(doctorEntry.getLat());

            Geometry geometry = new Geometry();
            geometry.setLocation(location);

            Result doctor = new Result();
            doctor.setDetailResult(detailResult);
            doctor.setGeometry(geometry);
            doctor.setFormattedAddress(doctorEntry.getDoctorAddress());
            doctor.setName(doctorEntry.getDoctorName());
            doctor.setPlaceId(doctorEntry.getPlaceId());
            result.add(doctor);
        }

        return result;
    }
}
