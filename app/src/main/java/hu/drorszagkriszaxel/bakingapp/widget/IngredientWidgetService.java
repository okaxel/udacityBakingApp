package hu.drorszagkriszaxel.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import hu.drorszagkriszaxel.bakingapp.R;
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
 * This class maintains widget service and with its subclass the creation of ListView items too.
 */
public class IngredientWidgetService extends RemoteViewsService {

    Recipe recipe;

    /**
     * Simple getter method to receive ViewFactory.
     *
     * @param intent Holds data content for ListView
     * @return       A new ViewFactory
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Bundle bundle = new Bundle();

        bundle = intent.getBundleExtra(getString(R.string.key_ingredients_bundle));
        recipe = bundle.getParcelable(getString(R.string.key_bundle_ingredients));

        return new IngredientFactory(this.getApplicationContext());

    }

    public class IngredientFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;

        IngredientFactory(Context context) {

            this.context = context;

        }

        /**
         * Simple creator method. At this time not used.
         */
        @Override
        public void onCreate() {

        }

        /**
         * Simple creator method. At this time not used.
         */
        @Override
        public void onDataSetChanged() {

        }

        /**
         * Simple creator method. At this time not used.
         */
        @Override
        public void onDestroy() {

        }

        /**
         * Returns the size of the data set.
         *
         * @return The size
         */
        @Override
        public int getCount() {

            return recipe == null ? 0 : recipe.getIngredients().size();

        }

        /**
         * Constructs list item at given position.
         *
         * @param i Item's position
         * @return  The constructed item
         */
        @Override
        public RemoteViews getViewAt(int i) {

            RemoteViews ingredient = new RemoteViews(context.getPackageName(),R.layout.ingredients_widget_item);
            String quantity;

            if (recipe.getIngredients().get(i).getQuantity() == Math.round(recipe.getIngredients().get(i).getQuantity())) {

                quantity = String.valueOf(Math.round(recipe.getIngredients().get(i).getQuantity()));

            } else {

                quantity = String.valueOf(recipe.getIngredients().get(i).getQuantity());

            }

            ingredient.setTextViewText(R.id.ingredient_list_ingredient,recipe.getIngredients().get(i).getIngredient());
            ingredient.setTextViewText(R.id.ingredient_list_quantity,quantity);
            ingredient.setTextViewText(R.id.ingredient_list_measure,recipe.getIngredients().get(i).getMeasure());

            return ingredient;

        }

        /**
         * Simple getter method. At this time not used.
         */
        @Override
        public RemoteViews getLoadingView() {

            return null;

        }

        /**
         * Simple getter method. Because we have only 1 kind of list, it returns 1 always.
         */
        @Override
        public int getViewTypeCount() {

            return 1;

        }

        /**
         * Simple getter method. It gets its parameter without change.
         */
        @Override
        public long getItemId(int i) {

            return i;

        }

        /**
         * Returns whether the ids of the elements are stable or not.
         *
         * @return It returns true always.
         */
        @Override
        public boolean hasStableIds() {

            return true;

        }

    }
}
