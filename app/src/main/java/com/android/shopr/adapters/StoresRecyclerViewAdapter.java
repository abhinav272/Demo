package com.android.shopr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.R;
import com.android.shopr.adapters.viewholders.SingleImageAndTextViewHolder;
import com.android.shopr.model.Store;
import com.squareup.picasso.Picasso;

/**
 * Created by abhinav.sharma on 20/01/17.
 */

public class StoresRecyclerViewAdapter extends RecyclerView.Adapter<SingleImageAndTextViewHolder>{

    public interface DelegateEvent{
        void delegateToHost(int position);
    }

    private Store.List mStores;
    private Context mContext;
    private DelegateEvent delegateEvent;

    public StoresRecyclerViewAdapter(Store.List mStores, Context mContext, DelegateEvent delegateEvent) {
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
        Picasso.with(mContext).load(getItem(position).getImgUrl()).into(holder.mImageView);
        holder.mTextView.setText(getItem(position).getStoreName());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegateEvent.delegateToHost(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStores.size();
    }

    private Store getItem(int position) {
        return mStores.get(position);
    }
}