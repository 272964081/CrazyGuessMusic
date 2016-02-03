package com.imooc.crazyguessmusic.db;

import com.imooc.crazyguessmusic.data.Const;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelperDAOimpl implements DBHelperDAO {

	private DBHelper mDBHelper;

	private Context mContext;

	public DBHelperDAOimpl(Context context) {
		mContext = context;
		mDBHelper = DBHelper.getInstance(mContext);
	}

	@Override
	public synchronized void saveInfo(int stage, int coins) {
		Log.i("lang", "save");
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		db.execSQL(
				"insert into game_info(currentStage,total_coins) values(?,?)",
				new Object[] { stage, coins });
		db.close();
	}

	@Override
	public synchronized int[] loadInfo() {
		Log.i("lang", "load");
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from game_info",
				null);
		int[] info = {Const.INDEX_STAGE,Const.TOTAL_COINS};
		while (cur.moveToNext()) {
			info = new int[2];
			info[0] = cur.getInt(cur.getColumnIndex("currentStage"));
			info[1] = cur.getInt(cur.getColumnIndex("total_coins"));
		}

		cur.close();
		db.close();
		return info;

	}

}
