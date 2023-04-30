package com.meowing.loud.play.di.module;

import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.play.contract.PlayContract;
import com.meowing.loud.play.model.PlayModel;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayModule {
    private PlayContract.View view;

    /**
     * 构建PlayerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PlayModule(PlayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PlayContract.View providePlayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PlayContract.Model providePlayModel(PlayModel model) {
        return model;
    }
}
