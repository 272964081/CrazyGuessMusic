package com.imooc.crazyguessmusic.ui;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.imooc.crazyguessmusic.R;
import com.imooc.crazyguessmusic.data.Const;
import com.imooc.crazyguessmusic.modle.IWordClickListener;
import com.imooc.crazyguessmusic.modle.Song;
import com.imooc.crazyguessmusic.modle.WordButton;
import com.imooc.crazyguessmusic.myUi.MyGridView;
import com.imooc.crazyguessmusic.util.RandomCharUitl;
import com.imooc.crazyguessmusic.util.ViewUtil;

public class MainActivity extends Activity implements OnClickListener,
		IWordClickListener {

	/** 检测答案不完整 */
	public static final int MSG_ANSWER_LACK = 1;
	/** 检测答案正确 */
	public static final int MSG_ANSWER_RIGHT = 2;
	/** 检测答案错误 */
	public static final int MSG_ANSWER_WRONG = 3;
	/** 取消播放按钮的显示 */
	public static final int MSG_DISPLAY_CANCEL = 4;
	/** 文字闪烁延迟信息 */
	public static final int MSG_DISPLAY_FLASH = 5;
	/** 文字闪烁次数 */
	public static final int WORDS_FLASH_COUNTS = 6;
	// 文字闪烁标志位
	private boolean isFlash;
	// 控件
	private Animation mPanAnim, mBar_in, mBar_out;
	private ImageButton mIbtn_play;
	private ImageView mImg_pan, mImg_bar;
	private MyGridView mMyGridView;
	private View mPassView;
	private TextView mTV_totalCoins;
	private ImageButton mBtn_delete, mBtn_tips;
	//当前关索引
	private TextView mTV_StageIndex;
	//当前关过关关数,过关歌曲
	private TextView mPassStageIndex, mPassSongName;
	//过关界面,下一关按钮
	private ImageButton mBtn_nextStage;
	// 是否正在播放的标志位
	private boolean isRunning = false;
	// 已选择文字显示容器
	private LinearLayout mSelectedContainer;

	private Timer mTimer_flash;
	// 当前关的歌曲信息
	private Song mCurrentStageSong;
	// 当前关卡信息
	private int mCurrentStageIndex = -1;
	// 已选择文字信息存储容器
	private ArrayList<WordButton> mWordSelected;
	// 非答案按钮
	private ArrayList<WordButton> mNotAnswerList;
	// 答案按钮存储容器
	private ArrayList<WordButton> mIsAnswerList;
	// 全部按钮文字信息容器
	private ArrayList<WordButton> mAllForSelectList = new ArrayList<WordButton>();
	// 玩家初始金币总额
	private int mCurrentCoins = Const.TOTAL_COINS;

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
		mTV_totalCoins = (TextView) findViewById(R.id.tv_coins);
		mBtn_delete = (ImageButton) findViewById(R.id.btn_delete_word);
		mBtn_tips = (ImageButton) findViewById(R.id.btn_tip);
		mTV_StageIndex = (TextView) findViewById(R.id.tv_currentStage);
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
		mBtn_delete.setOnClickListener(this);
		mBtn_tips.setOnClickListener(this);
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
		mAllForSelectList = getButtonList();
		// 更新MyGridView
		mMyGridView.updateData(mAllForSelectList);
		// 更新金币信息
		mTV_totalCoins.setText(mCurrentCoins + "");
		// 区分所有按钮
		initAllWordButton();
		//更新当前关索引
		mTV_StageIndex.setText(mCurrentStageIndex+1+"");
	}

	/**
	 * 界面按钮点击事件的实现
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_play:
			// 点击播放后的操作
			handlePlay();

			break;
		case R.id.btn_delete_word:
			// 点击删除错误答案按钮
			handleDelete();
			break;
		case R.id.btn_tip:
			// 点击提示按钮
			handleTipAnswer();
			break;
		}
	}

	/**
	 * 处理点击播放后的事件
	 */
	private void handlePlay() {
		// TODO Auto-generated method stub
		mImg_bar.startAnimation(mBar_in);
	}

	// 提示的最多次数
	int MaxTimes = 0;

	/**
	 * 处理删除错误答案事件
	 */
	private void handleDelete() {

		// 最大可删除文字数量
		int maxCounts = MyGridView.SONG_NAMES_COUNT
				- mCurrentStageSong.getSongNameLength();
		if (MaxTimes < maxCounts) {
			// 减少金币
			if (!handleCoins(-getDeleteCoins())) {
				// TODO 金币不足,弹出对话框
				return;
			}
			// 删除一个非答案的文字
			deleteNotAnwserWord();
			MaxTimes++;
		} else {
			// TODO 超出提示范围
		}
	}

	/**
	 * 设置一个随机的非答案按钮可见性为不可见
	 */
	private void deleteNotAnwserWord() {
		// 设置一个随机按钮为不可见
		while (true) {
			int ran = RandomCharUitl.getRandomInt(0, mNotAnswerList.size());
			Log.i("lang", "random:" + ran);
			WordButton buf = mNotAnswerList.get(ran);
			if (buf.getmButton().getVisibility() == View.VISIBLE) {
				setButtonVisibility(buf, View.INVISIBLE);
				break;
			}
		}

	}

	/**
	 * 区分所有待选按钮信息
	 */
	private void initAllWordButton() {
		// 当前歌曲名数组
		char[] mCurrentSongName = mCurrentStageSong.getCharacter();
		// 获取所有非答案的按钮
		mNotAnswerList = new ArrayList<WordButton>();
		mIsAnswerList = new ArrayList<WordButton>();
		mNotAnswerList.addAll(mAllForSelectList);
		for (int i = 0; i < mNotAnswerList.size(); i++) {
			for (int j = 0; j < mCurrentSongName.length; j++) {
				if (mNotAnswerList.get(i).getmWordString()
						.contains(mCurrentSongName[j] + "")) {
					// 将是答案的按钮对象添加进答案容器
					mIsAnswerList.add(mNotAnswerList.get(i));
					// 将不是答案的按钮对象从非答案容器中移除
					mNotAnswerList.remove(i);
				}
			}
		}
	}

	/**
	 * 处理点击提示按钮事件
	 */
	private void handleTipAnswer() {

		// 找到可用的空缺位置
		boolean isFindPosition = false;
		// 添加按钮进已选文字
		for (int i = 0; i < mWordSelected.size(); i++) {
			if (mWordSelected.get(i).getmWordString().length() == 0) {
				// 扣除金币
				if (!handleCoins(-getTipsCoins())) {
					return;
				}
				// 如果空缺,点击按钮
				WordButton button = findButton(i);
				if (button.getmButton().getVisibility() == View.VISIBLE) {
					onWordClick(button);
				} else {
					Toast.makeText(MainActivity.this, "答案已被选择",
							Toast.LENGTH_SHORT).show();
				}
				isFindPosition = true;
				break;
			}
		}
		// 如果没有找到可用位置,则闪烁提醒
		if (!isFindPosition) {
			flashDelayMessage();
		}
	}

	/**
	 * 查找和正确答案对应的按钮
	 * 
	 * @param index
	 *            第几个待填入按钮
	 * @return 对应的WordButton
	 */
	private WordButton findButton(int index) {
		for (int i = 0; i < mIsAnswerList.size(); i++) {
			if (mIsAnswerList.get(i).getmWordString()
					.equals(mCurrentStageSong.getCharacter()[index] + "")) {
				return mIsAnswerList.get(i);
			}
		}
		return null;
	}

	/**
	 * 处理金币增减的操作
	 * 
	 * @param data
	 *            增减数量,负数代表减
	 * @return true 增/减成功,false 失败
	 */
	private boolean handleCoins(int data) {
		if (mCurrentCoins + data >= 0) {
			mCurrentCoins += data;
			mTV_totalCoins.setText(mCurrentCoins + "");
			return true;
		} else {
			// 金币不足
			return false;
		}
	}

	/**
	 * 从配置文件读取删除需要的金币
	 * 
	 * @return
	 */
	private int getDeleteCoins() {
		return this.getResources().getInteger(R.integer.pay_delete_word);
	}

	/**
	 * 从配置文件读取提示需要的金币 config.xml
	 * 
	 * @return
	 */
	private int getTipsCoins() {
		return this.getResources().getInteger(R.integer.pay_tip_anwser);
	}

	/**
	 * 用来实现更新UI
	 */
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case MSG_ANSWER_LACK:

				// 答案不完整
				for (int i = 0; i < mWordSelected.size(); i++) {
					mWordSelected.get(i).getmButton().setTextColor(Color.WHITE);
				}
				break;
			case MSG_ANSWER_RIGHT:
				// 答案正确
				handlePassEvent();
				break;
			case MSG_ANSWER_WRONG:
				// 答案错误，发送文字闪烁信息
				// 初始化标志位
				isFlash = false;
				flashDelayMessage();
				break;
			case MSG_DISPLAY_FLASH:
				// 实现文字闪烁
				flashWord();
				break;
			}
		};
	};

	/**
	 * 过关事件处理
	 */
	private void handlePassEvent() {
		mPassView = findViewById(R.id.passView);
		if (mPassView != null) {
			// 显示过关索引和歌曲名
			mPassView.setVisibility(View.VISIBLE);
			//初始化控件
			mPassStageIndex = (TextView) mPassView
					.findViewById(R.id.tv_pass_index);
			mPassStageIndex.setText(mCurrentStageIndex + 1 + "");
			mBtn_nextStage = (ImageButton) mPassView.findViewById(R.id.imgBtn_next_stage);
			//设置参数
			mPassSongName = (TextView) mPassView
					.findViewById(R.id.tv_stage_songName);

			mPassSongName.setText(mCurrentStageSong.getmSongName());
			//添加点击事件
			mBtn_nextStage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isPassApp()){
						//TODO 通关界面
					}else{
						//TODO 加载下一关
						mPassView.setVisibility(View.GONE);
						initCurrentStageData();
					}
				}
			});
		}

	}
	
	private boolean isPassApp(){
		return mCurrentStageIndex==Const.SONG_INFO.length;
	}

	/**
	 * 文字闪烁实现方法
	 */
	private void flashWord() {
		for (int i = 0; i < mWordSelected.size(); i++) {
			mWordSelected.get(i).getmButton()
					.setTextColor(isFlash ? Color.RED : Color.WHITE);
		}
		isFlash = !isFlash;
	}

	/**
	 * 文字闪烁发送方法；
	 */
	private void flashDelayMessage() {
		mTimer_flash = new Timer();
		mTimer_flash.schedule(new TimerTask() {
			int counts = 0;

			@Override
			public void run() {
				if (++counts > WORDS_FLASH_COUNTS) {
					mTimer_flash.cancel();
					return;
				}
				mHandler.sendEmptyMessage(MSG_DISPLAY_FLASH);
			}
		}, 0, 150);
	}

	/**
	 * 更新当前关卡的已选择文字
	 */
	public void updateSelectedContainer() {
		mWordSelected = this.initSelectedWords();
		LayoutParams lp = new LayoutParams(-2, -2);
		// 设置水平居中
		mSelectedContainer.setGravity(Gravity.CENTER_HORIZONTAL);
		mSelectedContainer.removeAllViews();
		// 给LinearLayout设置View
		for (int i = 0; i < mWordSelected.size(); i++) {
			Button btn = mWordSelected.get(i).getmButton();
			btn.setTextColor(Color.WHITE);
			mSelectedContainer.addView(btn, lp);
		}
	}

	/**
	 * 初始化已选文字数据
	 * 
	 * @return
	 */
	public ArrayList<WordButton> getButtonList() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		// 获得所有待选文字
		String[] words = generateWords();
		// 设置歌曲名字
		for (int i = 0; i < MyGridView.SONG_NAMES_COUNT; i++) {
			WordButton button = new WordButton();
			button.setmIndex(i);
			button.setmWordString(words[i]);
			data.add(button);
		}
		return data;
	}

	/**
	 * 初始化所有待选文字
	 * 
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
			words[i] = RandomCharUitl.getRandomChar() + "";
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
			final WordButton holder = new WordButton();
			holder.setmButton((Button) view.findViewById(R.id.item_button));
			final Button mButton = holder.getmButton();
			mButton.setTextColor(Color.WHITE);
			mButton.setText("");
			holder.setmIsVisiable(false);
			mButton.setBackgroundResource(R.drawable.game_wordblack);

			mButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!"".equals(mButton.getText())) {
						// 清除已选文字
						clearTheAnswer(holder);
						// 调用此方法，为将文字颜色还原成白色
						checkTheAnswer();
					}
				}
			});
			data.add(holder);
		}
		return data;
	}

	/**
	 * 清除已选文字
	 * 
	 * @param wordButton
	 */
	protected void clearTheAnswer(WordButton wordButton) {
		// 隐藏被点击的已选答案
		wordButton.getmButton().setText("");
		wordButton.setmIsVisiable(false);
		wordButton.setmWordString("");
		// 显示原来的按钮
		setButtonVisibility(mAllForSelectList.get(wordButton.getmIndex()),
				View.VISIBLE);
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
			mIbtn_play.setVisibility(View.GONE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}

	/**
	 * 播放杆退出动画监听器
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
		// 点击待选区按钮，将文字设置到已选框中，并且属性为可见
		setSelectedWord(wordButton);
		// 检测答案状态
		checkTheAnswer();

	}

	private void setSelectedWord(WordButton button) {
		// 遍历已选文字框，如果没有赋值，则赋予
		for (int i = 0; i < mCurrentStageSong.getSongNameLength(); i++) {
			WordButton selectedButton = mWordSelected.get(i);
			if (selectedButton.getmWordString().length() == 0) {
				setWordButtonInfo(button, selectedButton);
				break;
			}
		}
	}

	/**
	 * 设置Button
	 * 
	 * @param button
	 * @param selectedButton
	 */
	private void setWordButtonInfo(WordButton button, WordButton selectedButton) {
		// 设置Button文字信息
		selectedButton.getmButton().setText(button.getmWordString());
		// 初始化文字颜色为白色
		selectedButton.getmButton().setTextColor(Color.WHITE);
		// 设置WordButton的属性
		selectedButton.setmWordString(button.getmWordString());
		// 设置wordButton索引
		selectedButton.setmIndex(button.getmIndex());
		// 设置Button可见性
		setButtonVisibility(button, View.VISIBLE);
		// 设置被点击Button不可见
		setButtonVisibility(button, View.INVISIBLE);
	}

	/**
	 * 设置WordButton可见性；
	 * 
	 * @param button
	 * @param visible
	 */
	private void setButtonVisibility(WordButton button, int visible) {
		// 设置按钮可见性
		button.getmButton().setVisibility(visible);
		// 设置按钮对象可见性属性
		button.setmIsVisiable(visible == View.VISIBLE ? true : false);
	}

	private void checkTheAnswer() {
		// 1.检测答案是否完整
		for (int i = 0; i < mWordSelected.size(); i++) {
			if (mWordSelected.get(i).getmWordString().length() == 0) {
				// 发送不完整消息
				mHandler.sendEmptyMessage(MSG_ANSWER_LACK);
				return;
			}
		}
		// 2.判断答案是否正确
		// 2.1提取答案
		StringBuffer sb = new StringBuffer();
		for (WordButton wordButton : mWordSelected) {
			sb.append(wordButton.getmWordString());
		}
		// 2.2比较答案
		if (sb.toString().equals(mCurrentStageSong.getmSongName())) {
			// 发送正确消息
			mHandler.sendEmptyMessage(MSG_ANSWER_RIGHT);
		} else {
			// 发送错误消息
			mHandler.sendEmptyMessage(MSG_ANSWER_WRONG);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
