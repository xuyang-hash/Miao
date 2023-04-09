package com.meowing.loud.arms.base;

import com.meowing.loud.arms.integration.EventBusManager;

import org.greenrobot.eventbus.EventBus;

public class BasePresenter<M extends IModel, V extends IView> implements IPresenter{

    protected M mModel;
    protected V mRootView;

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;
    }
    @Override
    public void onStart() {
        if (useEventBus())//如果要使用 EventBus 请将此方法返回 true
        {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBusManager.getInstance().register(this);//注册 EventBus
            }
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onReStart() {

    }

    @Override
    public void onDestroy() {
        if (useEventBus())//如果要使用 EventBus 请将此方法返回 true
        {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBusManager.getInstance().unregister(this);//注销 EventBus
            }
        }
        if (mModel != null) {
            mModel.onDestroy();
        }
        this.mModel = null;
        this.mRootView = null;
    }

    public boolean useEventBus() {
        return true;
    }
}
