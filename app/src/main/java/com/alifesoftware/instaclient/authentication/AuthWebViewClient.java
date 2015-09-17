package com.alifesoftware.instaclient.authentication;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alifesoftware.instaclient.interfaces.IAuthDialogListener;
import com.alifesoftware.instaclient.utils.Constants;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class creates a WebClient that is bound
 * to the WebView which we use for Instagram Authentication.
 *
 */
public class AuthWebViewClient extends WebViewClient {

    // Login Tag
    private static final String TAG = "AuthWebViewClient";

    // Authentication Listener
    IAuthDialogListener authListener = null;

    // Authentication Dialog
    AuthDialog authDialog = null;

    /**
     * Constructor for the WebView Client
     *
     * @param listener
     * @param dialog
     */
    public AuthWebViewClient(IAuthDialogListener listener, AuthDialog dialog) {
        authListener = listener;
        authDialog = dialog;
    }

    /**
     * We must overload URL Loading because we want to
     * capture the redirect when Instagram sends a code
     * to the redirect URI.
     *
     * If we don't do this, the WebView will automatically
     * get redirected to the redirect URI, and we'll not get
     * the code in the client
     *
     * @param view
     * @param url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d(TAG, "Redirecting URL " + url);
        if (url.startsWith(Constants.REDIRECT_URI)) {
            String urls[] = url.split("=");
            authListener.onComplete(urls[1]);
            authDialog.dismiss();
            return true;
        }

        return false;
    }

    /**
     * Since we are using the WebView for initial Authentication
     * request, we have no other way of knowing if there is
     * a problem during server communication. Overriding
     * this method helps us get the error/exceptions.
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        Log.d(TAG, "Page error: " + description);

        super.onReceivedError(view, errorCode, description, failingUrl);
        authListener.onError(description);
        authDialog.dismiss();
    }

    /**
     * Method to indicate that WebView has started to
     * load a web page.
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d(TAG, "Loading URL: " + url);

        super.onPageStarted(view, url, favicon);
        authDialog.showProgressDialog();
    }

    /**
     * Method to indicate that WebView has finished loading
     * web page
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        Log.d(TAG, "onPageFinished URL: " + url);
        authDialog.hideProgressDialog();
    }
}
