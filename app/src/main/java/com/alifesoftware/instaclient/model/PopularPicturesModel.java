package com.alifesoftware.instaclient.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class serves as a Data Model for Popular Pictures
 * JSON response that we get from Instagram.
 *
 * This class implements a Parcelable because we
 * want to save this data in a Bundle to avoid
 * reloading of data when configuration changes, and to
 * add any custom object to Bundle, it must either implement
 * a Parcelable or a Serializable
 *
 */
public class PopularPicturesModel implements Parcelable  {

    // Note: There is a lot of information on each picture
    // that we retrieve via media/popular. Considering the scope
    // of this assignment, I am only taking into account the
    // following:
    //
    // Picture ID
    // Picture Caption
    // Picture URL - LowRes
    // Picture URL - StandadRes/HighRes
    // User has Liked

    // ID of the picture
    private String pictureId;

    // Caption of the picture
    private String pictureCaption;

    // URL of the picture
    private String pictureUrl;

    // URL of the picture in high-res/standard-res
    private String pictureUrlHighRes;

    // Flag to indicate whether current user has liked the picture
    private boolean hasUserLiked;

    public PopularPicturesModel() {
        // Nothing to do
    }

    /**
     * Parcelable Implementation
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Writing to Parcel and Reading from Parcel
     * must be done in the same order to preserve
     * the integrity of the data
     *
     * @param out
     * @param flags
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(pictureId);
        out.writeString(pictureCaption);
        out.writeString(pictureUrl);

        if(hasUserLiked) {
            out.writeInt(1);
        }
        else {
            out.writeInt(0);
        }
    }

    /**
     * Parcelable Creator
     */
    public static final Parcelable.Creator<PopularPicturesModel> CREATOR
            = new Parcelable.Creator<PopularPicturesModel>() {
        public PopularPicturesModel createFromParcel(Parcel in) {
            return new PopularPicturesModel(in);
        }

        public PopularPicturesModel[] newArray(int size) {
            return new PopularPicturesModel[size];
        }
    };

    /**
     * Reading frm Parcel must be done in the same order
     * as Writing to the Parcel to preserve the integrity of
     * data
     *
     * @param in
     */
    private PopularPicturesModel(Parcel in) {
        pictureId = in.readString();
        pictureCaption = in.readString();
        pictureUrl = in.readString();

        int flag = in.readInt();
        if(flag == 1) {
            hasUserLiked = true;
        }
        else {
            hasUserLiked = false;
        }
    }

    /**
     * Getter and Setter Methods for the above fields
     */
    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureCaption() {
        return pictureCaption;
    }

    public void setPictureCaption(String pictureCaption) {
        this.pictureCaption = pictureCaption;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getHighResPictureUrl() {
        return pictureUrlHighRes;
    }

    public void setHighResPictureUrl(String pictureUrl) {
        pictureUrlHighRes = pictureUrl;
    }

    public boolean isHasUserLiked() {
        return hasUserLiked;
    }

    public void setHasUserLiked(boolean hasUserLiked) {
        this.hasUserLiked = hasUserLiked;
    }
}
