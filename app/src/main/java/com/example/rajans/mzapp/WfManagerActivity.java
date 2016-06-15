package com.example.rajans.mzapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajans on 15/06/16.
 */
public class WfManagerActivity extends AppCompatActivity {

    private List<WfName> wfList = new ArrayList<>();
    //TODO
    private WfManagerAdapter wfNamesArrayAdaper;
    private ListView wfListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wfmanager_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        wfListView = (ListView) findViewById(R.id.wfListView);
        wfNamesArrayAdaper = new WfManagerAdapter(this, wfList);
        wfListView.setAdapter(wfNamesArrayAdaper);

        URL url = createURL();
        if (url != null) {
            WfManagerTask wfManagerTaskProvider = new WfManagerTask();
            wfManagerTaskProvider.execute(url);
        }
    }

    public URL createURL() {

        String urlString;
        String baseUrl = getString(R.string.web_service_url);

        try {
            urlString = baseUrl + "wfManager/active";//URLEncoder.encode("wfManager/active");
            System.out.println("urlString = " + urlString);
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } /*catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }*/
        return null; // Malformed exception thrown
    }

    private class WfManagerTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("line before = " + builder.toString());
                            builder.append(line);
                            System.out.println("builder.toString = " + builder.toString());
                        }
                    } catch (IOException e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout),
                                R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    try {
                        JSONArray array = new JSONArray(builder.toString());
                        return array;
                    } catch (JSONException ex) {
                        System.out.println("ex = " + ex.toString());
                    }
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONArray wfNamesArray) {
            for (int i = 0; i < wfNamesArray.length(); i++) {
                try {
                    JSONObject sysLog = wfNamesArray.getJSONObject(i);
                    System.out.println("sysLog onPostExecute() = " + sysLog.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            convertJSONtoArrayList(wfNamesArray); // repopulate syslogList
            wfNamesArrayAdaper.notifyDataSetChanged(); // rebind to ListView
            wfListView.smoothScrollToPosition(0); // scroll to top
        }
    }

    // Create SysLog objects from JSONObject containing the forecast
    // Here we have all the Syslog read from the system
    //private void convertJSONtoArrayList(JSONObject sysLog) {
    private void convertJSONtoArrayList(JSONArray wfNamesArray) {
        wfList.clear(); // clear old weather data

        for(int i = 0; i< wfNamesArray.length();i++) {

            try {
                JSONObject wfName = wfNamesArray.getJSONObject(i);

                if (wfName != null)
                    System.out.println("wfName = " + wfName.toString());

                wfList.add(new WfName(wfName.getString("wfName")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
