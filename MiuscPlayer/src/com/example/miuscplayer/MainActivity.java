package com.example.miuscplayer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.miuscplayer.aidl.IPlayerService;
import com.example.miuscplayer.aidl.IPlayerService.Stub;
import com.example.miuscplayer.aidl.OnProgressChanageListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";

	@ViewById(R.id.button_play)
	Button palyerButton;

	@ViewById(R.id.seek_bar)
	SeekBar mSeekBar;

	@ViewById(R.id.date_text)
	TextView mTextView;

	@ViewById(R.id.scroll_layout)
	ScrollLinearLayout scrollLayout;

	@ViewById(R.id.list_view)
	MyListView mListView;

	@ViewById(R.id.my_image_view)
	SimpleDraweeView mDraweeView;

	@SystemService
	NotificationManager mNotificationManager;
	
	private int startProgressTemp;
	private int stopProgressTemp;
	private int temp_direction;
	private boolean isTouch = false;
	private ServiceConnection serviceConnection;
	private IPlayerService.Stub mBinder;

	private VelocityTracker mVelocityTracker;

	@AfterViews
	public void init(){

		palyerButton.setEnabled(false);
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
						}

						@Override
						public void onBufferUpdateChange(int buffer) throws RemoteException {

						}
					});
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Intent intent = new Intent(this, PlayerService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

		final ArrayList<String> list = new ArrayList<>();
		for(int i = 1; i <= 50; i++){
//			TextView t = new TextView(this);
			list.add(i + "、测试");
//			scrollLayout.addView(t, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
		}
		final ImageListAdapter adapter = new ImageListAdapter(this, android.R.layout.simple_list_item_1, new PerfListener());
		mListView.setAdapter(adapter);
		mListView.setOnDeleteListener(new MyListView.OnDeleteListener() {

			@Override
			public void onDeleter(int position, AdapterView listView) {
				Log.d(TAG, "positon:" + position + "   " + list.get(position));
				list.remove(position);
				adapter.notifyDataSetChanged();
			}
		});

		Uri uri = Uri.parse("http://s1.dwstatic.com/group1/M00/A7/4B/5f3cfda08c56b855e96fe831f2b7fe3e.gif");
		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).build();

		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setImageRequest(request)
				.setAutoPlayAnimations(true).build();
		mDraweeView.setController(controller);
//		mDraweeView.setImageURI(uri);
	}

	@Click(R.id.button_play)
	public void play(View v){
		if(v.getTag() == null){
			try {
				mBinder.start();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			v.setTag(new Object());
			v.setBackgroundResource(R.drawable.ic_media_pause);
		}else{
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

	@SeekBarTouchStart(R.id.seek_bar)
	public void onStopTrackingTouch(SeekBar seekBar) {
		Log.d(TAG, "onStopTrackingTouch");
		stopProgressTemp = seekBar.getProgress();
		if(stopProgressTemp > startProgressTemp){
			temp_direction = 1;
		}else{
			temp_direction = -1;
		}
		try {
			mBinder.seekTo(stopProgressTemp);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isTouch = false;
	}

	@SeekBarTouchStop(R.id.seek_bar)
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStartTrackingTouch");
		startProgressTemp = seekBar.getProgress();
		isTouch = true;
	}

	@SeekBarProgressChange(R.id.seek_bar)
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Log.d(TAG, "progress:" + progress + "   fromUser:" + fromUser);
		mTextView.setText(getTime(progress) + " / " + getTime(seekBar.getMax()));
		startProgressTemp = 0;
		stopProgressTemp = 0;
		temp_direction = 0;
	}

	@UiThread
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
			RemoteViews v = new RemoteViews(getPackageName(), R.layout.notification_layout);
			v.setTextViewText(R.id.notification_text, "通知测试！");
			v.setImageViewResource(R.id.notification_image, R.drawable.image089);

			Notification n = new Notification(R.drawable.image089, "Notification", System.currentTimeMillis());
			n.flags = Notification.FLAG_AUTO_CANCEL;
			Intent intent = new Intent(this, MainActivity_.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			n.contentIntent = pi;
			n.contentView = v;

			mNotificationManager.notify(1, n);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	class ImageListAdapter extends ArrayAdapter<String> {

		private final PerfListener mPerfListener;

		public ImageListAdapter(Context context, int resource, PerfListener perfListener) {
			super(context, resource);
			mPerfListener = perfListener;
		}

		@Override
		public int getCount() {
			return Const.IMAGE_URL_LIST.size();
		}

		@Override
		public String getItem(int position) {
			return Const.IMAGE_URL_LIST.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		private int calcDesiredSize(int parentWidth, int parentHeight) {
			int orientation = getContext().getResources().getConfiguration().orientation;
			int desiredSize = (orientation == Configuration.ORIENTATION_LANDSCAPE) ?
					parentHeight / 2 : parentHeight / 3;
			return Math.min(desiredSize, parentWidth);
		}

		private void updateViewLayoutParams(View view, int width, int height) {
			ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
			if (layoutParams == null || layoutParams.height != width || layoutParams.width != height) {
				layoutParams = new AbsListView.LayoutParams(width, height);
				view.setLayoutParams(layoutParams);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			com.example.miuscplayer.InstrumentedDraweeView view = convertView != null ? (com.example.miuscplayer.InstrumentedDraweeView) convertView : createView();

			int size = calcDesiredSize(parent.getWidth(), parent.getHeight());
			updateViewLayoutParams(view, parent.getWidth(), 800);

			String uri = getItem(position);
			view.initInstrumentation(uri, mPerfListener);
			bind(view, uri);
			return view;
		}

		protected InstrumentedDraweeView createView() {
			GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(getResources())
					.setPlaceholderImage(Drawables.sPlaceholderDrawable)
					.setFailureImage(Drawables.sErrorDrawable)
					.setProgressBarImage(new ProgressBarDrawable())
					.build();
			InstrumentedDraweeView view = new InstrumentedDraweeView(MainActivity.this, gdh);
			view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
			return view;
		}

		protected void bind(final InstrumentedDraweeView view, String uri) {
//			ImageRequest imageRequest =
//					ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//							.setResizeOptions(
//									new ResizeOptions(view.getLayoutParams().width, view.getLayoutParams().height))
//							.build();
			ImageRequest imageRequest = ImageRequest.fromUri(uri);
			DraweeController draweeController = Fresco.newDraweeControllerBuilder()
					.setImageRequest(imageRequest)
					.setOldController(view.getController())
					.setControllerListener(view.getListener())
					.setAutoPlayAnimations(true)
					.build();
			view.setController(draweeController);
		}
	}
}
