package com.ingloriouscoders.chatbackend;

public interface OnUnreadMessagesListener {
	void onNewMessage(Contact _contact,int new_value);
}
