package com.android.shopr.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.android.shopr.R;

public class ShoprButton extends Button {


    public ShoprButton(Context context) {
        super(context);
    }

    public ShoprButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShoprButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ShoprButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
