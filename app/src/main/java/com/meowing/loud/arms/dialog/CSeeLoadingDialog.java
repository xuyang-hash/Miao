package com.meowing.loud.arms.dialog;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.meowing.loud.arms.utils.LoadingUtil;

public class CSeeLoadingDialog {
    private static final int ANIMATION_DURATION = 300;
    private LoadingUtil mLoadingUtil;
    private static CSeeLoadingDialog mInstance = null;

    public static CSeeLoadingDialog getInstance(Context context) {
        try {
            if (mInstance == null) {
                mInstance = new CSeeLoadingDialog(context);
            } else if (((mInstance.mLoadingUtil.getContext()) != null) && !((ContextWrapper) mInstance.mLoadingUtil.getContext()).getBaseContext().equals(context)
                    || mInstance.mLoadingUtil.getmDialog() == null) {
                mInstance.destroy();
                mInstance = new CSeeLoadingDialog(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mInstance = new CSeeLoadingDialog(context);
        }
        return mInstance;
    }

    public static CSeeLoadingDialog getNewInstance(Context context) {
        try {
            CSeeLoadingDialog CSeeLoadingDialog = new CSeeLoadingDialog(context);
            return CSeeLoadingDialog;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private CSeeLoadingDialog(Context context) {
        init(context);
    }

    private void init(Context context) {
        if (context == null) {
            return;
        }

        if (mLoadingUtil == null) {
            mLoadingUtil = new LoadingUtil(context);
        }
    }


    /**
     * @Title: show @Description: TODO(显示带文字的对话框) @param @param str 设定文件 @return
     * void 返回类型 @throws
     */
    public void show(String str) {
        try {
            if (mLoadingUtil != null && !mLoadingUtil.isShowing()) {
                Log.e("xy===", "showLoding show: ");
                mLoadingUtil.show(str);
            }
        } catch (Exception e) {
            Log.e("xy===", "showLoding error: ");
            e.printStackTrace();
        }
    }

    /**
     * @Title: show @Description: TODO(显示对话框) @param 设定文件 @return void
     * 返回类型 @throws
     */
    public void show() {
        try {
            if (mLoadingUtil != null && !mLoadingUtil.isShowing()) {
                Log.e("zx===", "showLoding show: ");
                mLoadingUtil.show();
            }
        } catch (Exception e) {
            Log.e("zx===", "showLoding error: ");
            e.printStackTrace();
        }
    }

    /**
     * @Title: dismiss @Description: TODO(使对话框消失) @param 设定文件 @return void
     * 返回类型 @throws
     */
    public void dismiss() {
        try {
            if (mLoadingUtil != null && mLoadingUtil.isShowing()) {
                Log.e("zx===", "LodingUtil dismiss: ");
                mLoadingUtil.dismiss();
            }
        } catch (Exception e) {
            Log.e("zx===", "LodingUtil dismiss error: ");
            e.printStackTrace();
        }
    }

    /**
     * @Title: setCanceledOnTouchOutside @Description:
     * TODO(注册外部点击取消监听) @param @param cancel 设定文件 @return void
     * 返回类型 @throws
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        if (mLoadingUtil != null) {
            mLoadingUtil.setCanceledOnTouchOutside(cancel);
        }
    }

    /**
     * 设置对话框是否取消在按下物理按键后
     *
     * @param cancel
     */
    public void setCancelable(boolean cancel) {
        if (mLoadingUtil != null) {
            mLoadingUtil.setCancelable(cancel);
        }
    }

    /**
     * @Title: destroy
     * @Description: TODO(销毁) @param 设定文件 @return void
     * 返回类型 @throws
     */
    public void destroy() {
        if (null != mLoadingUtil) {
            if (mLoadingUtil.isShowing()) {
                try {
                    mLoadingUtil.destroy();
                } catch (Exception e) {
                    System.out.println(this.getClass().getSimpleName() + "发生异常！！！！");
                    e.printStackTrace();
                }
            }
            mLoadingUtil = null;
        }
    }

    public static boolean isSameDialog(Context context) {
        try {
            if (mInstance != null && mInstance.mLoadingUtil != null) {
                if (((ContextWrapper) mInstance.mLoadingUtil.getContext()).getBaseContext().equals(context)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
