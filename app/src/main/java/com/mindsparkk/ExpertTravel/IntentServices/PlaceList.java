package com.mindsparkk.ExpertTravel.IntentServices;

import android.app.IntentService;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mindsparkk.ExpertTravel.Utils.PlaceListAdapter;
import com.mindsparkk.ExpertTravel.Utils.PlaceListDetail;
import com.mindsparkk.ExpertTravel.app.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitesh on 28-01-2017.
 */

public class PlaceList extends IntentService {

    private PlaceListAdapter placeListAdapter;
    private List<PlaceListDetail> placeListDetailList = new ArrayList<>();
    private static final String TAG_RESULT = "results";
    private static final String TAG_ICON = "icon";
    private static final String TAG_NAME = "name";
    private static final String TAG_PLACE_ID = "place_id";
    private static final String TAG_RATING = "rating";
    private static final String TAG_ADDRESS = "vicinity";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    private RecyclerView recyclerView;
    private Double latitude, longitude;
    String url;
    public PlaceList() {
        super("PlaceList_worker");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        url = intent.getExtras().getString("url");
        JsonObjectRequest placeReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    JSONArray list = jsonObject.getJSONArray(TAG_RESULT);
                    for (int i = 0; i < list.length(); i++) {

                        JSONObject m = list.getJSONObject(i);
                        PlaceListDetail placeListDetail = new PlaceListDetail();

                        placeListDetail.setPlace_id(m.getString(TAG_PLACE_ID));
                        placeListDetail.setIcon_url(m.getString(TAG_ICON));
                        placeListDetail.setPlace_address(m.getString(TAG_ADDRESS));
                        placeListDetail.setPlace_name(m.getString(TAG_NAME));

                        if (m.has(TAG_RATING)) {
                            placeListDetail.setPlace_rating(m.getDouble(TAG_RATING));
                        }

                        if (m.has(TAG_PHOTOS)) {
                            JSONArray photos = m.getJSONArray(TAG_PHOTOS);

                            Log.d("photos", photos.toString());

                            for (int j = 0; j < photos.length(); j++) {
                                JSONObject photo = photos.getJSONObject(j);

                                ArrayList<String> photos_reference = new ArrayList<>();
                                photos_reference.add(photo.getString(TAG_PHOTOS_REFERENCE));

                                placeListDetail.setPhoto_reference(photos_reference);
                            }
                        }

                        placeListDetailList.add(placeListDetail);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToRequestQueue(placeReq);
    }
}
