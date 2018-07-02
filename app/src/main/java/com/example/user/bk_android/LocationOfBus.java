package com.example.user.bk_android;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class LocationOfBus extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    GoogleMap mMap;
    Location current_position,Bus_station;
    Double lat[];
    Double lng[];
    String tentram[];
    ArrayList<LatLng> markerPoints;
    ArrayList<LatLng> coordList;
    TextView tv_bienso,tv_time,tv_vantoc,tv_khoangcach;
    int k = 0;
    int count = 0;
    Timer timer = new Timer();
    TimerTask timerTask;
    ImageView im_back3;
    static boolean kiemtra2 = false;
    Marker KTX;
    double khoangcach,vantoc;
    String bienso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_of_bus);
        initView();

        Bus_station = new Location("");
        Bus_station.setLatitude(getIntent().getDoubleExtra("lat_station",0));
        Bus_station.setLongitude(getIntent().getDoubleExtra("lng_station",0));
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        try {
            setViewForTracking();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        supportMapFragment.getMapAsync(this);
        SetOnClick();
        try {
            GetGPSOfBus();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(count == 0) {
            LatLng toado = new LatLng(current_position.getLatitude(), current_position.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(toado);
            KTX = mMap.addMarker(markerOptions);
            KTX.setTag(0);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(toado).zoom(15).bearing(90).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            try {
                Laydulieuxe50();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (k == 0) {
                direction(new LatLng(lat[0], lng[0]), new LatLng(lat[16], lng[16]));
                k++;
            }
            if (k == 1) {
                direction(new LatLng(lat[16], lng[16]), new LatLng(lat[29], lng[29]));
                k++;
            }
            count = 1;
        }
        if(count == 1){
            KTX.remove();
            LatLng toado = new LatLng(current_position.getLatitude(), current_position.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(toado);
            KTX = mMap.addMarker(markerOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(toado).zoom(15).bearing(90).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            try {
                setnewView();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("checkinhere",tv_vantoc.getText().toString());
        }

    }

    private void setnewView() throws InterruptedException, ExecutionException, JSONException {
        tv_vantoc.setText(String.valueOf(Math.round(vantoc*100)/100));
        if(vantoc > 1.0) {
            tv_time.setText(String.valueOf((double) Math.round( ((khoangcach / vantoc)*60)*100) / 100));
            Log.d("kiemtra tv_time", tv_time.getText().toString());
        }else{
            tv_time.setText(GetTimeBetween2Point(current_position,Bus_station));
            Log.d("kiemtra tv_time", tv_time.getText().toString());
        }
        tv_khoangcach.setText(String.valueOf(Math.round(khoangcach*100)/100));
        tv_bienso.setText(bienso);
    }

    private String getPositonCurrent() throws ExecutionException, InterruptedException {
        String s;
        GetCurrentBus50 getCurrentBus50 = new GetCurrentBus50(3);
        getCurrentBus50.execute();
        s = getCurrentBus50.get();
        Log.d("kiemtra HoDongtrieu",s);
        return s;
    }

    private void initView(){
        kiemtra2 = false;
        tv_bienso = findViewById(R.id.tv_bienso);
        tv_time = findViewById(R.id.tv_time);
        tv_vantoc = findViewById(R.id.tv_vantoc);
        tv_khoangcach = findViewById(R.id.tv_khoangcach);
        lat = new Double[30];
        lng = new Double[30];
        tentram = new String[30];
        markerPoints = new ArrayList<>();
        coordList = new ArrayList<>();
        im_back3 = findViewById(R.id.im_back3);
    }

    private void SetOnClick(){
        im_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemtra2 = true;
                timer.cancel();
                Intent intent = new Intent(LocationOfBus.this,TrackingOnBusStation.class);
                Bundle bundle = new Bundle();
                bundle.putString("activity","LocationOfBus");
                bundle.putInt("id",getIntent().getIntExtra("id",0));
                bundle.putString("header",getIntent().getStringExtra("header"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setViewForTracking() throws ExecutionException, InterruptedException, JSONException {
        String s = getPositonCurrent();
        JSONArray jsonArray = new JSONArray(s);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        current_position = new Location("");
        current_position.setLatitude(jsonObject.getDouble("latitude"));
        current_position.setLongitude(jsonObject.getDouble("longitude"));
        double khoangcach_t = DistanceBetween2Point(current_position,Bus_station);
        Log.d("kiemtra Khoangcach",khoangcach_t + "");
        double velocity = jsonObject.getDouble("speed");
        Log.d("kiemtra tv_velocity",velocity + "");
        tv_vantoc.setText(String.valueOf(Math.round(velocity*100)/100));
        if(velocity > 1.0) {
            tv_time.setText(String.valueOf((double) Math.round( ((khoangcach_t / velocity)*60)*100) / 100));
            Log.d("kiemtra tv_time", tv_time.getText().toString());
        }else{
            tv_time.setText(GetTimeBetween2Point(current_position,Bus_station));
            Log.d("kiemtra tv_time", tv_time.getText().toString());
        }
        tv_khoangcach.setText(String.valueOf(Math.round(khoangcach_t*100)/100));
        tv_bienso.setText(jsonObject.getString("bienso"));

    }


    private double DistanceBetween2Point(Location origin, Location destination) throws ExecutionException, InterruptedException, JSONException {
        double dist = 0;
        CalculationDistanceOnMap calculationDistanceOnMap = new CalculationDistanceOnMap(origin,destination);
        calculationDistanceOnMap.execute();
        String s = calculationDistanceOnMap.get();
        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonArray = jsonObject.getJSONArray("routes");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        JSONArray jsonArray1 = jsonObject1.getJSONArray("legs");
        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
        JSONObject jsonObject3 = jsonObject2.getJSONObject("distance");
        String temp = jsonObject3.getString("text");
        temp = temp.replace(" km","");
        dist = Double.valueOf(temp);
        return dist;
    }

    private String GetTimeBetween2Point(Location origin,Location destination) throws ExecutionException, InterruptedException, JSONException {
        String time = "0";
        CalculationDistanceOnMap calculationDistanceOnMap = new CalculationDistanceOnMap(origin,destination);
        calculationDistanceOnMap.execute();
        String s = calculationDistanceOnMap.get();
        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonArray = jsonObject.getJSONArray("routes");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        JSONArray jsonArray1 = jsonObject1.getJSONArray("legs");
        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
        JSONObject jsonObject3 = jsonObject2.getJSONObject("duration");
        String temp = jsonObject3.getString("text");
        temp = temp.replace(" phút","");
        temp = temp.replace(" mins","");
        time = String.valueOf(temp);
        Log.d("Hodongtrieu",time);
        return time;
    }

    private void Laydulieuxe50() throws ExecutionException, InterruptedException, JSONException {
        ParseTuyenXe50 parseTuyenXe50 = new ParseTuyenXe50();
        parseTuyenXe50.execute();
        String s1 = parseTuyenXe50.get();
        //markerPoints.clear();
        JSONArray jsonArray = new JSONArray(s1);
        for(int i = 0 ; i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            lat[i]= Double.valueOf(jsonObject.getString("latitude"));
            lng[i] = Double.valueOf(jsonObject.getString("longitude"));
            tentram[i] = jsonObject.getString("tentram");
            markerPoints.add(new LatLng(lat[i],lng[i]));
            coordList.add(new LatLng(lat[i],lng[i]));
        }

        for (int i = 0; i < lat.length ; i++){
            LatLng toado = new LatLng(lat[i],lng[i]);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.busstop);
            MarkerOptions option = new MarkerOptions();
            option.icon(icon);
            option.position(toado);
            Marker currentMarker = mMap.addMarker(option.title(tentram[i]));
            currentMarker.showInfoWindow();
        }
    }


    private void direction(LatLng vitrinhan,LatLng vitrigiao){
        if (markerPoints.size() > 1) {
            markerPoints.clear();
//            mMap.clear();
        }
        markerPoints.add(vitrinhan);
        MarkerOptions options1 = new MarkerOptions();
//
        options1.position(vitrinhan);
//
        markerPoints.add(vitrigiao);
//
        MarkerOptions options2 = new MarkerOptions();
//
        options2.position(vitrigiao);
//
//        if (markerPoints.size() == 1) {
//            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        } else if (markerPoints.size() == 2) {
//            options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vitrinhan, 16));
//        mMap.addMarker(options1);
//        mMap.addMarker(options2);

        if (markerPoints.size() >= 2) {
            vitrinhan = (LatLng) markerPoints.get(0);
            vitrigiao = (LatLng) markerPoints.get(1);

            String url = getDirectionsUrl1(vitrinhan, vitrigiao);

            LocationOfBus.DownloadTask downloadTask = new DownloadTask();

            downloadTask.execute(url);
        }


    }

    private void direction_demo(ArrayList<LatLng> markerPoints){
        //List<String> urls = new ArrayList<>();
        for(int j = 0; j<markerPoints.size();j++) {
            MarkerOptions options = new MarkerOptions();
            options.position(markerPoints.get(j));
        }
        List<String> urls = getDirectionsUrl(markerPoints);
        if (urls.size() > 1) {
            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i);
                LocationOfBus.DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        }
    }



    private List<String> getDirectionsUrl(ArrayList<LatLng> markerPoints) {
        List<String> mUrl = new ArrayList<>();
        if(markerPoints.size()>1) {

            String str_origin = markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;

            String str_dest = markerPoints.get(1).latitude + "," + markerPoints.get(1).longitude;

            String sensor = "sensor=false";
            String parameters = "origin=" +str_origin + "&destination=" + str_dest + "&" + sensor;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
            mUrl.add(url);

            for (int i = 2; i < markerPoints.size(); i++)//loop starts from 2 because 0 and 1 are already printed
            {
                str_origin = str_dest;
                str_dest = markerPoints.get(i).latitude + "," + markerPoints.get(i).longitude;
                parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" + sensor;
                url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                mUrl.add(url);
            }
        }

        return mUrl;
    }

    private String getDirectionsUrl1(LatLng origin, LatLng dest) {
        String waypoint = "";
        String key = "&key=AIzaSyBka9kF8sKdtzpYsrqQ6CIbO-fNUY7QfC8";
        switch (k){
            case 0:
                waypoint = coordList.get(15).latitude + "," + coordList.get(15).longitude ;
                break;
            case 1:
                waypoint = coordList.get(26).latitude + "," + coordList.get(26).longitude +"|" + coordList.get(27).latitude + "," + coordList.get(27).longitude;
                break;
        }

        Log.d("hodongtrieu",waypoint);
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";
        String mode = "mode=bus";
        String parameters = str_origin + "&" + str_dest  + "&waypoints=" + waypoint + "&" + sensor + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +key ;
        Log.d("hodongtrieu",url);
        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            LocationOfBus.ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.GREEN);  //set color for line
                lineOptions.geodesic(true);

            }
            mMap.addPolyline(lineOptions);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void GetGPSOfBus() throws ExecutionException, InterruptedException, JSONException {

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //refresh your textview
                    if (kiemtra2 == true) {
                        timerTask.cancel();
                    }
                    try {
                        getCurrent();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                supportMapFragment.getMapAsync(LocationOfBus.this);
                            }
                        };
                        runOnUiThread(runnable);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            timer.schedule(timerTask, 5000, 10000);
    }

    private void getCurrent() throws ExecutionException, InterruptedException, JSONException {
        String s = getPositonCurrent();
        JSONArray jsonArray = new JSONArray(s);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        current_position = new Location("");
        current_position.setLatitude(jsonObject.getDouble("latitude"));
        current_position.setLongitude(jsonObject.getDouble("longitude"));
        khoangcach = DistanceBetween2Point(current_position,Bus_station);
        vantoc = jsonObject.getDouble("speed");
        bienso = jsonObject.getString("bienso");
    }
}
