package com.alifesoftware.instaclient.businesslogic;

import android.app.ProgressDialog;
import android.content.Context;

import com.alifesoftware.instaclient.R;
import com.alifesoftware.instaclient.authentication.AuthDialog;
import com.alifesoftware.instaclient.interfaces.IAuthDialogListener;
import com.alifesoftware.instaclient.interfaces.IOauthListener;
import com.alifesoftware.instaclient.interfaces.ITokenResultListener;
import com.alifesoftware.instaclient.tasks.InstagramAccessTokenTask;
import com.alifesoftware.instaclient.utils.Utils;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * InstagramClient class is used to complete
 * Instagram Authentication. The first step in the
 * process is to launch a WebView that gets a Code using
 * client ID and Redirect URI. Once we get the code,
 * we exchange that Code for an AccessToken from the
 * server.
 *
 */
public class InstagramClient implements ITokenResultListener {
    // Session manager to Store Tokens
    private SessionManager sessionManager;

    // Authentication Dialog
    private AuthDialog authDialog;

    // OAuth Listener
    private IOauthListener oAuthListener;

    // Context
    private Context appContext;

    // Progress Dialog
    private ProgressDialog progressDialog;

    // Access Token
    private String accessToken;

    /**
     * Constructor
     *
     * @param context
     * @param width
     * @param height
     */
    public InstagramClient(Context context, int width, int height) {
        appContext = context;
        sessionManager = new SessionManager(appContext);
        accessToken = sessionManager.getAccessToken();

        // IAuthDialogListener receives callbacks from the Dialog
        // that contains the WebView for initial part of Authentication
        // process
        IAuthDialogListener listener = new IAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                // Get Access Token using the Code that we just fetched
                requestAccessToken(code);
            }

            @Override
            public void onError(String error) {
                // Since there was an error getting the Code, send the error
                // to the listener
                oAuthListener.onFail(appContext.getResources().getString(R.string.authentication_failed) + " :" + error);
            }
        };

        // Create the Dialog that contains WebView
        authDialog = new AuthDialog(appContext, width, height, listener);

        // Set up a Progress Dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    /**
     * Method to request Access Token using the Code.
     * This uses AsyncTask to get the AccessToken
     *
     * @param code
     */
    private void requestAccessToken(final String code) {
        // Create the AsyncTask
        InstagramAccessTokenTask accessTokenTask = new InstagramAccessTokenTask(appContext,
                                                            progressDialog, this);
        // Execute the AsyncTask
        accessTokenTask.execute(new String[] {code});
    }

    /**
     * // ITokenResultListener Implementation - This listener
     * is called when the AsyncTask that was fetcing the AccessToken
     * finishes the Async operation
     *
     * @param success
     * @param accessToken
     */
    @Override
    public void onTokenReceived(boolean success, String accessToken) {
        if(success &&
                !Utils.isNullOrEmpty(accessToken)) {
            // We got the AccessToken - Save it to SharedPreferences/SessionManager
            sessionManager.storeAccessToken(accessToken);
            // Indicate the AuthListener that Authentication was successful
            oAuthListener.onSuccess();
        }
        else {
            // Indicate to AuthListener that the Authentication failed
            oAuthListener.onFail(appContext.getResources().getString(R.string.failed_to_get_access_token));
        }
    }

    /**
     * Method to set a Listener for InstagramClient
     * @param listener
     */
    public void setListener(IOauthListener listener) {
        oAuthListener = listener;
    }

    /**
     * Method to start the Authentication process
     *
     */
    public void startAuthentication() {
        authDialog.show();
    }
}
