package com.example.rokly.notadoctor;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.DoctorEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.Model.PlaceDetail.DetailResult;
import com.example.rokly.notadoctor.Model.Places.Geometry;
import com.example.rokly.notadoctor.Model.Places.Location;
import com.example.rokly.notadoctor.Model.Places.Result;

import java.util.ArrayList;
import java.util.List;


public class ListWidgetService extends RemoteViewsService {
    public final static String EXTRA_DIAGNOSE = "extraDiagnose";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int diagnoseId = intent.getIntExtra(EXTRA_DIAGNOSE, 0);

        return new ListRemoteViewsFactory(this.getApplicationContext(), diagnoseId);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Result> doctores = new ArrayList<>();
    private int currentDiagnose;

    public ListRemoteViewsFactory(Context context, int currentDiagnose) {
        this.context = context;
        this.currentDiagnose = currentDiagnose;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (currentDiagnose != 0){
            final AppDatabase NotADoctor = AppDatabase.getInstance(context);

            List<DoctorEntry> doctorEntrys = NotADoctor.databaseDao().loadDoctorsByDiagnoseId(currentDiagnose);

                    for(DoctorEntry doctorEntry:doctorEntrys){
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
                        doctores.add(doctor);
                    }

        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (doctores == null) return 0;
        return doctores.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (doctores == null || doctores.size() == 0) return null;

        Result doctor = doctores.get(i);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

            views.setTextViewText(R.id.tv_widget_name, doctor.getName());
            views.setTextViewText(R.id.tv_widget_address, doctor.getFormattedAddress());
            views.setTextViewText(R.id.tv_widget_phone, doctor.getDetailResult().getFormattedPhoneNumber());


        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.list_row_main, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
