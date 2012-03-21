package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;

import android.view.Window;

import android.widget.ImageView;

import android.view.Menu;
import android.view.MenuInflater;

import java.util.Vector;
import java.util.List;
import android.app.ActionBar;
import android.app.ActionBar.Tab;

public class SheepMessenger extends FragmentActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        
        
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);    
        
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
        
        ab.setDisplayShowTitleEnabled(false);
        
        setContentView(R.layout.main);
        
        
        
        StartAdapter viewpager_adp = new StartAdapter(getSupportFragmentManager(),this);
        viewpager_adp.addPage(ContactFragment.class.getName());
        viewpager_adp.addPage(GroupFragment.class.getName());
        
        ViewPager vp = (ViewPager)findViewById(R.id.viewpager);
        vp.setAdapter(viewpager_adp);

        
        Tab tab_contacts = ab.newTab();
        tab_contacts.setText(R.string.tab_contacts_label);
        Tab tab_groups = ab.newTab();
        
        tab_groups.setText(R.string.tab_groups_label);
        tab_contacts.setTabListener(new ActionBarTabListener(vp, 0));
        tab_groups.setTabListener(new ActionBarTabListener(vp, 1));
        ab.addTab(tab_contacts);
        ab.addTab(tab_groups);
        
        List<Tab> start_tabs = new Vector<Tab>();
        start_tabs.add(tab_contacts);
        start_tabs.add(tab_groups);
        StartPageChange vp_page_listener = new StartPageChange(getActionBar(), start_tabs);
        
        vp.setOnPageChangeListener(vp_page_listener);

   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start, menu);
        return true;
    }
    
}