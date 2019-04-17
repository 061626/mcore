package com.wanger.mcore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wanger
 * @date 2019/4/16 14:24
 * @email xxx@gmail.com
 * @desc doc
 */
public class MBaseApp extends Application {
    public static Context AppContext;
    private static List<Activity> acList;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        acList = new ArrayList<>();
    }

    public static void addAc(Activity activity) {
        acList.add(activity);
    }

    public static void removeAc(Activity activity) {
        acList.remove(activity);
    }

    /**
     * 退出App
     */
    public static void exitApp() {
        Iterator<Activity> iterator = acList.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            acList.remove(activity);
            activity.finish();
        }
    }
}
