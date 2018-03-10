package com.example.yusufsatilmis.gezinge;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Data {
    static  ArrayList<String> latlng_original = new ArrayList<>();
    static  ArrayList<String> lat_original = new ArrayList<>();
    static  ArrayList<String> lng_original= new ArrayList<>();


    static ArrayList<String> latlng_response = new ArrayList<>();
    static ArrayList<String> lat_response = new ArrayList<>();
    static ArrayList<String> lng_response = new ArrayList<>();
    static ArrayList<String> id_response = new ArrayList<>();

    static ArrayList<String> latlng_response_mini = new ArrayList<>();
    static ArrayList<String> lat_response_mini = new ArrayList<>();
    static ArrayList<String> lng_response_mini = new ArrayList<>();
    static ArrayList<String> id_response_mini = new ArrayList<>();

    static ArrayList<String> latlng_original_mini = new ArrayList<>();
    static ArrayList<String> lat_original_mini = new ArrayList<>();
    static ArrayList<String> lng_original_mini = new ArrayList<>();
    static ArrayList<String> id_original_mini = new ArrayList<>();

    static String ip_adress;
    static String port1_adress;
    static String port2_adress;

    static LatLng center;
    static Float zoom = 14.0f;



    static void originalDataClear(){
        latlng_original.clear();
        lat_original.clear();
        lng_original.clear();

    }

    static void responseDataClear(){
        latlng_response.clear();
        lat_response.clear();
        lng_response.clear();
        id_response.clear();

    }

    static void responseMiniDataClear(){
        latlng_response_mini.clear();
        lat_response_mini.clear();
        lng_response_mini.clear();
        id_response_mini.clear();

    }

    static void responseOriginalDataClear(){
        latlng_original_mini.clear();
        lat_original_mini.clear();
        lng_original_mini.clear();
        id_original_mini.clear();
    }


    static void connectionAdressSet(String ip,String port1,String port2){
         ip_adress = ip;
         port1_adress=port1;
       port2_adress=port2;

    }


}
