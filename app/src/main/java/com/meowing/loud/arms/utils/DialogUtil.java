package com.meowing.loud.arms.utils;

import android.content.Context;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.meowing.loud.R;
import com.meowing.loud.arms.dialog.BaseCustomDialog;
import com.meowing.loud.arms.dialog.CustomConfirmDialog;
import com.meowing.loud.arms.dialog.CustomEditDialog;

import java.util.HashMap;

public class DialogUtil {
    private Context mContext;
    private String title;
    private String content;
    private BaseCustomDialog.DialogCallback mDialogCallback;
    private String cancelTitle;
    private String confirmTitle;
    private BaseCustomDialog loginOutDialog;
    private static HashMap<Object, Long> isDialogShow = new HashMap<>();
    private static final int SAME_DLG_SHOW_TIMES = 5000; //相同弹窗未消失的时候5s内只修改弹窗内容；
    private String hint;
    private int maxLength;
    private boolean isHideCancel = false;
    private boolean isCancelable = false;   //默认点击提示框外部不消失
    private boolean isCenterGravity = true; // 默认中间文本居中
    private int keyboardType = -1;  //Edit键盘设置(-1即默认设置)
    public static int KEYBOARD_PHONE = 0;   //Edit弹窗，弹起phone键盘
    public static int KEYBOARD_NUMBER = 1;  //Edit弹窗，弹起number键盘
    private int resLeft = -1;
    private int resMid = -1;
    private int resRight = -1;

    public DialogUtil setDialogCallback(BaseCustomDialog.DialogCallback mDialogCallback) {
        this.mDialogCallback = mDialogCallback;
        return this;
    }

    private static boolean isShowing(Object key) {
        if (isDialogShow != null) {
            synchronized (isDialogShow) {
                if (isDialogShow.containsKey(key)) {
                    long showTime = isDialogShow.get(key);
                    if ((System.currentTimeMillis() - showTime) <= SAME_DLG_SHOW_TIMES) {
                        return true;
                    }
                }
            }

            isDialogShow.put(key, System.currentTimeMillis());
        }

        return false;
    }

    public DialogUtil(Context mContext, String mTitle, String mContent, boolean misHideCancel, boolean isCancelable) {
        this.mContext = mContext;
        title = mTitle;
        isHideCancel = misHideCancel;
        content = mContent;
        cancelTitle = mContext.getString(R.string.common_cancel);
        confirmTitle = mContext.getString(R.string.common_confirm);
        this.isCancelable = isCancelable;
    }

    public DialogUtil(Context mContext, String mTitle, String mContent, boolean misHideCancel, boolean isCancelable, boolean isCenterGravity) {
        this.mContext = mContext;
        title = mTitle;
        isHideCancel = misHideCancel;
        content = mContent;
        cancelTitle = mContext.getString(R.string.common_cancel);
        confirmTitle = mContext.getString(R.string.common_confirm);
        this.isCenterGravity = isCenterGravity;
        this.isCancelable = isCancelable;
    }

    public DialogUtil(Context mContext, String mTitle, String mContent) {
        this.mContext = mContext;
        title = mTitle;
        content = mContent;
        cancelTitle = mContext.getString(R.string.common_cancel);
        confirmTitle = mContext.getString(R.string.common_confirm);
    }

    public DialogUtil(Context mContext, String mTitle, String mContent, String mCancelTitle, String mConfirmTitle) {
        this.mContext = mContext;
        title = mTitle;
        cancelTitle = StringUtils.isStringNULL(mCancelTitle) ? mContext.getString(R.string.common_cancel) : mCancelTitle;
        content = mContent;
        confirmTitle = StringUtils.isStringNULL(mConfirmTitle) ? mContext.getString(R.string.common_confirm) : mConfirmTitle;
    }

    public DialogUtil(Context mContext, String mTitle, String mContent, String mCancelTitle, String mConfirmTitle, boolean isCancelable) {
        this.mContext = mContext;
        title = mTitle;
        cancelTitle = StringUtils.isStringNULL(mCancelTitle) ? mContext.getString(R.string.common_cancel) : mCancelTitle;
        content = mContent;
        confirmTitle = StringUtils.isStringNULL(mConfirmTitle) ? mContext.getString(R.string.common_confirm) : mConfirmTitle;
        this.isCancelable = isCancelable;
    }


    /**
     * 需要输入提示语的editDlg
     *
     * @param mContext
     * @param mTitle
     * @param mContent
     * @param hint
     * @param misHideCancel
     * @param isCancelable
     */
    public DialogUtil(Context mContext, String mTitle, String mContent, String hint, int maxLength, boolean misHideCancel, boolean isCancelable) {
        this.mContext = mContext;
        title = mTitle;
        content = mContent;
        isHideCancel = misHideCancel;
        this.isCancelable = isCancelable;
        this.hint = hint;
        this.maxLength = maxLength;
        cancelTitle = mContext.getString(R.string.common_cancel);
        confirmTitle = mContext.getString(R.string.common_confirm);
    }

    /**
     * 需要输入提示语的editDlg
     *
     * @param mContext
     * @param mTitle
     * @param mContent
     * @param hint
     * @param misHideCancel
     * @param isCancelable
     */
    public DialogUtil(Context mContext, String mTitle, String mContent, String hint, int maxLength, boolean misHideCancel, boolean isCancelable, int keyboardType) {
        this.mContext = mContext;
        title = mTitle;
        content = mContent;
        isHideCancel = misHideCancel;
        this.isCancelable = isCancelable;
        this.hint = hint;
        this.maxLength = maxLength;
        cancelTitle = mContext.getString(R.string.common_cancel);
        confirmTitle = mContext.getString(R.string.common_confirm);
        this.keyboardType = keyboardType;
    }

    public DialogUtil(Context mContext, String mTitle, String mContent, String mCancelTitle, String mConfirmTitle, boolean isCancelable, int resLeft, int resRight) {
        this.mContext = mContext;
        title = mTitle;
        cancelTitle = mCancelTitle;
        content = mContent;
        confirmTitle = mConfirmTitle;
        this.isCancelable = isCancelable;
        this.resLeft = resLeft;
        this.resRight = resRight;
    }

    public DialogUtil(Context mContext, String mTitle, String mContent, String mCancelTitle, String mConfirmTitle, boolean isCancelable, int resLeft, int resMid, int resRight) {
        this.mContext = mContext;
        title = mTitle;
        cancelTitle = mCancelTitle;
        content = mContent;
        confirmTitle = mConfirmTitle;
        this.isCancelable = isCancelable;
        this.resLeft = resLeft;
        this.resMid = resMid;
        this.resRight = resRight;
    }

    public void show() {

        loginOutDialog = new CustomConfirmDialog(mContext, title, content, isHideCancel, isCenterGravity, cancelTitle, confirmTitle);
        ((CustomConfirmDialog) loginOutDialog).setDialogCallback(new CustomConfirmDialog.DialogCallback() {
            @Override
            public void onConfirm() {
                if (mDialogCallback != null) {
                    mDialogCallback.onConfirm();
                }
            }

            @Override
            public void onCancel() {

                if (mDialogCallback != null) {
                    mDialogCallback.onCancel();
                }
            }

            @Override
            public void onDismiss() {
                dismissDlg(content);
                if (mDialogCallback != null) {
                    mDialogCallback.onDismiss();
                }
            }
        });
        new XPopup.Builder(mContext)
                .dismissOnTouchOutside(isCancelable)
                .dismissOnBackPressed(isCancelable)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asCustom(loginOutDialog)
                .show();
    }

    /**
     * 显示带编辑框的对话框
     */
    public void showEditDlg() {

            loginOutDialog = new CustomEditDialog(mContext, title, content, hint, maxLength, isHideCancel, cancelTitle, confirmTitle, keyboardType);
            ((CustomEditDialog) loginOutDialog).setDialogCallback(new CustomEditDialog.DialogCallback() {
                @Override
                public void onConfirm(String content) {
                    if (mDialogCallback != null) {
                        mDialogCallback.onConfirm(content);
                    }
                }

                @Override
                public void onCancel() {
                    if (mDialogCallback != null) {
                        mDialogCallback.onCancel();
                    }
                }

                @Override
                public void onDismiss() {
                    dismissDlg(content);
                    if (mDialogCallback != null) {
                        mDialogCallback.onDismiss();
                    }
                }
            });
            new XPopup.Builder(mContext)
                    .dismissOnTouchOutside(isCancelable)
                    .dismissOnBackPressed(isCancelable)
                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                    .asCustom(loginOutDialog)
                    .show();
    }

    /**
     * 修改已显示的弹窗内容
     *
     * @param content
     */
    public void setContent(String content) {
        if (loginOutDialog != null) {
            loginOutDialog.setContent(content);
        }
    }

    private static void dismissDlg(Object key) {
        if (isDialogShow != null) {
            synchronized (isDialogShow) {
                if (isDialogShow.containsKey(key)) {
                    isDialogShow.remove(key);
                }
            }
        }
    }

    /**
     * 消失弹窗
     */
    public void dismiss() {
        if (loginOutDialog != null) {
            loginOutDialog.dismiss();
        }
    }

    public boolean isDialogShow() {
        if (loginOutDialog != null) {
            return loginOutDialog.isShow();
        }
        return false;
    }
}

