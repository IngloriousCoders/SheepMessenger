package com.ingloriouscoders.sheep;
import android.support.v4.view.ViewPager;

import android.app.ActionBar;
import java.util.List;
import android.app.ActionBar.Tab;

public class StartPageChange implements ViewPager.OnPageChangeListener
{
	List<Tab> mTabs;
	ActionBar mAb;
	public StartPageChange(ActionBar refering_ab,List<Tab> tabs_by_index)
	{
		mTabs = tabs_by_index;
		mAb = refering_ab;
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		if (position < mTabs.size() || position >= 0)
		{
			mAb.selectTab(mTabs.get(position));
		}
	}

}
