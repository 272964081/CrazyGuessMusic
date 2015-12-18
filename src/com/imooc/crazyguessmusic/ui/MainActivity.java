package com.imooc.crazyguessmusic.ui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.imooc.crazyguessmusic.R;
import com.imooc.crazyguessmusic.modle.WordButton;
import com.imooc.crazyguessmusic.myUi.MyGridView;

public class MainActivity extends Activity implements OnClickListener {

	private Animation mPanAnim, mBar_in, mBar_out;
	private ImageButton mIbtn_play;
	private ImageView mImg_pan, mImg_bar;
	private MyGridView mMyGridView;
	//是否正在播放的标志位
	private boolean isRunning = false;
	
	private Timer mTimer;
	//存储按钮数据的list
	private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化控件
		mIbtn_play = (ImageButton) findViewById(R.id.btn_play);
		mImg_pan = (ImageView) findViewById(R.id.img_pan);
		mImg_bar = (ImageView) findViewById(R.id.img_bar);
		mMyGridView = (MyGridView) findViewById(R.id.myGridView);
		// 初始化动画
		// 盘片动画
		mPanAnim = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.rotate_pan);
		mPanAnim.setInterpolator(new LinearInterpolator());
		mPanAnim.setAnimationListener(new PanListener());
		// 播放杆动画
		mBar_in = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.rotate_bar_in);
		mBar_in.setFillAfter(true);
		mBar_in.setAnimationListener(new BarInListener());
		mBar_out = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.rotate_bar_out);
		mBar_out.setAnimationListener(new BarOutListener());
		// 设置点击事件
		mIbtn_play.setOnClickListener(this);
		//初始化数据
		initCurrentStageData();
	}
	/**
	 * 按钮点击事件的实现
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_play:
			handlePlayButton();
			break;
		}
	}
	/**
	 * 用来实现更新UI
	 */
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				mIbtn_play.setVisibility(View.GONE);
				mTimer.cancel();
			}
		};
	};

	/**
	 * 点击播放按钮后的操作
	 * 通过Timer来实现点击后延迟0.5秒再隐藏按钮
	 */
	private void handlePlayButton() {
		mTimer = new Timer();
		if(!isRunning){
			mImg_bar.startAnimation(mBar_in);
			//隐藏播放按钮
			mTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					mHandler.sendEmptyMessage(1);
				}
			}, 600);
			isRunning = true;
		}
	}
	
	public void initCurrentStageData(){
		//获取数据
		mArrayList = getButtonList();
		//更新MyGridView
		mMyGridView.updateData(mArrayList);
	}
	
	public ArrayList<WordButton> getButtonList(){
		//获得所有待选文字 TODO
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		for(int i=0;i<24;i++){
			WordButton button = new WordButton();
			button.setmIndex(i);
			button.setmWordString("测");
			data.add(button);
		}
		return data;
	}

	/**
	 * 盘片动画监听器
	 */
	class PanListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			mImg_bar.startAnimation(mBar_out);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 播放杆进入动画监听器
	 *
	 */
	class BarInListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			mImg_pan.startAnimation(mPanAnim);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 播放器退出动画监听器
	 *
	 */
	class BarOutListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			//播放杆动画完成后显示Play按钮
			isRunning = false;
			mIbtn_play.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}
	
	@Override
	protected void onPause() {
		mImg_pan.clearAnimation();
		mImg_bar.clearAnimation();
		super.onPause();
	}
}
