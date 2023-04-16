package com.meowing.loud.trends.di.component;

import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.trends.di.module.TrendModule;
import com.meowing.loud.trends.view.Fragment.TrendFragment;

import dagger.Component;

@ActivityScope
@Component(modules = TrendModule.class, dependencies = AppComponent.class)
public interface TrendComponent {

    void inject(TrendFragment activity);
}
