package com.ingloriouscoders.sheep;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;

class ActionBarTabListener implements ActionBar.TabListener
{
	ViewPager mViewPager;
	int mIndex;
	public ActionBarTabListener(ViewPager parent_pager,int index)
	{
		mViewPager = parent_pager;
		mIndex = index;
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(mIndex);
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
}