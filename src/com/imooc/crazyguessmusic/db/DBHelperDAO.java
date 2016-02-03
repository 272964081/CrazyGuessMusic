package com.imooc.crazyguessmusic.db;

public interface DBHelperDAO {
	public void saveInfo(int stage,int coins);
	public int[] loadInfo();
}
