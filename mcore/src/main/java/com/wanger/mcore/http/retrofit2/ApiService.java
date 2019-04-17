package com.wanger.mcore.http.retrofit2;

import java.util.Collection;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author wanger
 * @date 2018/11/23 13:00
 * @email xxx@gmail.com
 * @desc 统一Retrofit2请求service
 */
public interface ApiService {

    /**
     * 通用-post接口 异步
     */
    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> executePost(@Path("url") String url,
                                         @HeaderMap Map<String, String> headers,
                                         @FieldMap Map<String, String> params);

    /**
     * 通用-post接口 异步
     */
    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> executePost(@Path("url") String url,
                                         @FieldMap Map<String, String> params);

    /**
     * 通用-get接口 异步
     */
    @GET("{url}")
    Observable<ResponseBody> executeGet(@Path("url") String url,
                                        @HeaderMap Map<String, String> headers,
                                        @QueryMap Map<String, String> params);

    /**
     * 通用-文件上传，可携带参数
     */
    @JsonOrXmlConverterFactory.ResponseFormat("xml")
    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(@Path("url") String url,
                                        @HeaderMap Map<String, String> headers,
                                        @Part Collection<MultipartBody.Part> paramsList);

    /**
     * 通用-文件下载
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
