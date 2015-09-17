package com.alifesoftware.instaclient.interfaces;

/**
 * Created by anujsaluja on 6/12/15.
 *
 * This interface is needed by any class suppots
 * Data Refresh (for example, PopularPicturesFragment
 * may need to refresh it's data)
 *
 */
public interface IDataRefreshListener {
    void refreshData();
}
