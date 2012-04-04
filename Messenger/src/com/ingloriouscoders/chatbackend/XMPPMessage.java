package com.ingloriouscoders.chatbackend;

import java.util.Calendar;

import android.util.Log;

public class XMPPMessage extends org.jivesoftware.smack.packet.Message {
	static int INTERNAL, CHAT;
	static String PARAM_READ = "&markasread", PARAM_WRITING = "&iswriting", PARAM_ADMINTROLL = "&trollhard"; 
	
	private int msgtype;
	
	public XMPPMessage(int _messagetype) {
		if (_messagetype == 1) {
			this.msgtype = INTERNAL;
			this.setType(Type.error);
			this.setBody(null);
			this.setSubject("INTERNALMESSAGE");
		}
		else
			this.msgtype = CHAT;
	}
	
	public void setParam(String _param) {
		if (this.getType() == Type.error) {

			if (this.getSubject() == null) 
				this.setSubject(_param);
			else
				this.setSubject(this.getSubject() + _param);

		}
	}
	
	public void setTimestamp(int day, int month, int year, int hour, int minute, int second) {
		if (this.getSubject() == null)
			this.setSubject("&time=" + day + ";" + month + ";" + year + ";" + hour + ";" + minute + ";" + second);
		else
			this.setSubject(this.getSubject() + "&time=" + day + ";" + month + ";" + year + ";" + hour + ";" + minute + ";" + second);
	}
	
	public void setTimestamp() {
		Calendar c = Calendar.getInstance();
		String timestampStr = "&time=" + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + ";" + Integer.toString(c.get(Calendar.MONTH) + 1) + ";" + Integer.toString(c.get(Calendar.YEAR)) + ";" + Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ";" + Integer.toString(c.get(Calendar.MINUTE)) + ";" + Integer.toString(c.get(Calendar.SECOND));
		this.setSubject(timestampStr);
	}

	public void addParameters(String _params) {
		this.setSubject(_params);
	}
}
