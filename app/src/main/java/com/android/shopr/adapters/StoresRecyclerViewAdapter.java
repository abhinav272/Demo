package com.android.shopr.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.DrawableContainer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.R;
import com.android.shopr.adapters.viewholders.SingleImageAndTextViewHolder;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.model.Store;
import com.android.shopr.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abhinav.sharma on 20/01/17.
 */

public class StoresRecyclerViewAdapter extends RecyclerView.Adapter<SingleImageAndTextViewHolder>{

    public interface DelegateEvent{
        void delegateToHost(int storeId);
    }

    private List<PlaceWiseStores> mStores;
    private Context mContext;
    private DelegateEvent delegateEvent;

    public StoresRecyclerViewAdapter(List<PlaceWiseStores> mStores, Context mContext, DelegateEvent delegateEvent) {
        this.mStores = mStores;
        this.mContext = mContext;
        this.delegateEvent = delegateEvent;
    }

    @Override
    public SingleImageAndTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.two_image_and_text, parent, false);
        return new SingleImageAndTextViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(SingleImageAndTextViewHolder holder, final int position) {
        Picasso.with(mContext).load(getItem(position).getImageUrl())
                .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).into(holder.mImageView);
        holder.mTextView.setText(getItem(position).getStoreName());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegateEvent.delegateToHost(getItem(position).getStoreId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStores.size();
    }

    private PlaceWiseStores getItem(int position) {
        return mStores.get(position);
    }
}
