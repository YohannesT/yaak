/*
 * 
 * |\     /|(  ___  )(  ___  )| \    /\
 * ( \   / )| (   ) || (   ) ||  \  / /
 *  \ (_) / | (___) || (___) ||  (_/ / 
 *   \   /  |  ___  ||  ___  ||   _ (  
 *    ) (   | (   ) || (   ) ||  ( \ \ 
 *    | |   | )   ( || )   ( ||  /  \ \
 *    \_/   |/     \||/     \||_/    \/
 * 
 * Yet another Accessible Keyboard
 * Author: Sebastian & David
 * Coding Event SS12
 */


package at.starcoders.yaak;

import android.util.Log;

/** Abstraction over standard Android logger (Log). */
public class Logger {
	
	private final static String TAG = "YAAK";
	
	public final static int VERBOSE = 1;
	public final static int DEBUG = 2;
	public final static int INFORMATION = 3;
	public final static int WARNING = 4;
	public final static int ERROR = 5;

	/** default logging is level DEBUG */
	public static void log(String msg) {
		Log.d(TAG, msg);
	}
	
	/** default logging is level DEBUG */
	public static void log(String msg, Throwable tr) {
		Log.d(TAG, msg, tr);
	}
	
	public static void log(String msg, int level) {
		switch (level) {
		case VERBOSE: logv(msg); break;
		case DEBUG: logd(msg); break;
		case INFORMATION: logi(msg); break;
		case WARNING: logw(msg); break;
		case ERROR: loge(msg); break;
		}
	}
	
	public static void logv(String msg) {
		Log.v(TAG, msg);
	}
	
	public static void logv(String msg, Throwable tr) {
		Log.v(TAG, msg, tr);
	}
	
	public static void logd(String msg) {
		Log.d(TAG, msg);
	}
	
	public static void logd(String msg, Throwable tr) {
		Log.d(TAG, msg, tr);
	}
	
	public static void logi(String msg) {
		Log.i(TAG, msg);
	}
	
	public static void logi(String msg, Throwable tr) {
		Log.i(TAG, msg, tr);
	}
	
	public static void logw(String msg) {
		Log.w(TAG, msg);
	}
	
	public static void logw(String msg, Throwable tr) {
		Log.w(TAG, msg, tr);
	}
	
	public static void loge(String msg) {
		Log.e(TAG, msg);
	}
	
	public static void loge(String msg, Throwable tr) {
		Log.e(TAG, msg, tr);
	}
}
