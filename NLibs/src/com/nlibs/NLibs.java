package com.nlibs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

import com.nlibs.controller.NLibsCache;
import com.nlibs.controller.NLibsCache.TYPE;
import com.nlibs.controller.NLibsRequest;
import com.nlibs.imageloader.PhotoView;
import com.nlibs.support.DataRequestCallback;

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
}
