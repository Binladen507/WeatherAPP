package com.banele.WeatherApp.di.main;

import androidx.lifecycle.ViewModel;

import com.banele.WeatherApp.di.ViewModelKey;
import com.banele.WeatherApp.ui.main.MainViewModel;
import com.banele.WeatherApp.ui.main.forecast.ForecastViewModel;
import com.banele.WeatherApp.ui.main.today.TodayViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(TodayViewModel.class)
    public abstract ViewModel bindTodayViewModel(TodayViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel.class)
    public abstract ViewModel bindForecastViewModel(ForecastViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

}
