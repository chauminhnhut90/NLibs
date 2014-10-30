package com.nlibs.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * Lớp chức năng, hỗ trợ việc tạo kết nối và gửi 1 yêu cầu lên server và nhận dữ
 * liệu về
 * 
 * @author Chau Minh Nhut
 * 
 */
public class HttpUtils {

	private static final String TAG = HttpUtils.class.getName();

	/**
	 * Gửi 1 request ra Internet
	 * 
	 * @param url
	 * @return
	 */
	public static String requestHttpGET(String url) {
		String result = null;
		HttpGet httpGet = new HttpGet(url);

		BasicHttpParams timeoutParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(timeoutParams, 10000);
		HttpConnectionParams.setSoTimeout(timeoutParams, 10000);
		httpGet.setParams(timeoutParams);

		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			HttpResponse response = httpClient.execute(httpGet);
			InputStream streamContent = response.getEntity().getContent();

			// Convert into String
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					streamContent));
			StringBuilder responseStr = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				responseStr.append(line);
			}
			result = responseStr.toString().trim();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			// httpClient.close();
			httpClient.getConnectionManager().shutdown();
		}

		return result;
	}

	public static String requestHttpPOST(String url, List<NameValuePair> params) {
		String result = null;
		HttpPost httpPost = new HttpPost(url);

		BasicHttpParams timeoutParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(timeoutParams, 10000);
		HttpConnectionParams.setSoTimeout(timeoutParams, 10000);
		httpPost.setParams(timeoutParams);

		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			httpPost.setEntity(ent);
			HttpResponse responsePOST = httpClient.execute(httpPost);
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			// httpClient.close();
			httpClient.getConnectionManager().shutdown();
		}

		return result;
	}
}
