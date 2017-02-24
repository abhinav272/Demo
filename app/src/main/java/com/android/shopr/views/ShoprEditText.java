package com.android.shopr.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.android.shopr.R;

public class ShoprEditText extends EditText {


    public ShoprEditText(Context context) {
        super(context);
    }

    public ShoprEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShoprEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ShoprEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
