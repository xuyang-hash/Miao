package com.meowing.loud.login.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.launcher.ARouter;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.constant.ARouterConstant;
import com.meowing.loud.arms.constant.EventConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.entity.MessageWrap;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.ActivityRegisterLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;
import com.meowing.loud.login.view.fragment.ForgetPwdInputPwdFragment;
import com.meowing.loud.login.view.fragment.ForgetPwdSetConfidentialityFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ForgetPasswordActivity extends BaseActivity<ActivityRegisterLayoutBinding, LoginPresenter> implements LoginContract.View {

    private ForgetPwdInputPwdFragment forgetPwdInputPwdFragment;
    private ForgetPwdSetConfidentialityFragment forgetPwdSetConfidentialityFragment;

    public static UserResp userResp;

    public static void start(Context context) {
        Intent starter = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this)) //请将RegisterModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public void confirmToFinish() {
        if (forgetPwdInputPwdFragment.isHidden()) {
            forgetPwdSetConfidentialityFragment.onDestroy();
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(forgetPwdInputPwdFragment)
                    .replace(R.id.fl_container, forgetPwdInputPwdFragment)
                    .commit();
            forgetPwdSetConfidentialityFragment = ForgetPwdSetConfidentialityFragment.getInstance();
        } else {
            finish();
        }
    }

    @Override
    public void initView() {
        forgetPwdInputPwdFragment = ForgetPwdInputPwdFragment.getInstance();
        forgetPwdSetConfidentialityFragment = ForgetPwdSetConfidentialityFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, forgetPwdSetConfidentialityFragment, "forgetPwdSetConfidentialityFragment")
                .commit();
    }

    /**
     * 注册成功回调
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventForgetPwdSuccess(MessageWrap messageWrap) {
        if (messageWrap.requestCode == EventConstant.ModuleLogin.ACCOUNT_FORGET_PWD_MATE_Q_AND_A_SUCCESS) {
            userResp = (UserResp) messageWrap.message;
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_right_in,
                            R.anim.slide_left_out,
                            R.anim.slide_left_in,
                            R.anim.slide_right_out
                    )
                    .hide(forgetPwdSetConfidentialityFragment)
                    .add(R.id.fl_container, forgetPwdInputPwdFragment)
                    .addToBackStack("forgetPwdInputPwdFragment")
                    .commit();
        } else if (messageWrap.requestCode == EventConstant.ModuleLogin.ACCOUNT_FORGET_PWD_UPDATE_SUCCESS) {
            ARouter.getInstance().build(ARouterConstant.LoginConstant.LOGIN_PAGE).navigation();
            finish();
        }
    }

    @Override
    public void error(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void showLoading() {
        CSeeLoadingDialog.getInstance(this).show();
    }

    @Override
    public void hideLoading() {
        CSeeLoadingDialog.getInstance(this).dismiss();
    }
}
