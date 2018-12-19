package com.example.rokly.notadoctor;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.rokly.notadoctor.Database.DiagnoseEntry;

public class DoctorAppWidgetService extends IntentService {

    public static final String ACTION_UPDATE_DOCTOR_WIDGETS = "com.example.rokly.notadoctor.action.update_doctor_widgets";
    public static final String DIAGNOSE_EXTRA = "diagnoseExtra";

    public DoctorAppWidgetService() {
        super("DoctorAppWidgetService");
    }


    /**
     * Starts this service to perform UpdateBakeWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateDoctorWidget(Context context, DiagnoseEntry diagnoseEntry) {

        Intent intent = new Intent(context, DoctorAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_DOCTOR_WIDGETS);
        intent.putExtra(DIAGNOSE_EXTRA, diagnoseEntry);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_DOCTOR_WIDGETS.equals(action)) {
                DiagnoseEntry currentDiagnose = intent.getParcelableExtra(DIAGNOSE_EXTRA);
                handleActionUpdateDoctorWidgets(currentDiagnose);
            }
        }
    }

    /**
     * Handle action UpdateBakeWidgets in the provided background thread
     */
    private void handleActionUpdateDoctorWidgets(DiagnoseEntry currentDiagnose) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, DoctorAppWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_widget_view);

        //Now update all widgets
        DoctorAppWidget.updateDoctorWidgets(this, appWidgetManager, currentDiagnose , appWidgetIds);


    }
}
