package com.meowing.loud.arms.base;

public interface IView {
    /**
     * 显示加载
     */
    default void showLoading() {

    }

    /**
     * 隐藏加载
     */
    default void hideLoading() {

    }

    default void error(String msg) {

    }
}
