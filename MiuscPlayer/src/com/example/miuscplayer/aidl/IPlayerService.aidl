package com.example.miuscplayer.aidl;
import com.example.miuscplayer.aidl.OnProgressChanageListener;
interface IPlayerService{
	void stop();
	void start();
	void seekTo(in int progress);
	int getPosition();
	int getDuration();
	void setOnProgressChanagerListener(in OnProgressChanageListener onProgressChanagerListener);
}