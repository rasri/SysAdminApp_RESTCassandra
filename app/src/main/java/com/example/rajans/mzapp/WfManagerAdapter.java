package com.example.rajans.mzapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajans on 15/06/16.
 */
public class WfManagerAdapter extends ArrayAdapter<WfName> {


    private View.OnClickListener adapterDynaListener = null;
    private static Activity context;
    private List<WfName> list;
    private StopTask stopTask;

    private List<WfName> wfList = new ArrayList<>();


    public WfManagerAdapter(Context context, final List<WfName> wfNames) {
        super(context, -1, wfNames);
        this.list = wfNames;
        adapterDynaListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = createURL();

                System.out.println("url = " + url);
                stopTask = new StopTask();
                stopTask.execute(url);
            }

        };

    }

    public URL createURL() {
        try {
            return new URL("http://10.240.32.45:3000/service/stop/_server._1_server_1.workflow_1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null; // Malformed exception thrown
    }


    private static class ViewHolder {
        TextView wfName;
        Button button;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get Weather object for this specified ListView position
        WfName item = getItem(position);

        ViewHolder viewHolder; // object that reference's list item's views

        // check for reusable ViewHolder from a ListView item that scrolled
        // offscreen; otherwise, create a new ViewHolder
        if (convertView == null) { // no reusable ViewHolder, so create one
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.wfmanager_list_item, parent, false);
            //TODO assignment of image view to text view
            viewHolder.wfName= (TextView) convertView.findViewById(R.id.wfName);
            viewHolder.button= (Button)  convertView.findViewById(R.id.BtnToClick);
            viewHolder.button.setTag(viewHolder);
            convertView.setTag(viewHolder);
        }
        else { // reuse existing ViewHolder stored as the list item's tag
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // get other data from Weather object and place into views
        Context context = getContext(); // for loading String resources context for array adapter
        // set information
        viewHolder.wfName.setText(context.getString(R.string.activeWf, item.wfName));
        viewHolder.button.setText("button");
        System.out.println("wfName = " + viewHolder.wfName);
        viewHolder.button.setOnClickListener(adapterDynaListener);

        return convertView; // return completed list item to display
    }






    private class StopTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection con = null;

            try {
                con = (HttpURLConnection) params[0].openConnection();
                System.out.println("connection = " + con);


                con.setRequestMethod("POST");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                //Request Parameters you want to send
                String urlParameters = "_server._1_server_1.workflow_1";

                // Send post request
                con.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + params[0]);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                con.disconnect();
            }
            return null;
        }
    }
}
