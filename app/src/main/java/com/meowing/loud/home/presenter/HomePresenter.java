package com.meowing.loud.home.presenter;

import android.app.Application;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.ErrorCodeManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.home.contract.HomeContract;

import javax.inject.Inject;

public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    private Application mApplication;

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView, Application application) {
        super(model, rootView);
        this.mApplication = application;
    }

    /**
     * 获取所有待审核的音乐
     */
    public void findAllWaitMusicData() {
        mModel.findAllWaitMusicData(new HomeContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.findAllWaitMusicResult();
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }

    /**
     * 获取所有审核通过的音乐
     */
    public void findAllPassMusicData() {
        mModel.findAllPassMusicData(new HomeContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.findAllPassMusicResult();
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }

    /**
     * 获取所有审核未通过的音乐
     */
    public void findAllRefuseMusicData() {
        mModel.findAllRefuseMusicData(new HomeContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.findAllRefuseMusicResult();
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }

    /**
     * 更新音乐赞
     *
     * @param username
     * @param resp
     * @param isAdd
     */
    public void updateMusicGood(String username, MusicResp resp, int position, boolean isAdd) {
        mModel.updateMusicGood(username, resp, new HomeContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.updateMusicGoodResult(true, resp, position, isAdd);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                if (errorId == AccountCode.UPDATE_MUSIC_GOOD_FAILED.getCode()) {
                    mRootView.updateMusicGoodResult(false, resp, position, isAdd);
                } else {
                    mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
                }
            }
        });
    }

    public void updateMusicLike(String username, MusicResp resp, int position, boolean isLike) {
        mModel.updateMusicLike(username, resp, new HomeContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.updateMusicLikeResult(true, resp, position, isLike);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                if (errorId == AccountCode.UPDATE_MUSIC_GOOD_FAILED.getCode()) {
                    mRootView.updateMusicLikeResult(false, resp, position, isLike);
                } else {
                    mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
                }
            }
        });
    }

    /**
     * 上传音乐
     * @param musicResp
     */
    public void addMusic(MusicResp musicResp) {
        mModel.addMusic(musicResp, new HomeContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.addMusicResult(true);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }
}
