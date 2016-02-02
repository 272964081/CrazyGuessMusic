package com.imooc.crazyguessmusic.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.imooc.crazyguessmusic.R;
import com.imooc.crazyguessmusic.data.Const;
import com.imooc.crazyguessmusic.modle.IAlertDialogClickListener;
import com.imooc.crazyguessmusic.ui.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.AvoidXfermode.Mode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewUtil {

	private static AlertDialog mAlertDialog;

	/**
	 * 返回适配器视图
	 * 
	 * @param context
	 * @param itemId
	 * @return
	 */
	public static View getInflatedView(Context context, int resoure) {
		return LayoutInflater.from(context).inflate(resoure, null);
	}

	/**
	 * Activity之间跳转实现
	 * 
	 * @param context
	 * @param desti
	 */

	public static void startActivity(Context context, Class desti) {
		Intent intent = new Intent(context, desti);
		context.startActivity(intent);

		Activity ac = (Activity) context;
		ac.finish();
	}

	/**
	 * 显示对话框方法
	 * 
	 * @param context
	 * @param msg
	 *            显示内容
	 * @param listener
	 *            OK按键接口实现
	 */

	public static void showAlertDialog(final Context context, String msg,
			final IAlertDialogClickListener listener) {

		AlertDialog.Builder buidler = new AlertDialog.Builder(context);
		View alertDialogView = getInflatedView(context, R.layout.dialog_view);
		TextView tv_message = (TextView) alertDialogView
				.findViewById(R.id.tv_dialog_msg);
		ImageButton btn_ok = (ImageButton) alertDialogView
				.findViewById(R.id.btn_dialogOk);
		ImageButton btn_cancel = (ImageButton) alertDialogView
				.findViewById(R.id.btn_dialogCancel);
		if (tv_message != null) {
			tv_message.setText(msg);
		}

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mAlertDialog != null) {
					mAlertDialog.cancel();
				}
				if (listener != null) {
					MyPlayer.playTone(context, MyPlayer.TONE_INDEX_ENTER);
					listener.onclick();
				}
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mAlertDialog != null) {
					MyPlayer.playTone(context, MyPlayer.TONE_INDEX_CANCEL);
					mAlertDialog.cancel();
				}
			}
		});
		// 创建对话框
		buidler.setView(alertDialogView);
		mAlertDialog = buidler.create();
		// 显示对话框
		mAlertDialog.show();
	}
	/**
	 * 保存游戏信息
	 * @param context
	 * @param stage
	 * @param coins
	 */
	public static void saveData(Context context,int stage,int coins){
		Log.i("lang", "save");
		FileOutputStream fos=null;
		try {
			fos = context.openFileOutput(Const.DATA_NAME,Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(stage);
			dos.writeInt(coins);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 读取游戏信息
	 * @param context
	 * @return
	 */
	public static int[] loadData(Context context){
		Log.i("lang", "load");
		FileInputStream fis = null;
		int[] datas = {Const.INDEX_STAGE,Const.TOTAL_COINS};
		try {
				fis=context.openFileInput(Const.DATA_NAME);
				DataInputStream dis = new DataInputStream(fis);
				datas[Const.INDEX_DATA_STAGE] = dis.readInt();
				datas[Const.INDEX_DATA_COINS] = dis.readInt();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return datas;
	}
	/**
	 * 重置关卡
	 * @param context
	 */
	public static void resetData(Context context){
		saveData(context, Const.INDEX_STAGE, Const.TOTAL_COINS);
	}
	
	

}
