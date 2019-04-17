package com.wanger.mcore.http.okhttp3;

import okhttp3.Response;

/**
 * @author wanger
 * @date 2019/4/16 16:19
 * @email xxx@gmail.com
 * @desc 数据加载监听
 */
public interface IDataLoadListener {
    void loadSuccess(String code, Response t);

    void loadFail(String code, String msg);
}
