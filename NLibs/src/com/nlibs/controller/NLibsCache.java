package com.nlibs.controller;

import android.content.Context;
import android.graphics.Bitmap;

import com.nlibs.process.DiskLruCacheUtils;
import com.nlibs.process.MemoryCacheUtils;

/**
 * Lớp điều hướng, điều khiển luồng lưu trữ theo cấu hình
 * 
 * @author Chau Minh Nhut
 * 
 */
public class NLibsCache {

	static NLibsCache mInstance;
	Context mContext;

	public enum TYPE {
		MEM_CACHE, DISK_CACHE, BOTH
	}

	public static NLibsCache getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new NLibsCache(context);
		}
		return mInstance;
	}

	public NLibsCache(Context context) {
		mContext = context;
	}

	/**
	 * Khởi tạo các vùng nhớ
	 */
	public void init() {
		MemoryCacheUtils.getInstance();
		DiskLruCacheUtils.getInstance(mContext);
	}

	/**
	 * Get 1 bitmap được lưu trong memory, disk cache or cả 2 thông qua url
	 * 
	 * @param url
	 * @param type
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url, TYPE type) {
		Bitmap bitmap = null;
		if (type == TYPE.MEM_CACHE) {
			bitmap = MemoryCacheUtils.getInstance().getBitmapFromMemCache(url);
		} else if (type == TYPE.DISK_CACHE) {
			bitmap = DiskLruCacheUtils.getInstance(mContext)
					.getBitmapFromDiskCache(url);
		} else {
			bitmap = MemoryCacheUtils.getInstance().getBitmapFromMemCache(url);
			if (null == bitmap) {
				bitmap = DiskLruCacheUtils.getInstance(mContext)
						.getBitmapFromDiskCache(url);
				if (null != bitmap)
					MemoryCacheUtils.getInstance().addBitmapToMemoryCache(url,
							bitmap);
			}

		}
		return bitmap;
	}

	/**
	 * Add bitmap to cache, có thể lưu trên memory hoặc disk hoặc cả 2
	 * 
	 * @param url
	 * @param bitmap
	 * @param type
	 */
	public void putBitmapToCache(String url, Bitmap bitmap, TYPE type) {
		if (type == TYPE.MEM_CACHE) {
			MemoryCacheUtils.getInstance().addBitmapToMemoryCache(url, bitmap);
		} else if (type == TYPE.DISK_CACHE) {
			DiskLruCacheUtils.getInstance(mContext).addBitmapToDiskCache(url,
					bitmap);
		} else {
			MemoryCacheUtils.getInstance().addBitmapToMemoryCache(url, bitmap);
			DiskLruCacheUtils.getInstance(mContext).addBitmapToDiskCache(url,
					bitmap);
		}
	}
}
