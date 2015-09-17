package com.alifesoftware.instaclient.parser;

import com.alifesoftware.instaclient.interfaces.IPopularImageParser;
import com.alifesoftware.instaclient.model.PopularPicturesModel;
import com.alifesoftware.instaclient.utils.Constants;
import com.alifesoftware.instaclient.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * Parser to parse response for Popular Pictures request
 * using standard JSON library. An alternative could
 * have been to use Google GSON library to parse
 * the data and create an object from it.
 *
 * I generally use JSON Editor
 * extension for Chrome to visualize a JSON response as it helps
 * to understand the structure better than looking at raw
 * JSON file.
 *
 */
public class InstagramPopularPictureParserJson implements IPopularImageParser {
    private final static String TAG = "InstagrameParser";

    /**
     * For now, I am using standard JSON libraries to
     * parse the data.
     *
     * Another option is to use Gson. If I have time towards the
     * end of this assignment, I will switch to Gson
     */

    /**
     * Method to parse the response. I generally use JSON Editor
     * extension for Chrome to visualize a JSON response as it helps
     * to understand the structure better than looking at raw
     * JSON file.
     *
     * @param jsonObj
     * @return
     */
    public ArrayList<PopularPicturesModel> parse(JSONObject jsonObj) {
        if (jsonObj == null ||
                jsonObj.length() <= 0) {
            return null;
        }

        ArrayList<PopularPicturesModel> popularPictures =
                new ArrayList<PopularPicturesModel>();

        // Get the JSONArray named "data" from the root object
        JSONArray dataArray = jsonObj.optJSONArray("data");

        if (dataArray != null &&
                dataArray.length() > 0) {
            // For each JSONObject in the dataArray, retrieve the
            // data as needed by PopularPicturesModel
            for (int count = 0; count < dataArray.length(); count++) {
                try {
                    JSONObject pictureObj = dataArray.optJSONObject(count);

                    if (pictureObj != null) {
                        PopularPicturesModel pictureModel = new PopularPicturesModel();

                        String imageUrlLowRes = "";
                        String imageUrlStandardRes = "";

                        // Get the Link to the Image
                        // Note: We also have type = video in JSON response
                        // and Videos also have images, so I am not sure
                        // if we want to ignore type=video or not
                        //
                        // Basically it is easy to ignore type=video - See below:
                        //

                        String type = pictureObj.optString("type");
                        boolean process = false;

                        if(type.equalsIgnoreCase("image")) {
                            process = true;
                        }
                        // If we want to NOT include the video type, and the
                        // current type is video, then do not process this
                        // entry
                        //
                        // Note: Current flag in Constants is set
                        // to include videos
                        else if(Constants.includeVideosInPopularPictures &&
                                type.equalsIgnoreCase("video")) {
                            process = true;
                        }

                        // Ignore all other types that we don't know off

                        if(process) {
                            JSONObject imagesObj = pictureObj.optJSONObject("images");
                            if (imagesObj != null) {
                                // Get the low-resolution image
                                JSONObject lowResolutionObj = imagesObj.optJSONObject("low_resolution");
                                JSONObject standardResolutionObj = imagesObj.optJSONObject("standard_resolution");

                                if (lowResolutionObj != null) {
                                    imageUrlLowRes = lowResolutionObj.optString("url");
                                }

                                if (standardResolutionObj != null) {
                                    imageUrlStandardRes = standardResolutionObj.optString("url");
                                }
                            }

                            // Get the ID
                            String id = pictureObj.optString("id", "-1");

                            // Get the user_has_liked flag
                            boolean userHasLiked = pictureObj.optBoolean("user_has_liked", false);

                            String captionText = "";

                            // Get the caption text from caption object
                            JSONObject captionObj = pictureObj.optJSONObject("caption");
                            if (captionObj != null) {
                                captionText = captionObj.optString("text", "");
                            }

                            // Add to the collection after checking for some
                            // basic required values
                            if (!Utils.isNullOrEmpty(imageUrlLowRes) &&
                                    !Utils.isNullOrEmpty(id)) {

                                pictureModel.setPictureUrl(imageUrlLowRes);
                                pictureModel.setHighResPictureUrl(imageUrlStandardRes);
                                pictureModel.setPictureId(id);
                                pictureModel.setPictureCaption(captionText);
                                pictureModel.setHasUserLiked(userHasLiked);

                                popularPictures.add(pictureModel);
                            }
                        }
                    }
                } catch (Exception e) {
                    android.util.Log.e(TAG, "Exception when parsing the data " + e);
                    continue;
                }
            }
        }

        return popularPictures;
    }
}
