package com.banele.WeatherApp.di;

import androidx.lifecycle.ViewModelProvider;

import com.banele.WeatherApp.viewmodel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory providerFactory);

}
