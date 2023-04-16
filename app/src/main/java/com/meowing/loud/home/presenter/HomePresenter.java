package com.meowing.loud.home.presenter;

import android.app.Application;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.ErrorCodeManager;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.home.contract.HomeContract;

import java.util.ArrayList;

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
     * 查询所有音乐列表数据
     */
    public void findAllMusicData() {
        mModel.findAllMusicData(new HomeContract.Model.Listener() {
            @Override
            public void onSuccess(Object obj) {
                mRootView.hideLoading();
                ArrayList<MusicResp> musicRespArrayList = (ArrayList<MusicResp>) obj;
                LocalDataManager.getInstance().setMusicRespList(musicRespArrayList);
                mRootView.findAllMusicSuccess();
            }

            @Override
            public void onFailed(int errorId) {
                mRootView.hideLoading();
                mRootView.error(ErrorCodeManager.parseErrorCode(mApplication, errorId, R.string.common_unknown_error, AccountCode.class));
            }
        });
    }

    public void updateMusicGood(String username, MusicResp resp, boolean isAdd) {
        mModel.updateMusicGood(username, resp, new HomeContract.Model.Listener() {
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
}
