package com.nlibs.process;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

/**
 * Lớp chức năng, xử lý lưu bitmap trên memory
 * 
 * @author Chau Minh Nhut
 * 
 */
public class MemoryCacheUtils {

	static MemoryCacheUtils mInstance;

	public static MemoryCacheUtils getInstance() {
		if (null == mInstance) {
			mInstance = new MemoryCacheUtils();
		}
		return mInstance;
	}

	public MemoryCacheUtils() {
		initMemoryCache();
	}

	/* Use a Memory Cache */
	private LruCache<String, Bitmap> mMemoryCache;

	public void initMemoryCache() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 4;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	public void addBitmapToMemoryCache(String url, Bitmap bitmap) {
		if (getBitmapFromMemCache(url) == null) {
			mMemoryCache.put(url, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String url) {
		return mMemoryCache.get(url);
	}
}
