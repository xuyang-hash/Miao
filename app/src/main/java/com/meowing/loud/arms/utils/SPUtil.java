package com.meowing.loud.arms.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SPUtil {
    private static final String SP_NAME = "Meowing";
    private static SPUtil instance;
    private SharedPreferences sp;
    SharedPreferences.Editor editor;

    private SPUtil(Context context) {
        this.sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SPUtil(context);
        }
        return instance;
    }

    public void setSettingParam(String key, boolean value) {
        if (null == editor) {
            editor = sp.edit();
        }
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setSettingParam(String key, String value) {
        if (null == editor) {
            editor = sp.edit();
        }
        editor.putString(key, value);
        editor.commit();
    }

    public void setLongParam(String key, long value) {
        if (null == editor) {
            editor = sp.edit();
        }
        editor.putLong(key, value);
        editor.commit();
    }

    public static boolean isTopActivity(Activity act) {
        if (act == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            String _className = tasksInfo.get(0).topActivity.getClassName();
            if (act.getClass().getName().equals(_className)) {
                return true;
            }
        }
        return false;
    }

    public static int ApplicationRunningState(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return 0;
            }

            for (ActivityManager.RunningAppProcessInfo ap : appProcesses) {
                if (ap.processName.equals(context.getPackageName())) {
                    if (ap.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                        return ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
                    } else if (ap.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                        return ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                    } else if (ap.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {

                        return ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE;
                    } else {
                        return 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setSettingParam(String key, int value) {
        if (null == editor) {
            editor = sp.edit();
        }
        editor.putInt(key, value);
        editor.commit();
    }

    public void setSettingParam(String key, long value) {
        if (null == editor) {
            editor = sp.edit();
        }
        editor.putLong(key, value);
        editor.commit();
    }

    public void setSettingParam(String key, float value) {
        if (null == editor) {
            editor = sp.edit();
        }
        editor.putFloat(key, value);
        editor.commit();
    }

    public boolean getSettingParam(String key, boolean defValue) {
        try {
            return sp.getBoolean(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            return defValue;
        }

    }

    public String getSettingParam(String key, String defValue) {
        try {
            return sp.getString(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            return defValue;
        }

    }

    public static int strCountByByte(String s) {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

    public float getSettingParam(String key, float defValue) {
        try {
            return sp.getFloat(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            return defValue;
        }
    }

    public int getSettingParam(String key, int defValue) {
        try {
            return sp.getInt(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            return defValue;
        }

    }


    public long getSettingParam(String key) {
        try {
            return sp.getLong(key, -1);
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }

    }

    public boolean isFirstTimeUseXWorld() {

        if (sp.getBoolean("isFirstTimeUseXWorld", true)) {
            editor = sp.edit();
            editor.putBoolean("isFirstTimeUseXWorld", false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean isFirstTimeUsePlayBack() {

        if (sp.getBoolean("isFirstTimeUsePlayBack", true)) {
            editor = sp.edit();
            editor.putBoolean("isFirstTimeUsePlayBack", false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 以简约模式显示设备列表
     *
     * @return
     */
    public boolean getDeviceListDisplayAsSimple() {
        try {
            return sp.getBoolean("DeviceListDisplayAsSimple", false);
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 设置是否以简约模式显示设备列表
     *
     * @return
     */
    public void setDeviceListDisplayAsSimple(boolean asSimple) {
        try {
            editor = sp.edit();
            editor.putBoolean("DeviceListDisplayAsSimple", asSimple);
            editor.commit();
        } catch (Exception e) {

        }
    }

    public boolean clearCache() {
        if (editor == null) {
            editor = sp.edit();
        }
        editor.clear();
        return editor.commit();
    }

    public boolean clearGeneralCache(String... settingKeys) {
        String key;
        Map<String, Object> value = (Map<String, Object>) sp.getAll();
        if (editor == null) {
            editor = sp.edit();
        }
        Iterator iterator = value.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();

            key = entry.getKey();
            if (null != key) {
                for (String settingKey : settingKeys) {
                    if (key.contains(settingKey)) {
                        editor.remove(entry.getKey());
                    }
                }
            }
        }
        return editor.commit();
    }

    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * 是否第一次进入哪一页面
     *
     * @param which
     * @return
     */
    public boolean isFirstInter(String which) {
        if (sp.getBoolean(which, true)) {
            editor = sp.edit();
            editor.putBoolean(which, false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public void setFirstInter(String which) {
        editor = sp.edit();
        editor.putBoolean(which, true);
        editor.commit();
    }
}
