package hu.drorszagkriszaxel.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

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
 * This class handles the single recipes.
 */
public class Recipe implements Parcelable {

    private int id;
    private String name;
    private ArrayList<IngredientsItem> ingredients;
    private ArrayList<StepsItem> steps;
    private int servings;
    private String image;

    /**
     * The most simple constructor.
     */
    public Recipe() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id          Id of the recipe
     * @param name        Name of the recipe
     * @param ingredients List of ingredients
     * @param steps       List of steps
     * @param servings    Count of servings
     * @param image       Link to concerning image
     */
    public Recipe(int id, String name, ArrayList<IngredientsItem> ingredients, ArrayList<StepsItem> steps, int servings, @Nullable String image) {

        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;

    }

    /**
     * Constructor from parcelable.
     *
     * @param in The parcelable
     */
    private Recipe(Parcel in) {

        this.id = in.readInt();
        this.name = in.readString();

        this.ingredients = new ArrayList<IngredientsItem>();
        in.readList(this.ingredients,IngredientsItem.class.getClassLoader());

        this.steps = new ArrayList<StepsItem>();
        in.readList(this.steps,StepsItem.class.getClassLoader());

        this.servings = in.readInt();
        this.image = in.readString();

    }

    /**
     * Standard getter method.
     *
     * @return The id
     */
    public int getId() {

        return id;

    }

    /**
     * Standard getter method.
     *
     * @return The name
     */
    public String getName() {

        return name;

    }

    /**
     * Standard getter method.
     *
     * @return List of ingredients
     */
    public ArrayList<IngredientsItem> getIngredients() {

        return ingredients;

    }

    /**
     * Standard getter method.
     *
     * @return The list of steps
     */
    public ArrayList<StepsItem> getSteps() {

        return steps;

    }

    /**
     * Standard getter method.
     *
     * @return The count of servings
     */
    public int getServings() {

        return servings;

    }

    /**
     * Standard getter method.
     *
     * @return The link to the image
     */
    public String getImage() {

        return image;

    }

    /**
     * Standard setter method
     *
     * @param id The id
     */
    public void setId(int id) {

        this.id = id;

    }

    /**
     * Standard setter method
     *
     * @param name The name
     */
    public void setName(String name) {

        this.name = name;

    }

    /**
     * Standard setter method
     *
     * @param ingredients The list of ingredients
     */
    public void setIngredients(ArrayList<IngredientsItem> ingredients) {

        this.ingredients = ingredients;

    }

    /**
     * Standard setter method
     *
     * @param steps The list of steps
     */
    public void setSteps(ArrayList<StepsItem> steps) {

        this.steps = steps;

    }

    /**
     * Standard setter method
     *
     * @param servings The count of servings
     */
    public void setServings(int servings) {

        this.servings = servings;

    }

    /**
     * Standard setter method
     *
     * @param image The link to the image
     */
    public void setImage(@Nullable String image) {

        this.image = image;

    }

    /**
     * In fact an unused but required method.
     *
     * @return It could be static final 0 at the moment
     */
    @Override
    public int describeContents() {

        return 0;

    }

    /**
     * Writes everything to parcelable.
     *
     * @param parcel The parcelable
     * @param i      Don't used nowadays
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);

    }

    /**
     * Usual parcelable creator.
     */
    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>() {

                @Override
                public Recipe createFromParcel(Parcel parcel) {

                    return new Recipe(parcel);

                }

                @Override
                public Recipe[] newArray(int i) {

                    return new Recipe[i];

                }
            };
}
