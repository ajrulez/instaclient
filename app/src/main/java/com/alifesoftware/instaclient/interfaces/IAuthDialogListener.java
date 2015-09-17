package com.alifesoftware.instaclient.interfaces;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * his interface is for a listener that is signaled
 * when first phase of Authentication is complete
 *
 */
public interface IAuthDialogListener {
    public void onComplete(String code);
    public void onError(String error);
}
