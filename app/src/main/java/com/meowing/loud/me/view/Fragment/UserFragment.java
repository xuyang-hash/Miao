package com.meowing.loud.me.view.Fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentUserBinding;
import com.meowing.loud.me.contract.UserContract;
import com.meowing.loud.me.di.component.DaggerUserComponent;
import com.meowing.loud.me.di.module.UserModule;
import com.meowing.loud.me.presenter.UserPresenter;
import com.meowing.loud.me.view.Activity.UserCenterActivity;

public class UserFragment extends BaseFragment<FragmentUserBinding, UserPresenter> implements UserContract.View, View.OnClickListener {

    private UserResp userResp;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))//请将HomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }


    @Override
    public void initView(View mView) {
        binding.ivAccountSetting.setOnClickListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        showLoading();
        mPresenter.findUser();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_account_setting) {
            UserCenterActivity.start(getContext());
        }
    }

    @Override
    public void onFindUserResult(boolean isSuccess, UserResp userResp, int errorId) {
        if (isSuccess) {
            this.userResp = userResp;
            LocalDataManager.getInstance().setUserInfo(userResp);
            if (!StringUtils.isStringNULL(userResp.getImageString())) {
                binding.ivAccountHead.setImageBitmap(ArmsUtils.toBitmapFromString(userResp.getImageString()));
            }
            binding.tvAccountUsername.setText(userResp.getUsername());
        } else {
            ToastUtils.showShort(getContext(), R.string.common_connect_failed);
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
