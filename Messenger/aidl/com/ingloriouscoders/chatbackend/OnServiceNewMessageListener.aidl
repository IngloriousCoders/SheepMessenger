package com.ingloriouscoders.chatbackend;

import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.ServiceConversation;

interface OnServiceNewMessageListener
{

	void onNewMessage(in ServiceConversation _conversation,in Message recieved_message);

}