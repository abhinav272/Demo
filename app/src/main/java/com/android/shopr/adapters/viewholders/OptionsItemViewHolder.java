package com.android.shopr.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.shopr.R;

/**
 * Created by Abhinav on 03/04/17.
 */
public class OptionsItemViewHolder extends RecyclerView.ViewHolder {

    public final TextView tvTax;
    public TextView tvBagTotalBeforeDiscount, tvBagDiscount, tvBagTotalAfterDiscount, tvApplyDiscount, tvAmountPayable;
    public FrameLayout flApplyCoupon, flApplyLoyaltyCard;

    public OptionsItemViewHolder(View itemView) {
        super(itemView);

        tvBagTotalBeforeDiscount = (TextView) itemView.findViewById(R.id.tv_bag_total);
        tvBagDiscount = ((TextView) itemView.findViewById(R.id.tv_bag_discount));
        tvBagTotalAfterDiscount = ((TextView) itemView.findViewById(R.id.tv_total_after_discount));
        tvApplyDiscount = (TextView) itemView.findViewById(R.id.tv_coupon);
        tvAmountPayable = (TextView) itemView.findViewById(R.id.tv_total_after_coupon);
        flApplyCoupon = (FrameLayout) itemView.findViewById(R.id.fl_apply_coupon);
        flApplyLoyaltyCard = (FrameLayout) itemView.findViewById(R.id.fl_apply_loyalty_card);
        tvTax = (TextView) itemView.findViewById(R.id.tv_tax);
    }
}
