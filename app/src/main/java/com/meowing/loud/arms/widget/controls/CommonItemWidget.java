package com.meowing.loud.arms.widget.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.meowing.loud.R;

public class CommonItemWidget extends LinearLayout {
    private Switch switchButton;
    private ImageView ivIcon;
    private TextView tvSubtitle;
    private TextView tvBind;
    private TextView tvTips;
    private TextView tvFunctionName;
    private String title = "";
    private String subTitle = "";
    private String tips = "";
    private int titleTextStyle;
    private int tipsColor;
    public int tintColor;
    public int subTitleColor;
    public AnimationRelativeLayout rlItem;
    private boolean headerIconVisible = true;
    private boolean switchButtonVisible = false;
    private boolean needBindWidget = false;
    private Drawable drawable;
    private ImageView ivNavigator;
    private boolean navigatorVisible = true;
    private float fkTextSize;

    private ImageView ivAvatar;

    public interface ItemClickListener {
        void onItemClickListener(int resId);
    }

    public CommonItemWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public CommonItemWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs);
    }


    public void initLayout(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_common_item_layout, this);
        tvBind = findViewById(R.id.tv_bind);
        ivAvatar = findViewById(R.id.iv_avatar);
        switchButton = findViewById(R.id.sw_controller);
        ivIcon = findViewById(R.id.iv_icon);
        rlItem = findViewById(R.id.item);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        ivNavigator = findViewById(R.id.iv_navigator);
        tvFunctionName = findViewById(R.id.tv_function_name);
        tvTips = findViewById(R.id.tv_tips);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonItemWidget);
        needBindWidget = typedArray.getBoolean(R.styleable.CommonItemWidget_needBind, false);
        switchButtonVisible = typedArray.getBoolean(R.styleable.CommonItemWidget_switchVisible, false);
        subTitle = typedArray.getString(R.styleable.CommonItemWidget_subTitle);
        tipsColor = typedArray.getColor(R.styleable.CommonItemWidget_tipsColor,getResources().getColor(R.color.theme_color));
        fkTextSize = typedArray.getDimension(R.styleable.CommonItemWidget_fkTextSize,0);
        headerIconVisible = typedArray.getBoolean(R.styleable.CommonItemWidget_headerIconVisible, true);
        subTitleColor = typedArray.getColor(R.styleable.CommonItemWidget_subTitleColor, getResources().getColor(R.color.black));
        title = typedArray.getString(R.styleable.CommonItemWidget_title);
        tips = typedArray.getString(R.styleable.CommonItemWidget_tips);
        navigatorVisible = typedArray.getBoolean(R.styleable.CommonItemWidget_navigatorVisible, true);
        tintColor = typedArray.getColor(R.styleable.CommonItemWidget_iconColor, getResources().getColor(R.color.black));
        drawable = typedArray.getDrawable(R.styleable.CommonItemWidget_headerIcon);
        titleTextStyle = typedArray.getInt(R.styleable.CommonItemWidget_android_textStyle, 0);


        setIcon(drawable);
        setTitle(title);
        setSubTitle(subTitle);
        setSubtitleColor(subTitleColor);
//        setTintColor(tintColor);
        setTips(tips);
        setTipsColor(tipsColor);
        setBindClickListener(needBindWidget);
        setSwitchButtonVisible(switchButtonVisible);
        setNavigatorVisible(navigatorVisible);
        setHeaderIconVisible(headerIconVisible);
        rlItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onItemClickListener(getId());
                }
            }
        });
        tvBind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onItemClickListener(getId());
                }
            }
        });
        typedArray.recycle();

    }

    public void setBindClickListener(boolean isNeedBind) {
        if (isNeedBind) {
            tvBind.setVisibility(VISIBLE);
            tvBind.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onItemClickListener(rlItem.getId());
                    }
                }
            });
        }
    }

    private BindListener bindListener;

    public interface BindListener {
        void onBindListener(int resId);
    }

    public void setUserAvatar(String avatar) {
        Glide.with(getContext())
                .asBitmap()
                .apply(new RequestOptions().transform(new RoundedCorners(60)))
                .load(avatar)
                .into(ivAvatar);
    }

    private static final String TAG = "CommonItemWidget";
    private ItemClickListener onClickListener;


    public void setItemClickListener(ItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setHeaderIconVisible(boolean headerIconVisible) {
        ivIcon.setVisibility(headerIconVisible ? VISIBLE : GONE);
    }

    public void setNavigatorVisible(boolean isVisible) {
        ivNavigator.setVisibility(isVisible ? VISIBLE : GONE);
    }

    private void setSwitchButtonVisible(boolean isVisible) {
        switchButton.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setIcon(Drawable icon) {
        ivIcon.setImageDrawable(icon);
    }


    public void setSubtitleColor(int color) {
        tvSubtitle.setTextColor(color);
    }

    public void setTipsColor(int color) {
        tvTips.setTextColor(color);
    }

    public void setTitleColor(int color) {
        tvFunctionName.setTextColor(color);
    }

    public void setSubTitle(String subTitle) {
        tvSubtitle.setText(subTitle);

    }
    public void setSubTitleVisible(int  subTitle) {
        tvSubtitle.setVisibility(subTitle);
    }


    public void setBindVisible(int visible){
        tvBind.setVisibility(visible);
        invalidate();
    }

    public boolean isNeedBind(){
        //是否需要绑定
        return tvBind.getVisibility() == VISIBLE;
    }
    public void setTintColor(int color) {
        if (drawable != null) {
            DrawableCompat.setTint(drawable, color);
        }
    }


    public String getSubTitleContent() {
        return tvSubtitle.getText().toString();
    }

    public void setTitle(String title) {
        tvFunctionName.setText(title);
        try {
            if(fkTextSize >0){
                float s =  fkTextSize /  getResources().getDisplayMetrics().scaledDensity;
                tvFunctionName.setTextSize(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvFunctionName.setTypeface(Typeface.defaultFromStyle(titleTextStyle));

    }


    public void setTips(String tips) {
        tvTips.setText(tips);
    }


}