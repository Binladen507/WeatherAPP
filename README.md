# Weather APP GBK
The repository contains an Android native application that listens to user location and show weather updates. The application an MVVM architeture,FusedLocationProvider for realtime location updates, retrofit for http request, Room for offline data persistence and Dagger2 for dependency injection.
# Overview 
# Model 
- Weather App GBK uses one repository as a data layer for the app. It create an abastration layer for the two main source of data;  Room persistence database for a local database and remote source :Rest Api using OpenWeatherMap API (https://openweathermap.org/api).
# View 
- The view layer comprises of a MainActivity and 2 TabLayout fragments; MainActivity ; Today Fragment contains/displays weather details for 'Today' ,refresh button for location updates Forecast Fragment; displays weather predictions for the nest 5 days.
# Viewmodel 
- Weather App utilizes GBK four ViewModels; First three for sending data between model (weatherRepository) and views (fragments) and one for sending data from MainActvity to child fragments.
# Application Requirements
- install the APK ( minimum API 23)
- Make sure you have internet access
- Turn on location otherwise app will not function properly
# Technologies and Libraries used
- Retrofit for network requests
- Room for offline persitence
- Dagger for dependency injection
- LiveData for implementing an observer pattern
- Google Play Services for Live location updates
- OKhttp3 for network requests
- Glide for image loading
