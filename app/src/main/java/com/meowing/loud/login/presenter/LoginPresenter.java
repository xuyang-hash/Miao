package com.meowing.loud.login.presenter;

import android.app.Application;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.ErrorCodeManager;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.model.LoginModel;

import javax.inject.Inject;

@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    private Application mApplication;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View view, Application application) {
        super(model, view);
        this.mApplication = application;
    }

    /**
     * 账号登录
     *
     * @param username    用户名
     * @param password    密码
     * @param isUserLogin true为用户，false为管理员
     */
    public void onAccountLogin(String username, String password, boolean isUserLogin) {
        if (isUserLogin) {
            mModel.userLogin(username, password, new LoginModel.Listener() {
                @Override
                public void onSuccess(Object obj) {
                    mRootView.hideLoading();
                    mRootView.onAccountLoginResult();
                }

                @Override
                public void onFailed(int errorId) {
                    mRootView.hideLoading();
                    mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
                }
            });
        } else {
            mModel.adminLogin(username, password, new LoginModel.Listener() {
                @Override
                public void onSuccess(Object obj) {
                    mRootView.hideLoading();
                    mRootView.onAccountLoginResult();
                }

                @Override
                public void onFailed(int errorId) {
                    mRootView.hideLoading();
                    mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
                }
            });
        }
    }

    /**
     * 注册新用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public void registerUser(String username, String password) {

        mModel.registerUser(username, password, new LoginModel.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.onRegisterResult();
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }

    /**
     * 通过用户名去查找用户
     * @param username
     */
    public void findUser(String username) {
        mModel.findUser(username, new LoginModel.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.onFindUserResult(true, (UserResp) obj, 0);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.onFindUserResult(false, null, errorId);
            }
        });
    }
}
