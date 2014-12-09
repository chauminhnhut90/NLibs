package com.nlibs.support;

import android.database.sqlite.SQLiteDatabase;

/**
 * Interface truy vấn dữ liệu
 * 
 * @author Chau Minh Nhut
 * @since 2014-12-09
 * @version 1.0
 */
public interface QueryExecutor {

	/**
	 * Return id of row (insert), number of row (select, delete, update)
	 * 
	 * @param database
	 * @return
	 */
	public long run(SQLiteDatabase database);
}
