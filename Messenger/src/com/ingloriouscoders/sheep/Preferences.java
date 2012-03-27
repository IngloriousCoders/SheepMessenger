package com.ingloriouscoders.sheep;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.content.SharedPreferences;
import android.os.Bundle;


public class Preferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference.OnPreferenceChangeListener standard_text_listener = new Preference.OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				((EditTextPreference)preference).setText((String)newValue);
				return false;
			}
		};
		
		
		
		EditTextPreference account_username = (EditTextPreference)this.findPreference("account_username");
		account_username.setSummary(account_username.getText());
		account_username.setOnPreferenceChangeListener(standard_text_listener);
		
		EditTextPreference server_address = (EditTextPreference)this.findPreference("server_address");
		server_address.setSummary(server_address.getText());
		server_address.setOnPreferenceChangeListener(standard_text_listener);
		
		
	}
	
}
