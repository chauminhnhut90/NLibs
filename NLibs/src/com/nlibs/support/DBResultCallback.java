package com.nlibs.support;

/**
 * Lớp abstract, chứa 1 callback để trả dữ liệu về từ Database
 * 
 * @author Chau Minh Nhut
 * @since 2014-12-09
 * @version 1.0
 */
public abstract class DBResultCallback {

	long mResult;

	public void setResult(long result) {
		mResult = result;
	}

	public abstract void onResult(long result, boolean continueWaiting);

	public void onResult(boolean continueWaiting) {
		onResult(mResult, continueWaiting);
	}
}
