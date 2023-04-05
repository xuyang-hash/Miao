package com.meowing.loud.arms.base;

public interface IPresenter {
    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 页面不可见的数据处理
     */
    void onStop();

    /**
     * 恢复可见的数据处理
     */
    void onReStart();

    /**
     * 在框架中 Activity的onDestroy()时会默认调用
     */
    void onDestroy();
}
