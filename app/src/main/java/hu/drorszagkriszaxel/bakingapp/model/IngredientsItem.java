package hu.drorszagkriszaxel.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

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
 * This class handles the single ingredients.
 */
public class IngredientsItem implements Parcelable{

    private float quantity;
    private String measure;
    private String ingredient;

    /**
     * The most simple constructor.
     */
    public IngredientsItem () {
    }

    /**
     * Constructor with all fields.
     *
     * @param quantity   Tge quantity of the ingredient
     * @param measure    The measure of the quantity
     * @param ingredient Tha name of the ingredient
     */
    public IngredientsItem(float quantity, String measure, String ingredient) {

        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;

    }

    /**
     * Constructor from parcelable.
     *
     * @param in The parcelable
     */
    private IngredientsItem(Parcel in) {

        this.quantity = in.readFloat();
        this.measure = in.readString();
        this.ingredient = in.readString();

    }

    /**
     * Standard getter method.
     *
     * @return The quantity
     */
    public float getQuantity() {

        return quantity;

    }

    /**
     * Standard getter method.
     *
     * @return The measure
     */
    public String getMeasure() {

        return measure;

    }

    /**
     * Standard getter method.
     *
     * @return The ingredient's name
     */
    public String getIngredient() {

        return ingredient;

    }

    /**
     * Standard setter method
     *
     * @param quantity The quantity
     */
    public void setQuantity(float quantity) {

        this.quantity = quantity;

    }

    /**
     * Standard setter method
     *
     * @param measure The measure of the quantity
     */
    public void setMeasure(String measure) {

        this.measure = measure;

    }

    /**
     * Standard setter method
     *
     * @param ingredient The ingredinet's name
     */
    public void setIngredient(String ingredient) {

        this.ingredient = ingredient;

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

        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);

    }

    /**
     * Usual parcelable creator.
     */
    public static final Parcelable.Creator<IngredientsItem> CREATOR =
            new Parcelable.Creator<IngredientsItem>() {

                @Override
                public IngredientsItem createFromParcel(Parcel parcel) {

                    return new IngredientsItem(parcel);

                }

                @Override
                public IngredientsItem[] newArray(int i) {

                    return new IngredientsItem[i];

                }
            };

}
