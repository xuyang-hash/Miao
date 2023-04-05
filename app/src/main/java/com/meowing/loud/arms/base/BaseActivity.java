package com.meowing.loud.arms.base;

import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.meowing.loud.arms.integration.EventBusManager;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册到事件主线
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBusManager.getInstance().register(this);
        }
        initView();
        initData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        if (mPresenter != null) {
            mPresenter.onStart();
        }
        super.onStart();
    }

    @Override
    protected void onRestart() {
        if (mPresenter != null) {
            mPresenter.onStart();
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBusManager.getInstance().unregister(this);
        }
        super.onDestroy();
    }

    protected abstract void initView();
}
