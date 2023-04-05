package com.meowing.loud.login.presenter;

import android.app.Application;

import com.google.gson.Gson;
import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.login.contract.LoginContract;

import javax.inject.Inject;

@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    private Application mApplication;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View view, Application application) {
        super(model, view);
        this.mApplication = application;
    }
}
