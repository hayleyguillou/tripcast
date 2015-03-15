package com.comp4020.tripcast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.util.Log;
import android.os.AsyncTask;

/*I found this code at http://stackoverflow.com/questions/14444228/
android-how-to-draw-route-directions-google-maps-api-v2-from-current-location-t
It's not mine!! But it works pretty well.
 */

public class GMapV2Direction extends AsyncTask<LatLng, Void, Document>{
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    public GMapV2Direction() {
    }

    protected Document doInBackground(LatLng...pts) {
        LatLng start = pts[0];
        LatLng end = pts[1];

        String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=driving";
        Log.d("url", url);

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Document doc) {
        Log.i("hi", "hi");
    }

    public String getDurationText(Document doc) {
        try {

            NodeList nl1 = doc.getElementsByTagName("duration");
            Node node1 = nl1.item(0);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "text"));
            Log.i("DurationText", node2.getTextContent());
            return node2.getTextContent();
        } catch (Exception e) {
            return "0";
        }
    }

    public int getDurationValue(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("duration");
            Node node1 = nl1.item(0);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "value"));
            Log.i("DurationValue", node2.getTextContent());
            return Integer.parseInt(node2.getTextContent());
        } catch (Exception e) {
            return -1;
        }
    }

    public String getDistanceText(Document doc) {
    /*
     * while (en.hasMoreElements()) { type type = (type) en.nextElement();
     *
     * }
     */

        try {
            NodeList nl1;
            nl1 = doc.getElementsByTagName("distance");

            Node node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = null;
            nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "value"));
            Log.d("DistanceText", node2.getTextContent());
            return node2.getTextContent();
        } catch (Exception e) {
            return "-1";
        }

    /*
     * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
     * doc.getElementsByTagName("distance");
     *
     * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
     * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
     * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
     * node2.getTextContent()); return node2.getTextContent(); } else return
     * "-1";} else return "-1";
     */
    }

    public int getDistanceValue(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("distance");
            Node node1 = null;
            node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "value"));
            Log.i("DistanceValue", node2.getTextContent());
            return Integer.parseInt(node2.getTextContent());
        } catch (Exception e) {
            return -1;
        }
    /*
     * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
     * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
     * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
     * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
     * node2.getTextContent()); return
     * Integer.parseInt(node2.getTextContent()); } else return 0;
     */
    }

    public String getStartAddress(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("start_address");
            Node node1 = nl1.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception e) {
            return "-1";
        }

    }

    public String getEndAddress(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("end_address");
            Node node1 = nl1.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception e) {
            return "-1";
        }
    }

    public String getCopyRights(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("copyrights");
            Node node1 = nl1.item(0);
            Log.i("CopyRights", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception e) {
            return "-1";
        }

    }

    public ArrayList<LatLng> getDirection(Document doc) {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2
                        .item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr
                            .get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    private int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    private String convertStreamToString(final InputStream input) throws Exception {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            final StringBuffer sBuf = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
            return sBuf.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                throw e;
            }
        }
    }

//    public List<Route> parse(String routesJSONString) throws Exception {
//        try {
//            List<Route> routeList = new ArrayList<Route>();
//            final JSONObject jSONObject = new JSONObject(routesJSONString);
//            JSONArray routeJSONArray = jSONObject.getJSONArray(ROUTES);
//            Route route;
//            JSONObject routesJSONObject;
//            for (int m = 0; m < routeJSONArray.length(); m++) {
//                route = new Route(context);
//                routesJSONObject = routeJSONArray.getJSONObject(m);
//                JSONArray legsJSONArray;
//                route.setSummary(routesJSONObject.getString(SUMMARY));
//                legsJSONArray = routesJSONObject.getJSONArray(LEGS);
//                JSONObject legJSONObject;
//                Leg leg;
//                JSONArray stepsJSONArray;
//                for (int b = 0; b < legsJSONArray.length(); b++) {
//                    leg = new Leg();
//                    legJSONObject = legsJSONArray.getJSONObject(b);
//                    leg.setDistance(new Distance(legJSONObject.optJSONObject(DISTANCE).optString(TEXT), legJSONObject.optJSONObject(DISTANCE).optLong(VALUE)));
//                    leg.setDuration(new Duration(legJSONObject.optJSONObject(DURATION).optString(TEXT), legJSONObject.optJSONObject(DURATION).optLong(VALUE)));
//                    stepsJSONArray = legJSONObject.getJSONArray(STEPS);
//                    JSONObject stepJSONObject, stepDurationJSONObject, legPolyLineJSONObject, stepStartLocationJSONObject, stepEndLocationJSONObject;
//                    Step step;
//                    String encodedString;
//                    LatLng stepStartLocationLatLng, stepEndLocationLatLng;
//                    for (int i = 0; i < stepsJSONArray.length(); i++) {
//                        stepJSONObject = stepsJSONArray.getJSONObject(i);
//                        step = new Step();
//                        JSONObject stepDistanceJSONObject = stepJSONObject.getJSONObject(DISTANCE);
//                        step.setDistance(new Distance(stepDistanceJSONObject.getString(TEXT), stepDistanceJSONObject.getLong(VALUE)));
//                        stepDurationJSONObject = stepJSONObject.getJSONObject(DURATION);
//                        step.setDuration(new Duration(stepDurationJSONObject.getString(TEXT), stepDurationJSONObject.getLong(VALUE)));
//                        stepEndLocationJSONObject = stepJSONObject.getJSONObject(END_LOCATION);
//                        stepEndLocationLatLng = new LatLng(stepEndLocationJSONObject.getDouble(LATITUDE), stepEndLocationJSONObject.getDouble(LONGITUDE));
//                        step.setEndLocation(stepEndLocationLatLng);
//                        step.setHtmlInstructions(stepJSONObject.getString(HTML_INSTRUCTION));
//                        legPolyLineJSONObject = stepJSONObject.getJSONObject(POLYLINE);
//                        encodedString = legPolyLineJSONObject.getString(POINTS);
//                        step.setPoints(decodePolyLines(encodedString));
//                        stepStartLocationJSONObject = stepJSONObject.getJSONObject(START_LOCATION);
//                        stepStartLocationLatLng = new LatLng(stepStartLocationJSONObject.getDouble(LATITUDE), stepStartLocationJSONObject.getDouble(LONGITUDE));
//                        step.setStartLocation(stepStartLocationLatLng);
//                        leg.addStep(step);
//                    }
//                    route.addLeg(leg);
//                }
//                routeList.add(route);
//            }
//            return routeList;
//        } catch (Exception e) {
//            throw e;
//        }
//    }
}