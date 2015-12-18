package com.imooc.crazyguessmusic.modle;

import android.widget.Button;

public class WordButton {
	private int mIndex;
	private String mWordString;
	private boolean mIsVisiable;
	private Button mButton;

	public WordButton() {
		mWordString = "";
		mIsVisiable = true;
	}

	public int getmIndex() {
		return mIndex;
	}

	public void setmIndex(int mIndex) {
		this.mIndex = mIndex;
	}

	public String getmWordString() {
		return mWordString;
	}

	public void setmWordString(String mWordString) {
		this.mWordString = mWordString;
	}

	public boolean ismIsVisiable() {
		return mIsVisiable;
	}

	public void setmIsVisiable(boolean mIsVisiable) {
		this.mIsVisiable = mIsVisiable;
	}

	public Button getmButton() {
		return mButton;
	}

	public void setmButton(Button mButton) {
		this.mButton = mButton;
	}
	
	

}
