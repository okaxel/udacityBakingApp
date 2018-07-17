package hu.drorszagkriszaxel.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

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
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_details) Toolbar toolbar;
    @BindView(R.id.btn_step_prev) CardView prevButton;
    @BindView(R.id.btn_step_next) CardView nextButton;
    @BindView(R.id.step_buttons_nest) LinearLayout buttonsNest;

    private int recipeId;
    private Recipe recipe;
    private int stepId;

    /**
     * Usual activity creator.
     *
     * @param savedInstanceState State if saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
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

        }

        if (inIntent.hasExtra(getString(R.string.key_step_item_id))) {

            stepId = inIntent.getIntExtra(getString(R.string.key_step_item_id),-1);
            setButtons();

        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (savedInstanceState == null) {

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(getString(R.string.key_step_item),recipe.getSteps().get(stepId));
            arguments.putBoolean(getString(R.string.key_fragment_single_pane_mode),true);

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container,fragment)
                    .commit();

        }

        toolbar.setTitle(recipe.getSteps().get(stepId).getShortDescription());

    }

    /**
     * It's required to override this to maintain the Intent-data-cycle between activities.
     */
    @Override
    public void onBackPressed() {

        Intent outIntent = new Intent(getApplicationContext(), StepListActivity.class);

        outIntent.putExtra(getString(R.string.key_recipe_id),recipeId);
        outIntent.putExtra(getString(R.string.key_recipe_object),recipe);
        outIntent.putExtra(getString(R.string.key_step_item_id),stepId);

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
     * This is the onClickListener of the previous-item-button.
     *
     * @param view Unused, the clicked view
     */
    public void prevClicked(View view) {

        if (stepId>0) {

            stepId--;

            StepListActivity.startDetailsActivity(getApplicationContext(), recipe, recipeId, stepId);

        }

    }

    /**
     * This is the onClickListener of the next-item-button.
     *
     * @param view Unused, the clicked view
     */
    public void nextClicked(View view) {

        if (stepId<recipe.getSteps().size() - 1) {

            stepId++;

            StepListActivity.startDetailsActivity(getApplicationContext(), recipe, recipeId, stepId);

        }

    }

    /**
     * Changes color of buttons to give some active-inactive feeling. The real validation of
     * activeness happens in listeners.
     */
    void setButtons() {

        if (stepId < 1) {

            prevButton.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBakingInactiveButton));

        } else {

            prevButton.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBakingPrimary));

        }

        if (stepId > recipe.getSteps().size() - 2) {

            nextButton.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBakingInactiveButton));

        } else {

            nextButton.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBakingPrimary));

        }

    }

}
