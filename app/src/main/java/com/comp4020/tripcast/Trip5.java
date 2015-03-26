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

//cleveland to philadelphia
//No detour: 6h 32m
//Via williamsport: 7h 18m
//Via morgantown: 6h 41m
public class Trip5 {
    static GoogleMap mMap;
    static int currRoute;

    static LatLng cleveland = new LatLng(41.4822, -81.6697);

    static LatLng forest = new LatLng(41.2444, -79.0010);
    static LatLng williamsport = new LatLng(41.2444, -77.0186);

    static LatLng pittsburgh = new LatLng(40.4397, -79.9764);
    static LatLng harrisburg = new LatLng(40.2697, -76.8756);

    static LatLng morgantown = new LatLng(39.6336, -79.9506);
    static LatLng baltimore = new LatLng(39.2833, -76.6167);

    static LatLng philadelphia = new LatLng(39.9500, -75.1667);

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
                    clevelandTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 32m");
                    break;
                case 2: //detour through forest
                    clevelandTowilliamsport();
                    williamsportTophiladelphia();

                    routeInfo.setText("Total trip time: 7h 18m");
                    break;
                case 3: //detour through morgantown
                    clevelandTomorgantown();
                    morgantownTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 41m");
                    break;
                default: //all routes
                    clevelandTowilliamsport();
                    williamsportTophiladelphia();
                    clevelandTophiladelphia();
                    clevelandTomorgantown();
                    morgantownTophiladelphia();
                    break;
            }

            //only need cleveland weather at first
            IconAdder.addIcon("Rain", mMap, cleveland);
        } else if (progress >= 15 && progress < 50) {

            //display the weather at 25% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, pittsburgh);

                    clevelandTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 32m");
                    break;
                case 2: //detour through forest
                    IconAdder.addIcon("Sun", mMap, forest);

                    clevelandTowilliamsport();
                    williamsportTophiladelphia();

                    routeInfo.setText("Total trip time: 7h 18m");
                    break;
                case 3: //detour through morgantown
                    IconAdder.addIcon("Sun", mMap, morgantown);

                    clevelandTomorgantown();
                    morgantownTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 41m");
                    break;
                default: //all routes
                    IconAdder.addIcon("Sun", mMap, pittsburgh);
                    IconAdder.addIcon("Tornado", mMap, forest);
                    IconAdder.addIcon("Sun", mMap, morgantown);

                    clevelandTowilliamsport();
                    williamsportTophiladelphia();
                    clevelandTophiladelphia();
                    clevelandTomorgantown();
                    morgantownTophiladelphia();
                    break;
            }
        } else if (progress >= 50 && progress < 85) {
            //display the weather at 50% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Tornado", mMap, harrisburg);

                    clevelandTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 32m");
                    break;
                case 2: //detour through forest
                    IconAdder.addIcon("Snow", mMap, williamsport);

                    clevelandTowilliamsport();
                    williamsportTophiladelphia();

                    routeInfo.setText("Total trip time: 7h 18m");
                    break;
                case 3: //detour through morgantown
                    IconAdder.addIcon("Snow", mMap, baltimore);

                    clevelandTomorgantown();
                    morgantownTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 41m");
                    break;
                default: //all routes
                    IconAdder.addIcon("Snow", mMap, harrisburg);
                    IconAdder.addIcon("Tornado", mMap, williamsport);
                    IconAdder.addIcon("Snow", mMap, baltimore);

                    clevelandTowilliamsport();
                    williamsportTophiladelphia();
                    clevelandTophiladelphia();
                    clevelandTomorgantown();
                    morgantownTophiladelphia();
                    break;
            }
        } else {
            //display the weather at 100% into the trip (green bay)
            switch (currRoute) {
                case 1: //no detour
                    clevelandTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 32m");
                    break;
                case 2: //detour through forest
                    clevelandTowilliamsport();
                    williamsportTophiladelphia();

                    routeInfo.setText("Total trip time: 7h 18m");
                    break;
                case 3: //detour through morgantown
                    clevelandTomorgantown();
                    morgantownTophiladelphia();

                    routeInfo.setText("Total trip time: 6h 41m");
                    break;
                default: //all routes
                    clevelandTowilliamsport();
                    williamsportTophiladelphia();
                    clevelandTophiladelphia();
                    clevelandTomorgantown();
                    morgantownTophiladelphia();
                    break;
            }

            IconAdder.addIcon("Sun", mMap, philadelphia);
        }
    }

    private static void clevelandTophiladelphia() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(cleveland, philadelphia).get();
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

    private static void clevelandTowilliamsport() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(cleveland, williamsport).get();
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

    private static void williamsportTophiladelphia() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(williamsport, philadelphia).get();
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

    private static void clevelandTomorgantown() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(cleveland, morgantown).get();
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

    private static void morgantownTophiladelphia() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(morgantown, philadelphia).get();
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