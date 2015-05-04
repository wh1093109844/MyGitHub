package com.example.miuscplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.miuscplayer.aidl.IPlayerService;
import com.example.miuscplayer.aidl.IPlayerService.Stub;
import com.example.miuscplayer.aidl.OnProgressChanageListener;

public class PlayerService extends Service{
	
	private MediaPlayer mPlayer;
	private OnProgressChanageListener onProgressChanageListener;
	private ProgressThread progressThread;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mPlayer = MediaPlayer.create(this, R.raw.test);
	}
	
	private IPlayerService.Stub mBinder = new Stub() {
		
		@Override
		public void stop() throws RemoteException {
			// TODO Auto-generated method stub
			mPlayer.pause();
			if(progressThread != null){
				progressThread.interrupt();
			}
		}
		
		@Override
		public void start() throws RemoteException {
			// TODO Auto-generated method stub
			mPlayer.start();
			if(progressThread != null){
				progressThread.interrupt();
			}
			progressThread = new ProgressThread();
			progressThread.start();
		}
		
		@Override
		public void setOnProgressChanagerListener(
				OnProgressChanageListener onProgressChanagerListener)
				throws RemoteException {
			// TODO Auto-generated method stub
			PlayerService.this.onProgressChanageListener = onProgressChanagerListener;
		}
		
		@Override
		public void seekTo(int progress) throws RemoteException {
			// TODO Auto-generated method stub
			mPlayer.seekTo(progress);
		}
		
		@Override
		public int getPosition() throws RemoteException {
			// TODO Auto-generated method stub
			return mPlayer.getCurrentPosition();
		}
		
		@Override
		public int getDuration() throws RemoteException {
			// TODO Auto-generated method stub
			return mPlayer.getDuration();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};

	public void onDestroy() {
		if (mPlayer != null) {
			mPlayer.release();
		}
	};
	
	/**
	 * 播放进度刷新线程
	 * 每隔100ms刷新一次播放进度
	 * @author wanghe
	 *
	 */
	class ProgressThread extends Thread{
		@Override
		public void run() {
			while(!isInterrupted()){
				int progress = mPlayer.getCurrentPosition();
				int max = mPlayer.getDuration();
				if(onProgressChanageListener != null){
					try {
						onProgressChanageListener.onProgressChange(progress, max);
						Thread.sleep(100);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
	}
}
