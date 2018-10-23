package com.classassignment.volleyhttplibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    /*
    Add implementation 'com.android.volley:volley:1.1.1' to app's build.gradle file
    Add the android.permission.INTERNET permission to your app's manifest.
    */

    private String tag = "QueueTag";
    private TextView tvData;
    private Button btnParse;
    private RequestQueue mReqQueue;

    // url for json API data
    private String url = "https://randomapi.com/api/6de6abfedb24f889e0b5f675edc50deb?fmt=raw&sole";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = findViewById(R.id.tvData);
        btnParse = findViewById(R.id.btnParse);

        // Initializes new volley request queue
        mReqQueue = Volley.newRequestQueue(this);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieves json data from url
                getJsonData();
            }
        });

//        Other method for json parser for json file
//        is described on below mentioned link
//        https://o7planning.org/en/10459/android-json-parser-tutorial
    }

    private void getJsonData() {

        // JsonArrayRequest class instance to get json array
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Iterates for each value in response array and
                    // sets to text view for display
                    for (int i = 0; i < response.length(); i++){
                        try {
                            // Gets jsonObject at index i
                            JSONObject jsonObject = response.getJSONObject(i);

                            // gets individual values of data according to key names
                            String name = jsonObject.getString("first") + " " + jsonObject.getString("last");
                            String email = jsonObject.getString("email");

                            tvData.append("Name: " + name + "\n");
                            tvData.append("Email: " + email + "\n\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // On error in parsing logs the error
                    Log.e("Volley", error.toString());
            }
        });

        // Sets tag for the request
        jsonArrayRequest.setTag(tag);
        // Adds jsonArrayRequest to the request queue
        mReqQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // if queue is not empty
        // cancels all the requests with given tag
        if (mReqQueue != null){
            mReqQueue.cancelAll(tag);
        }
    }
}
