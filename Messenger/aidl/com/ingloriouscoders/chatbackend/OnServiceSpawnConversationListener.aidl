package com.ingloriouscoders.chatbackend;

import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.ServiceConversation;

interface OnServiceSpawnConversationListener
{

	void onSpawnConversation(in ServiceConversation _conversation);

}