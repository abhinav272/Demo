package com.android.shopr.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.R;

/**
 * Created by abhinav.sharma on 20/01/17.
 */

public class SingleImageAndTextViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "SingleImageAndTextViewH";
    public ImageView mImageView;
    public TextView mTextView;

    public SingleImageAndTextViewHolder(View view) {
        super(view);
        mImageView = (ImageView) view.findViewById(R.id.iv_image);
        mTextView = (TextView) view.findViewById(R.id.tv_text);
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
    }
}
