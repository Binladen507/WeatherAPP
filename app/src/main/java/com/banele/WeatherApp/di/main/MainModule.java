package com.banele.WeatherApp.di.main;

import static com.banele.WeatherApp.utils.Constants.*;
import static com.banele.WeatherApp.utils.Constants.FASTEST_UPDATE_INTERVAL;
import static com.banele.WeatherApp.utils.Constants.LOCATION_UPDATE_INTERVAL;

import android.app.Application;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.banele.WeatherApp.data.remote.WeatherApi;
import com.banele.WeatherApp.ui.main.forecast.ForecastRecyclerAdapter;


import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Contains all dependencies for MainActivity.
 * The dependencies provided here will only be available
 * for use by MainActivity.
 */
@Module
public class MainModule {

    @Provides
    static WeatherApi provideWeatherAPI(Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }

    @Provides
    static ForecastRecyclerAdapter provideForecastAdapter(Application application) {
        return new ForecastRecyclerAdapter(application);
    }

    @Provides
    static FusedLocationProviderClient provideFusedClient(Application application) {
        return LocationServices.getFusedLocationProviderClient(application);
    }

    @Provides
    static SettingsClient provideSettingsClient(Application application) {
        return LocationServices.getSettingsClient(application);
    }

    @SuppressWarnings("deprecation")
    @Provides
    static LocationRequest provideLocationRequest() {
        return new LocationRequest()
                .setInterval(LOCATION_UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Provides
    static LocationSettingsRequest provideLocationSettingsRequest(LocationRequest locationRequest) {
        return new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
    }

    @Provides
    static String provideMsg() {
        return "MainActivity: ViewModel working from here";
    }

}
