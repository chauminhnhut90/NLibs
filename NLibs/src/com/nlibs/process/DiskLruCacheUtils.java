package com.nlibs.process;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.nlibs.support.DiskLruCache;

/**
 * Lớp chức năng, xử lý lưu trữ bitmap xuống bộ nhớ máy (hoặc thẻ nhớ)
 * 
 * @author Chau Minh Nhut
 * 
 */
public class DiskLruCacheUtils {

	static DiskLruCacheUtils mInstance;
	private DiskLruCache mDiskLruCache;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;
	private static final long DISK_CACHE_SIZE = 1024 * 1024 * 20; // 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";

	public static DiskLruCacheUtils getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new DiskLruCacheUtils(context);
		}
		return mInstance;
	}

	public DiskLruCacheUtils(Context context) {
		initDiskCache(context);
	}

	class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
		Context mContext;

		InitDiskCacheTask(Context context) {
			mContext = context;
		}

		@Override
		protected Void doInBackground(File... params) {
			synchronized (mDiskCacheLock) {
				File cacheDir = params[0];
				mDiskLruCache = DiskLruCache.openCache(cacheDir,
						DISK_CACHE_SIZE);
				mDiskCacheStarting = false; // Finished initialization
				mDiskCacheLock.notifyAll(); // Wake any waiting threads
			}
			return null;
		}
	}

	public void addBitmapToDiskCache(String key, Bitmap bitmap) {
		// // Add to memory cache as before
		// if (getBitmapFromMemCache(key) == null) {
		// mMemoryCache.put(key, bitmap);
		// }

		// Also add to disk cache
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
				mDiskLruCache.put(key, bitmap);
			}
		}
	}

	public Bitmap getBitmapFromDiskCache(String key) {
		synchronized (mDiskCacheLock) {
			// Wait while disk cache is started from background thread
			while (mDiskCacheStarting) {
				try {
					mDiskCacheLock.wait();
				} catch (InterruptedException e) {
				}
			}
			if (mDiskLruCache != null) {
				return mDiskLruCache.get(key);
			}
		}
		return null;
	}

	public void initDiskCache(Context context) {
		// Initialize disk cache on background thread
		File cacheDir = DiskLruCache
				.getDiskCacheDir(context, DISK_CACHE_SUBDIR);
		new InitDiskCacheTask(context).execute(cacheDir);
	}
}