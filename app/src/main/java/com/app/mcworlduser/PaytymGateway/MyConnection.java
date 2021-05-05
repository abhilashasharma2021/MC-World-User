package com.app.mcworlduser.PaytymGateway;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyConnection {
        static JSONObject jObj = null;
        HttpURLConnection urlConnection = null;
        private Context context;
        // constructor
        public MyConnection(Context context){
            this.context=context;
        }
        public JSONObject makeHttpRequest(String url, String method, String params) {



            try {

                URL url1 = new URL(url);
                // check for request method
                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                if (method == "POST") {
                    // request method is POST
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setFixedLengthStreamingMode(params.getBytes().length);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(params);
                    out.close();
                }
                InputStream in = urlConnection.getInputStream();
                byte[] bytes = new byte[10000];
                StringBuilder sb = new StringBuilder();
                int numRead = 0;
                while ((numRead = in.read(bytes)) >= 0) {
                    sb.append(new String(bytes, 0, numRead));
                }
                String response = sb.toString();
                jObj = new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Connectivity issue. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return jObj;
        }
}