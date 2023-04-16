package com.meowing.loud.trends.di.module;

import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.trends.contract.TrendContract;
import com.meowing.loud.trends.model.TrendModel;

import dagger.Module;
import dagger.Provides;

@Module
public class TrendModule {

    private TrendContract.View view;

    public TrendModule(TrendContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TrendContract.View provideTrendView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TrendContract.Model provideTrendModel(TrendModel model) {
        return model;
    }
}
