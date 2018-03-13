package com.example.yusufsatilmis.gezinge;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;

import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    Polyline polyline1;

    String socketAddress = Data.ip_adress;
    int socketPort1 = Integer.parseInt(Data.port1_adress);
    int socketPort2 = Integer.parseInt(Data.port2_adress);
    int seekBarProcces;
    int radius = 0;
    double center_lat = 0;
    double center_lng = 0;


    TextView time_tv;
    TextView rate_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        time_tv = findViewById(R.id.response_textView);
        rate_tv = findViewById(R.id.indirge_textView);


        Button original_data = findViewById(R.id.original_btn);
        Button indirge_button = findViewById(R.id.indirge_btn);
        Button temizle_button = findViewById(R.id.temizle_btn);
        Button kucuk_button = findViewById(R.id.kucuk_btn);

        final TextView seek_tv = findViewById(R.id.seekbar_procces);
        SeekBar seekbar = findViewById(R.id.seekBar);

        seekbar.setMax(100);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                seek_tv.setText("%" + i);
                seekBarProcces = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        original_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stylePolylineOriginal();

            }
        });


        indirge_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ındirgeme_server myClientTask = new ındirgeme_server();
                myClientTask.execute(createOriginalMessage());
            }
        });

        temizle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear_map();
            }
        });


        kucuk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search_server myClientTask = new search_server();
                myClientTask.execute(createSmallMessage());

            }
        });

    }


    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        Log.e("Yusuf", "onMapReady ");


        /*
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Data.center)
                .zoom(0)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        */

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                stylePolylineOriginal();
            }
        }, 1500);

        // stylePolylineOriginal();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng clickCoords) {
                Log.e("Yusuf", clickCoords.latitude + " --- " + clickCoords.longitude);

                Data.center = clickCoords;

                radius = (seekBarProcces + 1) * 100;
                center_lng = clickCoords.longitude;
                center_lat = clickCoords.latitude;


                Log.e("Yusuf radius", radius + "");

                Data.zoom = map.getCameraPosition().zoom;

                map.clear();

                CircleOptions circleOptions = new CircleOptions();

                // Specifying the center of the circle
                circleOptions.center(clickCoords);

                // Radius of the circle
                circleOptions.radius(radius);

                // Border color of the circle
                circleOptions.strokeColor(0xFF0000FF);

                // Fill color of the circle
                circleOptions.fillColor(0x110000FF);

                // Border width of the circle
                circleOptions.strokeWidth(2);

                // Adding the circle to the GoogleMap
                map.addCircle(circleOptions);
                stylePolylineOriginal();
               // miniDataResponse();
                stylePolylineResponse();

            }


        });


    }

    private String createOriginalMessage() {
        JSONObject root = new JSONObject();
        JSONObject root2 = new JSONObject();

        Random r = new Random();

        int min = 0;
        int max = 50;

        for (int i = 0; i < Data.latlng_original.size(); i++) {


            try {

                JSONObject latlng = new JSONObject();
                latlng.put("id", i);
                latlng.put("lat", Data.lat_original.get(i));
                latlng.put("lng", Data.lng_original.get(i));


                root.put(Data.latlng_original.get(i), latlng);


            } catch (JSONException e1) {

                Log.d("hata", "hatta");
            }


        }

        try {
            root2.put("array", root);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root2.toString();


    }


    private String createSmallMessage() {
        JSONObject root = new JSONObject();
        JSONObject root2 = new JSONObject();
        JSONObject root3 = new JSONObject();


        JSONObject round = new JSONObject();
        JSONObject lat = new JSONObject();
        JSONObject lng = new JSONObject();

        Random r = new Random();

        int min = 0;
        int max = 50;

        for (int i = 0; i < Data.latlng_original.size(); i++) {


            try {

                JSONObject latlng = new JSONObject();
                latlng.put("id", i);
                latlng.put("lat", Data.lat_original.get(i));
                latlng.put("lng", Data.lng_original.get(i));


                root.put(Data.latlng_original.get(i), latlng);


            } catch (JSONException e1) {

                Log.d("hata", "hatta");
            }


        }

        for (int i = 0; i < Data.latlng_response.size(); i++) {


            try {

                JSONObject latlng = new JSONObject();
                latlng.put("id", i);
                latlng.put("lat", Data.lat_response.get(i));
                latlng.put("lng", Data.lng_response.get(i));


                root2.put(Data.latlng_response.get(i), latlng);


            } catch (JSONException e1) {

                Log.d("hata", "hatta");
            }


        }

        try {

            round.put("roundd", radius);
            lat.put("latt", center_lat);
            lng.put("lngg", center_lng);

            root3.put("round", round);
            root3.put("lat", lat);
            root3.put("lng", lng);


            root3.put("array", root);
            root3.put("array2", root2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root3.toString();


    }

    private void stylePolylineOriginal() {

        Log.d("Yusuf", "stylePolylineOriginal Veri sayısı--> " + Data.latlng_original.size());


        Polyline polyline = map.addPolyline(new PolylineOptions()
                .clickable(true));

        ArrayList<LatLng> map_latlng = new ArrayList<>();


        for (int i = 0; i < Data.latlng_original.size(); i++) {


            map_latlng.add(new LatLng(Float.parseFloat(Data.lat_original.get(i)), Float.parseFloat(Data.lng_original.get(i))));

        }

        polyline.setPoints(map_latlng);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Data.center)
                .zoom(Data.zoom)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(Data.center, 14));
        //map.moveCamera(CameraUpdateFactory.);


        // polyline.setStartCap(new RoundCap());


        //polyline.setEndCap(new RoundCap());
        polyline.setWidth(12);
        polyline.setColor(Color.BLUE);
        // polyline.setJointType(JointType.ROUND);


    }


    private void stylePolylineResponse() {


        Log.d("Yusuf", "stylePolylineResponse Veri sayısı--> " + Data.latlng_response.size());


        Polyline polyline = map.addPolyline(new PolylineOptions()
                .clickable(true));

        ArrayList<LatLng> map_latlng = new ArrayList<>();


        for (int i = 0; i < Data.latlng_response.size(); i++) {


            map_latlng.add(new LatLng(Float.parseFloat(Data.lat_response.get(i)), Float.parseFloat(Data.lng_response.get(i))));

        }

        polyline.setPoints(map_latlng);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Data.center)
                .zoom(Data.zoom)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(Data.center, 14));


        // polyline.setStartCap(new RoundCap());


        //polyline.setEndCap(new RoundCap());
        polyline.setWidth(8);
        polyline.setColor(Color.RED);
        // polyline.setJointType(JointType.ROUND);
    }

    private void miniDataResponse() {


        Log.d("Yusuf", "Circle orjn Veri sayısı--> " + Data.latlng_original_mini.size());
        Log.d("Yusuf", "Circle resp Veri sayısı--> " + Data.latlng_response_mini.size());







        for (int i = 0; i < Data.latlng_original_mini.size(); i++) {


            LatLng mini = new LatLng(Float.parseFloat(Data.lat_original_mini.get(i)), Float.parseFloat(Data.lng_original_mini.get(i)));

            CircleOptions circleOptions = new CircleOptions();

            // Specifying the center of the circle
            circleOptions.center(mini);

            // Radius of the circle
            circleOptions.radius(6);

            // Border color of the circle
            circleOptions.strokeColor(Color.YELLOW);

            // Fill color of the circle
            circleOptions.fillColor(Color.YELLOW);

            // Border width of the circle
            circleOptions.strokeWidth(2);

            // Adding the circle to the GoogleMap
            map.addCircle(circleOptions);

            // Log.d("Yusuf", i+". çizildi " );

        }

        for (int i = 0; i < Data.latlng_response_mini.size(); i++) {


            LatLng mini = new LatLng(Float.parseFloat(Data.lat_response_mini.get(i)), Float.parseFloat(Data.lng_response_mini.get(i)));

            CircleOptions circleOptions = new CircleOptions();

            // Specifying the center of the circle
            circleOptions.center(mini);

            // Radius of the circle
            circleOptions.radius(4);

            // Border color of the circle
            circleOptions.strokeColor(Color.GREEN);

            // Fill color of the circle
            circleOptions.fillColor(Color.GREEN);

            // Border width of the circle
            circleOptions.strokeWidth(2);

            // Adding the circle to the GoogleMap
            map.addCircle(circleOptions);

            // Log.d("Yusuf", i+". çizildi " );

        }


        Data.zoom = 19f;

        int tmp =Data.latlng_response_mini.size()/2;


        if (Data.latlng_response_mini.size()>0) {
            Data.center = new LatLng(Float.parseFloat(Data.lat_response_mini.get(tmp)), Float.parseFloat(Data.lng_response_mini.get(tmp)));
        }


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Data.center)
                .zoom(Data.zoom)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    public class ındirgeme_server extends AsyncTask<String, Void, Void> {


        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        String response = "----";
        String time_s = "----";
        String rate_s = "----";

        @Override
        protected Void doInBackground(String... params) {
            String msgToServer = params[0];


            Data.responseDataClear();


            try {
                socket = new Socket(socketAddress, socketPort1);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (UnknownHostException e) {
                Log.e("Yusuf -- hata ", "Sunucu bulunamadı");


            } catch (IOException e) {

                Log.e("Yusuf -- hata ", "I/O exception:" + e.getMessage());

            }


            try {

                out.println(msgToServer);
                Log.d("Yusuf -- out", msgToServer);


                response = in.readLine();

                Log.d("Yusuf -- response", response);


                JSONObject jsonObj = new JSONObject(response);
                JSONObject rate = jsonObj.getJSONObject("rate");
                JSONObject time = jsonObj.getJSONObject("time");

                Log.d("Yusuf -- rate-time", rate.get("ratee").toString() + "--" + time.get("timee").toString());

                time_s = time.get("timee").toString();
                rate_s = rate.get("ratee").toString();

                JSONObject array = jsonObj.getJSONObject("array");


                Log.d("Yusuf -- array", array.length() + "");


                for (int i = 0; i < array.length(); i++) {

                    String name = "latlng" + i;


                    // Log.e("Yusuf -- name",name);


                    JSONObject lat_lng = array.getJSONObject(name);

                    Data.latlng_response.add(name);
                    Data.id_response.add(lat_lng.get("id").toString());
                    Data.lat_response.add(lat_lng.get("lat").toString());
                    Data.lng_response.add(lat_lng.get("lng").toString());


                }


                response = array.length() + "";


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.d("Yusuf -- bitis", "Baglanti kesiliyor...");


            try {
                out.close();
                in.close();
                //stdIn.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            time_tv.setText(time_s + " sn");
            rate_tv.setText("% " + rate_s);
            Log.d("Yusuf - latlng_rsp", "" + Data.latlng_response.size());
            stylePolylineResponse();


            super.onPostExecute(result);
        }
    }


    public class search_server extends AsyncTask<String, Void, Void> {


        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        String response = "----";
        String time_s = "----";
        String rate_s = "----";

        @Override
        protected Void doInBackground(String... params) {
            String msgToServer = params[0];


            Data.responseMiniDataClear();
            Data.responseOriginalDataClear();


            try {
                socket = new Socket(socketAddress, socketPort2);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (UnknownHostException e) {
                Log.e("Yusuf -- hata ", "Sunucu bulunamadı");


            } catch (IOException e) {

                Log.e("Yusuf -- hata ", "I/O exception:" + e.getMessage());

            }


            try {

                out.println(msgToServer);
                Log.d("Yusuf -- out", msgToServer);


                response = in.readLine();

                Log.d("Yusuf -- response", response);


                JSONObject jsonObj = new JSONObject(response);
                JSONObject rate = jsonObj.getJSONObject("rate");
                JSONObject time = jsonObj.getJSONObject("time");

                Log.d("Yusuf -- rate-time", rate.get("ratee").toString() + "--" + time.get("timee").toString());

                time_s = time.get("timee").toString();
                rate_s = rate.get("ratee").toString();

                JSONObject array = jsonObj.getJSONObject("array");
                Log.d("Yusuf -- array", array.length() + "");


                for (int i = 0; i < array.length(); i++) {
                    String name = "latlng" + i;

                    JSONObject lat_lng = array.getJSONObject(name);

                    Data.latlng_original_mini.add(name);
                    Data.id_original_mini.add(lat_lng.get("id").toString());
                    Data.lat_original_mini.add(lat_lng.get("lat").toString());
                    Data.lng_original_mini.add(lat_lng.get("lng").toString());

                }



                JSONObject array2 = jsonObj.getJSONObject("array2");
                Log.d("Yusuf -- array2", array2.length() + "");


                for (int i = 0; i < array2.length(); i++) {
                    String name = "latlng" + i;

                    JSONObject lat_lng = array2.getJSONObject(name);

                    Data.latlng_response_mini.add(name);
                    Data.id_response_mini.add(lat_lng.get("id").toString());
                    Data.lat_response_mini.add(lat_lng.get("lat").toString());
                    Data.lng_response_mini.add(lat_lng.get("lng").toString());

                }


                response = array.length() + "";


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }



            Log.d("Yusuf -- bitis", "Baglanti kesiliyor...");


            try {
                out.close();
                in.close();
                //stdIn.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            time_tv.setText(time_s +" sn");
            rate_tv.setText("% "+rate_s);
            Log.d("Yusuf - latlng_rsp_mini", ""+Data.latlng_response_mini.size());
            miniDataResponse();


            super.onPostExecute(result);
        }
    }


    public void clear_map() {
        map.clear();
        stylePolylineOriginal();

    }

}


