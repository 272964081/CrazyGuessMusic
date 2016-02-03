package com.imooc.crazyguessmusic.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

public class MyPlayer {
	// 音乐播放器
	private static MediaPlayer mMusicMediaPlayer;
	// 音乐文件名
	private static final String[] TONE_NAMES = { "enter.mp3", "cancel.mp3",
			"coin.mp3" };
	// 音效播放器
	private static MediaPlayer[] mToneMediaPlayer = new MediaPlayer[TONE_NAMES.length];
	/**
	 * 确认音效
	 */
	public static final int TONE_INDEX_ENTER = 0;
	/**
	 * 取消音效
	 */
	public static final int TONE_INDEX_CANCEL = 1;
	/**
	 * 金币音效
	 */
	public static final int TONE_INDEX_COIN = 2;

	/**
	 * 音效播放器
	 * 
	 * @param context
	 * @param index
	 *            第几个音效
	 */
	public static void playTone(Context context, int index) {

		// 准备文件
		AssetManager assetManager = context.getAssets();
		if (mToneMediaPlayer[index] == null) {
			mToneMediaPlayer[index] = new MediaPlayer();
			try {
				AssetFileDescriptor fd = assetManager.openFd(TONE_NAMES[index]);
				mToneMediaPlayer[index].setDataSource(fd.getFileDescriptor(),
						fd.getStartOffset(), fd.getLength());
				mToneMediaPlayer[index].prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 播放文件
		mToneMediaPlayer[index].start();

	}

	/**
	 * 播放音乐
	 * 
	 * @param context
	 * @param fileName
	 */
	public static void playSong(Context context, String fileName) {

		// 播放器单例模式
		if (mMusicMediaPlayer == null) {
			mMusicMediaPlayer = new MediaPlayer();
		}
		// 重置播放器
			mMusicMediaPlayer.reset();
		try {
			// 查找文件
			AssetManager mAssetManager = context.getAssets();
			AssetFileDescriptor fd = mAssetManager.openFd(fileName);
			mMusicMediaPlayer.setDataSource(fd.getFileDescriptor(),
					fd.getStartOffset(), fd.getLength());
			// 进入准备状态
			mMusicMediaPlayer.prepare();
			// 播放音乐
			mMusicMediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 停止播放
	 * 
	 * @param context
	 */
	public static void stopSong(Context context) {
		if (mMusicMediaPlayer != null) {
			mMusicMediaPlayer.stop();
		}
	}
	
	public static void releasePlayer(){
		if(mMusicMediaPlayer!=null){
			mMusicMediaPlayer.release();
			mMusicMediaPlayer = null;
		}
		for (int i = 0; i < mToneMediaPlayer.length; i++) {
			if(mToneMediaPlayer[i]!=null){
				mToneMediaPlayer[i].release();
				mToneMediaPlayer[i] = null;
			}
		}
	}

}
