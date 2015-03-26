package com.comp4020.tripcast;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by brianyeo on 2015-03-23.
 */

//Phoenix to Orlando
//No detour: 30 h
//Via albuquerque: 31 h
//Via monterrey: 38 h
public class Trip3 {
    static GoogleMap mMap;
    static int currRoute;

    static LatLng phoenix = new LatLng(33.4500, -112.0667);

    static LatLng albuquerque = new LatLng(35.1107, -106.6100);
    static LatLng dallas = new LatLng(32.7767, -96.7970);

    static LatLng ciudadJuarez = new LatLng(31.7394, -106.4869);
    static LatLng sanAntonio = new LatLng(29.4167, -98.5000);

    static LatLng monterrey = new LatLng(25.6667, -100.3000);
    static LatLng pensacola = new LatLng(30.4333, -87.2000);

    static LatLng orlando = new LatLng(28.4158, -81.2989);

    public static void displayWeather(GoogleMap map, int theRoute, TextView routeInfo, int progress) {
        mMap = map;
        currRoute = theRoute;

        mMap.clear(); //clear the screen

        String route = currRoute + "";
        Log.i("route", route);

        //update the weather seen
        if (progress < 15) {
            //display weather at 0% into the trip
            switch (currRoute) {
                case 1: //no detour
                    phoenixToorlando();

                    routeInfo.setText("Total trip time: 30h");
                    break;
                case 2: //detour through albuquerque
                    phoenixToAlbuquerque();
                    albuquerqueToorlando();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through monterry
                    phoenixTomonterrey();
                    monterreyToorlando();

                    routeInfo.setText("Total trip time: 38h");
                    break;
                default: //all routes
                    phoenixToAlbuquerque();
                    albuquerqueToorlando();
                    phoenixToorlando();
                    phoenixTomonterrey();
                    monterreyToorlando();
                    break;
            }

            //only need phoenix weather at first
            IconAdder.addIcon("Rain", mMap, phoenix);
        } else if (progress >= 15 && progress < 50) {

            //display the weather at 25% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, ciudadJuarez);

                    phoenixToorlando();

                    routeInfo.setText("Total trip time: 30h");
                    break;
                case 2: //detour through albuquerque
                    IconAdder.addIcon("Sun", mMap, albuquerque);

                    phoenixToAlbuquerque();
                    albuquerqueToorlando();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through monterrey
                    IconAdder.addIcon("Sun", mMap, monterrey);

                    phoenixTomonterrey();
                    monterreyToorlando();

                    routeInfo.setText("Total trip time: 38h");
                    break;
                default: //all routes
                    IconAdder.addIcon("Sun", mMap, ciudadJuarez);
                    IconAdder.addIcon("Tornado", mMap, albuquerque);
                    IconAdder.addIcon("Sun", mMap, monterrey);

                    phoenixToAlbuquerque();
                    albuquerqueToorlando();
                    phoenixToorlando();
                    phoenixTomonterrey();
                    monterreyToorlando();
                    break;
            }
        } else if (progress >= 50 && progress < 85) {
            //display the weather at 50% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, sanAntonio);

                    phoenixToorlando();

                    routeInfo.setText("Total trip time: 30h");
                    break;
                case 2: //detour through albuquerque
                    IconAdder.addIcon("Snow", mMap, dallas);

                    phoenixToAlbuquerque();
                    albuquerqueToorlando();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through monterrey
                    IconAdder.addIcon("Snow", mMap, pensacola);

                    phoenixTomonterrey();
                    monterreyToorlando();

                    routeInfo.setText("Total trip time: 38h");
                    break;
                default: //all routes
                    IconAdder.addIcon("Snow", mMap, sanAntonio);
                    IconAdder.addIcon("Tornado", mMap, dallas);
                    IconAdder.addIcon("Snow", mMap, pensacola);

                    phoenixToAlbuquerque();
                    albuquerqueToorlando();
                    phoenixToorlando();
                    phoenixTomonterrey();
                    monterreyToorlando();
                    break;
            }
        } else {
            //display the weather at 100% into the trip (green bay)
            switch (currRoute) {
                case 1: //no detour
                    phoenixToorlando();

                    routeInfo.setText("Total trip time: 30h");
                    break;
                case 2: //detour through albuquerque
                    phoenixToAlbuquerque();
                    albuquerqueToorlando();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through monterrey
                    phoenixTomonterrey();
                    monterreyToorlando();

                    routeInfo.setText("Total trip time: 38h");
                    break;
                default: //all routes
                    phoenixToAlbuquerque();
                    albuquerqueToorlando();
                    phoenixToorlando();
                    phoenixTomonterrey();
                    monterreyToorlando();
                    break;
            }

            IconAdder.addIcon("Sun", mMap, orlando);
        }
    }

    private static void phoenixToorlando() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(phoenix, orlando).get();
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

    private static void phoenixToAlbuquerque() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(phoenix, albuquerque).get();
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

    private static void albuquerqueToorlando() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(albuquerque, orlando).get();
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

    private static void phoenixTomonterrey() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(phoenix, monterrey).get();
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

    private static void monterreyToorlando() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(monterrey, orlando).get();
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