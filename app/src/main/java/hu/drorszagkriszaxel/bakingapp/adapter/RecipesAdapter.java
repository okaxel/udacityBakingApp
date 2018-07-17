package hu.drorszagkriszaxel.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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
 * This adapter handles recipe list.
 *
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private Recipe[] recipes;
    private LayoutInflater layoutInflater;

    /**
     * Simple constructor.
     *
     * @param context Context to work with
     * @param recipes Array of recipes to work with
     */
    public RecipesAdapter(Context context, Recipe[] recipes) {

        this.layoutInflater = LayoutInflater.from(context);
        this.recipes = recipes;


    }

    /**
     * Simple ViewHolder creator.
     *
     * @param parent   Parent ViewGroup
     * @param viewType Type of View
     * @return         The created ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recipe_item,parent,false);
        return new ViewHolder(view);

    }

    /**
     * Simple ViewHolder binder.
     *
     * @param holder   Holder to bind
     * @param position Position in data-set
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.recipeName.setText(recipes[position].getName());

        String details = recipes[position].getServings() + " servings from " +
                String.valueOf(recipes[position].getIngredients().size()) + " ingredients in " +
                String.valueOf(recipes[position].getSteps().size()) + " steps";

        holder.recipeDetails.setText(details);

        if (recipes[position].getImage() != null && !recipes[position].getImage().equals("")) {

            Picasso.get()
                    .load(recipes[position].getImage())
                    .into(holder.recipeImage);

        }

        holder.itemView.setOnClickListener(new RecipeClickListener(recipes[position], recipes[position].getId()));

    }

    /**
     * Counts the items in the data-set.
     *
     * @return Count of elements
     */
    @Override
    public int getItemCount() {

        if (recipes !=null) {

            return  recipes.length;

        } else {

            return 0;

        }

    }

    /**
     * Simple ViewHolder class.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.recipe_details) TextView recipeDetails;
        @BindView(R.id.recipe_image) ImageView recipeImage;

        /**
         * Simple constructor.
         *
         * @param itemView The View to bind.
         */
        ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    /**
     * Simple onClickListener class to handle recipe selection.
     */
    class RecipeClickListener implements View.OnClickListener {

        private int recipeId;
        private Recipe recipe;

        /**
         * Simple constructor.
         *
         * @param recipe   Array of recipes to work with and to send
         * @param recipeId The id of the clicked recipe
         */
        RecipeClickListener(Recipe recipe, int recipeId) {

            this.recipeId = recipeId;
            this.recipe = recipe;

        }

        /**
         * The listener itself.
         *
         * @param view Source of Context this time
         */
        @Override
        public void onClick(View view) {

            Context context = view.getContext();
            Intent intent = new Intent(context, StepListActivity.class);

            intent.putExtra(context.getString(R.string.key_recipe_id),recipeId);
            intent.putExtra(context.getString(R.string.key_recipe_object),recipe);

            context.startActivity(intent);

        }

    }

}
