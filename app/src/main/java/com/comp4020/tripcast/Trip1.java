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
public class Trip1 {
    static GoogleMap mMap;
    static int currRoute;

    static LatLng seattle = new LatLng(47.6097, -122.3331);

    static LatLng missoula = new LatLng(46.8625, -114.0117);
    static LatLng minneapolis = new LatLng(44.9778, -93.2650);

    static LatLng saltLakeCity = new LatLng(40.7500, -111.8833);
    static LatLng northPlatte = new LatLng(41.1359, -100.7705);

    static LatLng lasVegas = new LatLng(36.1215, -115.1739);
    static LatLng denver = new LatLng(39.7392, -104.9903);

    static LatLng stLouis = new LatLng(38.6272, -90.1978);

    public static void displayWeather1(GoogleMap map, int theRoute, TextView routeInfo, int progress) {
        mMap = map;
        currRoute = theRoute;

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
            routeInfo.setText("Seattle rainfall: 25mm");

        } else if (progress >= 25 && progress < 50) {

            //display the weather at 25% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, saltLakeCity);
                    routeInfo.setText("Salt Lake City rainfall: 0mm");

                    seattleToStLouis();
                    break;
                case 2: //detour through minneapolis
                    IconAdder.addIcon("Sun", mMap, missoula);
                    routeInfo.setText("Missoula rainfall: 10mm");

                    seattleToMinneapolis();
                    minneapolisToStLouis();
                    break;
                case 3: //detour through Las Vegas
                    IconAdder.addIcon("Sun", mMap, lasVegas);
                    routeInfo.setText("Las Vegas rainfall: 0mm");

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
        Document doc = null;

        try {
            doc = directions.execute(seattle, stLouis).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.YELLOW);

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
        Document doc = null;

        try {
            doc = directions.execute(seattle, minneapolis).get();
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
        Document doc = null;

        try {
            doc = directions.execute(minneapolis, stLouis).get();
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
        Document doc = null;

        try {
            doc = directions.execute(seattle, lasVegas).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.MAGENTA);

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
        Document doc = null;

        try {
            doc = directions.execute(lasVegas, stLouis).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (doc != null) {
            ArrayList<LatLng> directionPts = directions.getDirection(doc);

            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.MAGENTA);

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