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
    int tripNum = 0;
    TextView routeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        routeInfo = (TextView) findViewById(R.id.route_info_textview);

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
        //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //LocationListener locationListener = new MyLocationListener();
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng latLng;
        //if(location == null) {
            latLng = new LatLng(49.8994,-97.1392);
        //} else {
            //latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //}

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
        Button tripSetButton = (Button) findViewById(R.id.trip_set_button);
        tripSetButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String origin;
                String destination;
                EditText originField = (EditText) findViewById(R.id.route_input1);
                EditText destinationField = (EditText) findViewById(R.id.route_input2);

                origin = originField.getText().toString().toLowerCase();
                destination = destinationField.getText().toString();

                //determine correct trip we are going on
                if (origin.equals("seattle") && destination.equals("st. louis")) {
                    tripNum = 1;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.6097, -122.3331), 14.0f));
                }
                else if (origin.equals("sacramento") && destination.equals("green bay")) {
                    tripNum = 2;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.5556, -121.4689), 14.0f));
                }
                else if (origin.equals("phoenix") && destination.equals("orlando")) {
                    tripNum = 3;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.4500, -112.0667), 14.0f));
                }
                else if (origin.equals("new orleans") && destination.equals("chattanooga")) {
                    tripNum = 4;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.9500, -90.0667), 14.0f));
                }
                else if (origin.equals("cleveland") && destination.equals("philadelphia")) {
                    tripNum = 5;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.4822, -81.6697), 14.0f));
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(originField.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(destinationField.getWindowToken(), 0);

                //make the trip position slider visible
                final SeekBar tripPos = (SeekBar) findViewById(R.id.trip_pos);
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
                        Trip.newTrip(tripNum, mMap, currRoute, routeInfo, progress);
                    }
                });

                //make the route selection button visible
                Button routeButt = (Button) findViewById(R.id.route_button);
                routeButt.setVisibility(View.VISIBLE);
                routeButt.setBackgroundColor(Color.BLACK);
                routeButt.setTextColor(Color.WHITE);

                routeButt.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        Button route1 = (Button) findViewById(R.id.route1);
                        Button route2 = (Button) findViewById(R.id.route2);
                        Button route3 = (Button) findViewById(R.id.route3);

                        route1.setVisibility(View.VISIBLE);
                        route1.setBackgroundColor(Color.YELLOW);
                        route2.setVisibility(View.VISIBLE);
                        route2.setBackgroundColor(Color.BLUE);
                        route3.setVisibility(View.VISIBLE);
                        route3.setBackgroundColor(Color.MAGENTA);

                        //find id of additional information
                        final LinearLayout route1Info = (LinearLayout) findViewById(R.id.route_info_text);
                        final LinearLayout route2Info = (LinearLayout) findViewById(R.id.route_info_text);
                        final LinearLayout route3Info = (LinearLayout) findViewById(R.id.route_info_text);

                        route1.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                currRoute = 1;
                                tripPos.setProgress(0);
                                Trip.newTrip(tripNum, mMap, currRoute, routeInfo, 0);
                                route2Info.setVisibility(View.INVISIBLE);
                                route3Info.setVisibility(View.INVISIBLE);
                                route1Info.setVisibility(View.VISIBLE);
                            }
                        });

                        route2.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                currRoute = 2;
                                tripPos.setProgress(0);
                                Trip.newTrip(tripNum, mMap, currRoute, routeInfo, 0);
                                route1Info.setVisibility(View.INVISIBLE);
                                route3Info.setVisibility(View.INVISIBLE);
                                route2Info.setVisibility(View.VISIBLE);
                            }
                        });

                        route3.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                currRoute = 3;
                                tripPos.setProgress(0);
                                Trip.newTrip(tripNum, mMap, currRoute, routeInfo, 0);
                                route1Info.setVisibility(View.INVISIBLE);
                                route2Info.setVisibility(View.INVISIBLE);
                                route3Info.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                currRoute = 0;
                Trip.newTrip(tripNum, mMap, currRoute, routeInfo, 0); //display weather for all routes, at progress 0

                //Set textview scrollbars
                ((TextView) findViewById(R.id.route_info_textview)).setMovementMethod(new ScrollingMovementMethod());
                //((TextView) findViewById(R.id.route2_info_textview)).setMovementMethod(new ScrollingMovementMethod());
                //((TextView) findViewById(R.id.route3_info_textview)).setMovementMethod(new ScrollingMovementMethod());
            }
        });
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