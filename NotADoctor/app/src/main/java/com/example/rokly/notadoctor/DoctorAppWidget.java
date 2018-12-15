package com.example.rokly.notadoctor;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.rokly.notadoctor.Database.DiagnoseEntry;
import com.example.rokly.notadoctor.Model.Places.Places;

/**
 * Implementation of App Widget functionality.
 */
public class DoctorAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, DiagnoseEntry currentDiagnose,
                                int appWidgetId, boolean hasDiagnose) {

        RemoteViews views;

        if(currentDiagnose != null){
            views = getDoctorListRemoteView(context, currentDiagnose);
        }else{
            views = new RemoteViews(context.getPackageName(), R.layout.doctor_app_widget);
            views.setTextViewText(R.id.appwidget_text, "Depp");
        }



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getDoctorListRemoteView(Context context, DiagnoseEntry currentDiagnose) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.doctor_widget_list);
        // Set the ListWidgetService intent to act as the adapter for the ListView
        Intent intent = new Intent(context, ListWidgetService.class);
        int i = currentDiagnose.getId();
        intent.putExtra(ListWidgetService.EXTRA_DIAGNOSE, i);
        views.setRemoteAdapter(R.id.list_widget_view, intent);

        // Set the RecipeDetailActivity intent to launch when clicked
        Intent appIntent;
            appIntent = new Intent(context, FindADoctor.class);
            appIntent.putExtra(FindADoctor.EXTRA_IS_WIDGET, true);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            appIntent.putExtra(ListWidgetService.EXTRA_DIAGNOSE, i);


        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.list_widget_view, appPendingIntent);
        // Handle empty list
        views.setEmptyView(R.id.list_widget_view, R.id.empty_view);


        return views;
    }


    public static void updateDoctorWidgets(Context context, AppWidgetManager appWidgetManager,
                                           DiagnoseEntry currentDiagnose, int[] appWidgetIds, boolean hasDiagnose) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, currentDiagnose, appWidgetId, hasDiagnose);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, null, appWidgetId, true);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

