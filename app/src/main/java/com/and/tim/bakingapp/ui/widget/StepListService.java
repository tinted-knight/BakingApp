package com.and.tim.bakingapp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeDao;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;

import java.util.List;

public class StepListService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET = "com.and.tim.bakingapp.update_widget";

    public StepListService() {
        super("StepListService");
//        android.os.Debug.waitForDebugger();
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, StepListService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
//        RecipeListRepo repo = RecipeListRepo.get(this.getApplication());
//        RecipeEntity recipe = repo.getPinnedRecipe();
        String name = "no data";
//        if (recipe != null) {
//            name = recipe.name;
//        }
        Log.d("TAGG", "handleActionUpdateWidget");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, StepListWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lvSteps);

        StepListWidgetProvider.updateWidgets(this, appWidgetManager, appWidgetIds, name);
    }

}
