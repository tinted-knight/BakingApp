package com.and.tim.bakingapp.ui.widget_ingred_list;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.IngredientEntity;
import com.and.tim.bakingapp.repo.dao.IngredientsForRecipe;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;

public class IngredientsListWidgetService extends RemoteViewsService {

    @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        android.os.Debug.waitForDebugger();
        return new IngredientsListRemoteViewFactory(this.getApplicationContext());
    }

}

class IngredientsListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private IngredientsForRecipe recipe;

    public IngredientsListRemoteViewFactory(Context appContext) {
        context = appContext;
    }

    @Override public void onCreate() {
    }

    @Override public void onDataSetChanged() {
        RecipeListRepo repo = RecipeListRepo.get(context);
        RecipeEntity recipeEntity = repo.getPinnedRecipe();
        recipe = (recipeEntity == null) ? null : repo.getIngredientsForWidget(recipeEntity.id);
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
