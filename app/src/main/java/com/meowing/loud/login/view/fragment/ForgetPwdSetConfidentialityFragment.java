package com.meowing.loud.login.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentSetConfidentialityLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;

public class ForgetPwdSetConfidentialityFragment extends BaseFragment<FragmentSetConfidentialityLayoutBinding, LoginPresenter> implements LoginContract.View{

    public static ForgetPwdSetConfidentialityFragment getInstance() {
        return new ForgetPwdSetConfidentialityFragment();
    }
    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initView(View mView) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void error(String msg) {
        hideLoading();
        ToastUtils.showShort(getContext(), msg);
    }

    @Override
    public void showLoading() {
        CSeeLoadingDialog.getInstance(getContext()).show();
    }

    @Override
    public void hideLoading() {
        CSeeLoadingDialog.getInstance(getContext()).dismiss();
    }
}
