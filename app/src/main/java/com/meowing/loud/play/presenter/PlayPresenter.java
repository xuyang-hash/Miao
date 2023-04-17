package com.meowing.loud.play.presenter;

import android.app.Application;

import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.arms.di.scope.ActivityScope;
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
}
