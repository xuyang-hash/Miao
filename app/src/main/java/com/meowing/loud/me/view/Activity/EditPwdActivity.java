package com.meowing.loud.me.view.Activity;

import static com.meowing.loud.arms.constant.ReflectConstant.LOGIN_ACTIVITY;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.arms.utils.VerificationUtils;
import com.meowing.loud.databinding.ActivityEditPwdLayoutBinding;
import com.meowing.loud.login.view.activity.LoginActivity;
import com.meowing.loud.me.contract.UserContract;
import com.meowing.loud.me.di.component.DaggerUserComponent;
import com.meowing.loud.me.di.module.UserModule;
import com.meowing.loud.me.presenter.UserPresenter;

public class EditPwdActivity extends BaseActivity<ActivityEditPwdLayoutBinding, UserPresenter> implements UserContract.View, View.OnClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, EditPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))//请将HomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public void initView() {
        ArmsUtils.showPwdLevel(this, binding.etNewPassword, binding.passwordPowerPoor, binding.passwordPowerMiddle, binding.passwordPowerStrong);
        binding.cbEye1.setOnClickListener(this);
        binding.cbEye2.setOnClickListener(this);
        binding.cbEye3.setOnClickListener(this);
        binding.tvEditPwdSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_edit_pwd_submit) {
            String username = LocalDataManager.getInstance().getUserInfo().getUsername();

            String oldPassword = binding.etOldPassword.getText().toString().trim();
            String password = binding.etNewPassword.getText().toString().trim();
            String passwordConfirm = binding.etConfirmPassword.getText().toString().trim();
            if (StringUtils.isStringNULL(username)) {
                ToastUtils.showShort(this, R.string.account_reset_pwd_mate_user_error);
                return;
            }

            if (StringUtils.isStringNULL(oldPassword)) {
                ToastUtils.showShort(this, R.string.account_password_is_empty);
                return;
            }

            if (StringUtils.isStringNULL(password)) {
                ToastUtils.showShort(this, R.string.account_password_is_empty);
                return;
            }

            if (StringUtils.isStringNULL(passwordConfirm)) {
                ToastUtils.showShort(this, R.string.common_input_second_password);
                return;
            }

            if (!StringUtils.contrast(oldPassword, LocalDataManager.getInstance().getUserInfo().getPassword())) {
                ToastUtils.showShort(this, R.string.account_reset_pwd_old_error_tip);
                return;
            }

            if (!StringUtils.contrast(password, passwordConfirm)) {
                ToastUtils.showShort(this, R.string.common_edit_pwd_error1);
                return;
            }

            if (!VerificationUtils.isCorrectPassword(this, password, true)) {
                return;
            }
            showLoading();
            mPresenter.updatePass(username, password);
        } else if (id == R.id.cb_eye_1 || id == R.id.cb_eye_2 || id == R.id.cb_eye_3) {
            boolean afterState = !binding.cbEye1.isSelected();
            binding.cbEye1.setSelected(afterState);
            binding.cbEye2.setSelected(afterState);
            binding.cbEye3.setSelected(afterState);
            setEditTextVisible(binding.etOldPassword, !afterState);
            setEditTextVisible(binding.etNewPassword, !afterState);
            setEditTextVisible(binding.etConfirmPassword, !afterState);
        }
    }

    @Override
    public void updatePassResult() {
        ToastUtils.showShort(this, R.string.account_reset_pwd_success);
        LocalDataManager.getInstance().clear(true);
        //关闭除了登录页面的其他页面
        Class className = ReflectUtils.reflect(LOGIN_ACTIVITY).get();
        ActivityUtils.finishOtherActivities(className);
        //跳转到登录页面
        navigator(LoginActivity.class, true);
    }
}
