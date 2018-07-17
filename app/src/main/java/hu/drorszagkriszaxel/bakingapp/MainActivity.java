package hu.drorszagkriszaxel.bakingapp;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.bakingapp.adapter.RecipesAdapter;
import hu.drorszagkriszaxel.bakingapp.model.Recipe;
import hu.drorszagkriszaxel.bakingapp.network.ConnectionCheck;
import hu.drorszagkriszaxel.bakingapp.network.GetRecipes;
import hu.drorszagkriszaxel.bakingapp.network.RecipeListener;

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
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main) Toolbar mainToolbar;
    @BindView(R.id.recipe_container) RecyclerView recipesContainer;

    private Recipe[] recipes = null;

    public final CountingIdlingResource countingIdlingResource = new CountingIdlingResource(MainActivity.class.getSimpleName());

    /**
     * Usual activity creator.
     *
     * @param savedInstanceState State if saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    /**
     * Usual metod to restore instance state.
     *
     * @param savedInstanceState Lifecycle instances
     * @param persistentState    Persistent instances
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {

        super.onRestoreInstanceState(savedInstanceState, persistentState);

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(getString(R.string.bundle_key_recipes))) {

                Parcelable[] parcelables = savedInstanceState.getParcelableArray(getString(R.string.bundle_key_recipes));

                if (parcelables != null) {

                    recipes = new Recipe[parcelables.length];

                    for (int i=0;i<parcelables.length;i++) {

                        recipes[i] = (Recipe) parcelables[i];

                    }

                }

            }

        }

    }

    /**
     * Usual method to handle if activity is visible again.
     */
    @Override
    protected void onResume() {

        super.onResume();

        if (recipes != null) {

            startRecipesRecyclerView();

        } else {

            downloadRecipes();

        }

    }

    /**
     * Usual instance states saver method.
     *
     * At the moment this method uses only lifecycle instance states.
     *
     * @param outState           Lifecycle state
     * @param outPersistentState Persistent state
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        outState.putParcelableArray(getString(R.string.bundle_key_recipes),recipes);
        super.onSaveInstanceState(outState, outPersistentState);

    }

    /**
     * This method manages AsyncTask call to download recipes. It uses activity level variables to
     * handle result.
     */
    void downloadRecipes() {

        ConnectionCheck connectionCheck = new ConnectionCheck(getApplicationContext());
        if (connectionCheck.isConnected()) {

            countingIdlingResource.increment();
            RecipeListener recipeListener = new RecipeListener() {

                @Override
                public void getRecipesHeard(Recipe[] downloadedRecipes) {

                     recipes = downloadedRecipes;
                     startRecipesRecyclerView();
                     countingIdlingResource.decrement();

                }

            };

            GetRecipes getRecipes = new GetRecipes(recipeListener,getString(R.string.recipe_location));
            getRecipes.execute();

        } else {

            Toast.makeText(this, getString(R.string.toast_network_needed), Toast.LENGTH_LONG).show();

        }

    }

    /**
     * This method gives the sparkle to the RecyclerView of recipes. It uses activity level variables.
     */
    void startRecipesRecyclerView() {

        Configuration configuration = getResources().getConfiguration();

        recipesContainer.setAdapter(new RecipesAdapter(this,recipes));
        recipesContainer.getAdapter().notifyDataSetChanged();

        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && configuration.screenWidthDp >= 600) {

            recipesContainer.setLayoutManager(new GridLayoutManager(this,3));

        } else {

            recipesContainer.setLayoutManager(new LinearLayoutManager(this));

        }

    }

}
