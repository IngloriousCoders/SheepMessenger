package com.ingloriouscoders.sheep;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.app.Activity;
import android.content.Context;

import java.util.List;
import java.util.Vector;

public class StartAdapter extends FragmentPagerAdapter {
	List<Fragment> mPages = new Vector<Fragment>();
	Context mContext;
	
    public StartAdapter(FragmentManager fm,Context _mContext) {
        super(fm);
        mContext = _mContext;
    }

    @Override
    public int getCount() {											
        return mPages.size();
    }
    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }
    public void addPage(String classname)
    {
    	mPages.add(Fragment.instantiate(mContext, classname));
    }
}

