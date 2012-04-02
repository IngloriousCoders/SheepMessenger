package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.content.Intent;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.PreferenceManager;

import android.view.Window;

import android.widget.ImageView;

import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;

import java.util.Vector;
import java.util.List;

import com.ingloriouscoders.chatbackend.ChatContext;
import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Conversation;
import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.ServiceChatContext;
import com.ingloriouscoders.chatbackend.ServiceConversation;
import com.ingloriouscoders.util.Debug;

import android.app.ActionBar;
import android.app.ActionBar.Tab;

import com.ingloriouscoders.util.Debug;
import android.widget.Toast;
import android.util.Log;



public class SheepMessenger extends FragmentActivity {
	
    /** Called when the activity is first created. */
	private ContactFragment mContactFragment;
	private GroupFragment mGroupFragment;
	protected ChatService mChatService;
	protected ServiceChatContext mServiceChatContext;
	private boolean connected = false;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContactFragment =  new ContactFragment();
        mGroupFragment = new GroupFragment();
        
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
        startChatService();

   }
    private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
		    SheepMessenger.this.mServiceChatContext = ServiceChatContext.Stub.asInterface(service);
		    if (mServiceChatContext == null)
		    {
		    	Log.v("","ChatContext = 0");
		    	return;
		    } 
		    if (!mContactFragment.fillGrid(mServiceChatContext))
		    {
			    mContactFragment.mListener = new ContactFragment.OnContactFragmentCreated() {
					
					@Override
					public void contactFragmentCreated(ContactFragment _frag) {
						Log.v("SheepMessener","Called");
						_frag.fillGrid(SheepMessenger.this.mServiceChatContext);					
					}
				};
		    }
		    SheepMessenger.this.connected = true;
	    	
			Toast.makeText(SheepMessenger.this, "Verbunden",
					Toast.LENGTH_SHORT).show();
		}

		public void onServiceDisconnected(ComponentName className) {
			mChatService = null;
			mServiceChatContext = null;
			SheepMessenger.this.connected = false;
		}
	};
    
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
    	startChatService();
    }
    public void startChatService()
    {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	Intent serviceIntent = new Intent(this, ChatService.class);
    	this.startService(serviceIntent);
    	
    	
    	bindService(serviceIntent, mConnection,0);
    	
    }
    @Override
    public void onPause()
    {
    	super.onPause();
    }
    public void onResume()
    {
    	super.onResume();
    	startChatService();
    }
    public void onDestroy()
    {
    	super.onDestroy();
    	
    	unbindService(mConnection);
    	Log.v("SheepMessenger","Destroyed");
    }

    
}