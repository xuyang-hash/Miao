package com.meowing.loud.arms.dialog;

import static com.blankj.utilcode.util.StringUtils.getString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.meowing.loud.R;
import com.meowing.loud.arms.utils.DialogUtil;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.arms.widget.EmojiInputFilter;

/**
 * 通用的编辑框对话框
 */
public class CustomEditDialog extends BaseCustomDialog {
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTitle;
    private EditText etContent;
    private String title;
    private String content;
    private String confirmTitle = getString(R.string.common_confirm);
    private String cancelTitle = getString(R.string.common_cancel);
    private int maxLength;
    private String hint;
    private Context mContext;

    private boolean isHideCancel = false;

    private int keyBoardType;  //-1:不设置（即默认设置），0：phone键盘，1：number键盘

    public void setDialogCallback(DialogCallback mDialogCallback) {
        this.mDialogCallback = mDialogCallback;
    }

    public CustomEditDialog(@NonNull Context context, String title, String content, boolean hideCancel) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.isHideCancel = hideCancel;
        this.content = content;
    }

    public CustomEditDialog(@NonNull Context context, String title, String content) {
        super(context);
        this.title = title;
        this.content = content;
    }

    public CustomEditDialog(@NonNull Context context, String title, String content, boolean mideCancel, String cancelTitle) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.isHideCancel = mideCancel;
        this.content = content;
        this.cancelTitle = cancelTitle;
    }

    public CustomEditDialog(@NonNull Context context, String title, String content, boolean mideCancel, String cancelTitle, String confirmTitle) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.isHideCancel = mideCancel;
        this.content = content;
        this.cancelTitle = cancelTitle;
        this.confirmTitle = confirmTitle;
    }

    public CustomEditDialog(@NonNull Context context, String title, String content, String hint, int maxLength, boolean mideCancel, String cancelTitle, String confirmTitle) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.isHideCancel = mideCancel;
        this.content = content;
        this.cancelTitle = cancelTitle;
        this.confirmTitle = confirmTitle;
        this.hint = hint;
        this.maxLength = maxLength;
    }

    public CustomEditDialog(@NonNull Context context, String title, String content, String hint, int maxLength, boolean mideCancel, String cancelTitle, String confirmTitle, int keyBoardType) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.isHideCancel = mideCancel;
        this.content = content;
        this.cancelTitle = cancelTitle;
        this.confirmTitle = confirmTitle;
        this.hint = hint;
        this.maxLength = maxLength;
        this.keyBoardType = keyBoardType;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_common_edit_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvTitle = findViewById(R.id.tv_title);
        etContent = findViewById(R.id.et_content);
        etContent.setHint(hint);
        tvTitle.setText(title);
        tvCancel.setText(cancelTitle);
        etContent.setText(content);
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength), new EmojiInputFilter(maxLength)});
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setConfirmButtonClickable(s.toString().length() == 0);
            }
        });
        if (isHideCancel) {
            tvCancel.setVisibility(GONE);
        }
        if (keyBoardType != -1) {
            setKeyBoardType();
        }

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogCallback != null) {
                    mDialogCallback.onCancel();
                }

            }
        });
        tvConfirm.setText(confirmTitle);

        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogCallback != null) {
                    mDialogCallback.onConfirm(etContent.getText().toString().trim());
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mDialogCallback != null) {
                    mDialogCallback.onDismiss();
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setConfirmButtonClickable(boolean isContentEmpty){
        if(isContentEmpty) {
            tvConfirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(mContext, hint);
                }
            });
            tvConfirm.setBackground(getResources().getDrawable(R.drawable.bg_unable_click_r_20));
        }else {
            tvConfirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mDialogCallback != null) {
                        mDialogCallback.onConfirm(etContent.getText().toString().trim());
                    }
                }
            });
            tvConfirm.setBackground(getResources().getDrawable(R.drawable.bg_theme_color_20));
        }
    }

    /**
     * 修改已显示的弹窗内容
     *
     * @param content
     */
    public void setContent(String content) {
        if (content != null && etContent != null) {
            etContent.setText(content);
            requestLayout();
        }
    }

    private void setKeyBoardType() {
        if (keyBoardType == DialogUtil.KEYBOARD_PHONE) {
            etContent.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (keyBoardType == DialogUtil.KEYBOARD_NUMBER) {
            etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }
}
