package com.example.rajans.mzapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<SysLog> sysLogList = new ArrayList<>();
    public static final int NOTIFICATION_ID = 314159;


    // ArrayAdapter for binding syslogList objects to a ListView
    private SysLogArrayAdapter sysLogArrayAdapter;
    private ListView sysLogListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sysLogListView = (ListView)findViewById(R.id.sysLogListView);
        sysLogArrayAdapter = new SysLogArrayAdapter(this, sysLogList);
        sysLogListView.setAdapter(sysLogArrayAdapter);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText dayEditText = (EditText)findViewById(R.id.dayEditText);
                URL url = createURL(dayEditText.getText().toString());

                if(url!=null){
                    dismissKeyboard(dayEditText);
                    SysLogTask sysLogTaskProvider = new SysLogTask();
                    sysLogTaskProvider.execute(url);
                }
                else {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    public URL createURL(String day){

        String urlString;
        String baseUrl = getString(R.string.web_service_url);

        try {
            System.out.println("Input day = " + day);
            String date;

            switch (day.trim()) {
                case("today"):
                case("Today"):
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //date = sdf.format(new Date());
                    //break;
                    date= "2016-06-15";
                    break;
                default:
                    date = "all";
            }

            urlString = baseUrl + URLEncoder.encode(date, "UTF-8");

            //return new URL(getString(R.string.web_service_url));
            System.out.println("urlString = " + urlString);
            return new URL(urlString);

        } catch (MalformedURLException e ) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }


        return null; // Malformed exception thrown
    }

    // programmatically dismiss keyboard when user touches FAB
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class SysLogTask extends AsyncTask<URL,Void,JSONArray>{

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line ;
                        while((line = reader.readLine())!=null){
                            System.out.println("line before = " + builder.toString());
                            builder.append(line);
                            System.out.println("builder.toString = " + builder.toString());
                        }
                    }
                    catch (IOException e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout),
                                R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }/*catch(JSONException ex){
                        System.out.println("ex = " + ex.toString());
                    }*/

                    try{
                        JSONArray array = new JSONArray(builder.toString());
                        //JSONObject obj = new JSONObject(builder.toString());
                        //System.out.println("obj = " + obj.toString());

                        return array;
                    }catch(JSONException ex) {
                        System.out.println("ex = " + ex.toString());
                    }


                    //return new JSONObject(builder.toString());
                }
                else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                connection.disconnect();
            }
            return null;
        }


        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONArray sysLogArray) {
            for(int i =0; i<sysLogArray.length();i++){
                try {
                    JSONObject sysLog = sysLogArray.getJSONObject(i);
                    System.out.println("sysLog onPostExecute() = " + sysLog.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            convertJSONtoArrayList(sysLogArray); // repopulate syslogList
            sysLogArrayAdapter.notifyDataSetChanged(); // rebind to ListView
            sysLogListView.smoothScrollToPosition(0); // scroll to top
        }
    }

    // Create SysLog objects from JSONObject containing the forecast
    // Here we have all the Syslog read from the system
    //private void convertJSONtoArrayList(JSONObject sysLog) {
    private void convertJSONtoArrayList(JSONArray sysLogArray) {
        sysLogList.clear(); // clear old weather data

        for(int i = 0; i< sysLogArray.length();i++) {

            try {
                JSONObject sysLog = sysLogArray.getJSONObject(i);

                if (sysLog != null)
                    System.out.println("sysLog = " + sysLog.toString());

                System.out.println("message = " + sysLog.getString("message"));
                System.out.println("severity_type = " + sysLog.getString("severity_type"));
                System.out.println("wf_instance_name = " + sysLog.getString("wf_instance_name"));
                System.out.println("pico_name = " + sysLog.getString("pico_name"));
                System.out.println("ip_address = " + sysLog.getString("ip_address"));
                System.out.println(" = " + sysLog.getString("date"));

                if(sysLog.getString("severity_type").equals("2")){
                    System.out.println("sysLog.getString(\"severity_type\") = " + sysLog.getString("severity_type"));
                    notifyUser(sysLog.getString("message"));
                }

                //sysLogList.addAll((Collection<? extends SysLog>) sysLogArray);
                sysLogList.add(new SysLog(
                        sysLog.getString("message"),
                        sysLog.getString("severity_type"),
                        sysLog.getString("wf_instance_name"),
                        sysLog.getString("pico_name"),
                        sysLog.getString("ip_address"),
                        sysLog.getString("date")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyUser(String message) {

        Intent intent = new Intent(this, MyDesktop.class);
        TaskStackBuilder tsb = TaskStackBuilder.create(this);
        tsb.addParentStack(MyDesktop.class);
        tsb.addNextIntent(intent);

        PendingIntent pendingIntent = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_done_24dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setTicker(message)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);

    }


}
