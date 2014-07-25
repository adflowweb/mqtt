package kr.co.adflow.push;

import android.app.Application;

/**
 * @author nadir93
 * @date 2014. 7. 25.
 */
public class PushApp extends Application {

	public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void activityResumed() {
		activityVisible = true;
	}

	public static void activityPaused() {
		activityVisible = false;
	}

	private static boolean activityVisible;

}
