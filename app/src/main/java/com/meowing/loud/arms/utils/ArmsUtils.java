/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meowing.loud.arms.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.LocaleList;
import android.os.Process;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.App;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.integration.AppManager;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * ================================================
 * 一些框架常用的工具
 * <p>
 * Created by JessYan on 2015/11/23.
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ArmsUtils {
    private static final String SYS_XIAOMI = "xiaomi";
    static public Toast mToast;
    static Context mContext;

    private ArmsUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 设置hint大小
     *
     * @param size
     * @param v
     * @param res
     */
    public static void setViewHintSize(Context context, int size, TextView v, int res) {
        SpannableString ss = new SpannableString(getResources(context.getApplicationContext()).getString(
                res));
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        v.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    /**
     * dp 转 px
     *
     * @param context {@link Context}
     * @param dpValue {@code dpValue}
     * @return {@code pxValue}
     */
    public static int dip2px(@NonNull Context context, float dpValue) {
        final float scale = getResources(context.getApplicationContext()).getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     *
     * @param context {@link Context}
     * @param pxValue {@code pxValue}
     * @return {@code dpValue}
     */
    public static int pix2dip(@NonNull Context context, int pxValue) {
        final float scale = getResources(context.getApplicationContext()).getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp 转 px
     *
     * @param context {@link Context}
     * @param spValue {@code spValue}
     * @return {@code pxValue}
     */
    public static int sp2px(@NonNull Context context, float spValue) {
        final float fontScale = getResources(context.getApplicationContext()).getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px 转 sp
     *
     * @param context {@link Context}
     * @param pxValue {@code pxValue}
     * @return {@code spValue}
     */
    public static int px2sp(@NonNull Context context, float pxValue) {
        final float fontScale = getResources(context.getApplicationContext()).getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获得资源
     */
    public static Resources getResources(Context context) {
        return context.getApplicationContext().getResources();
    }

    /**
     * 得到字符数组
     */
    public static String[] getStringArray(Context context, int id) {
        return getResources(context.getApplicationContext()).getStringArray(id);
    }

    /**
     * 从 dimens 中获得尺寸
     *
     * @param context
     * @param id
     * @return
     */
    public static int getDimens(Context context, int id) {
        return (int) getResources(context.getApplicationContext()).getDimension(id);
    }

    /**
     * 从 dimens 中获得尺寸
     *
     * @param context
     * @param dimenName
     * @return
     */
    public static float getDimens(Context context, String dimenName) {
        return getResources(context.getApplicationContext()).getDimension(getResources(context.getApplicationContext()).getIdentifier(dimenName, "dimen", context.getPackageName()));
    }

    /**
     * 从String 中获得字符
     *
     * @return
     */

    public static String getString(Context context, int stringID) {
        return getResources(context.getApplicationContext()).getString(stringID);
    }

    /**
     * 从String 中获得字符
     *
     * @return
     */
    public static String getString(Context context, String strName) {
        return getString(context.getApplicationContext(), getResources(context.getApplicationContext()).getIdentifier(strName, "string", context.getPackageName()));
    }

    /**
     * findview
     *
     * @param view
     * @param viewName
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewByName(Context context, View view, String viewName) {
        int id = getResources(context.getApplicationContext()).getIdentifier(viewName, "id", context.getPackageName());
        return view.findViewById(id);
    }

    /**
     * findview
     *
     * @param activity
     * @param viewName
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewByName(Context context, Activity activity, String viewName) {
        int id = getResources(context.getApplicationContext()).getIdentifier(viewName, "id", context.getPackageName());
        return activity.findViewById(id);
    }

    /**
     * 根据 layout 名字获得 id
     *
     * @param layoutName
     * @return
     */
    public static int findLayout(Context context, String layoutName) {
        return getResources(context.getApplicationContext()).getIdentifier(layoutName, "layout", context.getPackageName());
    }

    /**
     * 填充view
     *
     * @param detailScreen
     * @return
     */
    public static View inflate(Context context, int detailScreen) {
        return View.inflate(context.getApplicationContext(), detailScreen, null);
    }

    /**
     * 单例 toast
     *
     * @param string
     */
    @SuppressLint("ShowToast")
    public static void makeText(Context context, String string) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_SHORT);
        }
        mToast.setText(string);
        mToast.show();
    }

    /**
     * 使用 {@link Snackbar} 显示文本消息
     * Arms 已将 com.google.android.material:material 从依赖中移除 (目的是减小 Arms 体积, design 库中含有太多 View)
     * 因为 Snackbar 在 com.google.android.material:material 库中, 所以如果框架使用者没有自行依赖 com.google.android.material:material
     * Arms 则会使用 Toast 替代 Snackbar 显示信息, 如果框架使用者依赖了 arms-autolayout 库就不用依赖 com.google.android.material:material 了
     * 因为在 arms-autolayout 库中已经依赖有 com.google.android.material:material
     *
     * @param text
     */
    public static void snackbarText(String text) {
        AppManager.getAppManager().showSnackbar(text, false);
    }

    /**
     * 使用 {@link Snackbar} 长时间显示文本消息
     * Arms 已将 com.google.android.material:material 从依赖中移除 (目的是减小 Arms 体积, design 库中含有太多 View)
     * 因为 Snackbar 在 com.google.android.material:material 库中, 所以如果框架使用者没有自行依赖 com.google.android.material:material
     * Arms 则会使用 Toast 替代 Snackbar 显示信息, 如果框架使用者依赖了 arms-autolayout 库就不用依赖 com.google.android.material:material 了
     * 因为在 arms-autolayout 库中已经依赖有 com.google.android.material:material
     *
     * @param text
     */
    public static void snackbarTextWithLong(String text) {
        AppManager.getAppManager().showSnackbar(text, true);
    }

    /**
     * 通过资源id获得drawable
     *
     * @param rID
     * @return
     */
    public static Drawable getDrawablebyResource(Context context, int rID) {
        return getResources(context.getApplicationContext()).getDrawable(rID);
    }

    /**
     * 跳转界面 1, 通过 {@link AppManager#startActivity(Class)}
     *
     * @param activityClass
     */
    public static void startActivity(Class activityClass) {
        AppManager.getAppManager().startActivity(activityClass);
    }

    /**
     * 跳转界面 2, 通过 {@link AppManager#startActivity(Intent)}
     *
     * @param
     */
    public static void startActivity(Intent content) {
        AppManager.getAppManager().startActivity(content);
    }

    /**
     * 跳转界面 3
     *
     * @param activity
     * @param homeActivityClass
     */
    public static void startActivity(Activity activity, Class homeActivityClass) {
        Intent intent = new Intent(activity.getApplicationContext(), homeActivityClass);
        activity.startActivity(intent);
    }

    /**
     * 跳转界面 4
     *
     * @param
     */
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getResources(context.getApplicationContext()).getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return getResources(context.getApplicationContext()).getDisplayMetrics().heightPixels;
    }

    /**
     * 获得颜色
     */
    public static int getColor(Context context, int rid) {
        return getResources(context.getApplicationContext()).getColor(rid);
    }

    /**
     * 获得颜色
     */
    public static int getColor(Context context, String colorName) {
        return getColor(context.getApplicationContext(), getResources(context.getApplicationContext()).getIdentifier(colorName, "color", context.getApplicationContext().getPackageName()));
    }

    /**
     * 移除孩子
     *
     * @param view
     */
    public static void removeChild(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(view);
        }
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
     * MD5
     *
     * @param string
     * @return
     * @throws Exception
     */
    public static String encodeToMD5(String string) {
        byte[] hash = new byte[0];
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hash = MessageDigest.getInstance("MD5").digest(
                        string.getBytes(StandardCharsets.UTF_8));
            } else {
                hash = MessageDigest.getInstance("MD5").digest(
                        string.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 全屏,并且沉侵式状态栏
     *
     * @param activity
     */
    public static void statuInScreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 配置 RecyclerView
     *
     * @param recyclerView
     * @param layoutManager
     * @deprecated Use {@link #configRecyclerView(RecyclerView, RecyclerView.LayoutManager)} instead
     */
    @Deprecated
    public static void configRecycleView(final RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 配置 RecyclerView
     *
     * @param recyclerView
     * @param layoutManager
     */
    public static void configRecyclerView(final RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 执行 {@link AppManager#killAll()}
     */
    public static void killAll() {
        AppManager.getAppManager().killAll();
    }

    /**
     * 执行 {@link AppManager#appExit()}
     */
    public static void exitApp() {
        AppManager.getAppManager().appExit();
    }

    public static AppComponent obtainAppComponentFromContext(Context context) {
        Preconditions.checkNotNull(context.getApplicationContext(), "%s cannot be null", Context.class.getName());
        Preconditions.checkState(context.getApplicationContext() instanceof App, "%s must be implements %s", context.getApplicationContext().getClass().getName(), App.class.getName());
        return ((App) context.getApplicationContext()).getAppComponent();
    }

    //////////  密码强度

    public static void showPwdLevel(Context context, EditText editText, View poor, View middle, View strong) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = editText.getText().toString().trim();
                int i = ArmsUtils.passwordStrong(str);
                if (i == 0) {
                    poor.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.theme_color));
                    middle.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.password_power_color));
                    strong.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.password_power_color));
                } else if (i == 1) {
                    poor.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.theme_color));
                    middle.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.theme_color));
                    strong.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.password_power_color));
                } else if (i == 2) {
                    poor.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.theme_color));
                    middle.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.theme_color));
                    strong.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.theme_color));
                } else {
                    poor.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.password_power_color));
                    middle.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.password_power_color));
                    strong.setBackgroundColor(getResources(context.getApplicationContext()).getColor(R.color.password_power_color));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 判断密码强度
     * 1. 单独一种：弱：纯数字N 纯大写字母L 纯小写字母l 特殊字符T 弱
     * 2. 两两组合 弱 ： NL Nl NT Ll LT lT
     * 3. 三三组合 中 : NLl
     * 4. 三三组合 带特殊字符 强： NLT NlT LlT
     * 5. 四种组合 强
     *
     * @return N = 数字 L = 大写字母 l = 小写字母 T=特殊字符
     */
    public static int passwordStrong(String passwordStr) {
        if (TextUtils.isEmpty(passwordStr)) {
            return -1;
        }
        int length = passwordStr.length();
        if (length < 8) {
            return 0;
        }

        // 纯数字
        String regexZ = "\\d*";
        // 纯大写字母
        String regexL = "^[A-Z]+$";
        // 纯小写字母
        String regexl = "^[a-z]+$";
        // 纯字符
        String regexT = "\\W+$";
        // 数字+大写字母
        String regexNL = "^[A-Z0-9]+$";
        // 数字+小写字母
        String regexNl = "^[a-z0-9]+$";
        // 大写字母 + 小写字母
        String regexLl = "^[A-Za-z]+$";
        // 数字+字符
        String regexNT = "[\\d\\W_]*+$";
        // 大写字母+字符
        String regexLT = "^[A-Z\\W_]*+$";
        // 小写字母+字符
        String regexlT = "^[a-z\\W_]*+$";
        // 大写字母 小写字母 数字 中等强度
        String regexNLl = "^[A-Za-z0-9]+$";
        // 大写字母 小写字母 字符
        String regexLlT = "^[a-zA-Z\\W_]+$";
        // 大写字母 数字 字符
        String regexNLT = "^[a-z0-9\\W_]+$";
        // 小写字母 数字 字符
        String regexNlT = "^[a-z0-9\\W_]+$";
        // 四种组合
        String regexNLlT = "^[A-Za-z0-9\\W_]+$";
        if (passwordStr.matches(regexZ) || passwordStr.matches(regexL) || passwordStr.matches(regexl)
                || passwordStr.matches(regexNL) || passwordStr.matches(regexNl) || passwordStr.matches(regexLl)
                || passwordStr.matches(regexNT) || passwordStr.matches(regexLT) || passwordStr.matches(regexlT)) {
            return 0;
        } else if (passwordStr.matches(regexNLl) || passwordStr.matches(regexLlT) || passwordStr.matches(regexNLT)
                || passwordStr.matches(regexNlT)) {
            return 1;
        } else if (passwordStr.matches(regexNLlT)) {
            return 2;
        } else {
            return -2;
        }
    }

    /**
     * 检查是否拥有权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        int perm = context.getApplicationContext().checkCallingOrSelfPermission(permission);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查定位权限是否开启
     *
     * @param context
     * @return
     */
    public static boolean checkLocationService(Context context) {
        if (context == null) {
            return true;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (context.getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isOpenGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isOpenNetWorkLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                return isOpenGPS || isOpenNetWorkLocation;
            } else {
                return false;
            }
        }

        return true;
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 设置自动语言
     */
    private static void autoLanguage(Context context) {
        mContext = context.getApplicationContext();
        Resources resources = context.getApplicationContext().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        String lan;
        Locale configLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lan = Resources.getSystem().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            lan = Locale.getDefault().getLanguage();
        }
        String country = Locale.getDefault().getCountry();
        configLocale = Locale.ENGLISH;
        updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        if (lan.compareToIgnoreCase("zh") == 0) {
            if (country.compareToIgnoreCase("TW") == 0
                    || country.compareToIgnoreCase("HK") == 0) {
                configLocale = Locale.TRADITIONAL_CHINESE;
                updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
            } else {
                configLocale = Locale.SIMPLIFIED_CHINESE;
                updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
            }
        } else if (lan.compareToIgnoreCase("ko") == 0) {
            if (country.compareToIgnoreCase("KR") == 0) {
                configLocale = Locale.KOREAN;
                updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
            }
        } else if (lan.compareToIgnoreCase("de") == 0) {
            configLocale = Locale.GERMAN;
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("tr") == 0) {
            configLocale = new Locale("tr", "TR");
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("es") == 0) {
            configLocale = new Locale("es", "ES");
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("pt") == 0) {
            configLocale = new Locale("pt", "PT");
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("fr") == 0) {
            configLocale = new Locale("fr", "FR");
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("ru") == 0) {
            configLocale = new Locale("ru", "RU");
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("ja") == 0) {
            configLocale = Locale.JAPANESE;
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("it") == 0) {
            configLocale = Locale.ITALIAN;
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("th") == 0) {
            configLocale = new Locale("th", "TH");
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        } else if (lan.compareToIgnoreCase("vi") == 0) {
            configLocale = new Locale("vi", "VI");
            updateConfiguration(config, dm, configLocale, context.getApplicationContext(), resources);
        }
    }

    /**
     * 更新语言配置
     *
     * @param config
     * @param dm
     * @param configLocale
     * @param context
     * @param resources
     */
    public static void updateConfiguration(Configuration config, DisplayMetrics dm, Locale configLocale, Context context, Resources resources) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(configLocale);
            LocaleList mLocaleList = new LocaleList(configLocale);
            mLocaleList.setDefault(mLocaleList);
            config.setLocales(mLocaleList);
        } else {
            config.locale = configLocale;
        }
        resources.updateConfiguration(config, dm);
    }

    /**
     * 获取系统环境语言
     */
    public static String getLanguageEnv() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        String lan = "en";
        String country = l.getCountry().toLowerCase();
        if (language.compareToIgnoreCase("zh") == 0) {
            if (country.compareToIgnoreCase("TW") == 0 || country.compareToIgnoreCase("HK") == 0) {
                lan = "zh_TW";
            } else {
                lan = "zh_CN";
            }
        } else if (language.compareToIgnoreCase("ko") == 0) {
            if (country.compareToIgnoreCase("KR") == 0) {
                lan = "ko_KR";//韩语
            }
        } else if (language.compareToIgnoreCase("vi") == 0) {
            lan = "vi_CN";//越南语
        } else if (language.compareToIgnoreCase("de") == 0) {
            lan = "de_US";//德语
        } else if (language.compareToIgnoreCase("es") == 0) {
            lan = "es_US";//西班牙语
        } else if (language.compareToIgnoreCase("fr") == 0) {
            lan = "fr_US";//法语
        } else if (language.compareToIgnoreCase("it") == 0) {
            lan = "it_US";//意大利语
        } else if (language.compareToIgnoreCase("ja") == 0) {
            lan = "ja_CN";//日本语
        } else if (language.compareToIgnoreCase("pt") == 0) {
            lan = "pt_BR";//葡萄牙语
        } else if (language.compareToIgnoreCase("th") == 0) {
            lan = "th_CN";//泰语
        } else if (language.compareToIgnoreCase("tr") == 0) {
            lan = "tr";   //土耳其语
        } else if (language.compareToIgnoreCase("ru") == 0) {
            lan = "ru_CN";//俄语
        } else if (language.compareToIgnoreCase("pl") == 0) {
            lan = "pl";//波兰语
        }

        return lan;
    }

    /**
     * 判断字符串是否为json格式
     *
     * @param str
     * @return
     */
    public static boolean isJsonStrByFastjson(String str) {
        if(str == null){
            return false;
        }
        try {
            JSONObject jsonElement = JSON.parseObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getSignatures(Activity activity) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA256");

            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(appendString);
                hexString.append(":");
            }
            String value = hexString.toString();
            String msg = "SHA256:" + value.substring(0, value.length() - 1);
            Log.d("zyh", "sha256::  " + msg);
            return value.substring(0, value.length() - 1);

        } catch (NoSuchAlgorithmException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * (x,y)是否在view的区域内
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    public static boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        return y >= top && y <= bottom && x >= left && x <= right;
    }

    /**
     * 检查是否开启通知权限
     *
     * @return
     */
    public static boolean checkNotificationAllowed() {
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(getContext());
        return notificationCompat.areNotificationsEnabled();
    }

    /**
     * 判断系统是否支持后台弹窗
     * （如果没有这个权限的，默认是支持）
     *
     * @return
     */
    public static boolean isAllowedBackstagePop(Context context) {
        AppOpsManager ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021;
            int uid = Process.myUid();
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, uid, context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 判断手机是否打开悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean isAllowedHoverWindow(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 是否允许后台弹窗权限
     * (Android 10以下默认为true)
     */
    public static boolean isAllowedBackgroundPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return true;
        }
        return isAllowedHoverWindow(context) && isAllowedBackstagePop(context);
    }

    /**
     * 获取手机底部导航栏高度
     * 方法链接：https://blog.csdn.net/baidu_32472003/article/details/96155894?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-96155894-blog-126948312.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-96155894-blog-126948312.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=1
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("zzw===", "Navi height:" + height);
        return height;
    }

    /**
     * 手机是否存在导航栏
     * 方法链接：https://blog.csdn.net/baidu_32472003/article/details/96155894?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-96155894-blog-126948312.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-96155894-blog-126948312.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=1
     * @param context
     * @return
     */
    public static boolean isHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 通过序列化和反序列化深度拷贝对象
     *
     * @param data
     * @return
     */
    public static <T> T deepClone(T data) {
        if (data != null) {
            return (T) new Gson().fromJson(new Gson().toJson(data), data.getClass());
        }
        return null;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将10进制变为16进制单字节字符串
     *
     * @param num
     */
    public static String intToHexString(int num) {
        return Integer.toHexString(num & 0x000000ff | 0xffffff00).substring(6);
    }

    /**
     * 将10进制变为16进制12bit字符串
     *
     * @param num
     */
    public static String intToHexString3(int num) {
        return Integer.toHexString(num & 0x00000fff | 0xfffff000).substring(5);
    }

    /**
     * 将10进制变为16进制双字节字符串
     *
     * @param num
     */
    public static String intToHexString2(int num) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((num >>> offset) & 0xff);
        }
        return StringUtils.bytes2HexString(targets);
    }


    /**
     * 将10进制长整形变为16进制四字节字符串
     *
     * @param num
     */
    public static String longToHexString(long num) {
        byte[] targets = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((num >>> offset) & 0xff);
        }
        return StringUtils.bytes2HexString(targets);
    }

    /**
     * 将10进制整形变为16进制四字节字符串
     *
     * @param num
     */
    public static String intToHexString4(int num) {
        byte[] targets = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((num >>> offset) & 0xff);
        }
        return StringUtils.bytes2HexString(targets);
    }

    public static Bitmap toBitmapFromString(String imageString) {
        byte[] image = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodeImg = BitmapFactory.decodeByteArray(image, 0, image.length);
        return decodeImg;
    }

    public static String toImageStringFromUri(Context context, Uri uri) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    

}

