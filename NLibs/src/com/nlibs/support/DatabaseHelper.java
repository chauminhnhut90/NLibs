package com.nlibs.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Xử lý việc tạo và quản lý phiên bản DB
 * 
 * @author Chau Minh Nhut
 * @since 2014-12-09
 * @version 1.0
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "database.db";
	public static final int DATABASE_VERSION = 1;
	//private Context mContext;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		/**
		 * Tạo tất cả table cần thiết
		 */
//		ProductModel.createTable(sqLiteDatabase);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {
		if (newVersion > oldVersion) {
			/**
			 * Xóa tất cả các table cũ
			 */
//			ProductModel.dropTable(sqLiteDatabase);

			/**
			 * Tại lại DB
			 */
			onCreate(sqLiteDatabase);
		}
	}
}
