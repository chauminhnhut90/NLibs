package com.nlibs.support;

/**
 * Lớp abstract, chứa 1 callback để trả dữ liệu về
 * 
 * @author Chau Minh Nhut(chauminhnhut90@gmail.com)
 * @since 2014-10-13
 * @version 1.0
 * @param <T>
 */
public abstract class DataRequestCallback<T> {
	private T mResult;

	public void setResult(T result) {
		mResult = result;
	}

	public abstract void onResult(T result, boolean continueWaiting);

	public void onResult(boolean continueWaiting) {
		onResult(mResult, continueWaiting);
	}

}
