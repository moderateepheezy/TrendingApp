package org.trends.trendingapp.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import org.trends.trendingapp.TrendingApplication;

/* Utils class holding helpers for the Roboto Text View */
public class AppUtils {

    static Typeface tfRobotRegular = null;
    static Typeface tfRobotMedium = null;
    static Typeface tfRobotBold = null;
    static Typeface tfRobotoLight = null;

    public static Typeface getTfRobotoRegular() {
        if (tfRobotRegular == null) {
            AssetManager mgr = TrendingApplication.getInstance().getResources().getAssets();
            tfRobotRegular = Typeface.createFromAsset(mgr, "fonts/roboto_regular.ttf");
        }

        return tfRobotRegular;
    }

    public static Typeface getTfRobotoLight() {
        if (tfRobotoLight == null) {
            AssetManager mgr = TrendingApplication.getInstance().getResources().getAssets();
            tfRobotoLight = Typeface.createFromAsset(mgr, "fonts/roboto_light.ttf");
        }

        return tfRobotoLight;
    }

    public static Typeface getTfRobotMedium() {
        if (tfRobotMedium == null) {
            AssetManager mgr = TrendingApplication.getInstance().getResources().getAssets();
            tfRobotMedium = Typeface.createFromAsset(mgr, "fonts/roboto_medium.ttf");
        }

        return tfRobotMedium;
    }

    public static Typeface getTfRobotBold() {
        if (tfRobotBold == null) {
            AssetManager mgr = TrendingApplication.getInstance().getResources().getAssets();
            tfRobotBold = Typeface.createFromAsset(mgr, "fonts/roboto_bold.ttf");
        }

        return tfRobotBold;
    }

}
