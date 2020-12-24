package com.google.firebase.and.chatter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;





public class News extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Article>> {
    public static final String GUARDIAN_API_URI = "https://content.guardianapis.com/search";
    private static final int LOADER_ID = 1;
    private static final String API_KEY = "test";
    private static final String TAG_NEEDED_FROM_API = "contributor";

    private NewsItemToViewAdapter newsItemToViewAdapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsItemToViewAdapter = new NewsItemToViewAdapter(this,
                new ArrayList<Article>());
        ListView newsView = findViewById(R.id.list);
        newsView.setAdapter(newsItemToViewAdapter);
        emptyView = (TextView) findViewById(R.id.empty_view);
        newsView.setEmptyView(emptyView);

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {

            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet_connection_found);
        }
        newsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = newsItemToViewAdapter.getItem(position);
                Uri uri = Uri.parse(currentArticle.getUrl());
                Intent openArticle = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openArticle);
            }
        });

    }

    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(GUARDIAN_API_URI);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("api-key", API_KEY);
        builder.appendQueryParameter("show-tags", TAG_NEEDED_FROM_API);
        NewsLoader newsLoader = new NewsLoader(this, builder.toString());
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> articles) {
        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        emptyView.setText(R.string.no_data);
        newsItemToViewAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            newsItemToViewAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {

    }

}