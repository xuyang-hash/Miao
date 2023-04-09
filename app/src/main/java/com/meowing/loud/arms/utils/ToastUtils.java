package com.meowing.loud.arms.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.meowing.loud.R;

public class ToastUtils {
    public static void showShort(Context context, int resId) {

        TextView tv = new TextView(context);
        tv.setText(context.getString(resId));
        tv.setBackground(context.getDrawable(R.drawable.bg_gray_ef_r16));
        tv.setPadding(ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10), ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10));
        tv.setTextColor(context.getResources().getColor(R.color.content_color));
        tv.setGravity(Gravity.CENTER);

        Toast toast = new Toast(context);
        toast.setView(tv);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showShort(Context context, String content) {

        TextView tv = new TextView(context);
        tv.setText(content);
        tv.setBackground(context.getDrawable(R.drawable.bg_gray_ef_r16));
        tv.setPadding(ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10), ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10));
        tv.setTextColor(context.getResources().getColor(R.color.content_color));
        tv.setGravity(Gravity.CENTER);

        Toast toast = new Toast(context);
        toast.setView(tv);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showLong(Context context, int resId) {

        TextView tv = new TextView(context);
        tv.setText(context.getString(resId));
        tv.setBackground(context.getDrawable(R.drawable.bg_gray_ef_r16));
        tv.setPadding(ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10), ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10));
        tv.setTextColor(context.getResources().getColor(R.color.content_color));
        tv.setGravity(Gravity.CENTER);

        Toast toast = new Toast(context);
        toast.setView(tv);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showLong(Context context, String content) {

        TextView tv = new TextView(context);
        tv.setText(content);
        tv.setBackground(context.getDrawable(R.drawable.bg_gray_ef_r16));
        tv.setPadding(ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10), ArmsUtils.dip2px(context, 16), ArmsUtils.dip2px(context, 10));
        tv.setTextColor(context.getResources().getColor(R.color.content_color));
        tv.setGravity(Gravity.CENTER);

        Toast toast = new Toast(context);
        toast.setView(tv);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
