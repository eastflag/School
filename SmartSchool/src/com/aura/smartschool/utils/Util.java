package com.aura.smartschool.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public final class Util {
	//MainApplication에서 주입받는다.
	//public static Context sContext;

	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}
	
	public static String getMdn(Context context) {
		TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	    return tMgr.getLine1Number();
	}
	
    private static Toast m_toast = null;
	public static void showToast(Context context, String text) {
		if (m_toast == null) {
			m_toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		m_toast.setText(text);
		m_toast.setDuration(Toast.LENGTH_SHORT);

		m_toast.show();
	}
}
