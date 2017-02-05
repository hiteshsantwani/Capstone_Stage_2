package com.mindsparkk.ExpertTravel.Loader;


import java.util.List;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.mindsparkk.ExpertTravel.Utils.DatabaseSave;


public class SQLiteDataLoader extends AsyncTaskLoader<List<String>> {


    String mode;
    DatabaseSave db = new DatabaseSave(getContext());

    public SQLiteDataLoader(Context context, DatabaseSave db, String mode) {
        super(context);
        this.db = db;
        this.mode = mode;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public List<String> loadInBackground() {
        List<String> data;
        switch (mode){
            case "place":
                data = db.getAllPlaces();
                break;
            case "restaurant":
                 data = db.getAllRes();
                 break;
            case "hotel":
                 data = db.getAllHotels();
                 break;
            default:
                 data = null;
        }
        return data;
    }

    @Override
    public void deliverResult(List<String> data) {
        super.deliverResult(data);
    }
}