package com.and.tim.bakingapp.ui.widget_step_list;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.StepEntity;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;

public class StepListWidgetService extends RemoteViewsService {

    @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StepListRemoteViewFactory(getApplicationContext());
    }

}

class StepListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private StepListForRecipe recipe;

    public StepListRemoteViewFactory(Context appContext) {
        context = appContext;
    }

    @Override public void onCreate() {
    }

    @Override public void onDataSetChanged() {
        RecipeListRepo repo = RecipeListRepo.get(context);
        RecipeEntity recipeEntity = repo.getPinnedRecipe();
        recipe = (recipeEntity == null) ? null : repo.getStepListForWidget(recipeEntity.id);
    }

    @Override public int getCount() {
        if (recipe == null) return 0;
        return recipe.steps.size();
    }

    @Override public RemoteViews getViewAt(int position) {
        if (recipe == null || recipe.steps.size() == 0) return null;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item_step);
        StepEntity step = recipe.steps.get(position);
        String item = String.valueOf(position + 1) + ". " + step.getShortDescription();
        //Populate views
        views.setTextViewText(R.id.tvIngredient, item);
        views.setViewVisibility(R.id.tvQuantity, View.GONE);
        //Click action
        Intent clickIntent = new Intent();
        clickIntent.putExtra(context.getString(R.string.stepKey), position);
        clickIntent.putExtra(context.getString(R.string.recipeKey), recipe._id);
        views.setOnClickFillInIntent(R.id.tvIngredient, clickIntent);

        return views;
    }

    @Override public void onDestroy() {

    }

    @Override public RemoteViews getLoadingView() {
        return null;
    }

    @Override public int getViewTypeCount() {
        return 1;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public boolean hasStableIds() {
        return true;
    }

}
