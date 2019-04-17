package com.wanger.mcore.http.retrofit2;

import android.util.Log;

import com.wanger.mcore.http.okhttp3.CustomInterceptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author Administrator
 * Time:2018/8/24 15:36
 * Project:MPlatform
 * Description: retrofit2 基础网络请求通用类
 */
public abstract class BaseHttp {

    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("HttpLogInfo", message));

    private OkHttpClient client = new OkHttpClient
            .Builder()
            .addInterceptor(new CustomInterceptor())
            .addNetworkInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();
    private Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(getBaseUrl())
            .client(client)
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(JsonOrXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    private ApiService apiService;

    protected BaseHttp() {
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 设置主机host
     *
     * @return
     */
    public abstract String getBaseUrl();

    /**
     * get提交
     *
     * @param url      地址
     * @param headers  头部
     * @param params   参数
     * @param observer 观察者
     */
    public void get(String url, HashMap<String, String> headers, HashMap<String, String> params,
                    Observer<ResponseBody> observer) {
        apiService.executeGet(url, headers, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new ErrorFunc<>())
                .subscribe(observer);
    }

    /**
     * post提交
     *
     * @param url      地址
     * @param headers  头部
     * @param params   参数
     * @param observer 观察者
     */
    public void post(String url, HashMap<String, String> headers, HashMap<String, String> params,
                     Observer<ResponseBody> observer) {
        apiService.executePost(url, headers, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new ErrorFunc<>())
                .subscribe(observer);
    }

    /**
     * post提交
     *
     * @param url      地址
     * @param params   参数
     * @param observer 观察者
     */
    public void post(String url, HashMap<String, String> params,
                     Observer<ResponseBody> observer) {
        apiService.executePost(url, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new ErrorFunc<>())
                .subscribe(observer);
    }

    /**
     * 上传文件
     *
     * @param url        地址
     * @param headers    头部
     * @param paramsList 文件与参数
     * @param observer   观察者
     */
    public void upLoadFile(String url, Map<String, String> headers, Collection<MultipartBody.Part> paramsList,
                           Observer<ResponseBody> observer) {
        apiService.upLoadFile(url, headers, paramsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new ErrorFunc<>())
                .subscribe(observer);

    }

    /**
     * 下载文件
     *
     * @param url      下载地址
     * @param observer 观察者
     */
    public void downloadFile(String url, Observer<ResponseBody> observer) {
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new ErrorFunc<>())
                .subscribe(observer);
    }
}
