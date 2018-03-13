package com.example.yusufsatilmis.gezinge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class FirstActivity extends AppCompatActivity {


    EditText ipText ;
    EditText Port1Text ;
    EditText Port2Text ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);



         ipText = findViewById(R.id.ip_editText);
         Port1Text = findViewById(R.id.port1_editText);
         Port2Text = findViewById(R.id.port2_editText);


         ipText.setText("192.168.0.112");
        Port1Text.setText("9001");
        Port2Text.setText("9008");


        Toast.makeText(getApplicationContext(),"Bağlantı bilgilarini girgikten sonra açacağınız dosyayı seçin", Toast.LENGTH_LONG).show();


        final File directory = new File(Environment.getExternalStorageDirectory().getPath()+"/Gezinge");//new File(path);
        final File[] files = directory.listFiles();

        Log.d("Yusuf", "Path: " + directory.getPath());
        Log.d("Yusuf", "Klasördeki dosya sayısı"+ files.length);


        final String[] files_s = new String[files.length];

        for (int i = 0; i < files.length; i++)
        {
            files_s[i] = i+1+"-->  "+files[i].getName();
            //Log.d("Yusuf", "FileName:" + files[i].getName());
        }


        ListView listView = findViewById(R.id.files_listview);

        ArrayAdapter<String> adapter = new  ArrayAdapter<>(this,android.R.layout.simple_list_item_1, android.R.id.text1, files_s);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (ipText.getText().length() == 0 || Port1Text.getText().length() == 0||Port2Text.getText().length() ==0 )
                {

                    Toast.makeText(getApplicationContext(),"Bağlantı bilgilerini kontrol ediniz", Toast.LENGTH_SHORT).show();
                    return;
                }


                String ip_tmp = ipText.getText().toString();
                int counter_ip = 0;
                for( int i=0; i<ip_tmp.length(); i++ ) {
                    if( ip_tmp.charAt(i) == '.' ) {
                        counter_ip++;
                    }
                }

                if (counter_ip!=3 )
                {

                    Toast.makeText(getApplicationContext(),"IP bilgilerini kontrol ediniz", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Toast.makeText(getApplicationContext(),files_s[position], Toast.LENGTH_LONG).show();
              txt_read(files[position] );

                Data.connectionAdressSet(ipText.getText().toString(),Port1Text.getText().toString(),Port2Text.getText().toString());

                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });







    }





    private void txt_read(File file){
        Data.originalDataClear();

        Log.e("Yusuf",file.toString());

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int index = 0;
            while ((line = br.readLine()) != null) {

                if (index>5){

                    String[] split = line.split(",");

                    Data.latlng_original.add("latlng"+(index-6) );
                    Data.lat_original.add(split[0]);
                    Data.lng_original.add(split[1]);

                }

                index++;
            }



            Log.d("Yusuf","latlong_ori_ilk---> "+Data.latlng_original.get(0));
            Log.d("Yusuf","lat_ori_ilk---> "+Data.lat_original.get(0));
            Log.d("Yusuf","lng_ori_ilk---> "+Data.lng_original.get(0));

            Log.d("Yusuf","latlong_ori---> "+Data.latlng_original.size());
            Log.d("Yusuf","lat_ori---> "+Data.lat_original.size());
            Log.d("Yusuf","lng_ori---> "+Data.lng_original.size());
            br.close();

            Data.center = new LatLng(Double.parseDouble(Data.lat_original.get(0)),Double.parseDouble(Data.lng_original.get(0)));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
