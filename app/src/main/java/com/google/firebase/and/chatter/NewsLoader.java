package com.google.firebase.and.chatter;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<Article>> {

    private static final String LOG_TAG =  NewsLoader.class.getName();
    private String mUrl;
    private Context localContext;

    //Constructor
    public NewsLoader(Context context, String url) {
        super(context);
        localContext = context;
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return Helper.fetchNewsData(localContext, mUrl);
    }
}
