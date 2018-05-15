package com.and.tim.bakingapp.ui.widget_step_list;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;

public class StepListService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET = "com.and.tim.bakingapp.update_steps_widget";

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
        RecipeListRepo repo = RecipeListRepo.get(this.getApplication());
        RecipeEntity recipe = repo.getPinnedRecipe();

        String name = (recipe == null) ? null : recipe.name;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, StepsWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lvSteps);

        StepsWidgetProvider.updateWidgets(this, appWidgetManager, appWidgetIds, name);
    }

}
