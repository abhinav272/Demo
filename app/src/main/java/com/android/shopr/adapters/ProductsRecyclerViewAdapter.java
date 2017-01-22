package com.android.shopr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.R;
import com.android.shopr.adapters.viewholders.SingleImageAndTextViewHolder;
import com.android.shopr.model.Category;
import com.android.shopr.model.CategoryWiseProducts;
import com.android.shopr.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<SingleImageAndTextViewHolder> {

    private Context mContext;
    private DelegateEvent delegateEvent;
    private CategoryWiseProducts mCategoryWiseProducts;
    private List<Product> mProducts;

    public ProductsRecyclerViewAdapter(Context mContext, DelegateEvent delegateEvent, CategoryWiseProducts mCategoryWiseProducts) {
        this.mContext = mContext;
        this.delegateEvent = delegateEvent;
        this.mCategoryWiseProducts = mCategoryWiseProducts;
        mProducts = mCategoryWiseProducts.getProducts();
    }

    public interface DelegateEvent {
        void delegateToHost(int storeId, int categoryId, int productId);
    }


    @Override
    public SingleImageAndTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.two_image_and_text, parent, false);
        return new SingleImageAndTextViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(SingleImageAndTextViewHolder holder, int position) {
        Picasso.with(mContext).load(getItem(position).getImageUrl()).into(holder.mImageView);
        holder.mTextView.setText(getItem(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    private Product getItem(int position) {
        return mProducts.get(position);
    }
}
