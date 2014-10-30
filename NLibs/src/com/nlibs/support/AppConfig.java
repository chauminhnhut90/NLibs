package com.nlibs.support;

import android.app.Application;
import android.content.Context;

public class AppConfig extends Application {

	private Context mContext;
	
	static AppConfig mInstance;
	
	public AppConfig getInstance(Context c){
		if(null==mInstance){
			mInstance = new AppConfig(c);
		}
		return mInstance;
	}
	
	public AppConfig(Context c) {
		setContext(c.getApplicationContext());
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
}
