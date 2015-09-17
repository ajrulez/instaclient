package com.alifesoftware.instaclient.interfaces;

import android.widget.Button;

import com.alifesoftware.instaclient.model.InstagramErrorModel;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This interface is for a listener that is signaled
 * when background operation for posting a Like
 * for a Popular Picture is finished
 *
 */
public interface ILikeResultListener {
    void onLikeCompleted(boolean success, Button button, InstagramErrorModel error);
}
