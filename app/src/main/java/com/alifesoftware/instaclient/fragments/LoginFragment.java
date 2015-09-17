package com.alifesoftware.instaclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alifesoftware.instaclient.R;
import com.alifesoftware.instaclient.businesslogic.InstagramClient;
import com.alifesoftware.instaclient.interfaces.IOauthListener;
import com.alifesoftware.instaclient.interfaces.IPopularPicturesViewSwitcher;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * LoginFragment is displayed when we do not have an AccessToken
 * saved in SessionManager/SharedPreferences.
 *
 * There could be cases where the AccessToken is saved, but has expired.
 * I haven't had the time to go through entire Instagram Authentication
 * documentation to see various error codes/exceptions that may indicate
 * that a token has expired. I have worked on OAuth clients before, and I
 * know that if an AccessToken has expired, user must be prompted to
 * login again. In general OAuth also has concept of RefreshToken, that is
 * used to get a new AccessToken without prompting the user for a login,
 * but I don't know if Instagram supports it. I didn't find any mention
 * of RefreshToken in Instagram documentation.
 *
 * Note: We want to disable the rotation when LoginFragment is being
 * displayed because a lot goes on during LoginFragment lifecycle -
 * WebView is displayed, ProgressDialog is displayed, and an Authentication
 * might be in progress. It is easier to block rotation for LoginFragment
 * than to control other aspects
 *
 */
public class LoginFragment extends Fragment {
    // Key to get View Width from Arguments
    public static final String ARGUMENTS_KEY_WIDTH = "width";

    // Key to get View Height from Arguments
    public static  final String ARGUMENTS_KEY_HEIGHT = "height";

    // Instagram Client is used for Authentication
    InstagramClient instaClient = null;

    /**
     * Setup the Fragment View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.login_fragment, container, false);

        // Login Button
        Button loginButton = (Button) view.findViewById(R.id.loginButton);

        // Create a new Instagram Client
        instaClient = new InstagramClient(getActivity(), getArguments().getInt(ARGUMENTS_KEY_WIDTH),
                getArguments().getInt(ARGUMENTS_KEY_HEIGHT));

        // This listener is used to get Authentication Result from Instagram
        instaClient.setListener(new IOauthListener() {
            @Override
            public void onSuccess() {
                // Tell the Activity to switch Fragment
                Activity mainActivity = getActivity();

                // Check if the Activity implements IPopularPicturesViewSwitcher,
                // and if it does, switch to PopularPicturesFragment to show the
                // Pictures
                if(IPopularPicturesViewSwitcher.class.isAssignableFrom(mainActivity.getClass())) {
                    IPopularPicturesViewSwitcher switcher = (IPopularPicturesViewSwitcher) mainActivity;
                    switcher.switchToPopularPicturesView();
                }
                else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_popularpicture_switcher),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFail(String error) {
                // Show Error
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Start Authentication process when Login button is clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instaClient.startAuthentication();
            }
        });

        // Return the Fragment View
        return view;
    }
}
