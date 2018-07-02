package com.example.user.bk_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bk_android.Adapter.RecyclerViewAdapter;
import com.example.user.bk_android.Adapter.RecyclerViewAdapterXe50;
import com.example.user.bk_android.RecyclerViewOnClick.RecyclerItemClickListener;
import com.example.user.bk_android.TableList.Table;
import com.example.user.bk_android.TableList.TableXe50;
import com.example.user.bk_android.fragment.PageFragment;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import java.util.concurrent.ExecutionException;

public class ChiTietChuyenXe extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    CameraPosition cameraPosition;
    SupportMapFragment supportMapFragment;
    TextView tv_luot,tv_chitiettuyenxe,tv_chuyenxe;
    ImageView im_soxe,im_back;
    boolean check = true;
    boolean check1 = false;
    boolean check_b = true;
    int pos;
    ArrayList<LatLng> markerPoints;
    ArrayList<LatLng> coordList;
    int k = 0;
    Double lat[];
    Double lng[];
    String tentram[];

    List<TableXe50> tableListXe50;
    RecyclerViewAdapterXe50 recyclerViewAdapterXe50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_chuyen_xe);


        try {
            initview();
            Laydulieuxe50();
            setRecyclerView();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_chuyenxe.setText(getIntent().getStringExtra("NameTuyen"));
        tv_chitiettuyenxe.setText(getIntent().getStringExtra("Name50"));
        im_soxe.setImageResource(R.drawable.number50);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(ChiTietChuyenXe.this);
        initClickListener();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < tentram.length ; i++){
            LatLng toado = new LatLng(lat[i],lng[i]);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.busstop);
            MarkerOptions option = new MarkerOptions();
            option.icon(icon);
            option.position(toado);
            Marker currentMarker = googleMap.addMarker(option.title(tentram[i]));
            currentMarker.showInfoWindow();
            if(i == 0){
                Marker KTX = googleMap.addMarker(option);
                KTX.setTag(0);
                cameraPosition = new CameraPosition.Builder().target(toado).zoom(15).bearing(90).tilt(0).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        }

        if(k==0) {
            direction(new LatLng(lat[0], lng[0]), new LatLng(lat[16], lng[16]));
            k++;
        }
        if(k == 1){
            direction(new LatLng(lat[16], lng[16]), new LatLng(lat[29], lng[29]));
            k++;
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(ChiTietChuyenXe.this,marker.getTitle(),Toast.LENGTH_LONG).show();
                String s = marker.getTitle();
                for(int i = 0; i < tentram.length ; i++){
                    if(tentram[i].equals(s)){
                        Intent intent = new Intent(ChiTietChuyenXe.this,TrackingOnBusStation.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("activity","ChiTietChuyenXe");
                        bundle.putInt("id",i);
                        bundle.putString("header",tentram[i]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        startActivity(intent);
                    }
                    else {
                        //do nothing
                    }
                }
            }
        });

    }

    private void initview(){
        tv_luot = findViewById(R.id.tv_luot);
        tv_chitiettuyenxe = findViewById(R.id.tv_chitiettuyenxe);
        tv_chuyenxe = findViewById(R.id.tv_chuyenxe);
        im_soxe = findViewById(R.id.im_soxe);
        im_back = findViewById(R.id.im_back);
        lat = new Double[30];
        lng = new Double[30];
        tentram = new String[30];
        markerPoints = new ArrayList<>();
        coordList = new ArrayList<LatLng>();
    }

    private void initClickListener(){
        tv_luot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == true) {
                    tv_luot.setText("Lượt về");
                    check = false;
                }else{
                    tv_luot.setText("Lượt đi");
                    check = true;
                }
            }
        });

        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietChuyenXe.this,tracuu.class);
                startActivity(intent);
            }
        });


    }

    private void Laydulieuxe50() throws ExecutionException, InterruptedException, JSONException {
        ParseTuyenXe50 parseTuyenXe50 = new ParseTuyenXe50();
        parseTuyenXe50.execute();
        String s1 = parseTuyenXe50.get();
        markerPoints.clear();
        JSONArray jsonArray = new JSONArray(s1);
        for(int i = 0 ; i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            lat[i]= Double.valueOf(jsonObject.getString("latitude"));
            lng[i] = Double.valueOf(jsonObject.getString("longitude"));
            tentram[i] = jsonObject.getString("tentram");
            markerPoints.add(new LatLng(lat[i],lng[i]));
            coordList.add(new LatLng(lat[i],lng[i]));
        }
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.reChuyenXe50);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ChiTietChuyenXe.this,recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                cameraPosition = new CameraPosition.Builder().target(new LatLng(lat[position],lng[position])).zoom(15).bearing(90).tilt(0).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        recyclerView.setHasFixedSize(true);
        tableListXe50 = new ArrayList<>();
        recyclerViewAdapterXe50 = new RecyclerViewAdapterXe50(tableListXe50);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        gridLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapterXe50);
        for (int j = 0 ; j < tentram.length ; j++){
            TableXe50 tableXe50 = new TableXe50();
            tableXe50.setString(tentram[j]);
            tableListXe50.add(tableXe50);
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

            DownloadTask downloadTask = new DownloadTask();

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
                DownloadTask downloadTask = new DownloadTask();
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

            ParserTask parserTask = new ParserTask();


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

}
