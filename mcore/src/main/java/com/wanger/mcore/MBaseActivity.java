package com.wanger.mcore;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import static com.wanger.mcore.MBaseSetting.compatDensity;
import static com.wanger.mcore.MBaseSetting.compatScaledDensity;
import static com.wanger.mcore.MBaseSetting.normalValue;
import static com.wanger.mcore.MBaseSetting.widthDp;

/**
 * @author wanger
 * @date 2019/4/16 14:25
 * @email xxx@gmail.com
 * @desc Activity 的基类，项目继承
 */
public abstract class MBaseActivity extends AppCompatActivity implements IViewAction {
    protected View mDecorView;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MBaseApp.addAc(this);
        setCustomDensity(this.getApplication());
        mDecorView = getWindow().getDecorView();
        mContext = this;
        initView(savedInstanceState);
        initData();
        initEvent();
        loadData();
    }

    @Override
    public void onSetResult(Bundle data) {
        Intent intent = getIntent();
        intent.putExtras(data);
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onCloseSelf() {
        this.finish();
    }

    /**
     * 设置缩放比例，适配尺寸终极解决方案
     *
     * @param application app
     */
    public void setCustomDensity(final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (compatDensity == normalValue) {
            compatDensity = appDisplayMetrics.density;
            compatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        compatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        float targetDensity = appDisplayMetrics.widthPixels / widthDp;
        float targetScaledDensity = targetDensity * (compatScaledDensity / compatDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        //缩放因子，正常情况下和density相等
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        //屏幕密度
        appDisplayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics activityDisplayMetrics = application.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    @Override
    protected void onDestroy() {
        MBaseApp.removeAc(this);
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化事件
     */
    public abstract void initEvent();

    /**
     * 初始化视图
     */
    public abstract void initView(Bundle savedInstanceState);

    @Override
    public void onShowLoading() {

    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onShowMsg(String msg) {

    }

    @Override
    public void onError(String error) {

    }
}
