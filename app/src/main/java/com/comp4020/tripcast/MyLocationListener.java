package com.comp4020.tripcast;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Alex on 12/03/2015.
 */
/* Class My Location Listener */
public class MyLocationListener implements LocationListener
{

    public void onLocationChanged(Location loc)
    {
        loc.getLatitude();
        loc.getLongitude();

        String Text = "My current location is: " +
                "Latitude = " + loc.getLatitude() +
                "Longitude = " + loc.getLongitude();

        //Toast.makeText(geta, Text, Toast.LENGTH_SHORT).show();
    }

    public void onProviderDisabled(String provider)
    {
        //Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
    }

    public void onProviderEnabled(String provider)
    {
        //Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }
}
