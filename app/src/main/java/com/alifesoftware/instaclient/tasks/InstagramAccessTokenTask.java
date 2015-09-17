package com.alifesoftware.instaclient.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alifesoftware.instaclient.R;
import com.alifesoftware.instaclient.interfaces.ITokenResultListener;
import com.alifesoftware.instaclient.utils.Constants;
import com.alifesoftware.instaclient.utils.Utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class is an AsyncTask that is used to complete the
 * Authentication process for a user and get their
 * AccessToken
 *
 */
public class InstagramAccessTokenTask extends AsyncTask<String, Void, String> {
    // Listener to get the Token Result
    ITokenResultListener tokenListener;

    // Context
    Context appContext;

    // Progress Dialog
    ProgressDialog progressDialog;

    public InstagramAccessTokenTask(Context ctx, ProgressDialog pdlg, ITokenResultListener listener) {
        appContext = ctx;
        progressDialog = pdlg;
        tokenListener = listener;
    }

    /**
     * Start the ProgressDialog before starting the
     * background Authentication
     *
     */
    @Override
    protected void onPreExecute() {
       // Show Progress Dialog
        if(appContext != null &&
                progressDialog != null) {
            progressDialog.setMessage(appContext.getResources().getString(R.string.requesting_access_token));
            progressDialog.show();
        }
    }

    /**
     * Authentication process runs in the background
     *
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        if(params.length == 0) {
            return null;
        }

        String accessToken = "";

        try {
            URL url = new URL(Constants.TOKEN_URL_REQUEST);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(
                    urlConnection.getOutputStream());

            writer.write("client_id=" + Constants.CLIENT_ID + "&client_secret="
                    + Constants.CLIENT_SECRET + "&grant_type=authorization_code"
                    + "&redirect_uri=" + Constants.REDIRECT_URI + "&code=" + params[0]);
            writer.flush();

            String response = Utils.streamToString(urlConnection.getInputStream());

            // Convert the String response to JSONObject
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

            accessToken = jsonObj.getString("access_token");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    /**
     * Signals the end of Authentication process that
     * was running in the background
     *
     * @param accessToken
     */
    @Override
    protected void onPostExecute(String accessToken) {
        // Dismiss the ProgressDialog
        if(progressDialog != null) {
            progressDialog.dismiss();
        }

        // Notify the listener of the result of Authentication Process
        if(tokenListener != null) {
            if(Utils.isNullOrEmpty(accessToken)) {
                tokenListener.onTokenReceived(false, "");
            }
            else {
                tokenListener.onTokenReceived(true, accessToken);
            }
        }
    }
}
