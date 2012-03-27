package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import android.view.Window;

import android.widget.ImageView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Vector;
import java.util.List;

import com.ingloriouscoders.chatbackend.ChatContext;
import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Conversation;
import com.ingloriouscoders.util.Debug;

import android.app.ActionBar;
import android.app.ActionBar.Tab;

import com.ingloriouscoders.util.Debug;

public class SheepMessenger extends FragmentActivity {
	
    /** Called when the activity is first created. */
	private ContactFragment mContactFragment = new ContactFragment();
	private GroupFragment mGroupFragment = new GroupFragment();
	
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
        viewpager_adp.addPage(mContactFragment);
        viewpager_adp.addPage(mGroupFragment);
   
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
    public void onPreferencesClicked(MenuItem item)
    {
    	Intent myIntent = new Intent(this,Preferences.class);
    	this.startActivityForResult(myIntent, 0);
    }
    public void onClickConnect(MenuItem item)
    {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	
    	ChatContext ctx = ChatContext.getChatContext(prefs.getString("account_username", ""), prefs.getString("account_password", ""));
    	Debug dbg = Debug.getInstance();
    	
    	if (!ctx.isInitiated())
    	{
    		dbg.out("Fehler beim initiieren:" + ctx.getLastErrorMessage());
    		return;
    	}
    	if (!ctx.connect())
    	{
    		dbg.out("Fehler beim verbinden:" + ctx.getLastErrorMessage());
    		return;
    	}
    	
    	if (mContactFragment.adp != null)
    	{
    	
    		for (int i=0;i<mContactFragment.adp.getCount();i++)
    		{
    			Contact ctc = mContactFragment.adp.mContacts.get(i);
    			Conversation newconv = Conversation.spawnConversation(ctc, ctx);
    			newconv.setOnUnreadMessagesListener(ctc.unreadListener);
    		}
    			
    	}
    	
    	dbg.out("Everything went good");
    }

    
}