package com.utils;

/**
 * Created by Administrator on 4/21/2016.
 */
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hust.chat.Conversation;
import com.model.Comments;
import com.model.Post_Info;
import com.model.ProfileUser;
import com.model.UserFriends;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "HUST";
    // Used to pass location from MainActivity to PostActivity
    public static final String INTENT_EXTRA_LOCATION = "location";

    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE = "searchDistance";

    private static final float DEFAULT_SEARCH_DISTANCE = 250.0f;

    private static SharedPreferences preferences;

    private static ConfigHelper configHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ProfileUser.class);
        ParseObject.registerSubclass(Post_Info.class);
        ParseObject.registerSubclass(Conversation.class);
        ParseObject.registerSubclass(Comments.class);
        ParseObject.registerSubclass(UserFriends.class);
        Parse.initialize(this, "ZVuYQgGmT79jqb4HpbbabGz8xenRTXaYI30cCTDM", "Zrw7TkZIy1jGGY4JEGl0MAtakRBLXCeIMnF4qjrQ");
        preferences = getSharedPreferences("com.parse.anywall", Context.MODE_PRIVATE);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        mInstance = this;
//        preferences = getSharedPreferences("com.parse.anywall", Context.MODE_PRIVATE);
//
//        configHelper = new ConfigHelper();
//        configHelper.fetchConfigIfNeeded();
    }
    public static float getSearchDistance() {
        return preferences.getFloat(KEY_SEARCH_DISTANCE, DEFAULT_SEARCH_DISTANCE);
    }
    public static void setSearchDistance(float value) {
        preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
