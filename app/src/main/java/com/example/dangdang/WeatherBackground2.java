package com.example.dangdang;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherBackground2 extends AsyncTask<String, Void, JSONArray> {
    private JSONArray gun_jsonArray;
    String inputString = "";
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... strUrl){


        try{
            URL url = new URL(strUrl[0]);
            //System.out.println("url222 : "+url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //System.out.println("conn222 : "+conn);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            //System.out.println("conn In222: "+conn.getInputStream());
            //System.out.println("url222 : "+url);
            //System.out.println("in222 : "+in);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            //System.out.println("bufferedReader222 : "+bufferedReader);
            StringBuffer builder = new StringBuffer();
            //System.out.println("builder222 : "+builder);


            if((inputString = bufferedReader.readLine()) != null){
                builder.append(inputString);
            }

            String s;
            s = builder.toString();
            gun_jsonArray = new JSONArray(s);

            conn.disconnect();
            bufferedReader.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gun_jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray result){ super.onPostExecute(result); }

    @Override
    protected void onCancelled(){
        super.onCancelled();
    }

}