package com.comp4020.tripcast;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Button;
import android.graphics.Color;
import android.view.*;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.*;

import com.comp4020.tripcast.IconAdder;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }*/

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

                Log.i("origin", origin);
                Log.i("destination", destination);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(originField.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(destinationField.getWindowToken(), 0);

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
                    }
                });

                //route1
                seattleToStLouis();

                //route2
                seattleToMinneapolis();
                minneapolisToStLouis();

                //route3
                seattleToLasVegas();
                lasVegasToStLouis();

                displayWeatherOrigDest();
                displayWeatherRoute1();
                displayWeatherRoute2();
                displayWeatherRoute3();
            }
        });
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
        }
    }

    private void seattleToLasVegas() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(47.6097,-122.3331);
        LatLng dest = new LatLng(36.1215,-115.1739); //detour through minneapolis
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
        }
    }

    private void displayWeatherOrigDest(){
        LatLng seattle = new LatLng(47.6097,-122.3331);
        LatLng stLouis = new LatLng(38.6272, -90.1978);

        IconAdder.addIcon("Rain", mMap, seattle);
        IconAdder.addIcon("Rain", mMap, stLouis);
    }

    private void displayWeatherRoute1(){
        LatLng saltLakeCity = new LatLng(40.7500, -111.8833);

        IconAdder.addIcon("Tornado", mMap, saltLakeCity);
    }

    private void displayWeatherRoute2(){
        LatLng minneapolis = new LatLng(44.9778, -93.2650);

        IconAdder.addIcon("Snow", mMap, minneapolis);
    }

    private void displayWeatherRoute3(){
        LatLng vegas = new LatLng(36.1215, -115.1739);

        IconAdder.addIcon("Sun", mMap, vegas);
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
