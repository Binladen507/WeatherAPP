package com.banele.WeatherApp.ui.main;

import static com.banele.WeatherApp.utils.Constants.LOCATION_REQUEST_CODE;
import static com.banele.WeatherApp.utils.Constants.REQUEST_CHECK_SETTINGS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.banele.BuildConfig;
import com.banele.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.banele.WeatherApp.models.CurrentWeather;
import com.banele.WeatherApp.ui.main.adapter.ViewPagerAdapter;
import com.banele.WeatherApp.ui.main.today.TodayFragment;
import com.banele.WeatherApp.utils.LocationUtil;
import com.banele.WeatherApp.utils.Util;
import com.banele.WeatherApp.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements
        TodayFragment.OnWeatherLoadedListener{

    /*Objects*/

    private Location currentLocation;
    private LocationCallback locationCallback;

    /*Dependency Injection*/

    @Inject
    FusedLocationProviderClient providerClient;

    @Inject
    SettingsClient settingsClient;

    @Inject
    LocationRequest locationRequest;

    @Inject
    LocationSettingsRequest locationSettingsRequest;

    @Inject
    ViewModelProviderFactory providerFactory;

    /*Views*/

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AppBarLayout appBarLayout;


    /*View Model*/

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setSupportActionBar(toolbar);

        viewPager.setUserInputEnabled(true);
        viewPager.setAdapter(createAdapter());
        tabLayout.setSmoothScrollingEnabled(true);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Today");
                    break;
                case 1:
                    tab.setText("Next 5 Days");
                    break;
            }
        }).attach();

        viewModel = new ViewModelProvider(this, providerFactory).get(MainViewModel.class);

        if (!LocationUtil.isLocationEnabled(this)) {
            showProgressBar();
            Util.toast(this, "Weather App GBK needs your location");
        }

        locationCallback = createLocationCallback();
    }

    private void init(){
        progressBar = findViewById(R.id.progress_bar);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        }
        else {
            // Pass location coordinates to fragments
            startLocationUpdates();
        }

    }

    /**
     * Not the best approach for doing this
     * but coupling here is really not a big deal
     * @return the viewPager adapter
     */
    private ViewPagerAdapter createAdapter() {
        return new ViewPagerAdapter(this);
    }

    /**
     * Change app theme based on weather results
     * @param currentWeather - weather data
     */
    @Override
    public void onWeatherLoaded(CurrentWeather currentWeather) {
        if (currentWeather != null) {
            //searchBar.setPlaceHolder(currentWeather.getName());
            toolbar.setTitle(currentWeather.getName());
            switch (currentWeather.getWeather().get(0).getMain()) {
                case "Clouds":
                    changeTheme(R.color.purple_light, R.color.purple_primary, R.color.purple_dark);
                    break;
                case "Rain":
                    changeTheme(R.color.rainy_light, R.color.rainy_primary, R.color.rainy_dark);
                    break;
                default:
                    changeTheme(R.color.purple_sunny_light, R.color.purple_sunny_primary, R.color.purple_sunny_dark);
                    break;
            }
        }
    }

    /**
     * Decide whether to hide or show splash screen
     * @param status - is app loading data or not?
     */
    @Override
    public void onLoad(TodayFragment.LoadStatus status) {
        if (status == TodayFragment.LoadStatus.LOADING) {
            // Show splash screen
            showProgressBar();
        }
        else if(status == TodayFragment.LoadStatus.ERROR) {
            showProgressBar();
            Util.toast(this, "Something happened, check internet connection");
        }
        else if (status == TodayFragment.LoadStatus.SWIPE_REFRESH) {
            viewModel.setSearchQuery(toolbar.getTitle().toString());
        }
        else {
            // If splash screen is showing, hide it after a second
            hideProgressBar();
        }
    }

    /*Options menu*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_open_map) {
            updateCoordinates();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*Support methods*/

    private int color(int id) {
        return getResources().getColor(id);
    }

    private void setToolbarColor(int id) {
        toolbar.setBackgroundColor(color(id));
    }

    private void setTabLayoutColors(int primary, int light) {
        tabLayout.setBackgroundColor(color(primary));
        tabLayout.setTabTextColors(color(light), color(R.color.white));
    }

    private void changeStatusBar(int color) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.setStatusBarColor(color(color));
    }

    private void setTransparentStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void setStatusBarTranslucent() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * Manually change the color scheme for the app
     */
    private void changeTheme(int lightColor, int primaryColor, int darkColor) {
        setToolbarColor(primaryColor);
        setTabLayoutColors(primaryColor, lightColor);
        changeStatusBar(darkColor);
    }

    private void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE) {
            appBarLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            setTransparentStatusBar();
        }
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            new Handler().postDelayed(() -> {
                appBarLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                setStatusBarTranslucent();
            }, 1000);
        }
    }

    /*SearchView listener methods*/


    /*Location permissions*/

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
    }

    /**
     * Request location permissions
     */
    private void requestPermissions() {

        boolean provideRationale = ActivityCompat.shouldShowRequestPermissionRationale
                (this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (provideRationale) {
            Util.showSnackbar(findViewById(R.id.main_activity_container),
                    R.string.location_permission_request, android.R.string.ok, view ->
                            requestLocationPermission());
        }
        else {
            requestLocationPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Get location and send coordinates to fragments
                startLocationUpdates();
            }
            else {
                // Permission denied

                // Notify the user by showing a snackbar that they have denied a core permission
                // for the app. Take the user to settings where they can grant location permissions.
                Util.showSnackbar(findViewById(R.id.main_activity_container),
                        R.string.location_permission_denied, R.string.settings, view -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    /*Location requests*/

    private LocationCallback createLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                updateCoordinates();
            }
        };
    }

    /**
     * Request updates for current location periodically
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    providerClient.requestLocationUpdates(locationRequest, locationCallback,
                            Looper.myLooper());
                    //updateCoordinates();
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException)e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ((ResolvableApiException) e).startResolutionForResult
                                        (MainActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Util.toast(MainActivity.this, "Fix location settings");
                            break;
                    }
                });
    }

    /**
     * Pass location coordinates to child fragments through view model
     */
    private void updateCoordinates() {
        if (currentLocation != null) {
            double[] coord = new double[2];
            coord[0] = currentLocation.getLatitude();
            coord[1] = currentLocation.getLongitude();
            viewModel.setCoordinates(coord);
        }
        else {
            Util.toast(this, "Could not  find your location, make sure location is on!");
        }
    }

}