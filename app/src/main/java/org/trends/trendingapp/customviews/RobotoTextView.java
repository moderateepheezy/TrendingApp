package org.trends.trendingapp.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import org.trends.trendingapp.R;
import org.trends.trendingapp.utils.AppUtils;


/* A simple custom text view that get the roboto fonts by passing the font attribute in the layout.*/
public class RobotoTextView extends TextView {

    private int font;
    private static final String TAG = RobotoTextView.class.getSimpleName();

    public RobotoTextView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(null, 0);
        }
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(attrs, 0);
        }
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(attrs, defStyleAttr);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RobotoTextView, defStyle, 0);

        font = a.getInteger(R.styleable.RobotoTextView_foundFont, 0);
        Typeface tf;

        if (font == getResources().getInteger(R.integer.font_roboto_bold)) {
            tf = AppUtils.getTfRobotBold();
        } else if (font == getResources().getInteger(R.integer.font_roboto_normal)) {
            tf = AppUtils.getTfRobotoRegular();
        } else if (font == getResources().getInteger(R.integer.font_roboto_light)) {
            tf = AppUtils.getTfRobotoLight();
        } else {
            tf = AppUtils.getTfRobotoRegular();
        }

        setTypeface(tf);

        a.recycle();
    }

    private void setTypefaceFonts(int typeface) {
        Typeface tf;
        if (typeface == getResources().getInteger(R.integer.font_roboto_bold)) {
            tf = AppUtils.getTfRobotBold();
        } else if (typeface == getResources().getInteger(R.integer.font_roboto_normal)) {
            tf = AppUtils.getTfRobotMedium();
        } else if (typeface == getResources().getInteger(R.integer.font_roboto_light)) {
            tf = AppUtils.getTfRobotoLight();
        } else {
            tf = AppUtils.getTfRobotoRegular();
        }
        super.setTypeface(tf);
    }

}
