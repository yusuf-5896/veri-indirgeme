import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class LineSimplification {


    public static QuadNode kok=null;
    public static ArrayList<Double> koordinatX = new ArrayList<Double>();
    public static ArrayList<Double> koordinaty = new ArrayList<Double>();
    public static ArrayList<Integer> koordinatid = new ArrayList<Integer>();
    public static ArrayList<Double> gelenkoordinatX = new ArrayList<Double>();
    public static ArrayList<Double> gelenkoordinaty = new ArrayList<Double>();
    public static ArrayList<Integer> gelenkoordinatid = new ArrayList<Integer>();


    static String clientGelen;
    static ServerSocket serverSocket = null;
    static Socket clientSocket = null;
    static int dstPort = 9001;
    static Boolean serverStatus=true;

    static ArrayList<String> latlng = new ArrayList<String>();
    static ArrayList<String> lat = new ArrayList<String>();
    static ArrayList<String> lng = new ArrayList<String>();
    static ArrayList<String> id = new ArrayList<String>();



    private static class Point extends Pair<Double, Double> {
        Point(Double key, Double value) {
            super(key, value);
        }

        @Override
        public String toString() {
            return String.format("(%f, %f)", getKey(), getValue());
        }
    }

    private static double perpendicularDistance(Point pt, Point lineStart, Point lineEnd) {
        double dx = lineEnd.getKey() - lineStart.getKey();
        double dy = lineEnd.getValue() - lineStart.getValue();

        // Normalize
        double mag = Math.hypot(dx, dy);
        if (mag > 0.0) {
            dx /= mag;
            dy /= mag;
        }
        double pvx = pt.getKey() - lineStart.getKey();
        double pvy = pt.getValue() - lineStart.getValue();

        // Get dot product (project pv onto normalized direction)
        double pvdot = dx * pvx + dy * pvy;

        // Scale line direction vector and subtract it from pv
        double ax = pvx - pvdot * dx;
        double ay = pvy - pvdot * dy;

        return Math.hypot(ax, ay);
    }

    private static void ramerDouglasPeucker(List<Point> pointList, double epsilon, List<Point> out) {
        if (pointList.size() < 2) throw new IllegalArgumentException("Not enough points to simplify");

        // Find the point with the maximum distance from line between the start and end
        double dmax = 0.0;
        int index = 0;
        int end = pointList.size() - 1;
        for (int i = 1; i < end; ++i) {
            double d = perpendicularDistance(pointList.get(i), pointList.get(0), pointList.get(end));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        // If max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            List<Point> recResults1 = new ArrayList<>();
            List<Point> recResults2 = new ArrayList<>();
            List<Point> firstLine = pointList.subList(0, index + 1);
            List<Point> lastLine = pointList.subList(index, pointList.size());
            ramerDouglasPeucker(firstLine, epsilon, recResults1);
            ramerDouglasPeucker(lastLine, epsilon, recResults2);

            // build the result list
            out.addAll(recResults1.subList(0, recResults1.size() - 1));
            out.addAll(recResults2);
            if (out.size() < 2) throw new RuntimeException("Problem assembling output");
        } else {
            // Just return start and end points
            out.clear();
            out.add(pointList.get(0));
            out.add(pointList.get(pointList.size() - 1));
        }
    }

    public static void ekle(QuadNode dugum, double x, double y,int id) {

        int index = 0;

        if (dugum == null) {
            kok = new QuadNode(x, y,id);
            //sayac++;
        } else {
            if (x >= dugum.x && y <= dugum.y) {
                if (dugum.sagUst != null) {

                    ekle(dugum.sagUst, x, y,id);
                } else {
                    System.out.println("sagust");
                    dugum.sagUst = new QuadNode(x, y,id);
                    //sayac++;
                }
            } else if (x >= dugum.x && y >= dugum.y) {
                if (dugum.sagAlt != null) {

                    ekle(dugum.sagAlt, x, y,id);
                } else {
                    index = (int) (Math.random() * 13);
                    dugum.sagAlt = new QuadNode(x, y,id);
                    System.out.println("sagalt");
                   // sayac++;
                }
            } else if (x <= dugum.x && y >= dugum.y) {
                if (dugum.solAlt != null) {
                    ekle(dugum.solAlt, x, y,id);
                } else {
                    index = (int) (Math.random() * 13);
                    dugum.solAlt = new QuadNode(x, y,id);
                    System.out.println("solalt");
                    //sayac++;
                }
            } else if (x <= dugum.x && y <= dugum.y) {
                if (dugum.solUst != null) {
                    ekle(dugum.solUst, x, y,id);
                } else {
                    index = (int) (Math.random() * 13);
                    dugum.solUst = new QuadNode(x, y,id);
                    System.out.println("solust");
                   // sayac++;
                }
            }

        }

    }


    public static QuadNode arama(QuadNode kok1, int x, int y) {
        String txt = "";

        if (kok1 == null) {
            return null;
        }
        if (kok1.x == x && kok1.y == y) {
            return kok1;
        }
        if (arama(kok1.sagAlt, x, y) != null) {

            return kok1;
        }
        if (arama(kok1.sagUst, x, y) != null) {

            return kok1;
        }
        if (arama(kok1.solAlt, x, y) != null) {

            return kok1;
        }
        if (arama(kok1.solUst, x, y) != null) {

            return kok1;
        }
        return null;
    }


    public static void dolas(QuadNode agac){
        if(agac==null)
            return;
        dolas(agac.sagAlt);
      //  System.out.println("veri->"+agac.x+agac.y);
        dolas(agac.sagUst);
       // System.out.println("veri->"+agac.x+agac.y);
        dolas(agac.solAlt);
        System.out.println("veri->"+agac.x+" "+agac.y+" "+agac.id);
        koordinatX.add(agac.x);
        koordinaty.add(agac.y);
        koordinatid.add(agac.id);
        dolas(agac.solUst);
    }



    static  void socket() throws IOException, JSONException{
        latlng.clear();
        lng.clear();
        lat.clear();
        id.clear();



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

            System.out.println("array_size--> "+array.length());

            //String pageName = obj.getJSONObject("pageInfo").getString("pageName");
            //JSONArray the_json_array = array.names();



            for(int i=0;i<array.length();i++){

                String name = "latlng" + i;

                JSONObject lat_lng = array.getJSONObject(name);

                latlng.add(name);
                id.add(lat_lng.get("id").toString());
                lat.add(lat_lng.get("lat").toString());
                lng.add(lat_lng.get("lng").toString());


            }

            //Islemler burada yapılacak


            List<Point> pointList = new ArrayList<>(koordinatid.size());


            System.out.println(koordinatid.size());

            for (int i = 0; i < id.size(); ++i) { //iterate over the elements of the list
                double theXValue = Double.parseDouble(lat.get(i));
                gelenkoordinatX.add(i,theXValue);
                double theYValue = Double.parseDouble(lng.get(i));
                gelenkoordinaty.add(i,theYValue);

                int gelenId= Integer.parseInt(id.get(i));
                gelenkoordinatid.add(i,gelenId);
            }

            System.out.println(gelenkoordinatid.size());
            /*
            System.out.println(koordinatid);
            System.out.println(koordinatX);
            System.out.println(koordinaty);

            System.out.println("\n\n\nasdkldsaljksdajkl\n\n\n");

            System.out.println(id);
            System.out.println(lat);
            System.out.println(lng);
*/
            for (int i=0;i<id.size();i++){
                if(kok==null){
                    kok=new QuadNode(gelenkoordinatX.get(i),gelenkoordinaty.get(i),gelenkoordinatid.get(i));
                }else{
                    ekle(kok,gelenkoordinatX.get(i),gelenkoordinaty.get(i),gelenkoordinatid.get(i));
                }
            }


    /*
            kok=new QuadNode(-35.016,143.321,0);
            ekle(kok, -34.747, 145.592,1);
            ekle(kok, -34.364, 147.891,2);
            ekle(kok, -33.501, 150.207,3);
            ekle(kok, -32.306, 149.208,4);
            ekle(kok, -32.491, 147.309,5);
*/



           // dolas(kok);
           // dolas(kok);
            dolas(kok);
           // System.out.println("X: "+koordinatX.get(0)+"Y: "+koordinaty.get(0)+"id "+koordinatid.get(0));
            System.out.println(gelenkoordinatid.size());
            System.out.println(koordinatid.size());

            for (int i=0;i<koordinatid.size();i++){
                System.out.println(i+". yer acildi");
                pointList.add(i,null);
            }


            for(int i=0;i<koordinatid.size();i++){
                System.out.println("X: "+koordinatX.get(i)+" Y: "+koordinaty.get(i)+" İD: "+koordinatid.get(i));
                double x=koordinatX.get(i);
                double y=koordinaty.get(i);
                int id=koordinatid.get(i);
                pointList.set(id,new Point(x,y));
            }

            System.out.println(pointList);


            System.out.println(pointList);


            List<Point> pointListOut = new ArrayList<>();
            ramerDouglasPeucker(pointList, 0.0001, pointListOut);
            System.out.println("Points remaining after simplification:");
            System.out.println(pointListOut.get(0));
            pointListOut.forEach(System.out::println);

            System.out.println("Son hali: "+ pointListOut.size());
            System.out.println("İlk hali: "+ pointList.size());


            System.out.println(pointListOut.get(0).getValue());
            System.out.println(pointListOut.get(0).getKey());


            JSONObject root = new JSONObject();
            JSONObject root2 = new JSONObject();

            for (int i = 0; i < pointListOut.size(); i++) {


                JSONObject latlng_response = new JSONObject();
                latlng_response.put("id", i);
                latlng_response.put("lat", pointListOut.get(i).getKey());
                latlng_response.put("lng", pointListOut.get(i).getValue());
                root.put("latlng"+i, latlng_response);//i yerine id gelecek
                System.out.println("id:  "+i+"X: "+pointListOut.get(i).getKey()+"Y: "+pointListOut.get(i).getValue());
                //root.put(latlng.get(i), latlng_response);
            }

            root2.put("array", root);
            //root2.put("time", firstTime-Lasttime);
            //root2.put("indirgemeOrani", firstTime-Lasttime);

            System.out.println(root2.toString());
            System.out.println(root.length());

            // System.out.println("----"+the_json_array.get(0));

            //sayi = Integer.valueOf(clientGelen);
            //out.println(sayi * sayi);
            out.println(root2.toString());



        }
        out.close();
        in.close();
        clientSocket.close();



    }

    public static void main(String[] args) throws IOException, JSONException {
        try {
            //*Server 7755 portundan Client'ı dinliyor *//
            serverSocket = new ServerSocket(dstPort);
        } catch (Exception e) {
            System.out.println("Port Hatası!");
        }


        while(serverStatus){
            socket();

        }


        serverSocket.close();

    }
}









































/*
if (aramaX < 512 && aramaY < 512) {
                for (int i = 0; i < 512; i++) {
                    for (int j = 0; j < 512; j++) {
                        if (Math.sqrt(Math.pow((aramaX - i), 2) + Math.pow(aramaY - j, 2)) <= aramaCapi / 2) {
                            QuadNode kontrol=arama(kok,i,j);

                            if (kontrol != null) {
                                System.out.println(kontrol.kose1x+" "+kontrol.kose1y);
                                ekleBulunan(bulunanKok, i, j, 2);
                                cemberEkle(i, j, 10, Color.WHITE, 1);
                                cizgiEkle(kontrol.sagUst.kose1x, kontrol.sagUst.y, kontrol.sagUst.kose2x, kontrol.sagUst.y, kontrol.sagUst.color, 1);
                                cizgiEkle(kontrol.sagUst.x, kontrol.sagUst.kose1y, kontrol.sagUst.x, kontrol.sagUst.kose2y, kontrol.sagUst.color, 1);
                                bulunanLabelTexti += "X: " + i + "Y: " + j + "<br>";
                                jLabel1.setText(bulunanLabelTexti);
                                bulduysam = true;
                            }
                        }
                    }
                }
            }
 */