package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.Managers.ApiManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

public class MainActivity extends AppCompatActivity {
    private ListView noteListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notes;
    private TextInputEditText searchText;
    private Button searchBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Отключаем проверку сертификата SSL
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new InsecureTrustManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        noteListView = findViewById(R.id.noteListView);
        this.searchText = (TextInputEditText)findViewById(R.id.textSearch);
        this.searchBTN = (Button)findViewById(R.id.button);
        this.searchBTN.setOnClickListener(v->{
            new HttpGetRequest().execute("https://10.0.2.2:7006/User/SearchNotes?searchtext="+this.searchText.getText(),"GET");
        });
        notes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        noteListView.setAdapter(adapter);

        new HttpGetRequest().execute("https://10.0.2.2:7006/User/GetNotes","GET");
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
                    notes.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String body = jsonObject.getString("body");
                        notes.add(title + "\n"+body);
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
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Не выполняем проверку клиентских сертификатов
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Не выполняем проверку сертификатов сервера
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

}