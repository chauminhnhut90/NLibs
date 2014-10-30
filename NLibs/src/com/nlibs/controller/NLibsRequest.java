package com.nlibs.controller;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nlibs.process.HttpUtils;
import com.nlibs.support.DataRequestCallback;
import com.nlibs.support.ParserUtils;
import com.nlibs.support.ThreadManager;

/**
 * Lớp điều hướng, thực hiện các request ra ngoài Internet và nhận dữ liệu trả về
 * 
 * @author Chau Minh Nhut (chauminhnhut90@gmail.com)
 * @since 13/10/2014
 * @version 1.0
 */
public class NLibsRequest {

	/**
	 * Gửi 1 request lên server, nhận giá trị trả về là 1 String
	 * 
	 * @param httpUrlString
	 * @return
	 */
	public static void getDataAsString(Context context,
			final String httpUrlString, int priority,
			final DataRequestCallback<String> callback) {

		final ThreadManager tm = ThreadManager.getInstance();
		tm.execute(new Runnable() {

			@Override
			public void run() {
				String value = HttpUtils.requestHttpGET(httpUrlString);
				callback.setResult(value);
				tm.callbackOnUIThread(callback, false);
			}
		}, priority);
	}

	/**
	 * Gửi 1 request lên server, nhận giá trị trả về là 1 danh sách đối tượng
	 * 
	 * @param context
	 * @param httpUrlString
	 * @param priority
	 * @param callback
	 */
	public static <T> void getDataAsListObject(Context context,
			final String httpUrlString, int priority,
			final DataRequestCallback<ArrayList<T>> callback) {

		final ThreadManager tm = ThreadManager.getInstance();
		tm.execute(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String jsonStr = HttpUtils.requestHttpGET(httpUrlString);
				ArrayList<T> listData = (ArrayList<T>) ParserUtils
						.parseObjectFromString(jsonStr,
								new TypeToken<List<T>>() {
								}.getType());
				callback.setResult(listData);
				tm.callbackOnUIThread(callback, false);
			}
		}, priority);
	}
}
