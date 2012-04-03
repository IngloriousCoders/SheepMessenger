package com.ingloriouscoders.chatbackend;

import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.OnServiceNewMessageListener;

interface ServiceConversation
{
	Contact getOpposite();
	
	List<Message> getHistory();
	
	Message prepareMessage();
	
	boolean sendMessage(in Message _msg);
	
	void addOnServiceNewMessageListener(in OnServiceNewMessageListener _listener);
	void removeOnServiceNewMessageListener(in OnServiceNewMessageListener _listener);
	
	void setNotificationMuted(in boolean state);
	
}