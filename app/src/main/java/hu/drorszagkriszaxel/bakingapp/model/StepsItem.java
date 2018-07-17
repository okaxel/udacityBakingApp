package hu.drorszagkriszaxel.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

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
 * This class handles the single steps.
 */
public class StepsItem implements Parcelable{

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    /**
     * The most simple constructor.
     */
    public StepsItem() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id               The id of the step
     * @param shortDescription The short description, eventually title of the step
     * @param description      The description, text of the step
     * @param videoURL         Link to video or noting
     * @param thumbnailURL     Link to thumbnail, in fact often to video
     */
    public StepsItem(int id, String shortDescription, String description, @Nullable String videoURL, @Nullable String thumbnailURL) {

        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;

    }

    /**
     * Constructor from parcelable.
     *
     * @param in The parcelable
     */
    private StepsItem(Parcel in) {

        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();

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
     * @return The short description
     */
    public String getShortDescription() {

        return shortDescription;

    }

    /**
     * Standard getter method.
     *
     * @return The description
     */
    public String getDescription() {

        return description;

    }

    /**
     * Standard getter method.
     *
     * @return The link to the video
     */
    public String getVideoURL() {

        return videoURL;

    }

    /**
     * Standard getter method.
     *
     * @return The link to the thumbnail
     */
    public String getThumbnailURL() {

        return thumbnailURL;

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
     * @param shortDescription The short description, in fact the step's name
     */
    public void setShortDescription(String shortDescription) {

        this.shortDescription = shortDescription;

    }

    /**
     * Standard setter method
     *
     * @param description The description of the step
     */
    public void setDescription(String description) {

        this.description = description;

    }

    /**
     * Standard setter method
     *
     * @param videoURL Link to the video resource
     */
    public void setVideoURL(@Nullable String videoURL) {

        this.videoURL = videoURL;

    }

    /**
     * Standard setter method
     *
     * @param thumbnailURL Link to the image file
     */
    public void setThumbnailURL(@Nullable String thumbnailURL) {

        this.thumbnailURL = thumbnailURL;

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
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);

    }

    /**
     * Usual parcelable creator.
     */
    public static final Parcelable.Creator<StepsItem> CREATOR =
            new Parcelable.Creator<StepsItem>() {

                @Override
                public StepsItem createFromParcel(Parcel parcel) {

                    return new StepsItem(parcel);

                }

                @Override
                public StepsItem[] newArray(int i) {

                    return new StepsItem[i];

                }

            };

}
