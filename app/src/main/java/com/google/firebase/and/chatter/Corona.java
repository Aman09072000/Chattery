package com.google.firebase.and.chatter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class Corona extends AppCompatActivity {

    public static final String LOG_TAG = Alert.class.getSimpleName();
    private static final String USGS_REQUEST_URL =
            "https://covid2019-api.herokuapp.com/v2/current";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona);

        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();
    }

    //.......................................................................................

    private void updateUi(CoEvent earthquake) {
        // Display the earthquake title in the UI
        try {
            TextView titleTextView = (TextView) findViewById(R.id.title);
            titleTextView.setText(earthquake.title);

            // Display the earthquake date in the UI
            TextView dateTextView = (TextView) findViewById(R.id.date);
            dateTextView.setText(earthquake.time);

            // Display whether or not there was a tsunami alert in the UI
            TextView tsunamiTextView = (TextView) findViewById(R.id.tsunami_alert);
            tsunamiTextView.setText(earthquake.tsunamiAlert);

            TextView recoverdTextView = (TextView) findViewById(R.id.recovered_alert);
            recoverdTextView.setText(earthquake.recoveredAlert);

            TextView activeTextView = (TextView) findViewById(R.id.active_alert);
            activeTextView.setText(earthquake.activeAlert);
        }catch (NullPointerException ignore){}
    }

    //.........................................................................................

    private class TsunamiAsyncTask extends AsyncTask<URL, Void, CoEvent> {

        @Override
        protected CoEvent doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(USGS_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link CoEvent} object
            CoEvent earthquake = extractFeatureFromJson(jsonResponse);

            // Return the {@link CoEvent} object as the result fo the {@link TsunamiAsyncTask}
            return earthquake;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {@link TsunamiAsyncTask}).
         */
        @Override
        protected void onPostExecute(CoEvent earthquake) {
            if (earthquake == null) {
                return;
            }

            updateUi(earthquake);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            ///
            if (url == null) {
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                //////
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


        private CoEvent extractFeatureFromJson(String earthquakeJSON) {
            ///
            if(TextUtils.isEmpty(earthquakeJSON)){
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
                JSONArray featureArray = baseJsonResponse.getJSONArray("data");

                // If there are results in the features array
                if (featureArray.length() > 0) {
                    // Extract out the first feature (which is an earthquake)
                    JSONObject firstFeature = featureArray.getJSONObject(1);
//                    JSONObject properties = firstFeature.getJSONObject("properties");

                    // Extract out the title, time, and tsunami values
                    String country = firstFeature.getString("location");
                    String conform = firstFeature.getString("confirmed");
                    String death = firstFeature.getString("deaths");
                    String recovered = firstFeature.getString("recovered");
                    String active = firstFeature.getString("active");

                    // Create a new {@link CoEvent} object
                    return new CoEvent(country, conform, death , recovered , active);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }

    }





}

