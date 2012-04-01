package com.ingloriouscoders.chatbackend;

import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.OnServiceNewMessageListener;
import com.ingloriouscoders.chatbackend.ServiceConversation;
import com.ingloriouscoders.chatbackend.OnServiceSpawnConversationListener;

interface ServiceChatContext
{
	ServiceConversation spawnConversation(in Contact opposite);
	
	void setStatus(in String status);
	
	void addOnServiceSpawnConversationListener(in OnServiceSpawnConversationListener listener);
	void removeOnServiceSpawnConversationListener(in OnServiceSpawnConversationListener listener);	
}