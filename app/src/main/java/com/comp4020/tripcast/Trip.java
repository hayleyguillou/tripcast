package com.comp4020.tripcast;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.app.Activity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by brianyeo on 2015-03-23.
 */

//Seattle to St Louis
public class Trip {

    public static void newTrip(int whichTrip, GoogleMap map, int theRoute, TextView routeInfo, int progress){
        if (whichTrip == 1) {
            Trip1.displayWeather1(map, theRoute, routeInfo, progress);
        }
        else if (whichTrip ==2) {
            Trip2.displayWeather(map, theRoute, routeInfo, progress);
        }
        else {

        }
    }

 }