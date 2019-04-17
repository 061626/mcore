package com.wanger.mcore.http.retrofit2;

import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

/**
 * @author wanger
 * @date 2018/12/6 14:12
 * @email xxx@gmail.com
 * @desc rxjava2异常转发器，统一处理数据请求异常信息
 */
public class ErrorFunc<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(handleException(throwable));
    }

    private Throwable handleException(Throwable e) {
        if (e instanceof HttpException ||
                e instanceof UnknownHostException) {
            return new Throwable("网络异常，请检查网络重试");
        }
        return new Throwable("未知错误");
    }
}
