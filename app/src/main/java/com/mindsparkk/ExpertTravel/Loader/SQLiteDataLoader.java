package com.mindsparkk.ExpertTravel.Loader;


import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.mindsparkk.ExpertTravel.Utils.DatabaseSave;


public class SQLiteDataLoader extends AsyncTaskLoader {

    private DatabaseSave mDataSource;
    private String mSelection;
    private String[] mSelectionArgs;

    @Override
    public Object loadInBackground() {
        return null;
    }

    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;

    public SQLiteDataLoader(Context context, DatabaseSave dataSource, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        super(context);
        mDataSource = dataSource;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
    }

//    protected List buildList() {
//        List testList = mDataSource.read(mSelection, mSelectionArgs, mGroupBy, mHaving, mOrderBy);
//        return testList;
//    }

//    public void insert(Test entity) {
//        new InsertTask(this).execute(entity);
//    }
//
//    public void update(Test entity) {
//        new UpdateTask(this).execute(entity);
//    }
//
//    public void delete(Test entity) {
//        new DeleteTask(this).execute(entity);
//    }
//
//    private class InsertTask extends ContentChangingTask<Test, Void, Void> {
//        InsertTask(SQLiteDataLoader loader) {
//            super(loader);
//        }
//
//
//        protected Void doInBackground(Test... params) {
//            mDataSource.insert(params[0]);
//            return (null);
//        }
//    }
//
//    private class UpdateTask extends ContentChangingTask<Test, Void, Void> {
//        UpdateTask(SQLiteDataLoader loader) {
//            super(loader);
//        }
//
//        protected Void doInBackground(Test... params) {
//            mDataSource.update(params[0]);
//            return (null);
//        }
//    }
//
//    private class DeleteTask extends ContentChangingTask<Test, Void, Void> {
//        DeleteTask(SQLiteDataLoader loader) {
//            super(loader);
//        }
//
//        protected Void doInBackground(Test... params) {
//            mDataSource.delete(params[0]);
//            return (null);
//        }
//    }
}