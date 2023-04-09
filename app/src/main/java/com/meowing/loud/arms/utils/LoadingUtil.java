package com.meowing.loud.arms.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.meowing.loud.R;

public class LoadingUtil {
    private Dialog mDialog;
//    private View contentView;
//    private ImageView imgProgress;
    /**
     * 等待框翻转
     */
    private ObjectAnimator rotation;
    /**
     * 等待框背景透明度
     */
    private ValueAnimator alpha;
//    private Window window;

    public LoadingUtil(Context context) {
        mDialog = new Dialog(context, R.style.custom_dialog);
        View contentView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog_loading_view, null);
        mDialog.setContentView(contentView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);

        ImageView imgProgress = (ImageView) contentView.findViewById(R.id.iv_progress);
        rotation = ObjectAnimator.ofFloat(imgProgress, "rotation", 0f, 359f);
        rotation.setRepeatCount(ObjectAnimator.INFINITE);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setDuration(500);

//        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                if (rotation != null) {
//                    rotation.start();
//                }
//            }
//        });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (rotation != null) {
                    rotation.cancel();
                }
            }
        });
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams p = window.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        p.width = (int) (dm.widthPixels * 0.4);
        window.setAttributes(p);
    }

    public Dialog getmDialog() {
        return mDialog;
    }

    public void setmDialog(Dialog mDialog) {
        this.mDialog = mDialog;
    }

    /**
     * 开启透明度变化动画
     */
    private void startAlphaAnimator() {
        alpha = ObjectAnimator.ofFloat(0f);
        alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    if (mDialog != null && alpha != null) {
                        synchronized (alpha) {
                            Window window = mDialog.getWindow();
                            if (window != null) {
                                //透明度值
                                float value = (Float) animation.getAnimatedValue();
                                //等待框外部的背景从完全透明到半透明
                                window.setDimAmount(value);
                                System.out.println("startAlphaAnimator:" + value);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alpha.setRepeatCount(0);
        alpha.setRepeatMode(ValueAnimator.REVERSE);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(1000);
        alpha.start();
    }

    private void stopAlphaAnimator() {
        if (alpha != null) {
            synchronized (alpha) {
                alpha.removeAllUpdateListeners();
                alpha.cancel();
                alpha = null;
                System.out.println("stopAlphaAnimator:" + getContext().hashCode());
            }
        }
        if(rotation != null){
            rotation.removeAllUpdateListeners();
            rotation.cancel();
            rotation = null;
        }
    }

    public void show() {
        if (mDialog != null) {
            startAlphaAnimator();
            mDialog.show();
        }
    }

    public void show(String content) {
        if (mDialog != null) {
            startAlphaAnimator();
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            stopAlphaAnimator();
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }

        return false;
    }

    public void destroy() {
        dismiss();
//        window = null;
        mDialog = null;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(cancel);
        }
    }

    public void setCancelable(boolean cancel) {
        if (mDialog != null) {
            mDialog.setCancelable(cancel);
        }
    }

    public Context getContext() {
        if (mDialog != null) {
            return mDialog.getContext();
        }

        return null;
    }
}
