package com.example.user.bk_android.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.bk_android.Adapter.RecyclerViewAdapter;
import com.example.user.bk_android.Adapter.RecyclerViewAdapter_waitingXe50;
import com.example.user.bk_android.CalculationDistanceOnMap;
import com.example.user.bk_android.LocationOfBus;
import com.example.user.bk_android.ParseBus50;
import com.example.user.bk_android.ParseBusStation;
import com.example.user.bk_android.R;
import com.example.user.bk_android.RecyclerViewOnClick.RecyclerItemClickListener;
import com.example.user.bk_android.TableList.Table_waiting;
import com.example.user.bk_android.TrackingOnBusStation;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class PageFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";


    List<Table_waiting> tableList_waiting;
    RecyclerViewAdapter_waitingXe50 recyclerViewAdapter_waitingXe50;
    private int mPage;
    int id;
    Double Lat_station[],Lng_station[],Lat_bus[],Lng_bus[];
    ArrayList<Double> khoangcach,Speed_bus;
    String tentram[],bienso[];
    ArrayList<String> time,time_backup;
    Location location1,location2;
    ArrayList<Integer> ObjIndex;
    ArrayList<Double> s;
    int check = 0;
    RecyclerView recyclerView;
    Timer timer = new Timer();
    TimerTask timerTask;
    boolean doit = false;
    static boolean kiemtra = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final TrackingOnBusStation trackingOnBusStation = (TrackingOnBusStation) getActivity();
        id = trackingOnBusStation.getData();
        Log.d("Hello 2",id+"");
        khoangcach = new ArrayList<Double>();
        time = new ArrayList<String>();
        ObjIndex = new ArrayList<Integer>();
        s = new ArrayList<Double>();
        time_backup = new ArrayList<>();
        //s1 = new ArrayList<Double>();
        View view = inflater.inflate(R.layout.fragment_page,container,false);
        check++;

        recyclerView = view.findViewById(R.id.reDanhSachXe50);

        tableList_waiting = new ArrayList<>();
        recyclerViewAdapter_waitingXe50 = new RecyclerViewAdapter_waitingXe50(tableList_waiting);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),1);
        gridLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addData();
        recyclerView.setAdapter(recyclerViewAdapter_waitingXe50);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(trackingOnBusStation, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //do something
                kiemtra = true;
                Intent intent = new Intent(trackingOnBusStation, LocationOfBus.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat_station",Lat_station[1]);
                bundle.putDouble("lng_station",Lng_station[1]);
                bundle.putInt("id",id);
                bundle.putString("header",trackingOnBusStation.getHeader());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        kiemtra = false;
        if(doit == false) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //refresh your textview
                    Log.d("Hodongtrieu kiemtra", kiemtra + "");
                    if (kiemtra == true) {
                        timerTask.cancel();
                    }
                    khoangcach.clear();
                    time.clear();
                    ObjIndex.clear();
                    s.clear();
                    time_backup.clear();
                    addData();
                    recyclerViewAdapter_waitingXe50.notifyItemRangeChanged(0, recyclerViewAdapter_waitingXe50.getItemCount());
                }
            };
            timer.schedule(timerTask, 5000, 10000);
        }
        return view;
    }

    private void getDataOfBus() throws ExecutionException, InterruptedException, JSONException {

        ParseBusStation parseBusStation = new ParseBusStation(id+1);
        parseBusStation.execute();
        String s = parseBusStation.get();
        JSONArray jsonArray = new JSONArray(s);
        Lat_station = new Double[jsonArray.length()];
        Lng_station = new Double[jsonArray.length()];
        tentram = new String[jsonArray.length()];
        for(int j = 0 ; j<jsonArray.length();j++){
            JSONObject jsonObject = jsonArray.getJSONObject(j);
            Lat_station[j]= Double.valueOf(jsonObject.getString("latitude"));
            Lng_station[j] = Double.valueOf(jsonObject.getString("longitude"));
            tentram[j] = jsonObject.getString("tentram");
        }

        ParseBus50 parseBus50 = new ParseBus50();
        parseBus50.execute(1+"");
        String s1 = parseBus50.get();
        JSONArray jsonArray1 = new JSONArray(s1);
        Lat_bus = new Double[jsonArray1.length()];
        Lng_bus = new Double[jsonArray1.length()];
        Speed_bus = new ArrayList<Double>();
        bienso = new String[jsonArray1.length()];
        for(int j = 0 ; j<jsonArray1.length();j++){
            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
            Lat_bus[j]= jsonObject1.getDouble("latitude");
            Lng_bus[j] = jsonObject1.getDouble("longitude");
            Speed_bus.add(jsonObject1.getDouble("speed"));
            bienso[j] = jsonObject1.getString("bienso");
        }
    }

    public void CalculationWhatBusIsComing() throws InterruptedException, ExecutionException, JSONException {
        for(int i = 0 ; i < Lat_bus.length ; i=i+2){
            if((Lat_bus[i]<Lat_station[1])&&(Lat_bus[i] > 10.77149)&&(Lat_bus[i] < 10.880471)){
                location1 = new Location("");
                location1.setLatitude(Lat_bus[i]);
                location1.setLongitude(Lng_bus[i]);
                location2 = new Location("");
                location2.setLatitude(Lat_station[1]);
                location2.setLongitude(Lng_station[1]);
                Log.d("kiemtraCheck",DistanceBetween2Point(location1,location2) +"");
                ObjIndex.add(i);
                s.add(DistanceBetween2Point(location1,location2));
                time_backup.add(GetTimeBetween2Point(location1,location2));
            }else if( (Lng_bus[i]<Lng_station[1]) && (Lng_bus[i] > 106.755545) && (Lng_bus[i] < 106.8054)){
                location1 = new Location("");
                location1.setLatitude(Lat_bus[i]);
                location1.setLongitude(Lng_bus[i]);
                location2 = new Location("");
                location2.setLatitude(Lat_station[1]);
                location2.setLongitude(Lng_station[1]);
                Log.d("kiemtraCheck",DistanceBetween2Point(location1,location2) +"");
                ObjIndex.add(i);
                s.add(DistanceBetween2Point(location1,location2));
                time_backup.add(GetTimeBetween2Point(location1,location2));
            }else{
                //do nothing
            }
        }

        if(s.size()==0){
            //do nothing
            Toast.makeText(getContext(),"Không có chuyến nào sắp đến trạm",Toast.LENGTH_LONG).show();
            doit = true;
        }else {
            doit = false;
            int index = 0;
            double temp_time = 0;
            String temp;
            for (int i = 0; i < s.size(); i++) {
                double temp_s = s.get(i);
                khoangcach.add((double) Math.round(temp_s*100)/100);
                if(Speed_bus.get(ObjIndex.get(i)) > 1.0) {
                    temp_time = khoangcach.get(i) / Speed_bus.get(ObjIndex.get(i));
                    temp_time = Math.round((temp_time*60)*100)/100;
                    temp = String.valueOf(temp_time);
                }else{
                    temp = time_backup.get(i);
                }
                time.add(temp);
                index++;
            }
        }

    }

    public void sortData(){
        double temp_double;
        int temp_int;
        String temp_String;
        double t_khoangcach_d[] = new double[khoangcach.size()];
        int t_index_int[] = new int[ObjIndex.size()];
        String t_time_s[] = new String[time.size()];
        for (int i = 0; i<t_khoangcach_d.length;i++){
            t_khoangcach_d[i] = khoangcach.get(i);
            t_index_int[i] = ObjIndex.get(i);
            try{
                t_time_s[i] = time.get(i);
            }catch (Exception e){
                Log.w("EceptionOfThis2",e.toString());
            }
        }
        for (int i = 0 ;i < t_khoangcach_d.length - 1; i++){
            for(int j = i+1 ; j < t_khoangcach_d.length; j++){
                if(t_khoangcach_d[i] > t_khoangcach_d[j]){
                    temp_double = t_khoangcach_d[i];
                    t_khoangcach_d[i] = t_khoangcach_d[j];
                    t_khoangcach_d[j] = temp_double;

                    temp_int = t_index_int[i];
                    t_index_int[i] = t_index_int[j];
                    t_index_int[j] = temp_int;

                    temp_String = t_time_s[i];
                    t_time_s[i] = t_time_s[j];
                    t_time_s[j] = temp_String;
                }
            }
        }
        for (int i = 0; i<t_khoangcach_d.length;i++){
            khoangcach.remove(i);
            khoangcach.add(i,t_khoangcach_d[i]);
            try{
                time.remove(i);
                time.add(i,t_time_s[i]);
            }catch (Exception e){
                Log.w("EceptionHere",e.toString());
            }
            ObjIndex.remove(i);
            ObjIndex.add(i,t_index_int[i]);
        }
    }

    public void addData(){

        try {
            getDataOfBus();
            CalculationWhatBusIsComing();
            sortData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tableList_waiting.clear();
        for(int i = 0; i< ObjIndex.size();i++){
            Table_waiting table_waiting = new Table_waiting();
            table_waiting.setBienso(bienso[ObjIndex.get(i)]);
            table_waiting.setTime(time.get(i));
            table_waiting.setVantoc(Speed_bus.get(ObjIndex.get(i)));
            table_waiting.setKhoangcach(khoangcach.get(i));
            tableList_waiting.add(table_waiting);
        }

        //recyclerView.setAdapter(recyclerViewAdapter_waitingXe50);
    }

    public double DistanceBetween2Point(Location origin,Location destination) throws ExecutionException, InterruptedException, JSONException {
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

    public void CancelRefresh(){
        Log.d("hodongtrieu","cancel");
        kiemtra = true;
        timer.cancel();
    }
}



