package com.mindsparkk.ExpertTravel.Activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.ExpertTravel.Loader.SQLiteDataLoader;
import com.mindsparkk.ExpertTravel.R;
import com.mindsparkk.ExpertTravel.Utils.DatabaseSave;
import com.mindsparkk.ExpertTravel.Utils.PlaceListAdapter;
import com.mindsparkk.ExpertTravel.Utils.PlaceListDetail;
import com.mindsparkk.ExpertTravel.Utils.ProgressWheel;
import com.mindsparkk.ExpertTravel.app.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hitesh on 11/09/15.
 */
public class SavedListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String >> {

    TextView title, nodata;
    DatabaseSave db = new DatabaseSave(this);
    String mode;
    private PlaceListAdapter placeListAdapter;
    private List<PlaceListDetail> placeListDetailList = new ArrayList<>();
    private static final String TAG_RESULT = "result";
    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    ProgressWheel progressWheel;
    private RecyclerView recyclerView;
    List<String> loadedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_list_layout);

        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        progressWheel.spin();

        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.name);
        nodata = (TextView) findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.savedDataTag);
        String mode = getIntent().getStringExtra("mode");
        this.mode = mode;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.getSupportLoaderManager().initLoader(R.id.string_loader_id, null, this);

        Context context;
        context = getApplicationContext();
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Object API_KEY = (Object)ai.metaData.get("com.google.android.geo.API_KEY");
        //please add api_key in manifest and use the context to get value in this class for accessing the end points.


        switch (mode) {
            case "place":
                title.setText(R.string.savedPlacesTag);
                placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
                recyclerView.setAdapter(placeListAdapter);

                if (loadedData != null && loadedData.size() > 0) {
                    for (String id : loadedData) {
                        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=" + API_KEY.toString();
                        getPlaceDetail(url);
                    }
                } else {
                    progressWheel.stopSpinning();
                    nodata.setVisibility(View.VISIBLE);
                }

                break;

            case "restaurant":
                title.setText(R.string.savedRestaurantTag);

                placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
                recyclerView.setAdapter(placeListAdapter);

                if (loadedData != null && loadedData.size() > 0) {
                for (String id : loadedData) {
                    String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=" + API_KEY.toString();
                    getPlaceDetail(url);
                }
            }else{
                progressWheel.stopSpinning();
                nodata.setVisibility(View.VISIBLE);
            }

            break;

            case "hotel":
                title.setText(R.string.savedHotelTag);

                placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
                recyclerView.setAdapter(placeListAdapter);

                if (loadedData != null && loadedData.size() > 0) {
                    for (String id : loadedData) {
                        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=" + API_KEY.toString();
                        getPlaceDetail(url);
                    }
                } else {
                    progressWheel.stopSpinning();
                    nodata.setVisibility(View.VISIBLE);
                }

        }

    }

    public void getPlaceDetail(String url) {
        final ArrayList<String> photos_reference = new ArrayList<>();
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    progressWheel.stopSpinning();
                    PlaceListDetail detail = new PlaceListDetail();

                    JSONObject list = jsonObject.getJSONObject(TAG_RESULT);

                    JSONArray photos = list.getJSONArray("photos");

                    for (int j = 0; j < photos.length(); j++) {
                        JSONObject photo = photos.getJSONObject(j);

                        photos_reference.add(photo.getString(TAG_PHOTOS_REFERENCE));
                        detail.setPhoto_reference(photos_reference);

                    }

                    String placename = list.getString("name");
                    detail.setPlace_name(placename);

                    String vicinity = list.getString("vicinity");
                    detail.setPlace_address(vicinity);
                    detail.setPlace_id(list.getString("place_id"));
                    detail.setPlace_rating(list.getDouble("rating"));
                    placeListDetailList.add(detail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                placeListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName(getString(R.string.savedPlacesScreenTag));
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        loadedData = new SQLiteDataLoader(getApplicationContext(), db, mode).loadInBackground();
        return new SQLiteDataLoader(getApplicationContext(), db, mode);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        loadedData = data;
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        loader.deliverResult(loadedData);
    }
}
