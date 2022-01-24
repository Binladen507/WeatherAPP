package com.banele.WeatherApp.di.main;

import com.banele.WeatherApp.ui.main.forecast.ForecastFragment;
import com.banele.WeatherApp.ui.main.today.TodayFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract TodayFragment contributeTodayFragment();

    @ContributesAndroidInjector
    abstract ForecastFragment contributeForecastFragment();

}
