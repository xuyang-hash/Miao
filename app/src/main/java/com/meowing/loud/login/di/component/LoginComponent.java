package com.meowing.loud.login.di.component;

import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.view.activity.ForgetPasswordActivity;
import com.meowing.loud.login.view.activity.LoginActivity;
import com.meowing.loud.login.view.activity.RegisterActivity;
import com.meowing.loud.login.view.fragment.ForgetPwdInputPwdFragment;
import com.meowing.loud.login.view.fragment.ForgetPwdSetConfidentialityFragment;
import com.meowing.loud.login.view.fragment.RegisterInputPwdFragment;
import com.meowing.loud.login.view.fragment.RegisterSetConfidentialityFragment;

import dagger.Component;

@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {

    void inject(ForgetPasswordActivity activity);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(ForgetPwdInputPwdFragment activity);

    void inject(RegisterInputPwdFragment activity);

    void inject(ForgetPwdSetConfidentialityFragment activity);

    void inject(RegisterSetConfidentialityFragment activity);
}
