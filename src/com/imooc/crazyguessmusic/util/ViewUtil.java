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

}
