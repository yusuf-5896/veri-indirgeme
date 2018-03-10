/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket_server2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.tools.JavaFileManager.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author yusufsatilmis
 */
public class Socket_server2 {

    static String clientGelen;
    static ServerSocket serverSocket = null;
    static Socket clientSocket = null;
    static int dstPort = 9008;
    static Boolean serverStatus = true;

    static ArrayList<String> latlng = new ArrayList<String>();
    static ArrayList<Double> lat = new ArrayList<Double>();
    static ArrayList<Double> lng = new ArrayList<Double>();
    static ArrayList<String> id = new ArrayList<String>();
    
    
    static ArrayList<String> latlng2 = new ArrayList<String>();
    static ArrayList<Double> lat2 = new ArrayList<Double>();
    static ArrayList<Double> lng2 = new ArrayList<Double>();
    static ArrayList<String> id2 = new ArrayList<String>();
    
    static double round=0;
    static double lat_center=0;
    static  double lng_center=0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, JSONException {

        try {

            serverSocket = new ServerSocket(dstPort);
        } catch (Exception e) {
            System.out.println("Port Hatası!");
        }

        while (serverStatus) {
            socket();

        }

        serverSocket.close();

    }

    static void socket() throws IOException, JSONException {
        latlng.clear();
        lng.clear();
        lat.clear();
        id.clear();
        
        latlng2.clear();
        lng2.clear();
        lat2.clear();
        id2.clear();

        System.out.println("SERVER BAŞLANTI İÇİN HAZIR...");
        //* Bağlantı sağlamadan program bir alt satırdaki kod parçasına geçmez (accept) *//
        clientSocket = serverSocket.accept();

        //* Client'a veri gönderimi için kullandığımız PrintWriter nesnesi oluşturulur *//
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        //* Client'dan gelen verileri tutan BufferedReader nesnesi oluşturulur *//
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while ((clientGelen = in.readLine()) != null) {
            //System.out.println("Client'dan gelen veri = " + clientGelen);

            JSONObject jsonObj = new JSONObject(clientGelen);
            JSONObject array = jsonObj.getJSONObject("array");
            JSONObject array2= jsonObj.getJSONObject("array2");
            JSONObject roundd= jsonObj.getJSONObject("round");
            JSONObject latt= jsonObj.getJSONObject("lat");
            JSONObject lngg= jsonObj.getJSONObject("lng");
            
            round=roundd.getDouble("roundd")/1000;
            lat_center=latt.getDouble("latt");
            lng_center=lngg.getDouble("lngg");
            
            

            
            System.out.println("round= "+round+ "  lat_center= " + lat_center+ "  lng_center= " + lng_center);
            
            System.out.println("array_size--> " + array.length());
            System.out.println("array_size2--> " + array2.length());

            //String pageName = obj.getJSONObject("pageInfo").getString("pageName");
            //JSONArray the_json_array = array.names();
            
            for (int i = 0; i < array.length(); i++) {

                String name = "latlng" + i;

                JSONObject lat_lng = array.getJSONObject(name);

                latlng.add(name);
                id.add(lat_lng.get("id").toString());
                lat.add(lat_lng.getDouble("lat"));
                lng.add(lat_lng.getDouble("lng"));

            }
            
            
            for (int i = 0; i < array2.length(); i++) {

                String name = "latlng" + i;

                JSONObject lat_lng = array2.getJSONObject(name);

                latlng2.add(name);
                id2.add(lat_lng.get("id").toString());
                lat2.add(lat_lng.getDouble("lat"));
                lng2.add(lat_lng.getDouble("lng"));

            }
            
            
            
      
            
            
            
            

            JSONObject root = new JSONObject();
            JSONObject time_json = new JSONObject();
            JSONObject rate_json = new JSONObject();
            JSONObject root2 = new JSONObject();
            JSONObject root3 = new JSONObject();
            
            
            int count=0;

            for (int i = 0; i < latlng.size(); i++) {
                
                Double dist = CalculationByDistance(lat_center, lng_center, lat.get(i), lng.get(i));
                
                if (dist<=round)
                    {
                     //   System.out.println(latlng.get(i) + "-->" + dist);

                        JSONObject latlng_response = new JSONObject();
                        latlng_response.put("id", i);
                        latlng_response.put("lat", lat.get(i));
                        latlng_response.put("lng", lng.get(i));

                        root.put("latlng"+count, latlng_response);
                        
                        count++;
                    } 
                
            }
            
            
            
            
             count = 0;

            for (int i = 0; i < latlng2.size(); i++) {

                Double dist = CalculationByDistance(lat_center, lng_center, lat2.get(i), lng2.get(i));

                if (dist <= round) {
                  //  System.out.println(latlng2.get(i) + "-->" + dist);

                    JSONObject latlng_response = new JSONObject();
                    latlng_response.put("id", i);
                    latlng_response.put("lat", lat2.get(i));
                    latlng_response.put("lng", lng2.get(i));

                    root2.put("latlng" + count, latlng_response);

                    count++;
                }

            }
            
            

            time_json.put("timee", "25");
            rate_json.put("ratee", "10");

            root3.put("array", root);
            root3.put("array2", root2);
            root3.put("time", time_json);
            root3.put("rate", rate_json);

            System.out.println("----" + root.length());
            System.out.println("----" + root2.length());

            //sayi = Integer.valueOf(clientGelen);
            //out.println(sayi * sayi);
            System.out.println("out-->"+root3.toString());
            out.println(root3.toString());



        }
        out.close();
        in.close();
        clientSocket.close();

    }
    
    
    static public double CalculationByDistance(double initialLat, double initialLong, double finalLat, double finalLong) {
        
        int R = 6371; // km (Earth radius)
        double dLat = toRadians(finalLat - initialLat);
        double dLon = toRadians(finalLong - initialLong);
        initialLat = toRadians(initialLat);
        finalLat = toRadians(finalLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(initialLat) * Math.cos(finalLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    static public double toRadians(Double deg) {
        return deg * (Math.PI / 180);
    }
    
    
}
