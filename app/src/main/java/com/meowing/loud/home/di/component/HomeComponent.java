package com.meowing.loud.home.di.component;

import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.home.di.module.HomeModule;
import com.meowing.loud.home.view.fragment.HomeFragment;

import dagger.Component;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {

    void inject(HomeFragment activity);
}
