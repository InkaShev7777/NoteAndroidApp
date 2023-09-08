package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CategoryActivity extends AppCompatActivity {
    private ListView categoryListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> categoryes;
    private ArrayList<String> categoryWhisID;
    private Integer ChID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        this.categoryListView = (ListView) findViewById(R.id.categorylist);

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new MainActivity.InsecureTrustManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        categoryes = new ArrayList<>();
        categoryWhisID = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryes);
        categoryListView.setAdapter(adapter);
        new CategoryActivity.HttpGetRequest().execute("https://10.0.2.2:7006/User/getCat","GET");
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nameCat = categoryes.get(position);
                SetIdSelectedCategory(nameCat);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("IDCategory",ChID);
                startActivity(intent);
            }
        });
    }
    private void SetIdSelectedCategory(String title){
        for (String i:categoryWhisID){
            String[] mas = i.split(" ");
            if(mas[1].equals(title)){
                ChID = Integer.parseInt(mas[0]);
            }
        }
    }
    private class HttpGetRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];

            try {
                URL url = new URL(apiUrl);
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if(params[1] == "GET"){
                    connection.setRequestMethod("GET");
                }
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    return response.toString();
                } else {
                    Log.e("HTTP GET Request", "HTTP Response Code: " + responseCode);
                    return null;
                }
            } catch (IOException e) {
                Log.e("HTTP GET Request", "IOException: " + e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    categoryes.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String idCat = jsonObject.getString("id");
                        categoryes.add(title);
                        categoryWhisID.add(idCat + " " + title);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Произошла ошибка при получении заметок.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static class InsecureTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}