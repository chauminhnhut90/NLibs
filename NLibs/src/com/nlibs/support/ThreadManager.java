package com.nlibs.support;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

/**
 * Quản lý luồng (Thread) cho các request lấy dữ liệu bên ngoài Internet
 * 
 * @author Chau Minh Nhut(chauminhnhut90@gmail.com)
 * @since 2014-10-13
 * @version 1.0
 * 
 */
public class ThreadManager {

	/**
	 * Độ ưu tiên bình thư�?ng, dành cho các task mà UI không bị block khi ch�?
	 * kết quả
	 */
	public static final int PRIORITY_NORMAL = 0;

	/**
	 * Độ ưu tiên cao, dành cho các task mà UI đang bị block để ch�? kết quả
	 * thực thi
	 */
	public static final int PRIORITY_BLOCKING = 1;

	private static final Object mLock = new Object();

	// Singleton
	private static ThreadManager mInstance;

	/**
	 * Số lượng thread trong thread pool
	 */
	private static final int CORE_POOL_SIZE = 8;

	/**
	 * Số lượng thread tối đa trong thread pool, hiện tại để bằng số lượng trong
	 * trư�?ng hợp bình thư�?ng để giới hạn tại mức đó luôn
	 */
	private static final int MAXIMUM_POOL_SIZE = 8;

	/**
	 * Th�?i gian giữ một thread tồn tại để ch�? dùng lại sau khi thực thi xong
	 */
	private static final int KEEP_ALIVE_TIME = 2;

	// Sets the Time Unit to seconds
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

	/**
	 * NOTE: This is the number of total available cores. On current versions of
	 * Android, with devices that use plug-and-play cores, this will return less
	 * than the total number of cores. The total number of cores is not
	 * available in current Android implementations.
	 */
	private static int NUMBER_OF_CORES = Runtime.getRuntime()
			.availableProcessors();

	/**
	 * Hàng đợi các task cần thực thi với thread pool
	 */
	private final BlockingQueue<Runnable> mNormalTaskQueue;

	/**
	 * Thread pool để xử lý các task thông thư�?ng không đòi h�?i độ ưu tiên cao
	 */
	private final ThreadPoolExecutor mTaskThreadPool;

	/**
	 * Quêu chứa các task quan trọng, cần thực hiện gấp
	 */
	private final BlockingQueue<Runnable> mUrgentTaskQueue;

	/**
	 * Pool chạy các task quan trọng
	 */
	private final ThreadPoolExecutor mUrgentTaskThreadPool;

	private Handler mUICallbackHandler;

	// A static block that sets class fields
	static {
		// The time unit for "keep alive" is in seconds
		KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

		// Creates a single static instance of PhotoManager
		mInstance = new ThreadManager();
	}

	public static ThreadManager getInstance() {
		synchronized (mLock) {
			return mInstance;
		}
	}

	private ThreadManager() {

		/**
		 * Những task background có độ ưu tiên trung bình
		 */
		mNormalTaskQueue = new LinkedBlockingQueue<Runnable>();
		mTaskThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
				MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				mNormalTaskQueue);
		mTaskThreadPool.allowCoreThreadTimeOut(true);

		/**
		 * Những task background có độ ưu tiên cao
		 */
		mUrgentTaskQueue = new LinkedBlockingQueue<Runnable>();
		mUrgentTaskThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES,
				NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				mUrgentTaskQueue);
		mUrgentTaskThreadPool.allowCoreThreadTimeOut(true);
		mUrgentTaskThreadPool.setThreadFactory(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setPriority(Thread.MAX_PRIORITY);
				return t;
			}
		});

	}

	/**
	 * Thực hiện callback báo kết quả trên UI thread
	 * 
	 * @param result
	 *            object callback có kèm kết quả
	 * @param continueWaiting
	 *            liệu bên ch�? kết quả có cần ch�? tiếp kết quả nữa hay không,
	 *            trong trư�?ng hợp dữ liệu trả v�? 2 lần, một lần từ cache và
	 *            một lần từ server
	 */
	public <T extends Object> void callbackOnUIThread(
			final DataRequestCallback<T> resultCallback,
			final boolean continueWaiting) {
		mUICallbackHandler.post(new Runnable() {
			@Override
			public void run() {
				resultCallback.onResult(continueWaiting);
			}
		});
	}

	/**
	 * Gửi yêu cầu thực thi một công việc nào đó
	 */
	public void execute(Runnable runnable) {
		execute(runnable, PRIORITY_NORMAL);
	}

	/**
	 * �?ăng ký thực thi một tác vụ load dữ liệu nào đó, truy�?n vào đối tượng
	 * DataLoaderTask mô tả tác vụ và độ ưu tiên để sắp xếp lại thứ tự thực thi
	 * nếu cần thiết, trong trư�?ng hợp rất gấp có thể ko dùng đến phương thức
	 * này của DataLoader vì không chắc chắn 100% là thực thi được ngay lập tức <br>
	 * <br>
	 * 
	 * <b>Lưu ý:</b> hiện tại độ ưu tiên chưa được implement, model này vẫn đang
	 * trong quá trình xây dựng
	 */
	public void execute(Runnable runnable, int priority) {
		if (priority == PRIORITY_BLOCKING) {
			if (mUrgentTaskThreadPool.getActiveCount() < NUMBER_OF_CORES) {
				mUrgentTaskThreadPool.execute(runnable);
			} else {
				mTaskThreadPool.execute(runnable);
			}
		} else {
			mTaskThreadPool.execute(runnable);
		}
	}

	/**
	 * Hoãn thực thi một task nào đó
	 */
	public void cancel(Runnable runnable) {
		mTaskThreadPool.remove(runnable);
		mUrgentTaskThreadPool.remove(runnable);
	}
}
