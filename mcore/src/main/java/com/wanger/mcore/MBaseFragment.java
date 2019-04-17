package com.wanger.mcore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Time:2018/8/24 13:36
 * Project:MPlatform
 * Description:
 *
 * @author Administrator
 */
public abstract class MBaseFragment extends Fragment implements IViewAction {
    /**
     * 界面布局文件是否加载完成
     */
    protected boolean mIsPrepared = false;
    /**
     * 是否第一次加载
     */
    protected boolean mIsFirst = true;
    protected boolean isVisibleLoad = false;
    private boolean isLazyLoad = true;
    /**
     * 上下文
     */
    public Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public MBaseFragment setLazyLoad(boolean isLazy) {
        isLazyLoad = isLazy;
        return this;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mIsFirst = true;
        mIsPrepared = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
        if (!isLazyLoad) {
            firstLoadData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIsPrepared = true;
        mIsFirst = true;
        return setRootView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //加载数据方式，是否为懒加载
        if (isLazyLoad) {
            firstLoadData();
        }
    }

    protected void setVisibleLoadMode(boolean visible) {
        isVisibleLoad = visible;
    }

    private void onVisible() {
        if (!isVisibleLoad) {
            return;
        }
        if (mIsPrepared && !mIsFirst && getUserVisibleHint()) {
            loadData();
        }
    }

    private void onInvisible() {
        if (!isVisibleLoad) {
            return;
        }
        if (mIsPrepared && !mIsFirst && !getUserVisibleHint()) {
            //
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        onVisible();
        /*if (!mIsFirst && isVisibleToUser && mIsPrepared) {
            loadData();
        }*/

        //加载数据方式，是否为懒加载
        if (isLazyLoad) {
            firstLoadData();
        }
    }

    /**
     * 界面显示时加载数据
     */
    private void firstLoadData() {
        //用户可见，且页面创建完毕
        if (getUserVisibleHint() && mIsPrepared && mIsFirst) {
            mIsFirst = false;
            loadData();
        }
    }

    @Override
    public void onSetResult(Bundle data) {

    }

    @Override
    public void onHideLoading() {
    }

    @Override
    public void onShowLoading() {
    }

    @Override
    public void onShowMsg(@NonNull String msg) {
    }

    @Override
    public void onCloseSelf() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 创建fragment视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    public abstract View setRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化事件
     */
    public abstract void initEvent();

    /**
     * 网络加载数据
     */
    public abstract void loadData();
}
