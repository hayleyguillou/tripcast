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

//newOrleans to chattanooga
//No detour: 7h 1m
//Via jackson: 8h 9m
//Via atlanta: 8h 20m
public class Trip4 {
    static GoogleMap mMap;
    static int currRoute;

    static LatLng newOrleans = new LatLng(29.9500, -90.0667);

    static LatLng jackson = new LatLng(32.2989, -90.1847);
    //static LatLng birmingham = new LatLng(32.7767, -96.7970); already defined

    static LatLng meridian = new LatLng(32.3747, -88.7042);
    static LatLng birmingham = new LatLng(33.5250, -86.8130);

    static LatLng montgomery = new LatLng(32.3617, -86.2792);
    static LatLng atlanta = new LatLng(33.7550, -84.3900);

    static LatLng chattanooga = new LatLng(35.0456, -85.2672);

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
                    newOrleansTochattanooga();

                    routeInfo.setText("Total trip time: 7h 1m");
                    break;
                case 2: //detour through jackson
                    newOrleansTojackson();
                    jacksonTochattanooga();

                    routeInfo.setText("Total trip time: 8h 9m");
                    break;
                case 3: //detour through atlanta
                    newOrleansToatlanta();
                    atlantaTochattanooga();

                    routeInfo.setText("Total trip time: 8h 20m");
                    break;
                default: //all routes
                    newOrleansTojackson();
                    jacksonTochattanooga();
                    newOrleansTochattanooga();
                    newOrleansToatlanta();
                    atlantaTochattanooga();
                    break;
            }

            //only need newOrleans weather at first
            IconAdder.addIcon("Rain", mMap, newOrleans);
        } else if (progress >= 15 && progress < 50) {

            //display the weather at 25% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, meridian);

                    newOrleansTochattanooga();

                    routeInfo.setText("Total trip time: 7h 1m");
                    break;
                case 2: //detour through jackson
                    IconAdder.addIcon("Sun", mMap, jackson);

                    newOrleansTojackson();
                    jacksonTochattanooga();

                    routeInfo.setText("Total trip time: 8h 9m");
                    break;
                case 3: //detour through atlanta
                    IconAdder.addIcon("Sun", mMap, montgomery);

                    newOrleansToatlanta();
                    atlantaTochattanooga();

                    routeInfo.setText("Total trip time: 8h 20m");
                    break;
                default: //all routes
                    IconAdder.addIcon("Sun", mMap, meridian);
                    IconAdder.addIcon("Tornado", mMap, jackson);
                    IconAdder.addIcon("Sun", mMap, montgomery);

                    newOrleansTojackson();
                    jacksonTochattanooga();
                    newOrleansTochattanooga();
                    newOrleansToatlanta();
                    atlantaTochattanooga();
                    break;
            }
        } else if (progress >= 50 && progress < 85) {
            //display the weather at 50% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, birmingham);

                    newOrleansTochattanooga();

                    routeInfo.setText("Total trip time: 7h 1m");
                    break;
                case 2: //detour through jackson
                    IconAdder.addIcon("Snow", mMap, birmingham);

                    newOrleansTojackson();
                    jacksonTochattanooga();

                    routeInfo.setText("Total trip time: 8h 9m");
                    break;
                case 3: //detour through montgomery
                    IconAdder.addIcon("Snow", mMap, atlanta);

                    newOrleansToatlanta();
                    atlantaTochattanooga();

                    routeInfo.setText("Total trip time: 8h 20m");
                    break;
                default: //all routes
                    IconAdder.addIcon("Snow", mMap, birmingham);
                    IconAdder.addIcon("Tornado", mMap, birmingham);
                    IconAdder.addIcon("Snow", mMap, atlanta);

                    newOrleansTojackson();
                    jacksonTochattanooga();
                    newOrleansTochattanooga();
                    newOrleansToatlanta();
                    atlantaTochattanooga();
                    break;
            }
        } else {
            //display the weather at 100% into the trip (green bay)
            switch (currRoute) {
                case 1: //no detour
                    newOrleansTochattanooga();

                    routeInfo.setText("Total trip time: 7h 1m");
                    break;
                case 2: //detour through jackson
                    newOrleansTojackson();
                    jacksonTochattanooga();

                    routeInfo.setText("Total trip time: 8h 9m");
                    break;
                case 3: //detour through montgomery
                    newOrleansToatlanta();
                    atlantaTochattanooga();

                    routeInfo.setText("Total trip time: 8h 20m");
                    break;
                default: //all routes
                    newOrleansTojackson();
                    jacksonTochattanooga();
                    newOrleansTochattanooga();
                    newOrleansToatlanta();
                    atlantaTochattanooga();
                    break;
            }

            IconAdder.addIcon("Sun", mMap, chattanooga);
        }
    }

    private static void newOrleansTochattanooga() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(newOrleans, chattanooga).get();
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

    private static void newOrleansTojackson() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(newOrleans, jackson).get();
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

    private static void jacksonTochattanooga() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(jackson, chattanooga).get();
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

    private static void newOrleansToatlanta() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(newOrleans, atlanta).get();
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

    private static void atlantaTochattanooga() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(atlanta, chattanooga).get();
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