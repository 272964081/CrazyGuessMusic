package com.imooc.crazyguessmusic.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class ViewUtil {
	/**
	 * 返回适配器视图
	 * @param context
	 * @param itemId
	 * @return
	 */
	public static View getInflatedView(Context context,int resoure){
		return LayoutInflater.from(context).inflate(resoure, null);
	}
}
