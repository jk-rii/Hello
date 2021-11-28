package com.example.dangdang;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Weather extends AppCompatActivity {

    TextView textView, poptxt;

    Document doc = null;
    LinearLayout layout = null;

    WeatherBackground task;
    WeatherBackground1 gu_task;
    WeatherBackground2 gun_task;

    JSONArray jsonArray = null;

    JSONObject city;
    JSONObject gu;
    JSONObject dong;

    ArrayList<String> arrayList, arrayList2, arrayList3;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2, arrayAdapter3;
    private WeatherAdapter adapter;
    private ListView listView;

    final String[] region = new String[1];

    Button prevBtnWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // 이전 버튼
        prevBtnWeather = findViewById(R.id.prevBtnWeather);
        prevBtnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weatherIntent = new Intent(Weather.this, SubActivity.class);
                startActivity(weatherIntent);
            }
        });

        poptxt = (TextView) findViewById(R.id.Poptxt);
        listView = (ListView) findViewById(R.id.weatherList);

        final Spinner city1 = (Spinner) findViewById(R.id.city1);
        final Spinner city2 = (Spinner) findViewById(R.id.city2);
        final Spinner city3 = (Spinner) findViewById(R.id.city3);

        final String url = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";

        city = new JSONObject();
        gu = new JSONObject();
        dong = new JSONObject();

        task = new WeatherBackground();

        try {
            jsonArray = task.execute(url).get();
            arrayList = new ArrayList<>();

            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrayList.add(jsonObject.getString("value"));

                city.put(jsonObject.getString("value"), jsonObject.getString("code"));
            }

            arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
            city1.setAdapter(arrayAdapter);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //city
        final int[] now1 = new int[1];
        city1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            JSONArray gu_jsonArray;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String s = parent.getItemAtPosition(position).toString();
                now1[0] = position;
                gu_task = new WeatherBackground1();

                try{
                    gu_jsonArray = gu_task.execute("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+city.getString(s)+".json.txt").get();
                    //System.out.println("시 코드 : " + city.getString(s));
                    arrayList2 = new ArrayList<>();

                    for(int i = 0; i < gu_jsonArray.length(); i++){
                        JSONObject jsonObject = gu_jsonArray.getJSONObject(i);
                        arrayList2.add(jsonObject.getString("value"));
                        gu.put(jsonObject.getString("value"), jsonObject.getString("code"));
                    }

                    arrayAdapter2 = new  ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList2);
                    city2.setAdapter(arrayAdapter2);

                }catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // gu
        final int[] now2 = new int[1];
        city2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            JSONArray gun_jsonArray = new JSONArray();

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                now2[0] = position;
                gun_task = new WeatherBackground2();

                try{
                    gun_jsonArray = gun_task.execute("http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."+gu.getString(s)+".json.txt").get();
                    //System.out.println("구 코드 : " + gu.getString(s));
                    arrayList3 = new ArrayList<>();

                    for(int i = 0; i < gun_jsonArray.length(); i++){
                        JSONObject jsonObject = gun_jsonArray.getJSONObject(i);
                        arrayList3.add(jsonObject.getString("value"));
                        dong.put(jsonObject.getString("value"), jsonObject.getString("code"));
                    }
                    arrayAdapter3 = new  ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList3);
                    city3.setAdapter(arrayAdapter3);

                }catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //dong
        final int[] now3 = new int[1];
        city3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getSelectedItem().toString();
                now3[0] = position;

                try {
                    region[0] = dong.getString(s);
                    //System.out.println("*********!in!RegionCode********* : "+region[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button weatherBtn = findViewById(R.id.weatherBtn);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("*********!out!RegionCode********* : "+region[0]);
                GetXMLTask task = new GetXMLTask();
                task.execute("https://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+region[0]);
                System.out.println("Today Weather : https://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+region[0]);

            }
        });

        //리스트뷰

        adapter = new WeatherAdapter();
        listView = (ListView) findViewById(R.id.weatherList);



    }

    //private inner class extending AsyncTask
    private class GetXMLTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {

            String dd;
            String d = " ", h = " ", t = " ", w = " ", p = " ";

            Drawable ww = getResources().getDrawable(R.drawable.clean);
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            for(int i = 0; i< nodeList.getLength(); i++){

                //날씨 데이터를 추출
                d += "" + "";
                h += "" + "";
                t += "" + "";
                w += "" + "";
                p += "" + "";

                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;

                NodeList dayList = fstElmnt.getElementsByTagName("day"); // 일
                NodeList timeList = fstElmnt.getElementsByTagName("hour"); //시간
                NodeList tempList = fstElmnt.getElementsByTagName("temp"); // 온도
                NodeList websiteList = fstElmnt.getElementsByTagName("wfKor"); //날씨
                NodeList rainList = fstElmnt.getElementsByTagName("pop"); // 비올 확률
                dd = dayList.item(0).getChildNodes().item(0).getNodeValue();

                if(dd.equals("0")){
                    d = "오늘"+" ";
                } else if (dd.equals("1")) {
                    d = "내일"+" ";
                }else if (dd.equals("2")){
                    d = "모레"+" ";
                }

                h = timeList.item(0).getChildNodes().item(0).getNodeValue()+"시";
                t = tempList.item(0).getChildNodes().item(0).getNodeValue() +"℃ ";
                w = websiteList.item(0).getChildNodes().item(0).getNodeValue();

                if (w.equals("맑음")){
                    ww = getResources().getDrawable(R.drawable.clean);
                }else if (w.equals("구름 많음")){
                    ww = getResources().getDrawable(R.drawable.most_cloudy);
                    if (h.equals("18시") || h.equals("21시") || h.equals("24시") || h.equals("3시")){
                        ww = getResources().getDrawable(R.drawable.most_night);
                    }
                }else if (w.equals("흐림")){
                    ww = getResources().getDrawable(R.drawable.cloudy);
                    if (h.equals("18시") || h.equals("21시") || h.equals("24시") || h.equals("3시")){
                        ww = getResources().getDrawable(R.drawable.cloudy_night);
                    }
                }else if (w.equals("비")){
                    ww = getResources().getDrawable(R.drawable.rain);

                }else if (w.equals("비/눈")){
                    ww = getResources().getDrawable(R.drawable.rain_snow);

                }else if (w.equals("눈")){
                    ww = getResources().getDrawable(R.drawable.snow);

                }else if (w.equals("소나기")){
                    ww = getResources().getDrawable(R.drawable.shower);
                }

                p = rainList.item(0).getChildNodes().item(0).getNodeValue()+"%";

                WeatherItem item = new WeatherItem();
                item.setDay(d);
                item.setHour(h);
                item.setTemp(t);
                item.setWfkor(ww);
                item.setPop(p);
                adapter.addItem(item);

            }

            listView.setAdapter(adapter);
            super.onPostExecute(doc);
        }

    }//end inner class - GetXMLTask


}