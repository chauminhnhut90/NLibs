package com.nlibs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.nlibs.controller.NLibsCache;
import com.nlibs.controller.NLibsCache.TYPE;
import com.nlibs.controller.NLibsRequest;
import com.nlibs.imageloader.PhotoView;
import com.nlibs.support.DBResultCallback;
import com.nlibs.support.DataRequestCallback;
import com.nlibs.support.DatabaseManager;
import com.nlibs.support.QueryExecutor;

/**
 * <h3>
 * LỚP QUAN TRỌNG NHẤT CỦA CẢ THƯ VIỆN</h3></br> Chứa tất cả các phương thức mà
 * thư viện hỗ trợ cho người dùng
 * 
 * @author Chau Minh Nhut
 * @since 2014-10-16
 * @version 1.0
 */
public class NLibs {

	static NLibs mInstance;
	Context mContext;

	/**
	 * Hàm khởi tạo các giá trị mặc định cho thư viện
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		getInstance(context);
	}

	/**
	 * Hàm khởi tạo DB nếu cần thiết
	 * 
	 * @param helper
	 */
	public static void initDatabase(SQLiteOpenHelper helper) {
		DatabaseManager.initializeInstance(helper);
	}

	public static NLibs getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new NLibs(context);
		}
		return mInstance;
	}

	public NLibs(Context context) {
		mContext = context.getApplicationContext();
		NLibsCache.getInstance(mContext).init();
	}

	/**
	 * Load 1 imageView từ 1 url
	 * 
	 * @param imageView
	 * @param localURL
	 * @param mEmptyDrawable
	 * @throws MalformedURLException
	 */
	public void loadBitmap(PhotoView imageView, URL localURL, int mEmptyResID)
			throws MalformedURLException {
		imageView.setImageURL(mContext, localURL, true, mEmptyResID);
	}

	/**
	 * Gửi 1 request lên server, nhận giá trị trả về là 1 String
	 * 
	 * @param context
	 * @param httpUrlString
	 * @param priority
	 * @param callback
	 */
	public void getDataAsString(Context context, final String httpUrlString,
			int priority, final DataRequestCallback<String> callback) {
		NLibsRequest
				.getDataAsString(context, httpUrlString, priority, callback);
	}

	/**
	 * Gửi 1 request lên server, nhận giá trị trả về là 1 danh sách đối tượng
	 * 
	 * @param context
	 * @param httpUrlString
	 * @param priority
	 * @param callback
	 */
	public <T> void getDataAsListObject(Context context,
			final String httpUrlString, int priority,
			final DataRequestCallback<ArrayList<T>> callback) {
		NLibsRequest.getDataAsListObject(context, httpUrlString, priority,
				callback);
	}
	
	/****************************************************************************/
	/****************************************************************************/
	/****************************************************************************/
	/****************************PHẦN XỬ LÝ CHO CACHE****************************/
	/****************************************************************************/
	/****************************************************************************/
	/****************************************************************************/

	/**
	 * Add 1 bitmap từ bộ nhớ cache (memory or disk cache)
	 * 
	 * @param url
	 * @param bitmap
	 * @param type
	 *            Chọn lưu trên memory , disk hoặc cả 2
	 */
	public void putBitmapToCache(String url, Bitmap bitmap, TYPE type) {
		NLibsCache.getInstance(mContext).putBitmapToCache(url, bitmap, type);
	}

	/**
	 * Lấy 1 bitmap từ bộ nhớ cache (memory or disk cache)
	 * 
	 * @param url
	 * @param type
	 *            Chọn lưu trên memory , disk hoặc cả 2
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url, TYPE type) {
		return NLibsCache.getInstance(mContext).getBitmapFromCache(url, type);
	}
	/****************************************************************************/
	/****************************************************************************/
	/****************************************************************************/
	/****************************PHẦN XỬ LÝ CHO DATABASE*************************/
	/****************************************************************************/
	/****************************************************************************/
	/****************************************************************************/
	/**
	 * Thực hiện truy vấn trên cùng thread đang gọi
	 * 
	 * @param executor
	 */
	public void executeQuery(QueryExecutor executor) {
		DatabaseManager.getInstance().executeQuery(executor);
	}

	/**
	 * Thực hiện truy vấn ở thread khác và không nhận giá trị trả về
	 * 
	 * @param executor
	 * @param forceUpdate
	 */
	public void executeQueryTask(final QueryExecutor executor,
			boolean forceUpdate) {
		DatabaseManager.getInstance().executeQueryTask(executor, forceUpdate);
	}

	/**
	 * Thực hiện truy vấn ở thread khác và có callback trả kết quả về
	 * 
	 * @param executor
	 * @param forceUpdate
	 */
	public void executeQueryTask(final QueryExecutor executor,
			boolean forceUpdate, final DBResultCallback callback) {
		DatabaseManager.getInstance().executeQueryTask(executor, forceUpdate,
				callback);
	}
}
