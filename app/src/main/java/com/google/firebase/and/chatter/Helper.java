package com.google.firebase.and.chatter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class Helper {

    private final static String LOG_TAG = "[" + Helper.class.getSimpleName() + "]";

    private Helper() {

    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL", e);
        }
        return url;
    }

    public static String makeHttpRequest(Context context, URL url) throws IOException {
        String jsonResponse = "";

        if (jsonResponse == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                Toast.makeText(context,
                        "Error from Guardian API!",
                        Toast.LENGTH_LONG)
                        .show();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON response", e);
        } finally {
            if (urlConnection == null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader buffer = new BufferedReader(reader);
            String line = buffer.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = buffer.readLine();
            }
        }
        return streamOutput.toString();
    }

    private static ArrayList<Article> extractDataFromJSON(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Article> news = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject info = root.getJSONObject("response");
            JSONArray results = info.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject article = results.getJSONObject(i);

                String title = article.getString("webTitle");

                String section = article.getString("sectionName");

                String webUrl = article.getString("webUrl");

                String date = article.getString("webPublicationDate");

                JSONArray tags = article.getJSONArray("tags");
                String author = null;
                if (tags.length() >= 1) {
                    JSONObject tag = tags.getJSONObject(0);
                    author = "Author : " + tag.optString("webTitle");
                }

                Article articleItem = new Article(title, author, section, date, webUrl);
                news.add(articleItem);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "News API JSON cannot be parsed because of ", e);
        }

        return news;
    }

    public static ArrayList<Article> fetchNewsData(Context context, String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(context, url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Article> news = extractDataFromJSON(jsonResponse);
        return news;

    }
}
