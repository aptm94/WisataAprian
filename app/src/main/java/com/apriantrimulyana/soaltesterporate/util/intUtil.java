package com.apriantrimulyana.soaltesterporate.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class intUtil {

    public static int dpToPx(int dp, Context ctx) {
        Resources r = ctx.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
