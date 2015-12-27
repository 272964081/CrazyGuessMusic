package com.imooc.crazyguessmusic.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RandomCharUitl {
	/**
	 * 获取随机常用汉字
	 * @return 单个汉字
	 */
	public static char getRandomChar(){
		String str = "";
		int heightPos ;
		int lowPos;
		
		Random random = new Random();
		
		heightPos = 176+ Math.abs(random.nextInt(39));
		lowPos = 161+Math.abs(random.nextInt(93));
		
		byte[] b = new byte[2];
		b[0] = Integer.valueOf(heightPos).byteValue();
		b[1] = Integer.valueOf(lowPos).byteValue();
		
		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return str.charAt(0);
	}

}
