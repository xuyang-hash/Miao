package com.meowing.loud.login.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.constant.EventConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.entity.MessageWrap;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.ActivityRegisterLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;
import com.meowing.loud.login.view.fragment.InputPwdFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterActivity extends BaseActivity<ActivityRegisterLayoutBinding, LoginPresenter> implements LoginContract.View {

    private InputPwdFragment inputPwdFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void confirmToFinish() {
        super.confirmToFinish();
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
    public void initView() {
        inputPwdFragment = InputPwdFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, inputPwdFragment, "inputPwdFragment")
                .commit();
    }

    /**
     * 注册成功回调
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRegisterSuccess(MessageWrap messageWrap) {
        if (messageWrap.requestCode == EventConstant.ModuleLogin.ACCOUNT_REGISTER_SUCCESS) {
            confirmToFinish();
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
