package com.ingloriouscoders.sheep;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.app.ActionBar;
import android.app.ActionBar.Tab;


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
    public int addPage(Fragment obj)
    {
    	mPages.add(obj);
    	return mPages.size()-1;    	
    }
}
