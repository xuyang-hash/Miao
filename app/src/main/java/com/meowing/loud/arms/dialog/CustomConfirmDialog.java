package com.meowing.loud.arms.dialog;

import static com.blankj.utilcode.util.StringUtils.getString;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.meowing.loud.R;
import com.meowing.loud.arms.utils.StringUtils;

import java.net.URL;

public class CustomConfirmDialog extends BaseCustomDialog {
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTitle;
    private TextView tvContent;
    private String title;
    private String content;
    private String confirmTitle = getString(R.string.common_confirm);
    private String cancelTitle = getString(R.string.common_cancel);

    private boolean isGravityCenter = true;
    private boolean isHideCancel = false;

    public void setDialogCallback(DialogCallback mDialogCallback) {
        this.mDialogCallback = mDialogCallback;
    }

    public CustomConfirmDialog(@NonNull Context context, String title, String content, boolean hideCancel) {
        super(context);
        this.title = title;
        this.isHideCancel = hideCancel;
        this.content = content;
    }

    public CustomConfirmDialog(@NonNull Context context, String title, String content) {
        super(context);
        this.title = title;
        this.content = content;
    }

    public CustomConfirmDialog(@NonNull Context context, String title, String content, boolean mideCancel, String cancelTitle) {
        super(context);
        this.title = title;
        this.isHideCancel = mideCancel;
        this.content = content;
        this.cancelTitle = cancelTitle;
    }

    public CustomConfirmDialog(@NonNull Context context, String title, String content, boolean mideCancel, boolean isContentGravityCenter, String cancelTitle, String confirmTitle) {
        super(context);
        this.title = title;
        this.isHideCancel = mideCancel;
        this.content = content;
        this.isGravityCenter = isContentGravityCenter;
        this.cancelTitle = cancelTitle;
        this.confirmTitle = confirmTitle;
    }


    final Html.ImageGetter imageGetter = new Html.ImageGetter() {

        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(), "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }
    };

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_common_confirm_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvTitle.setText(title);
        tvCancel.setText(cancelTitle);
        if (!StringUtils.isStringNULL(content)) {
            tvContent.setText(content);
            if (!isGravityCenter) {
                tvContent.setGravity(Gravity.LEFT);
            } else {
                tvContent.setGravity(Gravity.CENTER);
            }
        }

        if (isHideCancel) {
            tvCancel.setVisibility(GONE);
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
                    mDialogCallback.onConfirm();
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
}
