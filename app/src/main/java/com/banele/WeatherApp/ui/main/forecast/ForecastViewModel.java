package com.banele.WeatherApp.ui.main.forecast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.banele.WeatherApp.data.Resource;
import com.banele.WeatherApp.data.WeatherRepository;
import com.banele.WeatherApp.models.Forecast;

import javax.inject.Inject;

/**
 * Gets data from repository and passes it to forecast fragment
 */
public class ForecastViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;

    @Inject
    public ForecastViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public LiveData<Resource<Forecast>> getWeatherForecast(int id, boolean isConnected) {
        return weatherRepository.getWeatherForecast(id, isConnected);
    }

    public LiveData<Resource<Forecast>> getWeatherForecast(String cityName, boolean connected) {
        return weatherRepository.getWeatherForecast(cityName, connected);
    }

    public LiveData<Resource<Forecast>> getWeatherForecast(double[] coordinates, boolean connected) {
        return weatherRepository.getWeatherForecast(coordinates, connected);
    }

}
