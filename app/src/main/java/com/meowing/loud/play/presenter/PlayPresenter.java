package com.meowing.loud.play.presenter;

import android.app.Application;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.ErrorCodeManager;
import com.meowing.loud.arms.constant.MMKConstant;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.play.contract.PlayContract;

import javax.inject.Inject;

@ActivityScope
public class PlayPresenter extends BasePresenter<PlayContract.Model, PlayContract.View> {
    private Application mApplication;

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    @Inject
    public PlayPresenter(PlayContract.Model model, PlayContract.View rootView, Application application) {
        super(model, rootView);
        this.mApplication = application;
    }

    /**
     * 查找音乐的url
     * @param musicId
     */
    public void findMusicUrl(int musicId) {
        String localUrl = LocalDataManager.getInstance().getMusicUrl(musicId);
        if (StringUtils.isStringNULL(localUrl)) {
            mModel.findMusicUrl(musicId, new PlayContract.Model.Listener() {
                @Override
                public void onSuccess(Object obj) {
                    mRootView.hideLoading();
                    mRootView.findMusicUrlResult((String) obj);
                }

                @Override
                public void onFailed(int errorId) {
                    mRootView.hideLoading();
                    mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
                }
            });
        } else {
            mRootView.hideLoading();
            mRootView.findMusicUrlResult(localUrl);
        }
    }

    /**
     * 喜欢或者取消喜欢音乐
     * @param resp
     * @param isAdd
     */
    public void updateMusicLike(MusicResp resp, boolean isAdd) {
        String username = MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME);
        mModel.updateMusicLike(username, resp, new PlayContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.updateMusicLikeResult(true, resp, isAdd);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                if (errorId == AccountCode.UPDATE_MUSIC_GOOD_FAILED.getCode()) {
                    mRootView.updateMusicLikeResult(false, resp, isAdd);
                } else {
                    mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
                }
            }
        });
    }

    /**
     * 更新歌曲的赞
     *
     * @param resp
     * @param isAdd
     */
    public void updateMusicGood(MusicResp resp, boolean isAdd) {
        String username = MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME);
        mModel.updateMusicGood(username, resp, new PlayContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.updateMusicGoodResult(true, resp, isAdd);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                if (errorId == AccountCode.UPDATE_MUSIC_GOOD_FAILED.getCode()) {
                    mRootView.updateMusicGoodResult(false, resp, isAdd);
                } else {
                    mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
                }
            }
        });
    }

    /**
     * 更新歌曲的赞
     *
     */
    public void updateMusicState(MusicResp musicResp, boolean isPass) {
        mModel.updateMusicState(musicResp.getId(), isPass, new PlayContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                mRootView.updateMusicStateResult(true, musicResp, isPass);
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }
}
