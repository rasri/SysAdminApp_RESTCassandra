package com.example.rajans.mzapp;

import java.util.Date;

/**
 * Created by rajans on 20/05/16.
 */
public class SysLog {

    public final String dateMessage;
    public final String severity;
    public final String workflowName;
    public final String picoName;
    public final String ipAddress;
    public final String date;

    SysLog(String dateMessage, String severity,
           String workflowName, String picoName,
           String ipAddress, String date){

        this.dateMessage = dateMessage;
        this.severity = severity;
        this.workflowName = workflowName;
        this.picoName = picoName;
        this.ipAddress = ipAddress;
        this.date = date;
    }
}
