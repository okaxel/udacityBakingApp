package hu.drorszagkriszaxel.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.bakingapp.model.Recipe;
import hu.drorszagkriszaxel.bakingapp.widget.IngredientsWidget;

import java.util.ArrayList;

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
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 */
public class StepListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_steps) Toolbar toolbar;
    @BindView(R.id.tv_show_ingredients) TextView ingredientsList;
    @BindView(R.id.step_list) RecyclerView recyclerView;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private int recipeId;
    private Recipe recipe;


    /**
     * Usual activity creator.
     *
     * @param savedInstanceState State if saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        Intent inIntent = getIntent();

        if (inIntent.hasExtra(getString(R.string.key_recipe_id))) {

            recipeId = inIntent.getIntExtra(getString(R.string.key_recipe_id),-1);
        }

        if (inIntent.hasExtra(getString(R.string.key_recipe_object))) {

            recipe = inIntent.getParcelableExtra(getString(R.string.key_recipe_object));
            Log.d("StepListLoading",recipe.toString());

        }


        ingredientsList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent outIntent = new Intent(IngredientsWidget.ACTION_NEW_INGREDIENTS);

                outIntent.putExtra(getString(R.string.key_recipe_id),recipeId);
                outIntent.putExtra(getString(R.string.key_recipe_object),recipe);

                sendBroadcast(outIntent);

            }

        });

        if (findViewById(R.id.step_detail_container) != null) {

            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        }

        toolbar.setTitle(recipe.getName());
        setupRecyclerView();
    }

    /**
     * It's required to override this to maintain the Intent-data-cycle between activities.
     */
    @Override
    public void onBackPressed() {

        Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);

        outIntent.putExtra(getString(R.string.key_recipe_id),recipeId);
        outIntent.putExtra(getString(R.string.key_recipe_object),recipe);

        startActivity(outIntent);

        super.onBackPressed();
    }

    /**
     * It's required to override this to maintain the Intent-data-cycle between activities.
     *
     * @param item Unused, the selected item
     * @return     In case of success true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {

                onBackPressed();
                return true;

            }

            default: return super.onOptionsItemSelected(item);

        }

    }


    /**
     * This method gives the sparkle to the RecyclerView of steps. It uses activity level variables.
     */
    private void setupRecyclerView() {

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, recipe, recipeId, mTwoPane));
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * Starts StepDetailsActivity with the selected step and sends some additional data to maintain
     * the Intent-data-cycle between activities.
     *
     * @param context   Context, to ignite our intentions
     * @param recipe    Intent-data-cycle element
     * @param recipeId  Intent-data-cycle element
     * @param stepId    The id of the step to show
     */
    static void startDetailsActivity(Context context, Recipe recipe, int recipeId, int stepId) {

        Intent outIntent = new Intent(context,StepDetailActivity.class);

        outIntent.putExtra(context.getString(R.string.key_recipe_id),recipeId);
        outIntent.putExtra(context.getString(R.string.key_recipe_object),recipe);
        outIntent.putExtra(context.getString(R.string.key_step_item_id),stepId);

        context.startActivity(outIntent);

    }

    /**
     * ItemClickListener for the RecyclerView.
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private StepListActivity mParentActivity;
        private int recipeId;
        private Recipe recipe;
        private boolean mTwoPane;

        /**
         * Basic constructor.
         *
         * @param parentActivity Actually Context
         * @param recipe         Intent-data-cycle element
         * @param recipeId       Intent-data-cycle element
         * @param mTwoPane       Determines whether two-pane-mode or not
         */
        SimpleItemRecyclerViewAdapter(StepListActivity parentActivity, Recipe recipe, int recipeId, boolean mTwoPane) {

            this.mParentActivity = parentActivity;
            this.recipeId = recipeId;
            this.recipe = recipe;
            this.mTwoPane = mTwoPane;

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

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);

        }

        /**
         * Simple ViewHolder binder.
         *
         * @param holder   Holder to bind
         * @param position Position in data-set
         */
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.stepShortDescription.setText(recipe.getSteps().get(position).getShortDescription());
            holder.stepShortDescription.setOnClickListener(new View.OnClickListener() {

                /**
                 * Simple onClickListener.
                 *
                 * @param view Unused, the clicked View
                 */
                @Override
                public void onClick(View view) {

                    if (mTwoPane) {

                        Bundle arguments = new Bundle();
                        arguments.putParcelable(mParentActivity.getString(R.string.key_step_item),recipe.getSteps().get(position));
                        arguments.putBoolean(mParentActivity.getString(R.string.key_fragment_single_pane_mode),false);

                        StepDetailFragment fragment = new StepDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_detail_container,fragment)
                                .commit();

                    } else startDetailsActivity(mParentActivity.getBaseContext(), recipe, recipeId, position);

                }

            });

        }

        /**
         * Counts the items in the data-set.
         *
         * @return Count of elements
         */
        @Override
        public int getItemCount() {

            return recipe == null ? 0 : recipe.getSteps().size();

        }

        /**
         * Simple ViewHolder class.
         */
        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.step_short_description) TextView stepShortDescription;

            /**
             * Simple constructor.
             *
             * @param view The View to bind.
             */
            ViewHolder(View view) {

                super(view);
                ButterKnife.bind(this,view);

            }

        }

    }

}
