package com.meowing.loud.login.view.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;
import com.meowing.loud.moneybao.databinding.ActivityLoginLayoutBinding;

public class LoginActivity extends BaseActivity<ActivityLoginLayoutBinding, LoginPresenter> implements LoginContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this)) //请将LoginModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public void initView() {

    }
}
