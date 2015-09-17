package com.alifesoftware.instaclient.interfaces;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This interface is for a listener that is signaled
 * when background operation for getting AccessToken
 * is finished
 *
 */
public interface ITokenResultListener {
    void onTokenReceived(boolean success, String accessToken);
}
