package com.example.rajans.mzapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rajans on 20/05/16.
 * Enable us to bind the syslog object to the view in list_item
 */
public class SysLogArrayAdapter extends ArrayAdapter<SysLog>{


    private static class ViewHolder {
        TextView dateMessageTextView;
        TextView severityTextView;
        TextView wfNameTextView;
        TextView picoNameTextView;
        TextView ipAddressTextView;
    }

    public SysLogArrayAdapter(Context context, List<SysLog> sysLog) {
        super(context, -1, sysLog);
    }


    // creates the custom views for the ListView's items
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get Weather object for this specified ListView position
        SysLog item = getItem(position);

        ViewHolder viewHolder; // object that reference's list item's views

        // check for reusable ViewHolder from a ListView item that scrolled
        // offscreen; otherwise, create a new ViewHolder
        if (convertView == null) { // no reusable ViewHolder, so create one
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            //TODO assignment of image view to text view
            viewHolder.dateMessageTextView= (TextView) convertView.findViewById(R.id.dateMessageTextView);
            viewHolder.severityTextView = (TextView)convertView.findViewById(R.id.severityTextView);
            viewHolder.wfNameTextView = (TextView) convertView.findViewById(R.id.wfNameTextView);
            viewHolder.picoNameTextView = (TextView) convertView.findViewById(R.id.picoNameTextView);
            viewHolder.ipAddressTextView= (TextView) convertView.findViewById(R.id.ipAddressTextView);
            convertView.setTag(viewHolder);
        }
        else { // reuse existing ViewHolder stored as the list item's tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // get other data from Weather object and place into views
        Context context = getContext(); // for loading String resources context for array adapter
        // set information
        viewHolder.dateMessageTextView.setText(context.getString(
                R.string.dateMessage, item.dateMessage,item.dateMessage
        ));
        viewHolder.severityTextView.setText(context.getString(R.string.severity,item.severity));
        viewHolder.wfNameTextView.setText(context.getString(R.string.wfName, item.workflowName));
        viewHolder.picoNameTextView.setText(
                context.getString(R.string.picoName,item.picoName));
        viewHolder.ipAddressTextView.setText(
                context.getString(R.string.ipAdress, item.ipAddress));

        return convertView; // return completed list item to display
    }
}
