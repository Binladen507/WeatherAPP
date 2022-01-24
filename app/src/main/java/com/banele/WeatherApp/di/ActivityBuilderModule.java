package com.banele.WeatherApp.di;

import com.banele.WeatherApp.di.main.MainFragmentBuilderModule;
import com.banele.WeatherApp.di.main.MainModule;
import com.banele.WeatherApp.di.main.MainViewModelsModule;
import com.banele.WeatherApp.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Creates SubComponents for different activities
 */
@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
            modules = {
                    MainFragmentBuilderModule.class,
                    MainViewModelsModule.class,
                    MainModule.class,
            }
    )
    abstract MainActivity contributeMainActivity();

}
