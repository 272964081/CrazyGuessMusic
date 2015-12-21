package com.imooc.crazyguessmusic.modle;

public class Song {
	// 歌曲名称
	private String mSongName;
	// 歌曲文件名称
	private String mSongFileName;
	// 歌曲长度
	private int mSongNameLength;
	
	/**
	 * 获取文件名称的单个字符的数组
	 * @return char[] 
	 */
	public char[] getCharacter(){
		return mSongName.toCharArray();
	}

	public String getmSongName() {
		return mSongName;
	}

	public void setSongName(String songName) {
		this.mSongName = songName;
		mSongNameLength = songName.length();
	}

	public String getSongFileName() {
		return mSongFileName;
	}

	public void setSongFileName(String songFileName) {
		this.mSongFileName = songFileName;
	}

	public int getSongNameLength() {
		return mSongNameLength;
	}

}
