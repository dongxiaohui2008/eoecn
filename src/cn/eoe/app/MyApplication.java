package cn.eoe.app;

import java.io.File;

import android.app.Application;

public class MyApplication extends Application {
	public static  File cacheDir;
	
	@Override
	public void onCreate() {		
		super.onCreate();
	}	
}
