package com.meowing.loud.me.presenter;

import android.app.Application;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.ErrorCodeManager;
import com.meowing.loud.arms.constant.MMKConstant;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.login.model.LoginModel;
import com.meowing.loud.me.contract.UserContract;

import javax.inject.Inject;

@ActivityScope
public class UserPresenter extends BasePresenter<UserContract.Model, UserContract.View> {

    private Application mApplication;
    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    @Inject
    public UserPresenter(UserContract.Model model, UserContract.View rootView, Application application) {
        super(model, rootView);
        this.mApplication = application;
    }

    public void updateHead(UserResp userResp) {
        mModel.updateHead(userResp, new UserContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.onUpdateHeadResult(true);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }

    public void findUser() {
        String username = MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME);
        if (!StringUtils.isStringNULL(username)) {
            mModel.findUser(username, new UserContract.Model.Listener() {
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

    /**
     * 查找所有我的音乐
     */
    public void findAllMineMusic() {
        String username = MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME);
        if (!StringUtils.isStringNULL(username)) {
            mModel.findAllMineMusic(username, new UserContract.Model.Listener() {
                @Override
                public void onSuccess(Object obj) {
                    mRootView.hideLoading();
                    mRootView.findAllMineMusicResult(true);
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
     * 更新用户密码
     * @param username
     * @param password
     */
    public void updatePass(String username, String password) {
        mModel.updatePass(username, password, new UserContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.updatePassResult();
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }
}
