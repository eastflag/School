package com.aura.smartschool.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;

public final class Util {
	//MainApplication에서 주입받는다.
	public static Context sContext;

	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}
	
	public static String getMdn() {
		TelephonyManager tMgr =(TelephonyManager)sContext.getSystemService(Context.TELEPHONY_SERVICE);
	    return tMgr.getLine1Number();
	}
}
