package com.meowing.loud.home.di.component;

import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.home.di.module.HomeModule;
import com.meowing.loud.home.view.activity.HomeActivity;
import com.meowing.loud.home.view.activity.HomeAddMusicActivity;
import com.meowing.loud.home.view.fragment.CollectFragment;
import com.meowing.loud.home.view.fragment.HomeFragment;
import com.meowing.loud.home.view.fragment.RefuseFragment;
import com.meowing.loud.home.view.fragment.WaitMusicFragment;

import dagger.Component;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {

    void inject(HomeActivity activity);

    void inject(HomeAddMusicActivity activity);

    void inject(HomeFragment activity);

    void inject(CollectFragment activity);

    void inject(RefuseFragment activity);

    void inject(WaitMusicFragment activity);
}
