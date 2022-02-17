package com.example.restapicaller;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.restapicaller.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_first);
        text = (TextView) findViewById(R.id.textview_first);
        text.setMovementMethod(new ScrollingMovementMethod());
//        sendRequest();
    }

    public void sendRequest(View view){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.10.5:8080/tematy/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            response = new String(response.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
                            Type type = new TypeToken<List<Temat>>() {}.getType();
                            List<Temat> lista = new Gson().fromJson(response,type);
                            String wynik = "";
                            for(Temat temat: lista){
                                wynik=wynik+temat.toString();
                            }
                            text.setText(wynik);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                text.setText(error.toString());
            }
        });

        queue.add(stringRequest);
    }
}