package com.meowing.loud.me.di.module;

import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.me.contract.UserContract;
import com.meowing.loud.me.model.UserModel;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    private UserContract.View view;

    /**
     * 构建UserModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UserModule(UserContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserContract.View provideUserView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UserContract.Model provideUserModel(UserModel model) {
        return model;
    }
}
