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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Button;
import android.graphics.Color;

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
        setUpTripSetWindow();
        testDrawLine();

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
            latLng = new LatLng(49.8573,-97.1373);
        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current Location"));
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

            }
        });
    }

    //I know this is kind of weird but here's how to draw routes on the map!
    //you just need a start and end dest, google figures out the route from there
    private void testDrawLine() {
        GMapV2Direction directions = new GMapV2Direction();
        LatLng orig = new LatLng(40.8573,-97.1373);
        LatLng dest = new LatLng(49.8773,-97.1373);
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
