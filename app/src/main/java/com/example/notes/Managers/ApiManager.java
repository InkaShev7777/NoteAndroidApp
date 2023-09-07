package com.example.notes.Managers;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.notes.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public  class ApiManager {
    private ArrayList<String> notes;
    private ArrayAdapter<String> adapter;
    private Context context;

    public  ApiManager(Context context) {
        notes = new ArrayList<>();
        this.context = context;
        GetNotes();
//        this.notes = GetNotes();
    }
    private  void  GetNotes(){
        new Thread(()->{
            String apiUrl = "https://10.0.2.2:7006/User/GetNotes";
                try{
                    URL url = new URL(apiUrl);
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                     int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                this.notes.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String title = jsonObject.getString("title");
                                    String body = jsonObject.getString("body");
                                    this.notes.add(title+" \n"+body);
                                }

//                                adapter.notifyDataSetChanged();
//                                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, this.notes);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                        }
                    } else {
                        Log.e("HTTP GET Request", "HTTP Response Code: " + responseCode);
                    }
                } catch (IOException e) {
                    Log.e("HTTP GET Request", "IOException: " + e.getMessage());
                }
        }).start();
    }
    public ArrayList<String>getNotes() {
        return this.notes;
    }

}

