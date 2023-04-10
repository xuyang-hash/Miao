package com.meowing.loud.login.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.databinding.FragmentSetConfidentialityLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;

public class SetConfidentialityFragment extends BaseFragment<FragmentSetConfidentialityLayoutBinding, LoginPresenter> implements LoginContract.View {

    public static SetConfidentialityFragment getInstance() {
        return new SetConfidentialityFragment();
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
}
