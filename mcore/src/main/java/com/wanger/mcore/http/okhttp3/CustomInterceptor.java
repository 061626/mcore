package com.wanger.mcore.http.okhttp3;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author:Administrator
 * Time:2018/9/19 16:53
 * Project:Uu
 * Description:
 * 可以统一定制请求参数
 * 请求和结果拦截器,这个类的执行时间是返回结果返回的时候,返回一个json的String,对里面一些特殊字符做处理
 * 主要用来处理一些后台上会出现的bug,比如下面声明的这三种情况下统一替换为:null
 *
 * @author Administrator
 */
public class CustomInterceptor implements Interceptor {
    private String emptyString = ":\"\"";
    private String emptyObject = ":{}";
    private String emptyArray = ":[]";
    private String newChars = ":null";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        //请求定制：添加请求头部
        //请求定制：添加请求头
        Request.Builder requestBuilder = request
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //设置cookie
        //        String cookie= App.getCookie();
        //        if (StringUtil.checkStr(cookie)) {             //cookie判空检查
        //            requestBuilder.addHeader("Cookie", cookie);
        //        }
        //requestBuilder.build();
        //        chain.proceed(requestBuilder.build());


        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String json = responseBody.string();
            MediaType contentType = responseBody.contentType();
            if (contentType.toString().contains("multipart/form-data")) {
                contentType = MediaType.parse("application/json; charset=utf-8");
                ResponseBody body = ResponseBody.create(contentType, json);
                return response.newBuilder().body(body).build();
            }
            if (!json.contains(emptyString)) {
                ResponseBody body = ResponseBody.create(contentType, json);
                return response.newBuilder().body(body).build();
            } else {
                String replace = json.replace(emptyString, newChars);
                String replace1 = replace.replace(emptyObject, newChars);
                String replace2 = replace1.replace(emptyArray, newChars);
                ResponseBody body = ResponseBody.create(contentType, replace2);
                return response.newBuilder().body(body).build();
            }
        }
        return response;
    }
}