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
public class Route1 {
    static GoogleMap mMap;
    static int currRoute;

    public static void displayWeather(GoogleMap map, int theRoute, int progress) {
        mMap = map;
        currRoute = theRoute;

        LatLng seattle = new LatLng(47.6097, -122.3331);

        LatLng missoula = new LatLng(46.8625, -114.0117);
        LatLng minneapolis = new LatLng(44.9778, -93.2650);

        LatLng saltLakeCity = new LatLng(40.7500, -111.8833);
        LatLng northPlatte = new LatLng(41.1359, -100.7705);

        LatLng lasVegas = new LatLng(36.1215, -115.1739);
        LatLng denver = new LatLng(39.7392, -104.9903);

        LatLng stLouis = new LatLng(38.6272, -90.1978);

        mMap.clear(); //clear the screen

        String route = currRoute + "";
        Log.i("route", route);

        //update the weather seen
        if (progress < 25) {
            //display weather at 0% into the trip
            switch (currRoute) {
                case 1: //no detour
                    seattleToStLouis();
                    break;
                case 2: //detour through minneapolis
                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    break;
                case 3: //detour through Las Vegas
                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
                default: //all routes
                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    seattleToStLouis();
                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
            }

            //only need seattle weather at first
            IconAdder.addIcon("Rain", mMap, seattle);
        } else if (progress >= 25 && progress < 50) {

            //display the weather at 25% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, saltLakeCity);

                    seattleToStLouis();
                    break;
                case 2: //detour through minneapolis
                    IconAdder.addIcon("Sun", mMap, missoula);

                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    break;
                case 3: //detour through Las Vegas
                    IconAdder.addIcon("Sun", mMap, lasVegas);

                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
                default: //all routes
                    IconAdder.addIcon("Sun", mMap, missoula);
                    IconAdder.addIcon("Tornado", mMap, saltLakeCity);
                    IconAdder.addIcon("Sun", mMap, lasVegas);

                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    seattleToStLouis();
                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
            }
        } else if (progress >= 50 && progress < 75) {
            //display the weather at 50% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, northPlatte);

                    seattleToStLouis();
                    break;
                case 2: //detour through minneapolis
                    IconAdder.addIcon("Snow", mMap, minneapolis);

                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    break;
                case 3: //detour through Las Vegas
                    IconAdder.addIcon("Snow", mMap, denver);

                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
                default: //all routes
                    IconAdder.addIcon("Snow", mMap, minneapolis);
                    IconAdder.addIcon("Tornado", mMap, northPlatte);
                    IconAdder.addIcon("Snow", mMap, denver);

                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    seattleToStLouis();
                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
            }
        } else {
            //display the weather at 100% into the trip (St louis)
            switch (currRoute) {
                case 1: //no detour
                    seattleToStLouis();
                    break;
                case 2: //detour through minneapolis
                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    break;
                case 3: //detour through Las Vegas
                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
                default: //all routes
                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    seattleToStLouis();
                    seattleToLasVegas();
                    lasVegasToStLouis();
                    break;
            }

            IconAdder.addIcon("Sun", mMap, stLouis);
        }
    }

    private static void seattleToStLouis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(47.6097, -122.3331);
        LatLng dest = new LatLng(38.6272, -90.1978);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.RED);

            for (int i = 0; i < directionPts.size(); i++) {
                rectLine.add(directionPts.get(i));
            }


            mMap.addPolyline(rectLine);

            NodeList nl = doc.getElementsByTagName("html_instructions");
            //addWrittenDirections(nl, R.id.route_info_textview);
        }
    }

    private static void seattleToMinneapolis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(47.6097, -122.3331);
        LatLng dest = new LatLng(44.9778, -93.2650); //detour through minneapolis
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.BLUE);

            for (int i = 0; i < directionPts.size(); i++) {
                rectLine.add(directionPts.get(i));
            }


            mMap.addPolyline(rectLine);

            NodeList nl = doc.getElementsByTagName("html_instructions");
            //addWrittenDirections(nl, R.id.route_info_textview);
        }
    }

    private static void minneapolisToStLouis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(44.9778, -93.2650);
        LatLng dest = new LatLng(38.6272, -90.1978);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.BLUE);

            for (int i = 0; i < directionPts.size(); i++) {
                rectLine.add(directionPts.get(i));
            }


            mMap.addPolyline(rectLine);

            NodeList nl = doc.getElementsByTagName("html_instructions");
            //addWrittenDirections(nl, R.id.route_info_textview);
        }
    }

    private static void seattleToLasVegas() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(47.6097, -122.3331);
        LatLng dest = new LatLng(36.1215, -115.1739);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.GREEN);

            for (int i = 0; i < directionPts.size(); i++) {
                rectLine.add(directionPts.get(i));
            }


            mMap.addPolyline(rectLine);

            NodeList nl = doc.getElementsByTagName("html_instructions");
            //addWrittenDirections(nl, R.id.route_info_textview);
        }
    }

    private static void lasVegasToStLouis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(36.1215, -115.1739);
        LatLng dest = new LatLng(38.6272, -90.1978);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.GREEN);

            for (int i = 0; i < directionPts.size(); i++) {
                rectLine.add(directionPts.get(i));
            }
            mMap.addPolyline(rectLine);

            NodeList nl = doc.getElementsByTagName("html_instructions");
            //addWrittenDirections(nl, R.id.route_info_textview);
        }
    }

    /*private static void addWrittenDirections(NodeList instructions, int id) {
        TextView routeInfo = (TextView) findViewById(id);

        for (int i = 0; i < instructions.getLength(); i++) {
            routeInfo.append(
                    Html.fromHtml(instructions.item(i).getTextContent() + "\n")
            );
        }
    }*/
}