package com.example.miuscplayer.aidl;
interface OnProgressChanageListener{
	void onProgressChange(in int progress, in int max);
	void onBufferUpdateChange(in int buffer);
}