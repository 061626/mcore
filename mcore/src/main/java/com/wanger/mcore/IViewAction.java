package com.wanger.mcore;

import android.os.Bundle;

/**
 * @author wanger
 * @date 2019/4/16 17:47
 * @email xxx@gmail.com
 * @desc 页面行为通用接口
 */
public interface IViewAction {
    /**
     * 正在加载中
     */
    void onShowLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 显示提示信息
     *
     * @param msg 消息内容
     */
    void onShowMsg(String msg);

    /**
     * 显示错误信息
     *
     * @param error 错误内容
     */
    void onError(String error);

    /**
     * 关闭窗口
     */
    void onCloseSelf();

    /**
     * 返回数据
     *
     * @param data
     */
    void onSetResult(Bundle data);

    /**
     * 加载数据（网络或本地）
     */
    void loadData();
}
