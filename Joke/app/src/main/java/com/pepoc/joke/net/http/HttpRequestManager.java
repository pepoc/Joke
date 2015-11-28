package com.pepoc.joke.net.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Http请求管理器
 * @author yangchen
 *
 */
public class HttpRequestManager {
	
	private static HttpRequestManager instance = null;
	private AsyncHttpClient asyncHttpClient;

	/**
	 * 普通Http请求超时时间，单位：毫秒      默认10000
	 */
	private final static int HTTP_REQUEST_TIMEOUT = 10000;
	
	/**
	 * 请求重试次数
	 */
	private final static int HTTP_RETRY_TIMES = 1;
	
	private HttpRequestManager() {
		asyncHttpClient = new AsyncHttpClient();
	}
	
	public static HttpRequestManager getInstance() {
		if (null == instance) {
			instance = new HttpRequestManager();
		}
		return instance;
	}

	/**
	 * 发送Http请求
	 * @param request
	 */
	public void sendRequest(HttpRequest request) {
		Logger.i("Http sendRequest >>>>>>>>>>>>>>> ");
		if (HttpRequest.METHOD_GET.equals(request.getRequestMethod())) {
			sendGetRequest(request);
		} else {
			sendPostRequest(request);
		}
	}

	/**
	 * 发送Get请求
	 * @param request
	 */
	private void sendGetRequest(final HttpRequest request) {

		asyncHttpClient.get(request.context, request.getURL(), requestParamsConvert(request.getParams()), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Logger.i("Http response result <<<<<<<<<<<<<<< " + response);
				Object result = null;
				try {
					result = request.parseResponseResult(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				request.getOnHttpResponseListener().onHttpResponse(result);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	/**
	 * 发送Post请求
	 * @param request
	 */
	private void sendPostRequest(HttpRequest request) {
		asyncHttpClient.post(request.context, request.getURL(), requestParamsConvert(request.getParams()), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	/**
	 * Request params convert
	 * @param params
	 * @return
	 */
	private RequestParams requestParamsConvert(Map<String, String> params) {
		RequestParams requestParams = new RequestParams();
		if (params.size() > 0) {
			for (String key : params.keySet()) {
				requestParams.put(key, params.get(key));
			}
		}
		return requestParams;
	}

//	/**
//	 * 发送Http请求
//	 * @param request
//	 */
//	public void sendRequest(final HttpRequest request) {
//		String requestUrl = createGetRequestUrl(request);
//		Logger.i("Http request url >>>>>>>>>>>>>>> " + requestUrl);
//
//		if (HttpRequest.METHOD_GET.equals(request.getRequestMethod())) {
//			sendGetRequest(requestUrl, new Listener<String>() {
//
//				@Override
//				public void onResponse(String response) {
//					Logger.i("Http response result <<<<<<<<<<<<<<< " + response);
//					Object result = request.parseResponseResult(response);
//					request.getOnHttpResponseListener().onHttpResponse(result);
//				}
//
//			}, new ErrorListener() {
//
//				@Override
//				public void onErrorResponse(VolleyError error) {
//					request.getOnHttpResponseListener().onError();
//				}
//			});
//		} else if (HttpRequest.METHOD_POST.equals(request.getRequestMethod())) {
//			sendPostRequest(request.getURL(), new Listener<String>() {
//
//				@Override
//				public void onResponse(String response) {
//					Logger.i("Http response result <<<<<<<<<<<<<<< " + response);
//					Object result = request.parseResponseResult(response);
//					request.getOnHttpResponseListener().onHttpResponse(result);
//				}
//			}, new ErrorListener() {
//
//				@Override
//				public void onErrorResponse(VolleyError error) {
//					request.getOnHttpResponseListener().onError();
//				}
//			}, request.getParams());
//		}
//
//	}
//
//	/**
//	 * 发送Get请求
//	 * @param url
//	 * @param listener
//	 * @param errorListener
//	 */
//	private void sendGetRequest(String url, Listener<String> listener, ErrorListener errorListener) {
//		StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
//		DefaultRetryPolicy policy = new DefaultRetryPolicy(HTTP_REQUEST_TIMEOUT, HTTP_RETRY_TIMES, 1);
//		request.setRetryPolicy(policy);
//		mRequestQueue.add(request);
//	}
//
//	/**
//	 * 发送Post请求
//	 * @param url
//	 * @param listener
//	 * @param errorListener
//	 * @param params
//	 */
//	private void sendPostRequest(String url, Listener<String> listener, ErrorListener errorListener, final Map<String, String> params) {
//		StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				return params;
//			}
//		};
//		DefaultRetryPolicy policy = new DefaultRetryPolicy(HTTP_REQUEST_TIMEOUT, HTTP_RETRY_TIMES, 1);
//		request.setRetryPolicy(policy);
//		mRequestQueue.add(request);
//	}
	
//	/**
//	 * 组合一个get请求串
//	 * @param request
//	 * @return
//	 */
//	private String createGetRequestUrl(HttpRequest request) {
//		String requestUrl = request.getURL();
//		Map<String, String> params = request.getParams();
//		if (params.size() > 0) {
//			StringBuilder sb = new StringBuilder();
//			sb.append(requestUrl).append("?");
//			for (String key : params.keySet()) {
//				sb.append(key + "=" + params.get(key) + "&");
//			}
//			String str = sb.toString();
//			requestUrl = str.substring(0, str.length() - 1);
//		}
//		return requestUrl;
//	}
	
	public interface OnHttpResponseListener {
		void onHttpResponse(Object result);
		void onError();
	}
	
}
