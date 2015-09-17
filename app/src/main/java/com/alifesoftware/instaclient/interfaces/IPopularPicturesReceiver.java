package com.alifesoftware.instaclient.interfaces;

import com.alifesoftware.instaclient.model.PopularPicturesModel;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This interface is for a listener that is signaled
 * when background operation for getting Popular Pictures
 * data is finished
 *
 */
public interface IPopularPicturesReceiver {
    void onPictureDataRetrieved(ArrayList<PopularPicturesModel> popularPicturesArray);
}
