package com.meowing.loud.arms.base;

public interface IModel {
    /**
     * 在框架中 presenter的onDestroy时会默认调用
     */
    void onDestroy();
}
