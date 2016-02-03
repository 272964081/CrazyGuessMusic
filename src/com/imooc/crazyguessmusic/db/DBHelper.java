package com.imooc.crazyguessmusic.db;

import com.imooc.crazyguessmusic.data.Const;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "guessMusic.db";
	public static final int VERSION = 1;

	/** 创建表语句 */
	public static final String SQL_CREATE_DATABASE = "create table if not exists game_info(_id integer primary key autoincrement,"
			+ "currentStage integer default "+Const.INDEX_STAGE+",total_coins integer default "+Const.TOTAL_COINS+")";
	/** 删除表语句*/
	public static final String SQL_DROP_DATABASE = "drop table if exists game_info";

	private static DBHelper mDBHelper;
	private static Context mContext;

	private DBHelper() {
		super(mContext, DATABASE_NAME, null, VERSION);
	}

	public static DBHelper getInstance(Context context) {
		mContext = context;
		if (mDBHelper == null) {
			mDBHelper = new DBHelper();
		}

		return mDBHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP_DATABASE);
		db.execSQL(SQL_CREATE_DATABASE);
	}

}
