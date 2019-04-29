package online.cszt0.japanesekeyboard;

import android.app.Application;

import online.cszt0.japanesekeyboard.util.Dictionary;

public class JKIME extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// initialize Dictionary
		Dictionary.init(this);
	}
}
