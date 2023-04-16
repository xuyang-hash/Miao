package com.meowing.loud.arms.widget.controls;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.meowing.loud.R;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.integration.MoreClickManager;
import com.meowing.loud.arms.utils.ArmsUtils;

/**
 * 自定义控件参数说明：
 * <ul>
 * <li>BtValue：是否被选中，默认0
 * <li>NormalBkg：未按下的背景，默认为0
 * <li>SelectedBkg：按下后的背景，默认为0
 * <li>TextNormalColor：未按下的文字颜色，默认为R.color.default_selected_text_color
 * <li>TextSelectedColor：按下后的文字颜色，默认为R.color.default_selected_text_color
 * <li>TextSize：文本的尺寸，默认为12
 * <li>Text：文本内容，默认为null
 * <li>rightText:右侧文本内容，默认为null
 * </ul>
 * 自定义接口说明：
 * <ul>
 * <li>setOnButtonClick：设置点击监听 （接口方法返回值可设置是否作为checkBox）
 * </ul>
 *
 * @author
 * @ClassName: ButtonCheck
 * @Description: 可做CheckBox的带有图片，右侧文字，底部文字的 LinearLayout
 * @date 2016年7月18日 下午3:30:24
 */
public class ButtonCheck extends LinearLayout {
    private static final float TITLE_TEXT_DEFAULT_SIZE = 14f;
    private static final float TIP_TEXT_DEFAULT_SIZE = 12f;
    private static final float RIGHT_TEXT_DEFAULT_SIZE = 14f;
    public static final int STATE_DEFAULT = 0;
    public static final int STATE_CHECKED = 1;
    private LinearLayout mTab;
    private LinearLayout mLlContent;
    private ImageView mImage = null;
    // 0:Bottom,1:Right,2:left
    private TextView[] mTexts = new TextView[3];
    private String[] _texts = new String[3];
    private int _btnValue = AppConstant.Switch.Close;
    private int _imageID[] = new int[2];
    private int _textID[] = new int[2];
    private float rightTextLeftPadding;
    private float leftTextRightPadding;
    private float bottomTextTopPadding;
    private float contentPaddingTop;
    private float contentPaddingBottom;
    private float contentPaddingStart;
    private float contentPaddingEnd;
    private float _textSize;
    private int bottomLines;
    private OnButtonClickListener mOnBtnClickLs;
    //之前是否被击过
    private boolean isClickedBefore;
    //是否默认支持点击
    private boolean isDefaultClickable;
    private boolean isRippleBtnEnable;
    private boolean isSingleClick;
    private float imageWidth;
    private float imageHeight;

    public ButtonCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.bottom_tab_btn, this,
                true);
        TypedArray params = context.obtainStyledAttributes(attrs,
                R.styleable.ButtonCheck);
        // 取得
        _btnValue = params.getInteger(R.styleable.ButtonCheck_BtnValue, 0);
        _imageID[0] = params
                .getResourceId(R.styleable.ButtonCheck_NormalBkg, 0);
        _imageID[1] = params.getResourceId(R.styleable.ButtonCheck_SelectedBkg,
                0);
        _textID[0] = params.getColor(R.styleable.ButtonCheck_TextNormalColor,
                getResources().getColor(R.color.default_normal_text_color));
        _textID[1] = params.getColor(R.styleable.ButtonCheck_TextSelectedColor,
                getResources().getColor(R.color.default_selected_text_color));
        _textSize = params.getDimension(R.styleable.ButtonCheck_TextSize, ArmsUtils.sp2px(getContext(), TIP_TEXT_DEFAULT_SIZE));
        _texts[0] = params.getString(R.styleable.ButtonCheck_Text);
        _texts[1] = params.getString(R.styleable.ButtonCheck_rightText);
        _texts[2] = params.getString(R.styleable.ButtonCheck_leftText);
        bottomLines = params.getInteger(R.styleable.ButtonCheck_android_lines, 0);
        rightTextLeftPadding = params.getDimension(R.styleable.ButtonCheck_rightTextLeftPadding, 5);
        leftTextRightPadding = params.getDimension(R.styleable.ButtonCheck_leftTextRightPadding, 5);
        bottomTextTopPadding = params.getDimension(R.styleable.ButtonCheck_bottomTextTopPadding, 0);
        isDefaultClickable = params.getBoolean(R.styleable.ButtonCheck_DefaultClickable, true);
        contentPaddingTop = params.getDimension(R.styleable.ButtonCheck_contentPaddingTop, 0);
        contentPaddingBottom = params.getDimension(R.styleable.ButtonCheck_contentPaddingBottom, 0);
        contentPaddingStart = params.getDimension(R.styleable.ButtonCheck_contentPaddingStart, 0);
        contentPaddingEnd = params.getDimension(R.styleable.ButtonCheck_contentPaddingEnd, 0);
        isRippleBtnEnable = params.getBoolean(R.styleable.ButtonCheck_RippleBtnEnable, false);
        isSingleClick = params.getBoolean(R.styleable.ButtonCheck_singleClick, false);
        imageWidth = params.getDimension(R.styleable.ButtonCheck_imageWidth, 0);
        imageHeight = params.getDimension(R.styleable.ButtonCheck_imageHeight, 0);
        params.recycle();
    }

    @Override
    public void setBackgroundResource(int resId) {
        mImage.setImageResource(resId);
    }

    public void setBackgroundBitmap(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    /**
     * @param @param resId 设定文件
     * @return void 返回类型
     * @throws
     * @Title: setNormalBg
     * @Description: 设置图片
     */
    public void setNormalBg(int resId) {
        _imageID[0] = resId;
        if (_btnValue == AppConstant.Switch.Close) {
            mImage.setImageResource(resId);
        }
    }

    /**
     * @param @param resId 设定文件
     * @return void 返回类型
     * @throws
     * @Title: setSelectedBg
     * @Description: 设置选中后图片
     */
    public void setSelectedBg(int resId) {
        _imageID[1] = resId;
        if (_btnValue == AppConstant.Switch.Open) {
            mImage.setImageResource(resId);
        }
    }

    /**
     * @param @param value 设定文件
     * @return void 返回类型
     * @throws
     * @Title: setBtnValue
     * @Description: 设置是否被选中
     */
    public void setBtnValue(int value) {
        _btnValue = value;
        mImage.setImageResource(_imageID[_btnValue != AppConstant.Switch.Close ? AppConstant.Switch.Open : AppConstant.Switch.Close]);
        mImage.requestLayout();
        for (TextView textView : mTexts) {
            if (textView != null) {
                textView.setTextColor(_textID[_btnValue]);
            }
        }
    }

    /**
     * @param @return 设定文件
     * @return int 1为选中，0为否
     * @throws
     * @Title: getBtnValue
     * @Description: 获得是否被选中
     */
    public int getBtnValue() {
        return _btnValue;
    }

    public void Checked(boolean bChecked) {
        setBtnValue(bChecked ? AppConstant.Switch.Open : AppConstant.Switch.Close);
    }

    public boolean IsChecked() {
        return _btnValue == AppConstant.Switch.Open;
    }

    /**
     * @param @param msg 设定文件
     * @return void 返回类型
     * @throws
     * @Title: setBottomText
     * @Description: 设置底部文字
     */
    public void setBottomText(String msg) {
        _texts[0] = msg;
        mTexts[0].setText(msg);
    }

    public void setBottomTextVisible(int visible) {
        mTexts[0].setVisibility(visible);
    }

    /**
     * @param @param msg 设定文件
     * @return void 返回类型
     * @throws
     * @Title: setRightText
     * @Description: 设置右侧文字
     */
    public void setRightText(String msg) {
        _texts[1] = msg;
        mTexts[1].setText(msg);
    }

    public String getRightText() {
        if (mTexts[1] != null) {
            return mTexts[1].getText().toString();
        } else {
            return "";
        }
    }

    /**
     * @param @param msg 设定文件
     * @return void 返回类型
     * @throws
     * @Title: setLeftText
     * @Description: 设置左侧文字
     */
    public void setLeftText(String msg) {
        _texts[2] = msg;
        mTexts[2].setText(msg);
    }

    public String getLeftText() {
        if (mTexts[2] != null) {
            return mTexts[2].getText().toString();
        } else {
            return "";
        }
    }

    public void setImageWidth(float width) {
        if (width > 0 && mImage != null) {
            imageWidth = width;
            ViewGroup.LayoutParams layoutParams = mImage.getLayoutParams();
            if (layoutParams != null && imageWidth > 0 && imageHeight > 0) {
                layoutParams.width = (int) imageWidth;
                layoutParams.height = (int) imageHeight;
            }
        }
    }

    public void setImageHeight(float height) {
        if (height > 0 && mImage != null) {
            imageHeight = height;
            ViewGroup.LayoutParams layoutParams = mImage.getLayoutParams();
            if (layoutParams != null && imageWidth > 0 && imageHeight > 0) {
                layoutParams.width = (int) imageWidth;
                layoutParams.height = (int) imageHeight;
            }
        }
    }

    /**
     * 当View中所有的子控件 均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTab = findViewById(R.id.tab);
        mLlContent = findViewById(R.id.ll_content);
        mImage = findViewById(R.id.tab_image);
        mTexts[0] = findViewById(R.id.tab_tv_bottom);
        mTexts[1] = findViewById(R.id.tab_tv_right);
        mTexts[2] = findViewById(R.id.tab_tv_left);

        for (int i = 0; i < mTexts.length; ++i) {
            if (_texts[i] != null) {
                mTexts[i].setText(_texts[i]);
                mTexts[i].setTextSize(COMPLEX_UNIT_PX, _textSize);
                mTexts[i].setTextColor(_textID[_btnValue]);
            } else {
                mTexts[i].setVisibility(View.GONE);
            }
        }

        if (mTexts[0] != null && bottomTextTopPadding > 0) {
            mTexts[0].setPadding(0, (int) bottomTextTopPadding, 0, 0);
        }

        if (mTexts[1] != null && rightTextLeftPadding > 0) {
            mTexts[1].setPadding((int) rightTextLeftPadding, 0, 0, 0);
        }

        if (mTexts[2] != null && leftTextRightPadding > 0) {
            mTexts[2].setPadding(0, 0, (int) leftTextRightPadding, 0);
        }
        mImage.setImageResource(_imageID[_btnValue]);

        if (bottomLines > 0 && mTexts[0] != null) {
            mTexts[0].setMaxLines(bottomLines);
            mTexts[0].setEllipsize(TextUtils.TruncateAt.END);
        }

        mTab.setPadding((int) contentPaddingStart, (int) contentPaddingTop, (int) contentPaddingEnd, (int) contentPaddingBottom);
        if (isRippleBtnEnable) {
            mTab.setBackgroundResource(R.drawable.ripple_btn_selector);
        }

        ViewGroup.LayoutParams layoutParams = mImage.getLayoutParams();
        if (layoutParams != null && imageWidth > 0 && imageHeight > 0) {
            layoutParams.width = (int) imageWidth;
            layoutParams.height = (int) imageHeight;
        }

        mTab.setClickable(isDefaultClickable);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isDefaultClickable) {
            mTab.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isSingleClick) {
                        boolean isMoreClick = MoreClickManager.getInstance().isMoreClick(v.hashCode());
                        if (isMoreClick) {
                            return;
                        }
                    }

                    // 如果点击监听不为空，且返回值为true
                    isClickedBefore = true;
                    if (mOnBtnClickLs != null
                            && mOnBtnClickLs.onButtonClick(ButtonCheck.this,
                            _btnValue == AppConstant.Switch.Open)) {
                        _btnValue = _btnValue == AppConstant.Switch.Close ? AppConstant.Switch.Open : AppConstant.Switch.Close;
                    }

                    mImage.setImageResource(_imageID[_btnValue]);
                    if (_textID != null) {
                        for (TextView textView : mTexts) {
                            if (textView != null) {
                                textView.setTextColor(_textID[_btnValue]);
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * @param @param onBtnClickLs 监听器
     * @return void 返回类型
     * @Title: setOnButtonClick
     * @Description: 设置点击监听
     */
    public void setOnButtonClick(OnButtonClickListener onBtnClickLs) {
        this.mOnBtnClickLs = onBtnClickLs;

    }

    public boolean isClickedBefore() {
        return isClickedBefore;
    }


    public interface OnButtonClickListener {
        /**
         * @param bc             当前控件
         * @param bBeforeChecked BtValue==1
         * @return boolean 返回类型 true:作为CheckBox false:不作为CheckBox
         * @Title: onButtonClick
         * @Description: 设定是否作为checkbox，并实现点击事件
         */
        boolean onButtonClick(ButtonCheck bc, boolean bBeforeChecked);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mImage != null) {
            mImage.setEnabled(enabled);
            mTab.setEnabled(enabled);
        }

        for (TextView textView : mTexts) {
            if (textView != null) {
                textView.setEnabled(enabled);
            }
        }
    }

    private void dealWithAllViewCompleteDisplay() {

    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        dealWithAllViewCompleteDisplay();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMaxWidth(int width) {

    }

    public void setMaxHeight(int height) {
        int textHeight = mTexts[0].getHeight();
        ViewGroup.LayoutParams layoutParams = mLlContent.getLayoutParams();
        if (layoutParams != null) {
            if (height > 0) {
                layoutParams.height = height - textHeight;
            } else {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        mLlContent.requestLayout();
    }

    public void setRippleBtnEnable(boolean isEnable) {
        this.isRippleBtnEnable = isEnable;

        if (isEnable) {
            mTab.setBackgroundResource(R.drawable.ripple_btn_selector);
        } else {
            mTab.setBackgroundResource(-1);
        }
    }
}
