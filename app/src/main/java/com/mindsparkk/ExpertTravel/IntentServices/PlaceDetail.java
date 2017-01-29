package com.mindsparkk.ExpertTravel.IntentServices;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mindsparkk.ExpertTravel.app.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hitesh on 28-01-2017.
 */

public class PlaceDetail extends IntentService {

    private static final String TAG_RESULT = "result";
    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_LOCATION = "location";
    String st_lat, st_lng, url;
    Double lat, lng;

    public PlaceDetail() {
        super("PlaceDetail_worker");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        url = intent.getExtras().getString("url");
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject movies = jsonObject.getJSONObject(TAG_RESULT);
                    JSONObject geometry = movies.getJSONObject(TAG_GEOMETRY);
                    JSONObject location = geometry.getJSONObject(TAG_LOCATION);

                    st_lat = location.getString("lat");
                    st_lng = location.getString("lng");

                    lat = Double.parseDouble(st_lat);
                    lng = Double.parseDouble(st_lng);
                    Log.d("lat", lat + "");
                    Log.d("lng", lng + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToRequestQueue(movieReq);

    }
}
