package com.meowing.loud.arms.app;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.app.SyncNotedAppOp;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.App;
import com.meowing.loud.arms.base.delegate.AppDelegate;
import com.meowing.loud.arms.base.delegate.AppLifecycles;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.Preconditions;
import com.meowing.loud.arms.widget.refresh.CommonRefreshFooter;
import com.meowing.loud.arms.widget.refresh.CommonRefreshHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseApplication extends Application implements App {
    private AppLifecycles mAppDelegate;
    private static final String TAG = "BaseApplication";
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
    // 服务器版本
    public static int serverVersion = 2;
    private static final String NETWORK_MOBILE_TYPE = "MOBILE";
    private static final String NETWORK_WIFI_TYPE = "WIFI";
    private HashMap<String, Activity> actMap;
    private int initVal = -1;
    protected static BaseApplication instance;

    private boolean isFirstSDKNetSet = true;
    private String currentSsid = "default";
    private IntentFilter wifiFilter = null;
    //报警消息服务销毁后是否要处理其他事
    private boolean isAfterAlarmSDestroyDo;
    /**
     * APP本地保存图片路径
     */
    public static String PATH_PHOTO;
    /**
     * APP本地保存视频路径
     */
    public static String PATH_VIDEO;
    /**
     * APP本地临时图片路径
     */
    public static String PATH_PHOTO_TEMP;
    /**
     * APP本地临时抓图路径
     */
    public static String PATH_CAPTURE_TEMP;
    /**
     * APP本地推送图片路径
     */
    public static String PATH_PUSH_DOWNLOAD_PHOTO;
    /**
     * APP本地设备缩略图路径
     */
    public static String PATH_DEVICE_TEMP;
    /**
     * APP本地登录保存的设备列表
     */
    public static String PATH_LOCAL_DB;
    /**
     * APP本地保存的日志路径
     */
    public static String PATH_LOG;
    /**
     * APP本地保存第三方应用接收的文件路径
     */
    public static String PATH_RECEIVE_FILE;
    /**
     * 记录当前的Activity
     */
    private Activity curActivity = null;
    public static final String SERVER_IP = "223.4.33.127;54.84.132.236;112.124.0.188";
    public static final int SERVER_PORT = 15010; // 更新版本的服务器端口

    /**
     * 当前在前台的Acitity个数
     */
    private int activityCount = 0;
    /**
     * 当前APP是否处于前台
     */
    public static boolean isForeground = true;
    /**
     * 之前APP是否在前台
     */
    public static boolean isLastForeground = true;

    private boolean isSDKInit;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {


            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, android.R.color.white);//全局设置主题颜色
                return new CommonRefreshHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new CommonRefreshFooter(context);
            }
        });
    }

    public static BaseApplication getInstance() {
        return instance == null ? new BaseApplication() : instance;
    }

    /**
     * 这里会在 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegate(base);
        }

        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppOpsManager.OnOpNotedCallback appOpsCallback = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            appOpsCallback = new AppOpsManager.OnOpNotedCallback() {
                private void logPrivateDataAccess(String opCode, String trace) {
                    Log.e("TEST", "Private data accessed. Operation :" + opCode + "\nStackTrace : " + trace);
                }

                @Override
                public void onNoted(@NonNull SyncNotedAppOp syncNotedAppOp) {
                    logPrivateDataAccess(syncNotedAppOp.getOp(),
                            Arrays.toString(new Throwable().getStackTrace()));
                }

                @Override
                public void onSelfNoted(@NonNull SyncNotedAppOp syncNotedAppOp) {
                    logPrivateDataAccess(syncNotedAppOp.getOp(),
                            Arrays.toString(new Throwable().getStackTrace()));
                }

                @Override
                public void onAsyncNoted(@NonNull AsyncNotedAppOp asyncNotedAppOp) {
                    logPrivateDataAccess(asyncNotedAppOp.getOp(),
                            asyncNotedAppOp.getMessage());
                }
            };
        }

        AppOpsManager appOpsManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appOpsManager = getSystemService(AppOpsManager.class);
        }
        if (appOpsManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                appOpsManager.setOnOpNotedCallback(getMainExecutor(), appOpsCallback);
            }
        }

        if (mAppDelegate != null) {
            this.mAppDelegate.onCreate(this);
        }
        instance = this;
        ARouter.init(this);
        initSysConfig();
    }

    /**
     * 初始化系统默认配置
     */
    private void initSysConfig() {
        try {
            //加载系统默认设置，字体不随用户设置变化
            Resources res = super.getResources();
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this);
        }
    }

    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     *
     * @return AppComponent
     * @see ArmsUtils#obtainAppComponentFromContext(Context) 可直接获取 {@link AppComponent}
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", mAppDelegate.getClass().getName(), App.class.getName());
        return ((App) mAppDelegate).getAppComponent();
    }

    public void addActivity(Activity activity) {
        if (null == actMap) {
            actMap = new HashMap<String, Activity>();
        }
        actMap.put(activity.getClass().getSimpleName(), activity);
    }

    public void removeActivity(Activity activity) {
        if (actMap != null && activity != null) {
            String className = activity.getClass().getSimpleName();
            if (actMap.containsKey(className)) {
                if (curActivity == actMap.get(className)) {
                    curActivity = null;
                }
                actMap.remove(className);
            }
        }
    }

    public int getActivityCount() {
        if (null != actMap) {
            return actMap.size();
        }
        return 0;
    }

    public boolean isActivityExist(Class<?> _class) {
        if (null != actMap) {
            return actMap.containsKey(_class.getSimpleName());
        }
        return false;
    }

    public void returnToActivity(String _class) {
        Iterator<String> iter = actMap.keySet().iterator();
        while (iter.hasNext()) {
            String act = iter.next();
            if (!(act).equals(_class)) {
                actMap.get(act).finish();
            }
        }
        actMap.clear();
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        if (actMap == null) {
            return;
        }

        Iterator<String> iter = actMap.keySet().iterator();
        while (iter.hasNext()) {
            String act = iter.next();
            actMap.get(act).finish();
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public void removeActivityFromTaskStack(List<String> className) {
        Iterator<String> iter = actMap.keySet().iterator();
        for (String _class : className) {
            while (iter.hasNext()) {
                String act = iter.next();
                if ((act.getClass().getSimpleName()).equals(_class)) {
                    actMap.get(act).finish();
                    actMap.remove(act);
                }
            }
        }
    }

    public void exit() {
        for (Map.Entry<String, Activity> it : actMap.entrySet()) {
            Activity act = ((Activity) it.getValue());
            if (null != act) {
                act.finish();
            }
        }
    }

    public boolean isAfterAlarmSDestroyDo() {
        return isAfterAlarmSDestroyDo;
    }

    public void setAfterAlarmSDestroyDo(boolean afterAlarmSDestroyDo) {
        isAfterAlarmSDestroyDo = afterAlarmSDestroyDo;
    }

    public boolean isSDKInit() {
        return isSDKInit;
    }

    public static String getPathPhoto() {
        return PATH_PHOTO;
    }

    public static String getPathVideo() {
        return PATH_VIDEO;
    }

    public Activity getCurActive() {
        return curActivity;
    }

    /**
     * 设置当前Activity
     *
     * @param curActive
     */
    public void setCurActive(Activity curActive) {
        if (curActivity != null && curActivity.equals(curActive)) {
            return;
        }
        curActivity = curActive;
    }

    public static void listenAllBtns(ViewGroup layout, View.OnClickListener listener) {
        if (layout == null) {
            return;
        }
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = layout.getChildAt(i);
            if (v instanceof ViewGroup) {
                listenAllBtns((ViewGroup) v, listener);
            } else if (v instanceof SeekBar) {

            } else {// if (v instanceof Button || v instanceof ImageView || v
                // instanceof TextView) {
                // System.out.println(v.toString());
                v.setOnClickListener(listener);
            }
        }
    }
}

