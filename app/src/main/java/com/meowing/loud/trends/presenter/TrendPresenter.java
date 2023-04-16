package com.meowing.loud.trends.presenter;

import android.app.Application;

import com.meowing.loud.arms.base.BasePresenter;
import com.meowing.loud.trends.contract.TrendContract;

import javax.inject.Inject;

public class TrendPresenter extends BasePresenter<TrendContract.Model, TrendContract.View> {

    private Application mApplication;

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    @Inject
    public TrendPresenter(TrendContract.Model model, TrendContract.View rootView, Application application) {
        super(model, rootView);
        this.mApplication = application;
    }
}
