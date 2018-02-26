package coders.mich.gtdapp.utils;

import android.content.Context;

/**
 * Created by drew on 2/23/2018.
 */

public class helpers {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float getDimension(Context context, int resId) {
        return context.getResources().getDimension(resId);
    }

    public static float map(float value,
                            float iStart,
                            float iStop,
                            float oStart,
                            float oStop) {
        return oStart + (oStop - oStart) * ((value - iStart) / (iStop - iStart));
    }
}
