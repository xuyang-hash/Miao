package com.meowing.loud.play.di.component;

import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.play.di.module.PlayModule;
import com.meowing.loud.play.view.activity.PlayActivity;

import dagger.Component;

@ActivityScope
@Component(modules = PlayModule.class, dependencies = AppComponent.class)
public interface PlayComponent {
    void inject(PlayActivity activity);
}
