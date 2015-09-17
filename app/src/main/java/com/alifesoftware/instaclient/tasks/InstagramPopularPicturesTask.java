package com.alifesoftware.instaclient.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alifesoftware.instaclient.businesslogic.SessionManager;
import com.alifesoftware.instaclient.interfaces.IPopularImageParser;
import com.alifesoftware.instaclient.interfaces.IPopularPicturesReceiver;
import com.alifesoftware.instaclient.model.PopularPicturesModel;
import com.alifesoftware.instaclient.parser.ParserFactory;
import com.alifesoftware.instaclient.utils.Constants;
import com.alifesoftware.instaclient.utils.Utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class implements/extends an AsyncTask that is used
 * to get Popular Pictures from Instagram
 *
 */
public class InstagramPopularPicturesTask extends AsyncTask<Void, Void, ArrayList<PopularPicturesModel>> {

    // IPopularPicturesReceiver Receiver to receive the Results
    IPopularPicturesReceiver resultReceiver = null;

    // Progress Dialog
    private ProgressDialog progressDialog;

    // Context
    private Context appContext;

    /**
     * Constructor
     *
     * @param ctx
     * @param pdlg
     * @param rcvr
     */
    public InstagramPopularPicturesTask(Context ctx, ProgressDialog pdlg, IPopularPicturesReceiver rcvr) {
        appContext = ctx;
        progressDialog = pdlg;
        resultReceiver = rcvr;
    }

    /**
     * PreExecute shows a ProgressDialog
     *
     */
    @Override
    protected void onPreExecute() {
        if(progressDialog != null) {
            progressDialog.show();
        }
    }

    /**
     * doInBackground does the Async operation, such as
     * HTTP communication. In this case, it gets a JSON
     * response for Popular Pictures request from Instagram
     * and process that response into a Data Model for
     * Popular Pictures
     *
     * @param params
     * @return
     */
    @Override
    protected ArrayList<PopularPicturesModel> doInBackground(Void... params) {
        try {
            // Create a SessionManager (SharedPreference Wrapper)
            SessionManager sessionManager = new SessionManager(appContext);

            // get the AccessToken from SessionManager, and if it is
            // null or empty, then bail out because we have to have
            // a non-Null, not-Empty AccessToken to get Popular Pictures
            String accessToken = sessionManager.getAccessToken();
            if(Utils.isNullOrEmpty(accessToken)) {
                return null;
            }

            // Construct a URL to get Popular Pictures
            URL url = new URL(Constants.POPULAR_PHOTOS_URL + accessToken);

            // Using Android's HttpURLConnection for HTTP communication.
            // other reputed options include:
            //
            // 1. OkHttp by Square
            // 2. Volley by Google
            // 3. DefaultHttpClient by Apache - Deprecated
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");

            String response = Utils.streamToString(urlConnection.getInputStream());

            // Convert the String response to JSONObject
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

            if(jsonObj != null) {
                // Using a Factory Pattern just to demonstrate design skills
                // Not really needed in this case, but imagine if we have different
                // parsers for the same data (for whatever reason)
                IPopularImageParser parser = ParserFactory.createParser(ParserFactory.ParserType.PARSER_INSTAGRAM_JSON);
                if(parser != null) {
                    return parser.parse(jsonObj);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets called when Async operation is complete. Notifies the
     * listener with the result of the Async operation, which is Data Model
     * of Popular Pictures in this case
     *
     * @param popularPicturesArray
     */
    @Override
    protected void onPostExecute(ArrayList<PopularPicturesModel> popularPicturesArray) {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }

        if(resultReceiver != null) {
            resultReceiver.onPictureDataRetrieved(popularPicturesArray);
        }
    }
}
