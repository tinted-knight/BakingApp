package com.and.tim.bakingapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.IngredientEntity;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;

public class IngredientsListWidgetService extends RemoteViewsService {

    @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        android.os.Debug.waitForDebugger();
        return new IngredientsListRemoteViewFactory(this.getApplicationContext());
    }

}

class IngredientsListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private StepListForRecipe recipe;

    public IngredientsListRemoteViewFactory(Context appContext) {
        context = appContext;
    }

    @Override public void onCreate() {
    }

    @Override public void onDataSetChanged() {
        RecipeListRepo repo = RecipeListRepo.get(context);

        RecipeEntity recipeEntity = repo.getPinnedRecipe();
        if (recipeEntity != null)
            recipe = repo.getStepListForWidget(recipeEntity.id);
    }

    @Override public void onDestroy() {

    }

    @Override public int getCount() {
        if (recipe == null) return 0;
        return recipe.ingredients.size();
    }

    @Override public RemoteViews getViewAt(int position) {
        if (recipe == null || recipe.ingredients.size() == 0) return null;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item_step);
        IngredientEntity i = recipe.ingredients.get(position);
        String ingredient = i.ingredient;
        String quantity = i.quantity.toString() + " " + i.measure;

        views.setTextViewText(R.id.tvIngredient, ingredient);
        views.setTextViewText(R.id.tvQuantity, quantity);

//        Intent clickIntent = new Intent();
//        clickIntent.putExtra(context.getString(R.string.stepKey), position);
//        clickIntent.putExtra(context.getString(R.string.recipeKey), recipe._id);
//        views.setOnClickFillInIntent(R.id.tvIngredient, clickIntent);

        return views;
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
