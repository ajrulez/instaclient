package com.alifesoftware.instaclient.interfaces;

import com.alifesoftware.instaclient.model.PopularPicturesModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This interface is for a parser that is used
 * to parse the data that we get from Instagram
 * corresponding to the request we make for
 * Popular Pictures
 *
 */
public interface IPopularImageParser {
    ArrayList<PopularPicturesModel> parse(JSONObject jsonObj);
}
