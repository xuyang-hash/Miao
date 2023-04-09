package com.meowing.loud.arms.base.code;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * 错误码解析
 */
public class ErrorCodeManager {
    public static boolean isSuccessCode(int code, Class<?> _class) {
        Field[] fields = _class.getFields();
        for (Field field : fields) {
            try {
                SuccessCode successCode = (SuccessCode) field.get(_class);
                if (successCode != null) {
                    if (code == successCode.getCode()) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析错误码
     * @param context
     * @param errorCode
     * @param defaultErrorId

     * @param _class
     * @return
     */
    public static String parseErrorCode(Context context, int errorCode, int defaultErrorId, Class<?> _class) {
        //账号相关错误
        Field[] fields = _class.getFields();
        for (Field field : fields) {
            try {
                BusinessCode businessCode = (BusinessCode) field.get(_class);
                if (businessCode != null) {
                    if (errorCode == businessCode.getCode()) {
                        return context.getString(businessCode.getMessage());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return context.getString(defaultErrorId);
    }
}

