package com.wanger.mcore.http.retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 针对的是网络请求
 * retrofit 用来避免服务端返回没有响应体body
 * 需要注意的是，NullOnEmptyConverterFactory必需在GsonConverterFactory之前addConverterFactory
 *
 * @author Administrator
 * @date 2017/11/24
 */

public class NullOnEmptyConverterFactory extends Converter.Factory {


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
        return (Converter<ResponseBody, Object>) body -> {
            if (body.contentLength() == 0) {
                return null;
            }
            return delegate.convert(body);
        };
    }
}