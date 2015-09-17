package com.alifesoftware.instaclient.interfaces;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This interface is for a listener that is signaled
 * when Authentication process is complete
 *
 */
public interface IOauthListener {
    public void onSuccess();

    public void onFail(String error);
}
