package hu.drorszagkriszaxel.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import hu.drorszagkriszaxel.bakingapp.MainActivity;
import hu.drorszagkriszaxel.bakingapp.R;
import hu.drorszagkriszaxel.bakingapp.StepListActivity;
import hu.drorszagkriszaxel.bakingapp.model.Recipe;

/**
 *
 * Baking App component.
 *
 * Copyright © 2018 by Axel Ország-Krisz Dr.
 *
 * @author Axel Ország-Krisz Dr.
 * @version 1.0 - first try
 *
 * ---
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---
 *
 * This class provides home screen widget.
 */
public class IngredientsWidget extends AppWidgetProvider {

    public static final String ACTION_NEW_INGREDIENTS = "android.appwidget.action.APPWIDGET_UPDATE";

    private int recipeId = -1;
    private Recipe recipe;

    /**
     * This method updates the widgets.
     *
     * @param context          Context is always welcome
     * @param appWidgetManager WidgetManager is essential
     * @param appWidgetId      The id of the concrete widget instance
     * @param recipe           Intent-data-cycle element
     * @param recipeId         Intent-data-cycle element
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe, int recipeId) {

        CharSequence widgetText = context.getString(R.string.widget_title);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        if (recipe != null) {

            views.setTextViewText(R.id.widget_title, recipe.getName());

            Intent adapterIntent = new Intent(context,IngredientWidgetService.class);
            Bundle bundle = new Bundle();

            bundle.putParcelable(context.getString(R.string.key_bundle_ingredients),recipe);

            adapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            adapterIntent.putExtra(context.getString(R.string.key_ingredients_bundle),bundle);
            adapterIntent.setData(Uri.parse(adapterIntent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(R.id.widget_ingredients_list,adapterIntent);

        } else {

            views.setTextViewText(R.id.widget_title, widgetText);

        }

        Intent outIntent;


        if (recipeId == -1) {

            outIntent = new Intent(context, MainActivity.class);

        } else {

            // if (recipe != null) views.setTextViewText(R.id.widget_ingredients_list,formIngredients(recipe));

            outIntent = new Intent(context, StepListActivity.class);

            outIntent.putExtra(context.getString(R.string.key_recipe_id),recipeId);
            outIntent.putExtra(context.getString(R.string.key_recipe_object),recipe);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,outIntent,0);
        views.setOnClickPendingIntent(R.id.ll_widget,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_ingredients_list);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Cares about all widgets.
     *
     * @param context          Some Context to work with
     * @param appWidgetManager WidgetManager is essential
     * @param appWidgetIds     The id of the concrete widget instance
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId, recipe, recipeId);

        }

    }

    /**
     * This method caches broadcasts.
     *
     * @param context Context to work with
     * @param intent  Intent to get informed
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(ACTION_NEW_INGREDIENTS)) {

            if (intent.hasExtra(context.getString(R.string.key_recipe_id))) {

                recipeId = intent.getIntExtra(context.getString(R.string.key_recipe_id),-1);
            }

            if (intent.hasExtra(context.getString(R.string.key_recipe_object))) {

                recipe = intent.getParcelableExtra(context.getString(R.string.key_recipe_object));

            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            onUpdate(context,appWidgetManager,appWidgetManager.getAppWidgetIds(new ComponentName(context,IngredientsWidget.class)));

        }

        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Transforms recipe ingredients list to human readable form for the widget.
     *
     * @param recipe The recipe to handle
     * @return       String, the readable form
     */
    static String formIngredients(Recipe recipe) {

        StringBuilder builder = new StringBuilder();

        for (int i=0;i<recipe.getIngredients().size();i++) {

            builder.append(String.valueOf(recipe.getIngredients().get(i).getQuantity()));
            builder.append(" ");
            builder.append(recipe.getIngredients().get(i).getMeasure());
            builder.append(" of ");
            builder.append(recipe.getIngredients().get(i).getIngredient());
            builder.append("\n");

        }

        return builder.toString();

    }

}

