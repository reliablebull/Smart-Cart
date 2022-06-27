package com.example.hwang.smartcart.classModels.httpThread;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lee ho jun on 2017-01-06.
 */


public class HttpRequestThreadClass extends Thread {

    int responseCode;
    String searchUrl;

    public HttpRequestThreadClass(String searchUrl)
    {
        if(!searchUrl.contains("http://"))
            searchUrl = "http://"+ searchUrl;

        this.searchUrl = searchUrl;
    }
    @Override
    public void run() {
        super.run();
        try {
            URL url = new URL(searchUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            responseCode = conn.getResponseCode();
            conn.disconnect();

        } catch (Exception e){
            Log.e("Error",e.toString());
            responseCode = -1;
        }
    }
    public int getResponseCode() {
        return responseCode;
    }
}