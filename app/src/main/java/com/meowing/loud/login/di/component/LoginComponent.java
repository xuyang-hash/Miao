package com.meowing.loud.login.di.component;

import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.view.activity.LoginActivity;
import com.meowing.loud.login.view.activity.RegisterActivity;
import com.meowing.loud.login.view.fragment.InputPwdFragment;

import dagger.Component;

@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(InputPwdFragment activity);
}
