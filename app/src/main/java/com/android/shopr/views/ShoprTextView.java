package com.android.shopr.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.shopr.R;

public class ShoprTextView extends TextView {

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

    public ShoprTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShoprTextView, android.R.attr.textViewStyle, 0);
        if (a.hasValue(R.styleable.ShoprTextView_fontFile)) {
            String fontName = a.getString(R.styleable.ShoprTextView_fontFile);
            if (!isInEditMode()) {
                if (fontName != null) {
                    setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName));
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
            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName));
        }
    }


}
