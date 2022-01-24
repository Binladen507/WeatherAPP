package com.banele.WeatherApp.utils;

public class Constants {

    // API Key for OpenWeatherMap
    public static final String APP_ID = "53f9d8e4213222cf517d86dc406d67fc";

    // OpenWeatherMap REST API Base Url used by retrofit
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    // Connection timeout in seconds
    public static final int CONNECTION_TIMEOUT = 10;

    // Read timeout in seconds
    public static final int READ_TIMEOUT = 2;

    // write timeout in seconds
    public static final int WRITE_TIMEOUT = 2;

    // time until next update
    public static final long LOCATION_UPDATE_INTERVAL = 1000000000;

    // time until next update cannot be less than this
    public static final long FASTEST_UPDATE_INTERVAL = LOCATION_UPDATE_INTERVAL/2;

    // for requesting permissions
    public static final int LOCATION_REQUEST_CODE = 2021;

    // used to start dialog when user location is off
    public static final int REQUEST_CHECK_SETTINGS = 0x1;

}
