package com.alifesoftware.instaclient.businesslogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class is a wrapper over SharedPreferences and
 * is used to store user's AccessToken for Instagram.
 *
 * It makes for cleaner code when all SharedPreferences
 * access (read and write) is wrapped in one place instead
 * of having SharedPreferences code all over the place
 *
 */
public class SessionManager {
    // SharedPreferences to store AccessToken
    private SharedPreferences prefStore;

    // SharedPreferences Editor to write data to SharedPreferences
    private Editor prefEditor;

    // Key for out SharedPreferences file
    private static final String APP_PREF_STORE = "instaprefs";

    // Key for AccessToken data stored in SharedPreferences
    private static final String INSTAGRAM_ACCESS_TOKEN = "access_token";

    /**
     * Constructor
     * @param context
     */
    public SessionManager(Context context) {
        prefStore = context.getSharedPreferences(APP_PREF_STORE, Context.MODE_PRIVATE);
        prefEditor = prefStore.edit();
    }

    /**
     * Method to write/save AccessToken to SharedPreferences
     *
     * @param accessToken
     */
    public void storeAccessToken(String accessToken) {
        prefEditor.putString(INSTAGRAM_ACCESS_TOKEN, accessToken);
        prefEditor.commit();
    }

    /**
     * Method to clear the AccessToken stored in the
     * SharedPreferences.
     *
     * Note: I couldn't find any API to log off from
     * Instagram
     *
     */
    public void clearAccessToken() {
        prefEditor.putString(INSTAGRAM_ACCESS_TOKEN, null);
        prefEditor.commit();
    }

    /**
     * Method to get the AccessToken from SharedPreferences
     * @return
     */
    public String getAccessToken() {
        return prefStore.getString(INSTAGRAM_ACCESS_TOKEN, null);
    }
}
