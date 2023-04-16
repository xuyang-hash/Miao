package com.meowing.loud.trends.view.Fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.databinding.FragmentTrendBinding;
import com.meowing.loud.trends.contract.TrendContract;
import com.meowing.loud.trends.di.component.DaggerTrendComponent;
import com.meowing.loud.trends.di.module.TrendModule;
import com.meowing.loud.trends.presenter.TrendPresenter;

public class TrendFragment extends BaseFragment<FragmentTrendBinding, TrendPresenter> implements TrendContract.View {

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTrendComponent
                .builder()
                .appComponent(appComponent)
                .trendModule(new TrendModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initView(View mView) {

    }
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
