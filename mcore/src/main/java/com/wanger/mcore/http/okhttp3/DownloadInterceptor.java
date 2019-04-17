package com.wanger.mcore.http.okhttp3;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * @author wanger
 * @date 2019/4/16 17:01
 * @email xxx@gmail.com
 * @desc 下载文件拦截器
 */
public class DownloadInterceptor implements Interceptor {

    private IDownloadListener downloadListener;

    public DownloadInterceptor(IDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        DownloadResponseBody responseBody = new DownloadResponseBody(response);
        return response.newBuilder().body(responseBody).build();
    }

    private class DownloadResponseBody extends ResponseBody {
        private Response response;

        DownloadResponseBody(Response response) {
            this.response = response;
        }

        @Override
        public MediaType contentType() {
            return response.body().contentType();
        }

        @Override
        public long contentLength() {
            long length = response.body().contentLength();
            if (downloadListener != null) {
                downloadListener.start(length);
            }
            return length;
        }

        @Override
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(response.body().source()) {
                private long bytesReaded = 0;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                    if (downloadListener != null) {
                        downloadListener.loading(bytesReaded);
                    }
                    return bytesRead;
                }
            });
        }
    }
}
