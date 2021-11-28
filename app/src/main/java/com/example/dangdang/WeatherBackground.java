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

public class WeatherBackground extends AsyncTask<String, Void, JSONArray> {
    private JSONArray b_jsonArray;
    String inputString = "";
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... strUrl){

        try{
            URL url = new URL(strUrl[0]);
            //System.out.println("url : "+url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //System.out.println("conn : "+conn);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            //System.out.println("conn In: "+conn.getInputStream());
            //System.out.println("url : "+url);
            //System.out.println("in : "+in);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            //System.out.println("bufferedReader : "+bufferedReader);
            StringBuffer builder = new StringBuffer();
            //System.out.println("builder : "+builder);


            if((inputString = bufferedReader.readLine()) != null){
                builder.append(inputString);
                System.out.println("--------------------------");
            }

            String s;
            s = builder.toString();
            b_jsonArray = new JSONArray(s);

            conn.disconnect();
            bufferedReader.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return b_jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray result){
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled(){
        super.onCancelled();
    }

}