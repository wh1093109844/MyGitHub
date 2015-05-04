package com.example.miuscplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.miuscplayer.aidl.IPlayerService;
import com.example.miuscplayer.aidl.IPlayerService.Stub;
import com.example.miuscplayer.aidl.OnProgressChanageListener;


public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	private Button palyerButton; 
//	private MediaPlayer mPlayer;
	private SeekBar mSeekBar;
//	private SeekThread seekThreak;
	private TextView mTextView;
	
	private int startProgressTemp;
	private int stopProgressTemp;
	private int temp_direction;
	private boolean isTouch = false;
	private ServiceConnection serviceConnection;
	private IPlayerService.Stub mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        mPlayer = MediaPlayer.create(this, R.raw.test);
        palyerButton = (Button) findViewById(R.id.button_play);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mTextView = (TextView) findViewById(R.id.date_text);
        
//        mPlayer.setOnPreparedListener(new OnPreparedListener() {
//			
//			@Override
//			public void onPrepared(MediaPlayer mp) {
//				Log.d(TAG, "加载MP3完成！");
//				palyerButton.setEnabled(true);
//			}
//		});
//        mPlayer.prepareAsync();
        palyerButton.setEnabled(false);
        palyerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getTag() == null){
//					try {
//						mPlayer.prepare();
//					} catch (IllegalStateException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					mPlayer.start();
//					if(seekThreak != null){
//						seekThreak.interrupt();
//					}
//					seekThreak = new SeekThread();
//					seekThreak.start();
					try {
						mBinder.start();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					v.setTag(new Object());
					v.setBackgroundResource(R.drawable.ic_media_pause);
				}else{
//					mPlayer.pause();
//					if(seekThreak != null){
//						seekThreak.interrupt();
//					}
					try {
						mBinder.stop();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					v.setTag(null);
					v.setBackgroundResource(R.drawable.ic_media_play);
				}
			}
		});
        
//        mPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
//			
//			@Override
//			public void onBufferingUpdate(MediaPlayer mp, int percent) {
//				// TODO Auto-generated method stub
//				Log.d(TAG, "setOnBufferingUpdateListener:" + percent);
//			}
//		});
//        
//        mPlayer.setOnInfoListener(new OnInfoListener() {
//			
//			@Override
//			public boolean onInfo(MediaPlayer mp, int what, int extra) {
//				Log.d(TAG, "setOnInfoListener  what:" + what + "   extra:" + extra);
//				return false;
//			}
//		});
//        
//        mPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
//			
//			@Override
//			public void onSeekComplete(MediaPlayer mp) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
        
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d(TAG, "onStopTrackingTouch");
				stopProgressTemp = seekBar.getProgress();
				if(stopProgressTemp > startProgressTemp){
					temp_direction = 1;
				}else{
					temp_direction = -1;
				}
//				mPlayer.seekTo(stopProgressTemp);
				try {
					mBinder.seekTo(stopProgressTemp);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isTouch = false;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onStartTrackingTouch");
				startProgressTemp = seekBar.getProgress();
				isTouch = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Log.d(TAG, "progress:" + progress + "   fromUser:" + fromUser);
				mTextView.setText(getTime(progress) + " / " + getTime(seekBar.getMax()));
				startProgressTemp = 0;
				stopProgressTemp = 0;
				temp_direction = 0;
			}
		});
        
        serviceConnection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				mBinder = (Stub) service;
				palyerButton.setEnabled(true);
				try {
					mBinder.setOnProgressChanagerListener(new OnProgressChanageListener(){

						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void onProgressChange(int progress, int max)
								throws RemoteException {
							// TODO Auto-generated method stub
							Log.d(TAG, "progress:" + progress + "   max:" + max);
							updateSeekBar(progress, max);
						}});
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Intent intent = new Intent(this, PlayerService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
    public void updateSeekBar(int progress, int max){
    	if(isTouch){
    		return;
    	}
    	if(temp_direction > 0){
    		if(progress < stopProgressTemp){
    			return;
    		}
    	}else if(temp_direction < 0){
    		if(progress > startProgressTemp){
    			return;
    		}
    	}
    	mSeekBar.setMax(max);
    	mSeekBar.setProgress(progress);
    }
    
    Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		updateSeekBar(msg.arg2, msg.arg1);
    		
    	};
    };
    
    public String getTime(int time){
    	StringBuffer sbf = new StringBuffer();
    	int hLong = (int)Math.pow(60, 2) * 1000;
    	int mLong = 60 * 1000;
    	int sLong = 1000;
    	int h = time / hLong;
    	time = time % hLong;
    	int m = time / mLong;
    	time = time % mLong;
    	int s = time / sLong;
    	if(h != 0){
    		if(h < 10){
    			sbf.append(0);
    		}
    		sbf.append(h).append(":");
    	}
    	if(m < 10){
    		sbf.append(0);
    	}
    	sbf.append(m).append(":");
    	if(s < 10){
    		sbf.append(0);
    	}
    	sbf.append(s);
    	return sbf.toString();
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unbindService(serviceConnection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
