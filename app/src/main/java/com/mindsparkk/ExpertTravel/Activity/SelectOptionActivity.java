package com.mindsparkk.ExpertTravel.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mindsparkk.ExpertTravel.IntentServices.PlaceDetail;
import com.mindsparkk.ExpertTravel.R;
import com.mindsparkk.ExpertTravel.app.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hitesh on 04/09/15.
 */
public class SelectOptionActivity extends AppCompatActivity {

    String placeid;
    String url;
    LinearLayout hotels, restaurants, tourist, flights, shopping;
    private static final String TAG_RESULT = "result";
    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_LOCATION = "location";
    String st_lat, st_lng;
    Double lat, lng;
    FrameLayout frameLayout;
    ImageView back;
    TextView cityname;
    private static Context context;

    public SelectOptionActivity() throws PackageManager.NameNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_option_layout);
        SelectOptionActivity.context = getApplicationContext();
        hotels = (LinearLayout) findViewById(R.id.hotels);
        restaurants = (LinearLayout) findViewById(R.id.restaurants);
        shopping = (LinearLayout) findViewById(R.id.shopping);
        tourist = (LinearLayout) findViewById(R.id.tourist);
        flights = (LinearLayout) findViewById(R.id.flight);
        frameLayout = (FrameLayout) findViewById(R.id.mainFrame);
        back = (ImageView) findViewById(R.id.back);
        cityname = (TextView) findViewById(R.id.cityname);

        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Object API_KEY = (Object)ai.metaData.get("com.google.android.geo.API_KEY");
        //please add api_key in manifest and use the context to get value in this class for accessing the end points.

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().getExtras() != null) {
            placeid = getIntent().getStringExtra("placeid");
            String name = getIntent().getStringExtra("name");
            cityname.setText(name);
        }

        url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeid + "&key="+API_KEY.toString();
        Intent intent = new Intent(this, PlaceDetail.class);
        intent.putExtra("url", url);
        startService(intent);
        getPlaceDetail(url);

        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, RestaurantListActivity.class);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });

        tourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, TouristMainActivity.class);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, HotelListActivity.class);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

        flights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, FlightActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectOptionActivity.this, ShoppingActivity.class);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

    }

    public void getPlaceDetail(String url) {
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
