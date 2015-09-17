package com.alifesoftware.instaclient.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.alifesoftware.instaclient.businesslogic.SessionManager;
import com.alifesoftware.instaclient.interfaces.ILikeResultListener;
import com.alifesoftware.instaclient.model.InstagramErrorModel;
import com.alifesoftware.instaclient.parser.InstagramLikeResponseParser;
import com.alifesoftware.instaclient.utils.Constants;
import com.alifesoftware.instaclient.utils.Utils;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * Note: "Like" a picture feature is not working as the request fails
 * with an error. It is likely because even though we are requesting scope=like
 * Instagram is not granting that privilege to our AccessToken because they mention
 * that they need to review the app for any of the "write" scopes that we request
 *
 * Link: https://instagram.com/developer/authentication/
 * In the above link, see Scope (Permissions) section
 *
 * I just read more details on Instagram Developers portal, specifically the
 * page where I set up my Instagram Client, and it clearly states that any
 * new apps using Instagram API after April 2015 require an approval from
 * Instagram for likes, comments, posts etc. I'll be sending a screenshot
 * along with this assignment
 */


public class InstagramPictureLikeTask extends AsyncTask<String, Void, Boolean> {
    // ILikeResultListener
    private ILikeResultListener likeListener;

    // Context
    private Context appContext;

    // Button
    private Button button;

    // Error
    private InstagramErrorModel errorModel = null;

    public InstagramPictureLikeTask(Context ctx, Button btn, ILikeResultListener listener) {
        appContext = ctx;
        likeListener = listener;
        button = btn;
    }

    /**
     * Background operation to post a Like for a particular
     * picture
     *
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(String... params) {
        if(params.length == 0) {
            return Boolean.FALSE;
        }

        // Get the AccessToken from SessionManager/SharedPreferences
        SessionManager sessionManager = new SessionManager(appContext);
        String accessToken = sessionManager.getAccessToken();

        // If the AccessToken is null or empty, then bail out.
        //
        // Note: It could be possible that even though we have
        // AccessToken, it cold be expired
        if(Utils.isNullOrEmpty(accessToken)) {
            return Boolean.FALSE;
        }

        HttpURLConnection urlConnection = null;

        try {
            // Construct the Picture Like URL
            String likeUrl = Constants.PICTURE_LIKE_URL.replace("{media-id}", params[0]);
            URL url = new URL(likeUrl);
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(
                    urlConnection.getOutputStream());

            writer.write("access_token=" + accessToken);
            writer.flush();

            String response = Utils.streamToString(urlConnection.getInputStream());

            // Convert the String response to JSONObject
            JSONObject jsonObj = new JSONObject(response);

            InstagramLikeResponseParser parser = new InstagramLikeResponseParser();
            return parser.parse(jsonObj);
        }
        catch (Exception e) {
            e.printStackTrace();

            // Try to get the Error message from Instagram
            try {
                String error = Utils.streamToString(urlConnection.getErrorStream());
                if(!Utils.isNullOrEmpty(error)) {
                    JSONObject jsonErrorObj = new JSONObject(error);
                    InstagramLikeResponseParser parser = new InstagramLikeResponseParser();
                    errorModel = parser.parseError(jsonErrorObj);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return Boolean.FALSE;
    }

    /**
     * Signals the completion of background task
     *
     * @param success
     */
    @Override
    protected void onPostExecute(Boolean success) {
        if(likeListener != null) {
            likeListener.onLikeCompleted(success, button, errorModel);
        }
    }
}
