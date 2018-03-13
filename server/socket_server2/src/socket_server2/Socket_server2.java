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



    public static QuadNode kok=null;
    public static QuadNode indirgemeKok=null;
    public static QuadNode aramaKok=null;
    public static QuadNode aramaIndirgemeKok=null;
    public static int aramaId=0;
    public static ArrayList<Double> koordinatX = new ArrayList<Double>();
    public static ArrayList<Double> koordinaty = new ArrayList<Double>();
    public static ArrayList<Integer> koordinatid = new ArrayList<Integer>();
    public static ArrayList<Double> indirgemekoordinatX = new ArrayList<Double>();
    public static ArrayList<Double> indirgemekoordinaty = new ArrayList<Double>();
    public static ArrayList<Integer> indirgemekoordinatid = new ArrayList<Integer>();

    public static int yeniAgacId=0,yeniIndirgenmisAgacId=0;

    public static ArrayList<Double> gelenkoordinatX = new ArrayList<Double>();
    public static ArrayList<Double> gelenkoordinaty = new ArrayList<Double>();
    public static ArrayList<Integer> gelenkoordinatid = new ArrayList<Integer>();

    public static double aramaMerkezX;
    public static double aramaMerkezY;

    static String clientGelen;
    static ServerSocket serverSocket = null;
    static Socket clientSocket = null;
    static int dstPort = 9008;
    static Boolean serverStatus = true;

    static ArrayList<String> latlng = new ArrayList<String>();
    static ArrayList<Double> lat = new ArrayList<Double>();
    static ArrayList<Double> lng = new ArrayList<Double>();
    static ArrayList<String> id = new ArrayList<String>();


    static ArrayList<String> latlng2 = new ArrayList<String>();//indirgenmis
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




    public static void ekle(QuadNode dugum, double x, double y,int id) {

        int index = 0;

        if (dugum == null) {
            dugum = new QuadNode(x, y,id);
            //sayac++;
        } else {
            if (x >= dugum.x && y <= dugum.y) {
                if (dugum.sagUst != null) {

                    ekle(dugum.sagUst, x, y,id);
                } else {
                   //  System.out.println("sagust");
                    dugum.sagUst = new QuadNode(x, y,id);
                    //sayac++;
                }
            } else if (x >= dugum.x && y >= dugum.y) {
                if (dugum.sagAlt != null) {

                    ekle(dugum.sagAlt, x, y,id);
                } else {
                    index = (int) (Math.random() * 13);
                    dugum.sagAlt = new QuadNode(x, y,id);
                     // System.out.println("sagalt");
                    // sayac++;
                }
            } else if (x <= dugum.x && y >= dugum.y) {
                if (dugum.solAlt != null) {
                    ekle(dugum.solAlt, x, y,id);
                } else {
                    index = (int) (Math.random() * 13);
                    dugum.solAlt = new QuadNode(x, y,id);
                     //  System.out.println("solalt");
                    //sayac++;
                }
            } else if (x <= dugum.x && y <= dugum.y) {
                if (dugum.solUst != null) {
                    ekle(dugum.solUst, x, y,id);
                } else {
                    index = (int) (Math.random() * 13);
                    dugum.solUst = new QuadNode(x, y,id);
                     //  System.out.println("solust");
                    // sayac++;
                }
            }

        }

    }





/*
    public static void dolas(QuadNode agac){
        if(agac==null)
            return;
        dolas(agac.sagAlt);
        //  System.out.println("veri->"+agac.x+agac.y);
        dolas(agac.sagUst);
        // System.out.println("veri->"+agac.x+agac.y);
        dolas(agac.solAlt);
        // System.out.println("veri->"+agac.x+" "+agac.y+" "+agac.id);
    //    koordinatX.add(agac.x);
      //  koordinaty.add(agac.y);
     //   koordinatid.add(agac.id);

        Double dist = CalculationByDistance(lat_center, lng_center, agac.x, agac.y);
        if(dist<=round){
            koordinatX.add(agac.x);
            koordinaty.add(agac.y);
            koordinatid.add(yeniAgacId);
            yeniAgacId++;
        }

        dolas(agac.solUst);
    }
*/

/*
    public static void Indirgemedolas(QuadNode agac){
        if(agac==null)
            return;
        Indirgemedolas(agac.sagAlt);
        //  System.out.println("veri->"+agac.x+agac.y);
        Indirgemedolas(agac.sagUst);
        // System.out.println("veri->"+agac.x+agac.y);
        Indirgemedolas(agac.solAlt);
        // System.out.println("veri->"+agac.x+" "+agac.y+" "+agac.id);

        Double dist = CalculationByDistance(lat_center, lng_center, agac.x, agac.y);
        if(dist<=round){
            indirgemekoordinatX.add(agac.x);
            indirgemekoordinaty.add(agac.y);
            indirgemekoordinatid.add(yeniIndirgenmisAgacId);
            yeniIndirgenmisAgacId++;
        }


        //System.out.println("DOLASIYORUM");
        Indirgemedolas(agac.solUst);
    }



*/



    public static void dolas(QuadNode agac, ArrayList<Double> x, ArrayList<Double> y, ArrayList<Integer> id,int agacId){

        int myId;


        if(agac==null)
            return;
        dolas(agac.sagAlt,x,y,id,agacId);

        dolas(agac.sagUst,x,y,id,agacId);

        dolas(agac.solAlt,x,y,id,agacId);

        Double dist = CalculationByDistance(lat_center, lng_center, agac.x, agac.y);
        if(dist<=round){
            x.add(agac.x);
            y.add(agac.y);

            if (agacId==0){
                myId=yeniAgacId;
                yeniAgacId++;
                System.out.println("-");
            }else {
                myId = yeniIndirgenmisAgacId;
                yeniIndirgenmisAgacId++;
                System.out.println("+");
            }

            id.add(myId);


        }

        dolas(agac.solUst,x,y,id,agacId);
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


        kok=null;
        indirgemeKok=null;
        aramaKok=null;

        aramaIndirgemeKok=null;
        aramaKok=null;
        aramaId=0;
        koordinatX.clear();
        koordinaty.clear();
        koordinatid.clear();
        indirgemekoordinatX.clear();
        indirgemekoordinaty.clear();
        indirgemekoordinatid.clear();
        gelenkoordinatX.clear();
        gelenkoordinaty.clear();
        gelenkoordinatid.clear();



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

            aramaMerkezX=lat_center;
            aramaMerkezY=lng_center;


            System.out.println("round= "+round+ "  lat_center= " + lat_center+ "  lng_center= " + lng_center);

            System.out.println("array_size--> " + array.length());
            System.out.println("array_size2--> " + array2.length());


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



            for (int i=0;i<id.size();i++){
                if(kok==null){
                    kok=new QuadNode(lat.get(i),lng.get(i),Integer.parseInt(id.get(i)));
                }else{
                    ekle(kok,lat.get(i),lng.get(i),Integer.parseInt(id.get(i)));
                }
            }


            System.out.println("ID' SİZE ************"+id2.size());

            for (int i=0;i<id2.size();i++){
                if(indirgemeKok==null){
                    indirgemeKok=new QuadNode(lat2.get(i),lng2.get(i),Integer.parseInt(id2.get(i)));
                  //  System.out.println("EKLENDİ KOK");
                   // System.out.println(lat2.get(i)+" "+lng2.get(i)+" "+id2.get(i));
                }else{
                    ekle(indirgemeKok,lat2.get(i),lng2.get(i),Integer.parseInt(id2.get(i)));
                  //  System.out.println(lat2.get(i)+" "+lng2.get(i)+" "+id2.get(i));
                   // System.out.println("EKLENDİ DAL");
                }
            }

            long startTime = System.currentTimeMillis();
            //araDolas(kok,aramaKok,round);
            dolas(kok,koordinatX,koordinaty,koordinatid,0);

         //   System.out.println("arama sonrasi agac Size: "+koordinatid.get(10));


          //  araDolas(indirgemeKok,aramaIndirgemeKok,round);
           // System.out.println(indirgemeKok.sagAlt.sagUst.solUst.x+"sadsdasadsadsadsad");
            dolas(indirgemeKok,indirgemekoordinatX,indirgemekoordinaty,indirgemekoordinatid,1);

     //       System.out.println("arama sonrasi İndirgenmis agac Size: "+indirgemekoordinatid.get(3));





            JSONObject root = new JSONObject();
            JSONObject time_json = new JSONObject();
            JSONObject rate_json = new JSONObject();
            JSONObject root2 = new JSONObject();
            JSONObject root3 = new JSONObject();

            if(koordinatid.size()!=0) {
                for (int i = 0; i < koordinatid.size(); i++) {
                    JSONObject latlng_response = new JSONObject();
                    latlng_response.put("id", koordinatid.get(i));
                    latlng_response.put("lat", koordinatX.get(i));
                    latlng_response.put("lng", koordinaty.get(i));

                    root.put("latlng" + i, latlng_response);

                }

            }

            if(indirgemekoordinatid.size()!=0) {

                for (int i = 0; i < indirgemekoordinatid.size(); i++) {
                    JSONObject latlng_response = new JSONObject();
                    latlng_response.put("id", indirgemekoordinatid.get(i));
                    latlng_response.put("lat", indirgemekoordinatX.get(i));
                    latlng_response.put("lng", indirgemekoordinaty.get(i));

                    root2.put("latlng" + i, latlng_response);

                }


            }


            long endTime = System.currentTimeMillis();
            long estimatedTime = endTime - startTime; // Geçen süreyi milisaniye cinsinden elde ediyoruz
            double seconds = (double)estimatedTime/1000; // saniyeye çevirmek için 1000'e bölüyoruz.



            time_json.put("timee", seconds);
            rate_json.put("ratee", "---");

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
