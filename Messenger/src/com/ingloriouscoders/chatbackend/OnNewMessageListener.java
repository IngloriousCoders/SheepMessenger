package com.ingloriouscoders.chatbackend;

import java.util.List;

public interface OnNewMessageListener {
	void onNewMessage(Conversation _conversation,Message _newmessage);
}
