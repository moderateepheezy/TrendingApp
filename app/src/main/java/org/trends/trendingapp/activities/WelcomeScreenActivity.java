package org.trends.trendingapp.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import org.trends.trendingapp.R;
import org.trends.trendingapp.fragments.LoginFragment;

public class WelcomeScreenActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);


        final Slide loginSlide;
        final boolean customFragments = true;

        setButtonBackFunction(true ? BUTTON_BACK_FUNCTION_SKIP : BUTTON_BACK_FUNCTION_BACK);
        setButtonNextFunction(true ? BUTTON_NEXT_FUNCTION_NEXT_FINISH : BUTTON_NEXT_FUNCTION_NEXT);
        setButtonBackVisible(true);
        setButtonNextVisible(true);
        setButtonCtaVisible(true);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_BACKGROUND);
        TypefaceSpan labelSpan = new TypefaceSpan(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? "sans-serif-medium" : "sans serif");
        SpannableString label = SpannableString.valueOf(getString(R.string.label_button_cta_canteen_intro));
        label.setSpan(labelSpan, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setButtonCtaLabel(label);

        setPageScrollDuration(500);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setPageScrollInterpolator(android.R.interpolator.fast_out_slow_in);
        }


        addSlide(new SimpleSlide.Builder()
                .image(R.drawable.news_slide)
                .background(R.color.color_news)
                .backgroundDark(R.color.color_news_dar)
                .scrollable(true)
                .build());

        addSlide(new SimpleSlide.Builder()
                .image(R.drawable.events_slide)
                .background(R.color.color_events)
                .backgroundDark(R.color.color_event_dar)
                .scrollable(true)
                .build());

        addSlide(new SimpleSlide.Builder()
                .image(R.drawable.trends_slide)
                .background(R.color.color_trend)
                .backgroundDark(R.color.color_trend_dar)
                .scrollable(true)
                .build());

        addSlide(new SimpleSlide.Builder()
                .image(R.drawable.video_slide)
                .background(R.color.color_video)
                .backgroundDark(R.color.color_video_dar)
                .scrollable(true)
                .build());

        if (customFragments) {
            loginSlide = new FragmentSlide.Builder()
                    .background(R.color.generic)
                    .backgroundDark(R.color.primary)
                    .fragment(LoginFragment.newInstance())
                    .build();
            addSlide(loginSlide);
        } else {
            loginSlide = null;
        }

        autoplay(2500, INFINITE);
    }
}
