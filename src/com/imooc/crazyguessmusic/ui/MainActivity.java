package com.imooc.crazyguessmusic.ui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.imooc.crazyguessmusic.R;
import com.imooc.crazyguessmusic.R.color;
import com.imooc.crazyguessmusic.data.Const;
import com.imooc.crazyguessmusic.modle.IWordClickListener;
import com.imooc.crazyguessmusic.modle.Song;
import com.imooc.crazyguessmusic.modle.WordButton;
import com.imooc.crazyguessmusic.myUi.MyGridView;
import com.imooc.crazyguessmusic.util.RandomCharUitl;
import com.imooc.crazyguessmusic.util.ViewUtil;

public class MainActivity extends Activity implements OnClickListener,
		IWordClickListener {

	private Animation mPanAnim, mBar_in, mBar_out;
	private ImageButton mIbtn_play;
	private ImageView mImg_pan, mImg_bar;
	private MyGridView mMyGridView;
	// 已选择文字信息存储容器
	private ArrayList<WordButton> mWordSelected;
	// 是否正在播放的标志位
	private boolean isRunning = false;
	// 已选择文字显示容器
	private LinearLayout mSelectedContainer;

	private Timer mTimer;
	// 当前关的歌曲信息
	private Song mCurrentStageSong;
	// 当前关卡信息
	private int mCurrentStageIndex = -1;
	// 存储按钮数据的list
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
		mSelectedContainer = (LinearLayout) findViewById(R.id.selected_container);
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
		mMyGridView.setOnWordClickListener(this);
		// 初始化数据
		initCurrentStageData();
	}

	/**
	 * 初始化当前关卡
	 */
	public void initCurrentStageData() {
		// 更新已选择文字框数据
		updateSelectedContainer();
		// 获取数据
		mArrayList = getButtonList();
		// 更新MyGridView
		mMyGridView.updateData(mArrayList);
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
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				mIbtn_play.setVisibility(View.GONE);
				mTimer.cancel();
			}
		};
	};

	/**
	 * 点击播放按钮后的操作 通过Timer来实现点击后延迟0.5秒再隐藏按钮
	 */
	private void handlePlayButton() {
		mTimer = new Timer();
		if (!isRunning) {
			mImg_bar.startAnimation(mBar_in);

			// 隐藏播放按钮
			mTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					mHandler.sendEmptyMessage(1);
				}
			}, 600);
			isRunning = true;
		}
	}

	/**
	 * 更新当前关卡的已选择文字
	 */
	public void updateSelectedContainer() {
		mWordSelected = this.initSelectedWords();
		LayoutParams lp = new LayoutParams(-2, -2);
		// 设置水平居中
		mSelectedContainer.setGravity(Gravity.CENTER_HORIZONTAL);
		// 给LinearLayout设置View
		for (int i = 0; i < mWordSelected.size(); i++) {
			mSelectedContainer.addView(mWordSelected.get(i).getmButton(), lp);
		}
	}

	/**
	 * 初始化待选文字数据
	 * 
	 * @return
	 */
	public ArrayList<WordButton> getButtonList() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		// 获得所有待选文字 
		String[] words = generateWords();
		//设置歌曲名字
		for(int i = 0;i<MyGridView.SONG_NAMES_COUNT;i++){
			WordButton button = new WordButton();
			button.setmIndex(i);
			button.setmWordString(words[i]);
			data.add(button);
		}
		return data;
	}
	/**
	 * 初始化所有待选文字
	 * @return
	 */
	public String[] generateWords() {
		String[] words = new String[MyGridView.SONG_NAMES_COUNT];
		// 将歌曲名字添加进数组
		for (int i = 0; i < mCurrentStageSong.getSongNameLength(); i++) {
			words[i] = mCurrentStageSong.getCharacter()[i] + "";
		}
		// 将其他随机文字存入数组
		for (int i = mCurrentStageSong.getSongNameLength(); i < MyGridView.SONG_NAMES_COUNT; i++) {
			words[i] = RandomCharUitl.getRandomChar()+"";
		}
		return words;
	}

	/**
	 * 初始化已选择文字
	 * 
	 * @return
	 */
	public ArrayList<WordButton> initSelectedWords() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		mCurrentStageSong = getCurrentStageSong(++mCurrentStageIndex);
		for (int i = 0; i < mCurrentStageSong.getSongNameLength(); i++) {
			View view = ViewUtil.getInflatedView(MainActivity.this,
					R.layout.self_ui_gridview_item);
			WordButton holder = new WordButton();
			holder.setmButton((Button) view.findViewById(R.id.item_button));
			Button mButton = holder.getmButton();
			mButton.setTextColor(color.white);
			mButton.setText("");
			holder.setmIsVisiable(false);
			mButton.setBackgroundResource(R.drawable.game_wordblack);

			data.add(holder);
		}
		return data;
	}

	/**
	 * 获取当前关卡歌曲信息
	 * 
	 * @param mCurrentStageIndex2
	 * @return
	 */
	private Song getCurrentStageSong(int mCurrentStageIndex) {
		Song song = new Song();
		String[] songinfo = Const.SONG_INFO[mCurrentStageIndex];
		song.setSongFileName(songinfo[Const.INDEX_FILE_NAME]);
		song.setSongName(songinfo[Const.INDEX_SONG_NAME]);
		return song;
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

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}

	/**
	 * 播放器退出动画监听器
	 *
	 */
	class BarOutListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			// 播放杆动画完成后显示Play按钮
			isRunning = false;
			mIbtn_play.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}

	@Override
	protected void onPause() {
		mImg_pan.clearAnimation();
		mImg_bar.clearAnimation();
		super.onPause();
	}

	/**
	 * 待选框点击事件接口实现方法
	 */
	@Override
	public void onWordClick(WordButton wordButton) {
		wordButton.setmIsVisiable(false);
	}
}
