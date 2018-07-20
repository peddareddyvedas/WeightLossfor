package com.vedas.weightloss.ServerApis;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.Settings.PersonalInfoActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by WAVE on 10/27/2017.
 */

public class LocationTracker {
    Context context;
    LocationManager locationManager;
    public static LocationTracker myObj;
    public Location currentLocation;
    public String currentLocationString = "";
    public String currentLocationZipcode = "";

    public static LocationTracker getInstance() {
        if (myObj == null) {
            myObj = new LocationTracker();

        }
        return myObj;
    }

    public void fillContext(Context context1) {
        context = context1;
        loadDefaultLocation();
    }

    public void startLocation() {
        // Set Default Location.
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation == null) {
            loadDefaultLocation();
        }
        refreshLocations();
    }

    public void refreshLocations() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            currentLocation = location;
            Log.e("latitude", "" + location.getLatitude());
            EventBus.getDefault().post(new PersonalInfoActivity.MessageEvent("locationSuccess"));
            getCurrentLoctionAddress(context, currentLocation.getLatitude(), currentLocation.getLongitude());
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void loadDefaultLocation() {
        currentLocation = new Location("Default");
        currentLocation.setLatitude(0);
        currentLocation.setLongitude(0);
    }
    public void getCurrentLoctionAddress(Context context, double LATITUDE, double LONGITUDE) {
        //Set Address
        Log.e("latilongi", "call" + LATITUDE + "" + LONGITUDE);
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                currentLocationString = city+", "+country;
                currentLocationZipcode = postalCode;
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setLocation(currentLocationString);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setZipcode(currentLocationZipcode);
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                Log.e("addressdetails", "getAddress:  address" + address);
                Log.e("addressdetails", "getAddress:  address" + city);
                Log.e("addressdetails", "getAddress:  address" + state);
                Log.e("addressdetails", "getAddress:  address" + postalCode);
                Log.e("addressdetails", "getAddress:  address" + knownName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }


}
