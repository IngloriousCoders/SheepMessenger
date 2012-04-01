package com.ingloriouscoders.chatbackend;


import android.util.Log;
import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;


public class Contact  implements Parcelable{
	
	private String username;
	private String showname;
	private String mAddress;
	private String photoURI = "";
	
	
	public Contact(String _username)
	{
		this.username = _username;
		this.showname = _username;
		
	}
	public Contact(String _username,String _showname)
	{
		this.username = _username;
		this.showname = _showname;
		
	}
	public Contact(String _username,String _showname,String photo_uri)
	{
		this.username = _username;
		this.showname = _showname;
		if (photo_uri != null)
		{
			this.photoURI = photo_uri;
		}
		
	}
	public Contact(String _username,String _showname,String photo_uri,String _address)
	{
		this.username = _username;
		this.showname = _showname;
		if (photo_uri != null)
		{
			this.photoURI = photo_uri;
		}
		mAddress = _address;
		
	}
	public void setShowName(String _showname)
	{
		this.showname = _showname;
	}
	public String getUsername()
	{
		return username;
	}
	public String getShowname()
	{
		return showname;
	}

	public String getPhotoURI()
	{
		return this.photoURI;
	}
	public String getAddress()
	{
		return mAddress;
	}
	public void setAddress(String _address)
	{
		this.mAddress = _address;
	}
	
	public static Contact getPlaceholder()
	{
		return new Contact("null","null name");
	}
	
	//Parcelable Zeugs
	

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
	   dest.writeString(username);
	   dest.writeString(showname);
	   dest.writeString(mAddress);


		
	}	
	public static final Parcelable.Creator<Contact> CREATOR
	    = new Parcelable.Creator<Contact>() {
	public Contact createFromParcel(Parcel in) {
	    return new Contact(in);
	}
	
	public Contact[] newArray(int size) {
	    return new Contact[size];
	}
	};
	
	protected Contact(Parcel in) {
		username = in.readString();
		showname = in.readString();
		mAddress = in.readString();

	}
	//Parcelable Zeugs ENDE
}
