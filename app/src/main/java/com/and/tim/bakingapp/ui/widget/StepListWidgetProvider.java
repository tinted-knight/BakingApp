package com.and.tim.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.and.tim.bakingapp.R;

public class StepListWidgetProvider extends AppWidgetProvider {

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if ("acc".equalsIgnoreCase(intent.getAction())) {
            int pos = intent.getIntExtra("pos", -1);
            Toast.makeText(context, "acc: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        StepListService.startActionUpdateWidget(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("TAGG", "onUpdate");
        StepListService.startActionUpdateWidget(context);
//        updateWidgets(context, appWidgetManager, appWidgetIds, "new string");
//        for (int appWidgetId : appWidgetIds) {
//            upd(context, appWidgetManager, appWidgetId);
//        }
    }

    private static void upd(Context context, AppWidgetManager appWidgetManager,int appWidgetId) {
        Log.d("TAGG", "upd");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_step_list);
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.lvSteps, intent);
        views.setEmptyView(R.id.lvSteps, R.id.layoutEmpty);

        setListClick(context, views);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvSteps);
    }

    private static void setListClick(Context context, RemoteViews views) {
//        Intent clickIntent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intent = new Intent(context, StepListWidgetProvider.class);
        intent.setAction("acc");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setPendingIntentTemplate(R.id.lvSteps, pendingIntent);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager,
                                     int[] appWidgetIds, String name) {
        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId, name);
            upd(context, appWidgetManager, appWidgetId);
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, String name) {

        Log.d("TAGG", "updateAppWidget");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_step_list);
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.lvSteps, intent);
        views.setEmptyView(R.id.lvSteps, R.id.layoutEmpty);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
