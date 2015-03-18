package com.comp4020.tripcast;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private SeekBar tripPos;
    private int currRoute; //0 for all routes, 1-3 for specific routes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

        currRoute = 0;

        setUpMapIfNeeded();
        setUpLocation();
        setUpMenuBar();
        setUpTripSetWindow();

        /*hideAlphaPane();
        View omegaPane = findViewById(R.id.drawer_layout);
        omegaPane.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft(){
                showAlphaPane();
                super.onSwipeLeft();
            }

            @Override
            public void onSwipeRight(){
                hideAlphaPane();
                super.onSwipeRight();
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //hideAlphaPane();
                return super.onTouch(v, event);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void setUpLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng latLng;
        if(location == null) {
            latLng = new LatLng(47.6097,-122.3331);
        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current Location"));
    }

    private void setUpMenuBar(){
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.menu_bar);
        //rl.setBackgroundColor(Color.WHITE);
    }



    private void setUpTripSetWindow() {
        Button tripSetButton = (Button)findViewById(R.id.trip_set_button);
        tripSetButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String origin;
                String destination;
                EditText originField = (EditText)findViewById(R.id.route_input1);
                EditText destinationField = (EditText)findViewById(R.id.route_input2);

                origin = originField.getText().toString();
                destination = destinationField.getText().toString();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(originField.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(destinationField.getWindowToken(), 0);

                //make the trip position slider visible
                SeekBar tripPos = (SeekBar)findViewById(R.id.trip_pos);
                tripPos.setVisibility(View.VISIBLE);

                tripPos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int progress = seekBar.getProgress();

                        //display the weather for the appropriate trip position and route
                        displayWeather(progress);
                    }
                });

                //make the route selection button visible
                Button routeButt = (Button)findViewById(R.id.route_button);
                routeButt.setVisibility(View.VISIBLE);
                routeButt.setBackgroundColor(Color.BLACK);
                routeButt.setTextColor(Color.WHITE);

                routeButt.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v) {
                        Button route1 = (Button)findViewById(R.id.route1);
                        Button route2 = (Button)findViewById(R.id.route2);
                        Button route3 = (Button)findViewById(R.id.route3);

                        route1.setVisibility(View.VISIBLE);
                        route1.setBackgroundColor(Color.RED);
                        route2.setVisibility(View.VISIBLE);
                        route2.setBackgroundColor(Color.BLUE);
                        route3.setVisibility(View.VISIBLE);
                        route3.setBackgroundColor(Color.GREEN);

                        //find id of additional information
                        final LinearLayout route1Info = (LinearLayout)findViewById(R.id.route1_info_text);
                        final LinearLayout route2Info = (LinearLayout)findViewById(R.id.route2_info_text);
                        final LinearLayout route3Info = (LinearLayout)findViewById(R.id.route3_info_text);

                        route1.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View v) {
                                currRoute = 1;
                                displayWeather(0);
                                route2Info.setVisibility(View.INVISIBLE);
                                route3Info.setVisibility(View.INVISIBLE);
                                route1Info.setVisibility(View.VISIBLE);
                            }
                        });

                        route2.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View v) {
                                currRoute = 2;
                                displayWeather(0);
                                route1Info.setVisibility(View.INVISIBLE);
                                route3Info.setVisibility(View.INVISIBLE);
                                route2Info.setVisibility(View.VISIBLE);
                            }
                        });

                        route3.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View v) {
                                currRoute = 3;
                                displayWeather(0);
                                route1Info.setVisibility(View.INVISIBLE);
                                route2Info.setVisibility(View.INVISIBLE);
                                route3Info.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                currRoute = 0;
                displayWeather(0); //display weather for all routes, at progress 0

                //Set textview scrollbars
                ((TextView)findViewById(R.id.route1_info_textview)).setMovementMethod(new ScrollingMovementMethod());
                ((TextView)findViewById(R.id.route2_info_textview)).setMovementMethod(new ScrollingMovementMethod());
                ((TextView)findViewById(R.id.route3_info_textview)).setMovementMethod(new ScrollingMovementMethod());
            }
        });
    }

    private void displayWeather(int progress){

        LatLng seattle = new LatLng(47.6097,-122.3331);

        LatLng missoula = new LatLng(46.8625, -114.0117);
        LatLng minneapolis = new LatLng(44.9778,-93.2650);

        LatLng saltLakeCity = new LatLng(40.7500, -111.8833);
        LatLng northPlatte = new LatLng(41.1359, -100.7705);

        LatLng lasVegas = new LatLng(36.1215,-115.1739);
        LatLng denver = new LatLng(39.7392, -104.9903);

        LatLng stLouis = new LatLng(38.6272,-90.1978);

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
        }
        else if (progress >= 25 && progress < 50) {

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
        }
        else if (progress >= 50 && progress < 75) {
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
        }
        else {
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

    private void seattleToStLouis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(47.6097,-122.3331);
        LatLng dest = new LatLng(38.6272,-90.1978);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        }
        catch (Exception e) {
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
            addWrittenDirections(nl, R.id.route1_info_textview);
        }
    }

    private void seattleToMinneapolis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(47.6097,-122.3331);
        LatLng dest = new LatLng(44.9778,-93.2650); //detour through minneapolis
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        }
        catch (Exception e) {
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
            addWrittenDirections(nl, R.id.route2_info_textview);
        }
    }

    private void minneapolisToStLouis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(44.9778,-93.2650);
        LatLng dest = new LatLng(38.6272,-90.1978);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        }
        catch (Exception e) {
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
            addWrittenDirections(nl, R.id.route2_info_textview);
        }
    }

    private void seattleToLasVegas() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(47.6097,-122.3331);
        LatLng dest = new LatLng(36.1215,-115.1739);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        }
        catch (Exception e) {
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
            addWrittenDirections(nl, R.id.route3_info_textview);
        }
    }

    private void lasVegasToStLouis() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(36.1215,-115.1739);
        LatLng dest = new LatLng(38.6272,-90.1978);
        Document doc = null;

        try {
            doc = directions.execute(orig, dest).get();
        }
        catch (Exception e) {
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
            addWrittenDirections(nl, R.id.route3_info_textview);
        }
    }

    private void addWrittenDirections(NodeList instructions, int id) {
        TextView routeInfo = (TextView)findViewById(id);

        for(int i = 0; i < instructions.getLength(); i++) {
            routeInfo.append(
                    Html.fromHtml(instructions.item(i).getTextContent() + "\n")
            );
        }
    }

    private void hideAlphaPane() {
        View alphaPane = findViewById(R.id.left_drawer);
        if (alphaPane.getVisibility() == View.VISIBLE) {
            alphaPane.setVisibility(View.GONE); }
    }

    /** * Method to show the Alpha pane */
    private void showAlphaPane() {
        View alphaPane = findViewById(R.id.drawer_layout);
        if (alphaPane.getVisibility() == View.GONE) {
            alphaPane.setVisibility(View.VISIBLE);
        }
    }
}
