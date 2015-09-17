package com.alifesoftware.instaclient.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alifesoftware.instaclient.R;
import com.alifesoftware.instaclient.model.PopularPicturesModel;
import com.alifesoftware.instaclient.utils.Constants;
import com.alifesoftware.instaclient.viewholders.PopularPicturesViewHolder;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * PopularPicturesAdapter is the custom Adapter that is
 * used to display Popular Pictures and the Like Button
 * in the ListView
 *
 * PopularPicturesAdapter uses Universal Image Loader (UIL)
 * to support Lazy Loading of images. Note that some methods
 * of UIL use deprecated APIs.
 *
 * Another option is to use Picaso for Lazy Loading, which is
 * as easy to use as UIL. I have used a configuration flag
 * that dictates whether we want to use Picaso or UIL
 *
 * (Note: Not showing off, I like to treat all my work as
 * a reference for future, which is why I am keeping both
 * UIL and Picaso as part of this assignment.
 *
 * Please ignore the overhead, I wouldn't usually do this in
 * production code
 *
 * This Adapter uses ViewHolder Pattern for smooth scrolling
 * of the ListView that is bound to this Adapter
 */
public class PopularPicturesAdapter extends ArrayAdapter<PopularPicturesModel> {

    // Data
    private ArrayList<PopularPicturesModel> arrPictureData = new ArrayList<PopularPicturesModel> ();

    // Using Universal Image Loader to LazyLoad the Images
    private ImageLoader imageLoader;

    // Context
    private Context appContext;

    // Activity
    private Activity activity;

    // OnClickListener for the Like Button
    private View.OnClickListener onClickListener;

    // Default Constructor
    public PopularPicturesAdapter(Context context, int resource, Activity activity, View.OnClickListener listener) {
        super(context, resource);

        appContext = context;
        this.activity = activity;
        onClickListener = listener;

        /**
         * Setup Universal Image Loader
         */
        @SuppressWarnings("deprecation")
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        @SuppressWarnings("deprecation")
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                appContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        imageLoader = ImageLoader.getInstance();
        /**
         * End Setup UIL
         */
    }

    /**
     * Method to update the Data for the Adapter
     * @param data
     */
    public synchronized void updateData(final ArrayList<PopularPicturesModel> data) {
        try {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    arrPictureData = data;
                    // Notify that the data has been changed
                    notifyDataSetChanged();
                }
            });
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the count of data items in the Adapter
     * @return
     */
    @Override
    public int getCount() {
        return arrPictureData.size();
    }


    /**
     * Implement getView method of the Adapter using
     * ViewHolder pattern for smooth scrolling
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularPicturesViewHolder holder = null;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.popular_pictures_row_item, null);

            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            holder = new PopularPicturesViewHolder();
            holder.tvCaption = (TextView) convertView.findViewById(R.id.popularCaptionTextView);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.popularImageView);
            holder.btnLike = (Button) convertView.findViewById(R.id.likeButton);
            holder.btnLike.setOnClickListener(onClickListener);

            convertView.setTag(holder);
        }

        else {
            // Get the ViewHolder back to get fast access to the TextView
            holder = (PopularPicturesViewHolder) convertView.getTag();
        }

        if(arrPictureData != null &&
                arrPictureData.size() > 0 &&
                position < arrPictureData.size()) {

            // Bind the data efficiently with the holder.
            holder.tvCaption.setText(arrPictureData.get(position).getPictureCaption());

            // While we load the Image in the ImageView, set a default Image
            holder.ivImage.setImageDrawable(appContext.getResources().getDrawable(R.mipmap.ic_launcher));

            String imageUrl = "";
            if(Constants.showHighResolutionImages) {
                imageUrl = arrPictureData.get(position).getHighResPictureUrl();
            }
            else {
                imageUrl = arrPictureData.get(position).getPictureUrl();
            }

            switch(Constants.lazyLoaderToUse) {
                case UNIVERSAL_IMAGE_LOADER: {
                    imageLoader.displayImage(imageUrl, holder.ivImage);
                }
                break;

                case SQUARE_PICASO: {
                    Picasso.with(appContext).load(imageUrl).into(holder.ivImage);
                }
                break;

                default:
                    // Nothing to do
            }

            // Add the ID of the image as a Tag for the button so when
            // the button is clicked, we can get the ID from the button
            // and post the Like
            holder.btnLike.setTag(arrPictureData.get(position).getPictureId());

            // Disable the Like button is user has already liked it
            if(arrPictureData.get(position).isHasUserLiked()) {
                holder.btnLike.setEnabled(false);
            } else {
                holder.btnLike.setEnabled(true);
            }
        }

        return convertView;
    }

    /**
     * Get the data/contents of this Adapter
     *
     * @return
     */
    public ArrayList<PopularPicturesModel> getData() {
        return arrPictureData;
    }

}
