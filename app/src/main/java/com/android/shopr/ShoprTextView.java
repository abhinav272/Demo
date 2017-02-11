package com.android.shopr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Abhinav on 11/02/17.
 */
public class ShoprTextView extends AppCompatTextView {
    public ShoprTextView(Context context) {
        super(context);
    }

    public ShoprTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShoprTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShoprTextView, android.R.attr.textViewStyle, 0);
        if (a.hasValue(R.styleable.ShoprTextView_fontFile)) {
            String fontName = a.getString(R.styleable.ShoprTextView_fontFile);
            if (!isInEditMode()) {
                if (fontName != null) {
                    setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf"));
                }
            }
        }
        a.recycle();

    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }


    public void setTypeface(Context context, String fontName) {
        if (fontName != null) {
            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf"));
        }
    }
}
