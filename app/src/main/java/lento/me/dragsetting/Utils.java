package lento.me.dragsetting;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by lento on 2017/5/22.
 */

public class Utils {
    public static final int DENSITY_LOW = 120;
    public static final int DENSITY_MEDIUM = 160;
    public static final int DENSITY_HIGH = 240;
    public static final int DENSITY_XHIGH = 320;
    public final static float BASE_SCREEN_HEIGHT = 1280f;

    public final static float BASE_SCREEN_DENSITY = 2f;


    private static final int DP_TO_PX = TypedValue.COMPLEX_UNIT_DIP;
    private static final int SP_TO_PX = TypedValue.COMPLEX_UNIT_SP;
    private static final int PX_TO_DP = TypedValue.COMPLEX_UNIT_MM + 1;
    private static final int PX_TO_SP = TypedValue.COMPLEX_UNIT_MM + 2;
    private static final int DP_TO_PX_SCALE_H = TypedValue.COMPLEX_UNIT_MM + 3;

    public static final int LDPI = 1;
    public static final int MDPI = 2;
    public static final int HDPI = 3;
    public static final int XHDPI = 4;
    public static final int XXHDPI = 5;
    public static final int XXXHDPI = 6;
    public static final int OTHER_DPI = 7;
    public static final int NULL_DPI = 0;


    public static int dp2px(Context context, float value) {
        final DisplayMetrics mMetrics = context.getResources()
                .getDisplayMetrics();
        return (int) applyDimension(DP_TO_PX, value, mMetrics);
    }

    private static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case DP_TO_PX:
            case SP_TO_PX:
                return TypedValue.applyDimension(unit, value, metrics);
            case PX_TO_DP:
                return value / metrics.density;
            case PX_TO_SP:
                return value / metrics.scaledDensity;
            case DP_TO_PX_SCALE_H:
                return TypedValue.applyDimension(DP_TO_PX, value * getScaleFactorH(metrics), metrics);
        }
        return 0;
    }

    public static Float sScaleW, sScaleH;


    public static float getScaleFactorH(DisplayMetrics metrics) {
        if (sScaleH == null) {
            sScaleH = (metrics.heightPixels * BASE_SCREEN_DENSITY)
                    / (metrics.density * BASE_SCREEN_HEIGHT);
        }
        return sScaleH;
    }
}
