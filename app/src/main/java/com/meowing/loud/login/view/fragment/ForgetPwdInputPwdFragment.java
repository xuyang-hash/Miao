package com.meowing.loud.login.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.constant.EventConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.entity.MessageWrap;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.arms.utils.VerificationUtils;
import com.meowing.loud.databinding.FragmentInputPwdLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;
import com.meowing.loud.login.view.activity.ForgetPasswordActivity;

import org.greenrobot.eventbus.EventBus;

public class ForgetPwdInputPwdFragment extends BaseFragment<FragmentInputPwdLayoutBinding, LoginPresenter> implements LoginContract.View, View.OnClickListener {

    public static ForgetPwdInputPwdFragment getInstance() {
        return new ForgetPwdInputPwdFragment();
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
        binding.tvBindTitle.setText(R.string.account_reset_pwd_title);
        ArmsUtils.showPwdLevel(getContext(), binding.etPassword1, binding.passwordPowerPoor, binding.passwordPowerMiddle, binding.passwordPowerStrong);
        binding.etPassword1.setHint(R.string.account_reset_pwd_new_hint);
        binding.etPassword2.setHint(R.string.account_reset_pwd_confirm_hint);
        binding.tvInputPwdSubmit.setText(R.string.common_submit);
        binding.llNicknameInput.setVisibility(View.GONE);
        binding.tvInputPwdSubmit.setOnClickListener(this);
        binding.cbEye1.setOnClickListener(this);
        binding.cbEye2.setOnClickListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_input_pwd_submit) {

            String username = null;
            if (ForgetPasswordActivity.userResp != null) {
                username = ForgetPasswordActivity.userResp.getUsername();
            }
            String password = binding.etPassword1.getText().toString().trim();
            String passwordConfirm = binding.etPassword2.getText().toString().trim();
            if (StringUtils.isStringNULL(username)) {
                ToastUtils.showShort(getContext(), R.string.account_reset_pwd_mate_user_error);
                return;
            }

            if (StringUtils.isStringNULL(password)) {
                ToastUtils.showShort(getContext(), R.string.account_password_is_empty);
                return;
            }

            if (StringUtils.isStringNULL(passwordConfirm)) {
                ToastUtils.showShort(getContext(), R.string.common_input_second_password);
                return;
            }

            if (!StringUtils.contrast(password, passwordConfirm)) {
                ToastUtils.showShort(getContext(), R.string.common_edit_pwd_error6);
                return;
            }

            if (!VerificationUtils.isCorrectPassword(getContext(), password, true)) {
                return;
            }
            showLoading();
            mPresenter.updatePass(username, password);
        } else if (id == R.id.cb_eye_1 || id == R.id.cb_eye_2) {
            boolean afterState = !binding.cbEye1.isSelected();
            binding.cbEye1.setSelected(afterState);
            binding.cbEye2.setSelected(afterState);
            setEditTextVisible(binding.etPassword1, !afterState);
            setEditTextVisible(binding.etPassword2, !afterState);
        }
    }

    @Override
    public void updatePassResult() {
        ToastUtils.showShort(getContext(), R.string.account_reset_pwd_success);
        EventBus.getDefault().post(new MessageWrap(EventConstant.ModuleLogin.ACCOUNT_FORGET_PWD_UPDATE_SUCCESS, null));
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
