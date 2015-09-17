package com.alifesoftware.instaclient.authentication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alifesoftware.instaclient.R;
import com.alifesoftware.instaclient.interfaces.IAuthDialogListener;
import com.alifesoftware.instaclient.utils.Constants;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class is used to create a Dialog that contains
 * a WebView, which is used for initial Authentication
 * call for Instagram
 *
 */
public class AuthDialog extends Dialog {
    // Authentication Listener
    private IAuthDialogListener authListener;

    // Context
    private Context dlgContext;

    // Progress Dialog
    private ProgressDialog progressDlg;

    // WebView for Auth
    private WebView authWebView;

    // Content Holder
    private LinearLayout contentHolder;

    // Title
    private TextView title;

    // Width of the Window
    private int windowWidth;

    // Height of the Window
    private int windowHeight;

    /**
     * Constructor for AuthDialog
     *
     * @param context
     * @param width
     * @param height
     * @param listener
     */
    public AuthDialog(Context context, int width, int height,  IAuthDialogListener listener) {
        super(context);

        dlgContext = context;
        windowWidth = width;
        windowHeight = height;
        authListener = listener;
    }

    /**
     * Creates the AuthDialog
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup a ProgressDialog
        progressDlg = new ProgressDialog(dlgContext);
        progressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDlg.setMessage(dlgContext.getResources().getString(R.string.please_wait));

        contentHolder = new LinearLayout(getContext());
        contentHolder.setOrientation(LinearLayout.VERTICAL);

        setUpTitle();
        setUpWebView();

        addContentView(contentHolder, new LinearLayout.LayoutParams(windowWidth, windowHeight));

        // Once the user Logs Off, and tries to re-login, they will not
        // be asked for their credentials again because the WebView caches
        // their credentials. Removing all cookies will ensure that
        // user is asked for their credentials again when they log off and
        // re-login
        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    /**
     * Method to set the title of the View that
     * holds WebView - Just a cosmetic method,
     * doesn't have any functionality
     */
    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        title = new TextView(dlgContext);
        title.setText(dlgContext.getResources().getString(R.string.instagram_authentication));
        title.setTextColor(Color.WHITE);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setBackgroundColor(Color.BLACK);
        contentHolder.addView(title);
    }

    /**
     * Method to setup the WebView for Instagram
     * authentication.
     *
     * We must set a client for the WebView to get
     * data from the WebView
     */
    private void setUpWebView() {
        authWebView = new WebView(getContext());
        authWebView.setVerticalScrollBarEnabled(false);
        authWebView.setHorizontalScrollBarEnabled(false);
        authWebView.setWebViewClient(new AuthWebViewClient(authListener, this));
        authWebView.getSettings().setJavaScriptEnabled(true);

        // Load the Authentication URL Request in the WebView
        // This request gets us the Code which we later exchange
        // to get AccessToken
        authWebView.loadUrl(Constants.AUTH_URL_REQUEST);
        authWebView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        contentHolder.addView(authWebView);
    }

    /**
     * Method to show the ProgressDialog
     */
    public void showProgressDialog() {
        progressDlg.show();
    }

    /**
     * Method to dismiss the ProgressDialog
     */
    public void hideProgressDialog() {
        progressDlg.dismiss();
    }
}
