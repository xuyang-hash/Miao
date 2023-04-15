package com.meowing.loud.login.view.activity;

import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.constant.ARouterConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.ActivityLoginLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;

@Route(path = ARouterConstant.LoginConstant.LOGIN_PAGE)
public class LoginActivity extends BaseActivity<ActivityLoginLayoutBinding, LoginPresenter> implements LoginContract.View, View.OnClickListener {

    /**
     * 用户名缓存
     */
    private String username;
    /**
     * 密码缓存
     */
    private String password;

    /**
     * 登录角色缓存
     */
    private boolean isUserLogin;
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
        isUserLogin = true;
        /**
         * 登录角色选择监听
         */
        binding.rbLoginTypeChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String tipsTitle = getString(R.string.account_login_type_tips);
                if (i == binding.rbLoginTypeUser.getId() && binding.rbLoginTypeUser.isChecked()) {
                    ToastUtils.showShort(LoginActivity.this, tipsTitle + getString(R.string.account_login_type_user_title));
                    isUserLogin = true;
                } else if (i == binding.rbLoginTypeAdmin.getId() && binding.rbLoginTypeAdmin.isChecked()) {
                    ToastUtils.showShort(LoginActivity.this, tipsTitle + getString(R.string.account_login_type_admin_title));
                    isUserLogin = false;
                }
            }
        });

        binding.cbEye.setOnClickListener(this);

        binding.tvLogin.setOnClickListener(this);
        binding.tvForgetPassword.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);

        binding.btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_login) {
            login();
        } else if (id == R.id.tv_forget_password) {
            ForgetPasswordActivity.start(this);
        } else if (id == R.id.tv_register) {
            RegisterActivity.start(this);
        } else if (id == R.id.cb_eye) {
            boolean afterState = !binding.cbEye.isSelected();
            binding.cbEye.setSelected(afterState);
            setEditTextVisible(binding.etPassword, !afterState);
        }
    }

    private void login() {
        username = binding.etUsername.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        if (StringUtils.isStringNULL(username)) {
            ToastUtils.showShort(this, R.string.account_login_input_account);
            return;
        }

        if (StringUtils.isStringNULL(password)) {
            ToastUtils.showShort(this, R.string.account_login_input_password);
            return;
        }
        showLoading();
        mPresenter.onAccountLogin(username, password, isUserLogin);
    }

    @Override
    public void onAccountLoginResult() {
        goMain();
    }

    private void goMain() {
        ToastUtils.showShort(this, R.string.account_login_success);
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
