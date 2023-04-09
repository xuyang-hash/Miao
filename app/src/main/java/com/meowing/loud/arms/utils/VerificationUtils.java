package com.meowing.loud.arms.utils;

import android.content.Context;

import com.meowing.loud.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于正则表达式判断输入是否符合规范的工具类
 */
public class VerificationUtils {

    // 匹配中文、数字、字母、下划线的正则表达式：^[\u4E00-\u9FA5A-Za-z0-9_-]{4,32}
    private static final String MATCH_CORRECT_USERNAME_PATTERN = "^[\\u4E00-\\u9FA5A-Za-z0-9-]{4,32}"; // 去掉了下划线
    private static final String MATCH_CORRECT_PWD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)([A-Za-z\\d]|[\\[\\]{}*'`\\s^#$%+=_|~<>•.,?!:;()&@\\-]){8,64}$";
    private static final int CONTINUOUS_MAX_COUNT = 4;//连续（包括倒叙）数字/字母4个以上（含4个））
    private static final int OVERLAPPING_MAX_COUNT = 3;//或者重叠的数字/字母3个以上（含3个)
    /**
     * 合法的用户名
     * @param userName
     * @return
     */
    public static boolean isAccountUserNameCorrect(String userName) {
        try {
            Pattern pattern = Pattern.compile(MATCH_CORRECT_USERNAME_PATTERN);
            Matcher matcher = pattern.matcher(userName);
            if (!matcher.matches()) {
                return false;
            } else {
                pattern = Pattern.compile("[0-9]+");
                matcher = pattern.matcher(userName);
                return !matcher.matches();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * 合法的密码
     *
     * @param pwd 密码
     * @return 是否合法
     */
    public static boolean isCorrectPassword(Context context, String pwd, boolean showErrorDlg) {
        int errorId = 0;
        String weekPasswordString = null;
        try {
            Pattern pattern = Pattern.compile(MATCH_CORRECT_PWD_PATTERN);
            Matcher isOk = pattern.matcher(pwd);
            if (isOk.matches()) {
                weekPasswordString = XMWeakPassword.matchWeakPassword(pwd);
                char[] chars = pwd.toCharArray();
                //连续
                if (isContinuous(chars) || isAntiContinuous(chars)) {
                    errorId = 2;
                } else if (isOverlapping(chars)) {//重叠
                    errorId = 3;
                } else if (!StringUtils.isStringNULL(weekPasswordString)) {
                    errorId = 4;
                }

            } else {
                errorId = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorId = 1;
        }

        if (errorId != 0 && showErrorDlg) {
            if (context != null) {
                String errorTip = context.getString(R.string.common_edit_pwd_error1);
                String[] errorTips = new String[]{
                        "",
                        context.getString(R.string.common_edit_pwd_error1),
                        context.getString(R.string.common_edit_pwd_error2),
                        context.getString(R.string.common_edit_pwd_error3),
                        context.getString(R.string.common_edit_pwd_error4)
                };

                if (errorId < errorTips.length) {
//                    if (MPhoneUtils.isChineseLanguage(context)) {
                    errorTip = errorTips[errorId];
                    if (errorId == 4) {
                        errorTip = String.format(errorTip, weekPasswordString);
                    }
//                    }
                }
                ToastUtils.showShort(context, errorTip);
            }
        }
        return errorId == 0;
    }

    /**
     * 是否存在4个及以上正序连续
     *
     * @param chars
     * @return
     */
    public static boolean isContinuous(char[] chars) {
        int continuousCount = 1;
        if (chars != null && chars.length > 0) {
            int ascii = chars[0];
            for (int i = 1; i < chars.length; ++i) {
                // 正序
                if (chars[i] - ascii == 1) {
                    continuousCount++;
                } else {
                    continuousCount = 1;
                }

                ascii = chars[i];

                if (continuousCount >= CONTINUOUS_MAX_COUNT) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 是否存在4个及以上倒序连续
     *
     * @param chars
     * @return
     */
    public static boolean isAntiContinuous(char[] chars) {
        int continuousCount = 1;
        if (chars != null && chars.length > 0) {
            int ascii = chars[0];
            for (int i = 1; i < chars.length; ++i) {
                if (chars[i] - ascii == -1) {
                    continuousCount++;
                } else {
                    continuousCount = 1;
                }

                ascii = chars[i];

                if (continuousCount >= CONTINUOUS_MAX_COUNT) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 是否存在3个及以上重叠
     *
     * @return
     */
    public static boolean isOverlapping(char[] chars) {
        int overlappingCount = 1;
        if (chars != null && chars.length > 0) {
            int ascii = chars[0];
            for (int i = 1; i < chars.length; ++i) {
                if (chars[i] == ascii) {
                    overlappingCount++;
                } else {
                    overlappingCount = 1;
                }

                ascii = chars[i];
                if (overlappingCount >= OVERLAPPING_MAX_COUNT) {
                    return true;
                }
            }
        }

        return false;
    }
}
