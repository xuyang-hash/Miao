package com.meowing.loud.arms.widget.controls;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public  class AnimationRelativeLayout extends RelativeLayout {
    private boolean down;
    private Paint maskPaint;
    private  Paint zonePaint;
    private  boolean isAnimationEffect;
    private OnClickListener l;
    public AnimationRelativeLayout(@NotNull Context context) {
        this(context, null);
    }

    public AnimationRelativeLayout(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationRelativeLayout(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.maskPaint = new Paint();
        this.zonePaint = new Paint();
        this.isAnimationEffect = true;
        this.enabled = true;
        this.init();
    }
    @Nullable
    private Boolean enabled;

    @Nullable
    public final Boolean getEnabled$MoneyBao_app() {
        return this.enabled;
    }

    public final void setEnabled$MoneyBao_app(@Nullable Boolean var1) {
        this.enabled = var1;
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        this.l = l;
    }

    private final void init() {
        this.maskPaint.setAntiAlias(true);
        this.maskPaint.setXfermode((Xfermode)(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)));
        this.zonePaint.setAntiAlias(true);
        this.zonePaint.setColor(-1);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setClickable(boolean clickable) {
        super.setClickable(true);
    }

    public boolean onTouchEvent(@NotNull MotionEvent event) {
        ScaleAnimation acaleAnimation = (ScaleAnimation)null;
        Rect viewRect = new Rect();
        this.getLocalVisibleRect(viewRect);
        boolean b = event.getX() < (float)viewRect.right && event.getX() > (float)viewRect.left && event.getY() < (float)viewRect.bottom && event.getY() > (float)viewRect.top;
        switch(event.getAction()) {
            case 0:
                if (!this.down) {
                    acaleAnimation = new ScaleAnimation(1.0F, this.getF(), 1.0F, this.getF(), 1, 0.5F, 1, 0.5F);
                    acaleAnimation.setDuration(100L);
                    acaleAnimation.setFillAfter(true);
                    if (this.isAnimationEffect) {
                        this.startAnimation((Animation)acaleAnimation);
                    }

                    this.down = true;
                    this.setPressed(true);
                }
                break;
            case 1:
                this.clearAnimation(acaleAnimation, b, true);
                break;
            case 2:
                if (!b) {
                    this.clearAnimation(acaleAnimation, b, false);
                }
                break;
            case 3:
                this.clearAnimation(acaleAnimation, b, false);
                break;
            default:
                this.clearAnimation(acaleAnimation, b, false);
        }

        return true;
    }

    private final float getF() {
        Resources var10003 = this.getResources();
        return 1.0F - (float)Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_FRACTION, 10.0F, var10003.getDisplayMetrics())) / (float)this.getWidth();
    }

    private final void clearAnimation(ScaleAnimation scaleAnimation, final boolean b, boolean up) {
        this.setPressed(false);
        if (this.down) {
            this.down = false;
            ScaleAnimation acaleAnimation = new ScaleAnimation(this.getF(), 1.0F, this.getF(), 1.0F, 1, 0.5F, 1, 0.5F);
            acaleAnimation.setDuration(100L);
            if (up) {
                acaleAnimation.setAnimationListener((Animation.AnimationListener)(new Animation.AnimationListener() {
                    public void onAnimationStart(@NotNull Animation paramAnimation) {

                    }

                    public void onAnimationRepeat(@NotNull Animation paramAnimation) {

                    }

                    public void onAnimationEnd(@NotNull Animation paramAnimation) {
                        if (AnimationRelativeLayout.this.l != null && b) {
                            OnClickListener var10000 = AnimationRelativeLayout.this.l;
                            var10000.onClick((View) AnimationRelativeLayout.this);
                        }

                    }
                }));
            }

            if (this.isAnimationEffect) {
                this.startAnimation(acaleAnimation);
            } else if (up && this.l != null && b) {
                OnClickListener var10000 = this.l;
                var10000.onClick((View)this);
            }
        }

    }



}

