package com.and.tim.bakingapp.ui.widget_ingred_list;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.ui.main.MainActivity;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        IngredientsListService.startActionUpdateWidget(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientsListService.startActionUpdateWidget(context);
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String name) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_step_list);
        Intent intent = new Intent(context, IngredientsListWidgetService.class);
        views.setRemoteAdapter(R.id.lvSteps, intent);
        views.setEmptyView(R.id.lvSteps, R.id.layoutEmpty);

        setEmptyClick(context, views);

        if (name != null) {
            String caption = name + context.getString(R.string.widget_ingredients);
            views.setTextViewText(R.id.tvName, caption);
            views.setViewVisibility(R.id.tvName, View.VISIBLE);
        } else
            views.setViewVisibility(R.id.tvName, View.GONE);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvSteps);
    }

    private static void setEmptyClick(Context context, RemoteViews views) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.layoutEmpty, pendingIntent);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager,
                                     int[] appWidgetIds, String name) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId, name);
        }
    }

}
