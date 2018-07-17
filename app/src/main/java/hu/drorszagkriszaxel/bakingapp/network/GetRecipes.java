package hu.drorszagkriszaxel.bakingapp.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
 * This class maintains AsyncTask to get recipes data.
 */
public class GetRecipes extends AsyncTask<Void,Void,Recipe[]> {

    private RecipeListener recipeListener;
    private String downloadFrom;

    /**
     * Simple constructor.
     *
     * @param recipeListener A listener to handle data if downloaded
     * @param downloadFrom   Link of the data set
     */
    public GetRecipes(RecipeListener recipeListener, String downloadFrom) {

        this.recipeListener = recipeListener;
        this.downloadFrom = downloadFrom;

    }

    /**
     * This method gets recipes in the background.
     *
     * @param voids Don't used however voids are amazingly complicated
     * @return      Array of recipes
     */
    @Override
    protected Recipe[] doInBackground(Void... voids) {

        URL url = null;
        String jsonString = null;

        try {

            url = new URL(downloadFrom);

        } catch (MalformedURLException e) {

            Log.e("",e.getLocalizedMessage());

        }

        if (url != null) {

            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {

                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                if (inputStream != null) {

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String oneLine;

                    while ((oneLine = reader.readLine()) != null) {

                        stringBuilder.append(oneLine)
                                .append("\n");

                    }

                    if (stringBuilder.length() > 0) jsonString = stringBuilder.toString();

                }

            } catch (IOException e) {

                Log.e("",e.getLocalizedMessage());

            } finally {

                if (connection != null) connection.disconnect();

                if (reader != null) {

                    try {

                        reader.close();

                    } catch (IOException e) {

                        Log.e("",e.getLocalizedMessage());

                    }


                }

            }

        }

        if (jsonString != null) {

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(jsonString,Recipe[].class);

        }

        return null;

    }

    /**
     * This method helps to transfer data into the main thread.
     *
     * @param recipes Array of recipes to transfer
     */
    @Override
    protected void onPostExecute(Recipe[] recipes) {

        super.onPostExecute(recipes);
        recipeListener.getRecipesHeard(recipes);

    }

}
