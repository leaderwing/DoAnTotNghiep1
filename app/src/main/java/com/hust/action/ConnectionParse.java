package com.hust.action;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by QUY2016 on 4/17/2016.
 */
public class ConnectionParse  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "ZVuYQgGmT79jqb4HpbbabGz8xenRTXaYI30cCTDM", "Zrw7TkZIy1jGGY4JEGl0MAtakRBLXCeIMnF4qjrQ");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
