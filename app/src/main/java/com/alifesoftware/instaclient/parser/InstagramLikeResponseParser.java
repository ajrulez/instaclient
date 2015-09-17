package com.alifesoftware.instaclient.parser;

import com.alifesoftware.instaclient.interfaces.ILikeResponseParser;
import com.alifesoftware.instaclient.model.InstagramErrorModel;

import org.json.JSONObject;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * Parser to parse response for Like request corresponding
 * to a Like on a Popular Picture
 */
public class InstagramLikeResponseParser implements ILikeResponseParser {
    @Override
    public Boolean parse(JSONObject jsonObj) {
        if(jsonObj == null) {
            return Boolean.FALSE;
        }

        try {
            JSONObject metaObject = jsonObj.optJSONObject("meta");
            if (metaObject != null) {
                int code = metaObject.optInt("code", -1);
                if (code == 200) {
                    return Boolean.TRUE;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return Boolean.FALSE;
    }

    @Override
    public InstagramErrorModel parseError(JSONObject jsonError) {
        if(jsonError == null) {
            return null;
        }

        try {
            JSONObject metaObject = jsonError.optJSONObject("meta");
            if(metaObject != null) {
                InstagramErrorModel error = new InstagramErrorModel();
                error.setErrorType(metaObject.optString("error_type", ""));
                error.setErrorMessage(metaObject.optString("error_message", ""));
                error.setErrorCode(metaObject.optInt("code", -1));

                return error;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
