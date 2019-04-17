package com.wanger.mcore.http.okhttp3;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author wanger
 * @date 2019/4/16 15:38
 * @email xxx@gmail.com
 * @desc okHttp请求管理
 */
public class OkHttpManager {
    private OkHttpManager okHttpManager = null;
    private OkHttpClient httpClient = null;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("HttpLogInfo", message));

    private OkHttpManager() {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new CustomInterceptor())
                .addNetworkInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    private OkHttpManager OkInstance() {
        synchronized (this) {
            if (okHttpManager == null) {
                okHttpManager = new OkHttpManager();
            }
        }
        return okHttpManager;
    }

    /**
     * 下载文件
     *
     * @param url              下载链接
     * @param loadListener     数据回调
     * @param downloadListener 下载进度回调 可为空
     * @return
     */
    public Call downloadFile(String url, IDataLoadListener loadListener, @Nullable IDownloadListener downloadListener) {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(url)
                .build();
        Call newCall = httpClient.newBuilder()
                .addNetworkInterceptor(new DownloadInterceptor(downloadListener))
                .build()
                .newCall(request);
        newCall.enqueue(new OkCallBack(url, loadListener));
        return newCall;
    }

    /**
     * post请求
     *
     * @param url          请求地址
     * @param headers      头部
     * @param body         内容
     * @param loadListener 回调
     * @return Call可取消
     */
    public Call executePost(String url, HashMap<String, String> headers, RequestBody body, IDataLoadListener loadListener) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.headers(Headers.of(headers))
                .post(body)
                .url(url)
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new OkCallBack(url, loadListener));
        return newCall;
    }

    public Call executeGet(String url, HashMap<String, String> headers, HashMap<String, String> params, IDataLoadListener loadListener) {
        /*组装参数*/
        if (params != null && !params.isEmpty()) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("?");
            for (String key : params.keySet()) {
                strBuilder.append(key)
                        .append("=")
                        .append(params.get(key))
                        .append("&");
            }
            url += strBuilder.toString().trim();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(url)
                .headers(Headers.of(headers))
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new OkCallBack(url, loadListener));
        return newCall;
    }

    /**
     * 回调接口OkHttp
     */
    class OkCallBack implements Callback {
        private String code;
        private IDataLoadListener loadListener;

        OkCallBack(String code, IDataLoadListener loadListener) {
            this.code = code;
            this.loadListener = loadListener;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            loadListener.loadFail(code, e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            loadListener.loadSuccess(code, response);
        }
    }
}
