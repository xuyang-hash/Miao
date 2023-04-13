package com.meowing.loud.login.view.activity;

import android.content.Context;
import android.content.Intent;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.ActivityRegisterLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;
import com.meowing.loud.login.view.fragment.RegisterInputPwdFragment;
import com.meowing.loud.login.view.fragment.SetConfidentialityFragment;

public class ForgetPasswordActivity extends BaseActivity<ActivityRegisterLayoutBinding, LoginPresenter> implements LoginContract.View {

    private RegisterInputPwdFragment registerInputPwdFragment;
    private SetConfidentialityFragment setConfidentialityFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, RegisterActivity.class);
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
        if (registerInputPwdFragment.isHidden()) {
            setConfidentialityFragment.onDestroy();
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(registerInputPwdFragment)
                    .replace(R.id.fl_container, registerInputPwdFragment)
                    .commit();
            setConfidentialityFragment = SetConfidentialityFragment.getInstance();
        } else {
            finish();
        }
    }

    @Override
    public void initView() {
        registerInputPwdFragment = RegisterInputPwdFragment.getInstance();
        setConfidentialityFragment = SetConfidentialityFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, registerInputPwdFragment, "registerInputPwdFragment")
                .commit();
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
