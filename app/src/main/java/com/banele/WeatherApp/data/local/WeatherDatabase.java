package com.banele.WeatherApp.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.banele.WeatherApp.data.local.dao.WeatherDao;
import com.banele.WeatherApp.models.CurrentWeather;
import com.banele.WeatherApp.models.FavouriteLocation;
import com.banele.WeatherApp.models.Forecast;
import com.banele.WeatherApp.persistence.ListConverter;

/**
 * The local database responsible for persisting weather data
 */
@Database(entities = {CurrentWeather.class, Forecast.class, FavouriteLocation.class},
        version = 2, exportSchema = false)
@TypeConverters({ListConverter.class})
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
