package admin.mx.com.perron.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import admin.mx.com.perron.MainActivity;

import static android.location.LocationManager.*;
import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by garaiza on 2/22/16.
 */
public class GPSTracker extends Service implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int MY_PERMISSIONS = 123;
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    private final MainActivity mainActivity;
    Location location; // location
    double latitude;
    double longitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context, MainActivity mainActivity) {
        this.mContext = context;
        this.mainActivity = mainActivity;
        Log.d("CheckSDKVersion: ", String.valueOf((int) Build.VERSION.SDK_INT));
        CheckSDKVersion();
    }

    public void CheckSDKVersion() {

        Log.d("CheckSDKVersion: ", String.valueOf((int) Build.VERSION.SDK_INT));
        if ((int) Build.VERSION.SDK_INT < 23) {
            canGetLocation = true;
            return;
        } else {

            String[] permission = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
            int requestId = 123;
            requestPermissions(permission, requestId);
        }
    }

    public void requestPermissions(String[] permission, int requestId) {
        ActivityCompat.requestPermissions(mainActivity, permission, requestId);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("GPSTracker: ", requestCode + "");
        switch (requestCode) {
            case MY_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canGetLocation = true;

                    Toast.makeText(mContext, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(mContext, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean getLocation() {
        boolean result = true;
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(GPS_PROVIDER);
            if (isGPSEnabled){
                Toast.makeText(mContext, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();

                Log.d("GPSTracker: ", "GPS is Enabled in your device");
                // getting GPS status

                // getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER);
                Log.d("GPSTracker: ", "isNetworkEnabled");
    /*
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        */
                Log.d("GPSTracker: ", "before check permission");
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("GPSTracker: ",   "after check permission");
                    if (!isGPSEnabled && !isNetworkEnabled) {
                        Log.d("GPSTracker: ",   "!isGPSEnabled && !isNetworkEnabled");
                    } else {
                        this.canGetLocation = true;
                        // First get location from Network Provider
                        if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("Network", "Network");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                        // if GPS Enabled get lat/long using GPS Services
                        if (isGPSEnabled) {
                            if (location == null) {
                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                Log.d("GPS Enabled", "GPS Enabled");
                                if (locationManager != null) {
                                    Log.d("GPS Enabled", "Assigning value to location");
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }else{
                                        Log.d("GPS Enabled", "NO value to location");
                                    }
                                }
                            }
                        }
                    }
                }else{
                    Log.d("GPSTracker: ",   "NO permission");
                }
                final StringBuilder msg = new StringBuilder("lat:");
                msg.append(location.getLatitude());
                msg.append(";lng: ");
                msg.append(location.getLongitude());
                Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(mContext, "GPS is NOT Enabled in your device", Toast.LENGTH_SHORT).show();
                showSettingsAlert();
                result = false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * Function to check if best network provider
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mainActivity);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mainActivity.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    @TargetApi(Build.VERSION_CODES.M)
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }




    @Override
    public void onLocationChanged(Location location) {

        final StringBuilder msg = new StringBuilder("lat:");
        msg.append(location.getLatitude());
        msg.append(";lng: ");
        msg.append(location.getLongitude());

        Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onProviderDisabled(String provider) {

        if ("gps".equals(provider)){


        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }

    public void setIsGPSEnabled(boolean isGPSEnabled) {
        this.isGPSEnabled = isGPSEnabled;
    }

}
