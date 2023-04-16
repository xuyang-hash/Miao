package com.meowing.loud.home.di.module;

import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.home.model.HomeModel;
import com.meowing.loud.home.contract.HomeContract;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    private HomeContract.View view;

    public HomeModule(HomeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HomeContract.View provideHomeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HomeContract.Model provideHomeModel(HomeModel model) {
        return model;
    }
}
