package com.ingloriouscoders.util;

import android.util.Log;
import android.widget.Toast;
import android.content.Context;

public class Debug {
	private static Debug mInstance;
	private Context mContext;
	
	public enum debug_level {
		LOG,TOAST
	}
	
	private debug_level level;
	
	public static Debug getInstance(Context _ctx)
	{
		if (mInstance == null)
		{
			mInstance = new Debug();
		}
		mInstance.mContext = _ctx;
		return mInstance;
	}
	
	public static Debug getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new Debug();
		}
		return mInstance;
	}
	private Debug()
	{
		this.level = debug_level.LOG;
	}
	public void setLevel(debug_level _level)
	{
		this.level = _level;
	}
	public boolean out(String msg)
	{
		if (level == debug_level.LOG)
		{
			Log.v("inglourious-debug-context",msg);
			return true;
		}
		else if (level == debug_level.TOAST)
		{
			if (this.mContext == null)
			{
				Log.v("inglourious-debug-context","WARNING: Toast-Context not avaible, switching back to Log");
				Log.v("inglourious-debug-context",msg);
				return true;
			}
			else
			{
				Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
				return true;
			}
		}

		return false;
		
		
	}
}
