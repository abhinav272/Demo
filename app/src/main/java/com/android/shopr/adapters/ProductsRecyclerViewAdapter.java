package com.android.shopr.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.shopr.R;
import com.android.shopr.adapters.viewholders.ProductImageViewHolder;
import com.android.shopr.adapters.viewholders.SingleImageAndTextViewHolder;
import com.android.shopr.model.Category;
import com.android.shopr.model.CategoryWiseProducts;
import com.android.shopr.model.Product;
import com.android.shopr.model.StoreWiseCategories;
import com.android.shopr.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductImageViewHolder> {

    private Context mContext;
    private DelegateEvent delegateEvent;
    private StoreWiseCategories mStoreWiseCategories;

    public ProductsRecyclerViewAdapter(Context mContext, StoreWiseCategories mStoreWiseCategories, DelegateEvent delegateEvent) {
        this.mContext = mContext;
        this.delegateEvent = delegateEvent;
        this.mStoreWiseCategories = mStoreWiseCategories;
    }

    public interface DelegateEvent {
        void delegateToHost(int categoryId, Product product);
    }

    @Override
    public void onViewDetachedFromWindow(ProductImageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mImageView.clearAnimation();
//        holder.mTextView.clearAnimation();
    }

    @Override
    public ProductImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.product_image_and_description, parent, false);
        return new ProductImageViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ProductImageViewHolder holder, final int position) {
        Picasso.with(mContext).load(getItem(position).getImageUrl())
                .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).into(holder.mImageView);
        holder.productName.setText(getItem(position).getProductName());
        holder.originalPrice.setText(getItem(position).getPriceBeforeDiscount());
        holder.productDiscount.setText(getItem(position).getDiscount());
        holder.priceAfterDiscount.setText(getItem(position).getPriceAfterDiscount());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegateEvent.delegateToHost(mStoreWiseCategories.getCategoryId(), getItem(position));
            }
        });
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
//        holder.mTextView.startAnimation(animation);
//        holder.mImageView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mStoreWiseCategories.getProducts().size();
    }

    private Product getItem(int position) {
        return mStoreWiseCategories.getProducts().get(position);
    }
}
