package com.meowing.loud.arms.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;

import org.jetbrains.annotations.NotNull;

public abstract class BaseCustomDialog extends CenterPopupView {
    protected DialogCallback mDialogCallback;
    public BaseCustomDialog(@NonNull @NotNull Context context) {
        super(context);
    }

    /**
     * 修改已显示的弹窗内容
     *
     * @param content
     */
    public void setContent(String content) {

    }

    public interface DialogCallback {
        default void onConfirm() {

        }

        default void onConfirm(String content) {

        }

        default void onCancel() {
        }

        default void onDismiss() {
        }

        default void onLeftImageOnClickListener() {
        }

        default void onMidImageOnClickListener() {
        }

        default void onRightImageOnCLickListener() {

        }
    }
}
