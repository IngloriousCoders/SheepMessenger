package com.ingloriouscoders.sheep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChatServiceAutoStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, ChatService.class);
		context.startService(service);
	}
}