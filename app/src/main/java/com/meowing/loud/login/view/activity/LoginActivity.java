package com.meowing.loud.login.view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.presenter.LoginPresenter;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @Override
    protected void initView() {
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }
}
