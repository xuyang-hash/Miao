package com.meowing.loud.arms.utils;

import android.content.Context;

import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.constant.MMKConstant;

public class MeoSPUtil {
    static SPUtil spUtil;

    public static void init(Context context) {
        spUtil = SPUtil.getInstance(context);
    }

    public static void putString(String key, String value) {
        if (spUtil != null) {
            spUtil.setSettingParam(key, value);
        }
    }


    public static void putLong(String key, long value) {
        if (spUtil != null) {
            spUtil.setSettingParam(key, value);
        }
    }

    public static void putInt(String key, int value) {
        if (spUtil != null) {
            spUtil.setSettingParam(key, value);
        }
    }

    public static void putBoolean(String key, boolean value) {
        if (spUtil != null) {
            spUtil.setSettingParam(key, value);
        }
    }


    public static boolean getBoolean(String key) {
        if (spUtil != null) {
            return spUtil.getSettingParam(key, false);
        }

        return false;
    }


    public static boolean getBoolean(String key, boolean defaultValue) {
        if (spUtil != null) {
            return spUtil.getSettingParam(key, defaultValue);
        }

        return defaultValue;
    }

    public static int getInt(String key, int value) {
        if (spUtil != null) {
            return spUtil.getSettingParam(key, value);
        }

        return value;
    }

    public static long getLong(String key) {
        if (spUtil != null) {
            return spUtil.getSettingParam(key, 0);
        }

        return 0;
    }

    public static String getString(String key) {
        if (spUtil != null) {
            return spUtil.getSettingParam(key, "");
        }

        return "";
    }

    public static String getString(String key, String defaultValue) {
        if (spUtil != null) {
            return spUtil.getSettingParam(key, defaultValue);
        }

        return defaultValue;
    }

    public static boolean removePageKeyword(String tag) {
        if (spUtil != null) {
            return spUtil.clearGeneralCache(tag);
        }

        return false;
    }

    public static boolean contains(String key){
        if(spUtil != null){
            return  spUtil.contains(key);
        }
        return false;
    }

    public static boolean isUserLogin() {
        return getInt(MMKConstant.LOGIN_USER_TYPE, AppConstant.ROLE_TYPE_USER) == AppConstant.ROLE_TYPE_USER;
    }
}
