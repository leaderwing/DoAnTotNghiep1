package com.hust.friend_find;

import android.app.Application;
import android.app.Dialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quy2016.doantotnghiep.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.model.ProfileUser;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.utils.AppController;
import com.utils.Utils;

/**
 * Created by Administrator on 4/21/2016.
 */
public class SearchGPSFragment extends FragmentActivity implements LocationListener , GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener {
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;

    /*
     * Constants for handling location results
     */
    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    private static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    private static final int MAX_POST_SEARCH_RESULTS = 20;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 100;
    private LocationRequest locationRequest;
    private GoogleApiClient locationClient;
    private Location lastLocation;
    private Location currentLocation;
    // Fields for the map radius in feet
    private float radius;
    private float lastRadius;
    private ParseQueryAdapter<ProfileUser> profileUserParseQueryAdapter;

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        currentLocation = getLocation();
        startPeriodicUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                Utils.ErrorDialogFragment errorFragment = new Utils.ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), AppController.APPTAG);
            }
            return false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        ParseQueryAdapter.QueryFactory<ProfileUser> factory =
                new ParseQueryAdapter.QueryFactory<ProfileUser>() {
                    public ParseQuery<ProfileUser> create() {
                        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                        ParseQuery<ProfileUser> query = ProfileUser.getQuery();
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.whereWithinKilometers("location", geoPointFromLocation(myLoc), radius
                                * METERS_PER_FEET / METERS_PER_KILOMETER);
                        query.setLimit(MAX_POST_SEARCH_RESULTS);
                        return query;
                    }
                };
        profileUserParseQueryAdapter = new ParseQueryAdapter<ProfileUser>(this ,factory)
        {
            @Override
            public View getItemView(ProfileUser object, View v, ViewGroup parent) {
                //return super.getItemView(object, v, parent);
                if(v==null)
                {
                    v = View.inflate(getContext(), R.layout.list_item_friend_row,null);
                }
                super.getItemView(object,v,parent);
                ParseImageView profile = (ParseImageView) v.findViewById(R.id.avatar);
                ParseFile photoFile = object.getThumbnail();
                if (photoFile != null) {
                    profile.setParseFile(photoFile);
                    profile.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do
                        }
                    });
                }
                TextView userName = (TextView) v.findViewById(R.id.user_name);
                try {
                    userName.setText(object.getParseUser("user").fetchIfNeeded().getString("name"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TextView school = (TextView) v.findViewById(R.id.user_school);
                school.setText(object.getSchool());
                ImageView status = (ImageView) v.findViewById(R.id.arrow);

                if(object.getBoolean("Online"))
                    status.setImageResource(R.drawable.online);
                else
                    status.setImageResource(R.drawable.off);

                return v;

            }
        };
        profileUserParseQueryAdapter.setAutoload(false);

        // Disable pagination, we'll manage the query limit ourselves
        profileUserParseQueryAdapter.setPaginationEnabled(false);

    }
    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }
}
