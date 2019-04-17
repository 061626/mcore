package com.wanger.mcore.http.okhttp3;

/**
 * @author wanger
 * @date 2019/4/16 17:20
 * @email xxx@gmail.com
 * @desc doc
 */
public interface IDownloadListener {
    void start(long max);

    void loading(long progress);

    void loadFail(String msg);

    void complete();
}
