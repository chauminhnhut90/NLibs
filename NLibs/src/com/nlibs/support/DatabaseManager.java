package com.nlibs.support;

import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Lớp quản lý kết nối DB, đảm bảo luôn luôn chỉ có 1 connection + 1 DB
 * 
 * @author Chau Minh Nhut
 * @version 1.0
 * @since 2014-12-09
 * 
 */
public class DatabaseManager {

	private AtomicInteger mOpenCounter = new AtomicInteger();

	private static DatabaseManager instance;
	private SQLiteOpenHelper mDatabaseHelper;
	private SQLiteDatabase mDatabase;

	private DatabaseManager(SQLiteOpenHelper helper) {
		mDatabaseHelper = helper;
	}

	/**
	 * Khởi tạo database
	 * 
	 * @param helper
	 */
	public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
		if (instance == null) {
			instance = new DatabaseManager(helper);
		}
	}

	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					DatabaseManager.class.getSimpleName()
							+ " is not initialized, call initializeInstance(..) method first.");
		}

		return instance;
	}

	private synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			// Opening new database
			mDatabase = mDatabaseHelper.getWritableDatabase();
		}
		return mDatabase;
	}

	private synchronized void closeDatabase() {
		if (mOpenCounter.decrementAndGet() == 0) {
			// Closing database
			mDatabase.close();

		}
	}

	/**
	 * Thực hiện query task trên UI Thread
	 * 
	 * @param executor
	 */
	public void executeQuery(QueryExecutor executor) {
		SQLiteDatabase database = openDatabase();
		executor.run(database);
		closeDatabase();
	}

	/**
	 * Thực hiện query ở thread khác, không phải hồi giá trị trả về khi thực
	 * hiện xong
	 * 
	 * @param executor
	 * @param forceUpdate
	 */
	public void executeQueryTask(final QueryExecutor executor,
			boolean forceUpdate) {

		final ThreadManager t = ThreadManager.getInstance();

		int priority;
		if (forceUpdate) {
			priority = ThreadManager.PRIORITY_BLOCKING;
		} else {
			priority = ThreadManager.PRIORITY_NORMAL;
		}

		t.execute(new Runnable() {

			@Override
			public void run() {
				SQLiteDatabase database = openDatabase();
				executor.run(database);
				closeDatabase();
			}
		}, priority);

	}

	/**
	 * Thực hiện truy vấn ở thread khác, có phản hồi giá trị trả về tương ứng
	 * 
	 * @param executor
	 * @param forceUpdate
	 * @param callback
	 */
	public void executeQueryTask(final QueryExecutor executor,
			boolean forceUpdate, final DBResultCallback callback) {

		final ThreadManager t = ThreadManager.getInstance();

		int priority;
		if (forceUpdate) {
			priority = ThreadManager.PRIORITY_BLOCKING;
		} else {
			priority = ThreadManager.PRIORITY_NORMAL;
		}

		t.execute(new Runnable() {

			@Override
			public void run() {
				SQLiteDatabase database = openDatabase();
				long value = executor.run(database);
				closeDatabase();
				callback.setResult(value);
				t.callbackOnUIThread(callback, value, false);
			}
		}, priority);

	}
}
