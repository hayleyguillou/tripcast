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

//Sacremento to Green Bay
//No detour: 31 h
//Via sioux falls: 31 h
//Via kansas city: 34 h
public class Trip2 {
    static GoogleMap mMap;
    static int currRoute;

    static LatLng sacramento = new LatLng(38.5556, -121.4689);

    static LatLng saltLakeCity = new LatLng(40.7500, -111.8833);
    static LatLng siouxFalls = new LatLng(43.5364, -96.7317);

    //LatLng saltLakeCity = new LatLng(40.7500, -111.8833); salt lake city already defined
    static LatLng desMoines = new LatLng(41.5908, -93.6208);

    //LatLng saltLakeCity = new LatLng(40.7500, -111.8833); salt lake city already defined
    static LatLng kansasCity = new LatLng(39.0997, -94.5783);

    static LatLng greenBay = new LatLng(44.5133, -88.0158);

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
                    sacramentoTogreenBay();
                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 2: //detour through siouxFalls
                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();
                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through Kansas City
                    sacramentoTokansasCity();
                    kansasCityTogreenBay();
                    routeInfo.setText("Total trip time: 34h");
                    break;
                default: //all routes
                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();
                    sacramentoTogreenBay();
                    sacramentoTokansasCity();
                    kansasCityTogreenBay();
                    break;
            }

            //only need sacramento weather at first
            IconAdder.addIcon("Rain", mMap, sacramento);
        } else if (progress >= 15 && progress < 50) {

            //display the weather at 25% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Rain", mMap, saltLakeCity);

                    sacramentoTogreenBay();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 2: //detour through siouxFalls
                    IconAdder.addIcon("Rain", mMap, saltLakeCity);

                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through Kansas City
                    IconAdder.addIcon("Rain", mMap, saltLakeCity);

                    sacramentoTokansasCity();
                    kansasCityTogreenBay();

                    routeInfo.setText("Total trip time: 34h");
                    break;
                default: //all routes
                    IconAdder.addIcon("Sun", mMap, saltLakeCity);
                    IconAdder.addIcon("Tornado", mMap, saltLakeCity);
                    IconAdder.addIcon("Sun", mMap, saltLakeCity);

                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();
                    sacramentoTogreenBay();
                    sacramentoTokansasCity();
                    kansasCityTogreenBay();
                    break;
            }
        } else if (progress >= 50 && progress < 85) {
            //display the weather at 50% into the trip, depending on which route has been chosen
            switch (currRoute) {
                case 1: //no detour
                    IconAdder.addIcon("Rain", mMap, desMoines);

                    sacramentoTogreenBay();
                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 2: //detour through siouxFalls
                    IconAdder.addIcon("Snow", mMap, siouxFalls);

                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();
                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through Kansas City
                    IconAdder.addIcon("Rain", mMap, kansasCity);

                    sacramentoTokansasCity();
                    kansasCityTogreenBay();
                    routeInfo.setText("Total trip time: 34h");
                    break;
                default: //all routes
                    IconAdder.addIcon("Snow", mMap, siouxFalls);
                    IconAdder.addIcon("Tornado", mMap, desMoines);
                    IconAdder.addIcon("Snow", mMap, kansasCity);

                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();
                    sacramentoTogreenBay();
                    sacramentoTokansasCity();
                    kansasCityTogreenBay();
                    break;
            }
        } else {
            //display the weather at 100% into the trip (green bay)
            switch (currRoute) {
                case 1: //no detour
                    sacramentoTogreenBay();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 2: //detour through siouxFalls
                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();

                    routeInfo.setText("Total trip time: 31h");
                    break;
                case 3: //detour through Kansas City
                    sacramentoTokansasCity();
                    kansasCityTogreenBay();

                    routeInfo.setText("Total trip time: 34h");
                    break;
                default: //all routes
                    sacramentoTosiouxFalls();
                    siouxFallsTogreenBay();
                    sacramentoTogreenBay();
                    sacramentoTokansasCity();
                    kansasCityTogreenBay();
                    break;
            }

            IconAdder.addIcon("Sun", mMap, greenBay);
        }
    }

    private static void sacramentoTogreenBay() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(sacramento, greenBay).get();
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

    private static void sacramentoTosiouxFalls() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(sacramento, siouxFalls).get();
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

    private static void siouxFallsTogreenBay() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(siouxFalls, greenBay).get();
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

    private static void sacramentoTokansasCity() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(sacramento, kansasCity).get();
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

    private static void kansasCityTogreenBay() {
        GMapV2Direction directions = new GMapV2Direction();
        Document doc = null;

        try {
            doc = directions.execute(kansasCity, greenBay).get();
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