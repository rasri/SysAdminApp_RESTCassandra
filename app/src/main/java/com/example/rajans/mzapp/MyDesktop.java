package com.example.rajans.mzapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by rajans on 09/06/16.
 */
public class MyDesktop extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_desktop);
        //TODO
        Log.d(TAG, "onCreate");

        // Syslog Viewer
        ImageButton systemLogButton = (ImageButton)findViewById(R.id.systemLog);
        systemLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualizeSystemLog();
            }
        });


        // Workflow Manager
        ImageButton wfManagerButton = (ImageButton)findViewById(R.id.wfManager);
        wfManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualizeWfManager();
            }
        });

    }

    public void visualizeSystemLog(){
        //textView.setText(String.format("System Logs"));
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", "system Logs");
        startActivity(intent);
    }



    public void visualizeWfManager(){
        Intent intent = new Intent(this, WfManagerActivity.class);
        intent.putExtra("name", "system Logs");
        startActivity(intent);
    }
}
