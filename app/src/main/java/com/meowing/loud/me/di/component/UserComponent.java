package com.meowing.loud.me.di.component;

import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.me.di.module.UserModule;
import com.meowing.loud.me.view.Activity.UserCenterActivity;
import com.meowing.loud.me.view.Fragment.UserFragment;

import dagger.Component;

@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserFragment activity);

    void inject(UserCenterActivity activity);
}
