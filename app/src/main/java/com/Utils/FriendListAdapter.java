package com.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quy2016.doantotnghiep.R;
import com.model.ProfileUser;
import com.model.UserFriends;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by Administrator on 4/21/2016.
 */
public class FriendListAdapter extends ParseQueryAdapter<ProfileUser> {

    public FriendListAdapter(Context context) {
        super(context, new QueryFactory<ProfileUser>() {
            public ParseQuery<ProfileUser> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery<UserFriends> innerQuery = new ParseQuery<UserFriends>("User_friends");
                innerQuery.whereMatches("relation","follow");
                innerQuery.whereEqualTo("from_user",ParseUser.getCurrentUser());
                ParseQuery query = new ParseQuery("user_details");
                query.whereMatchesKeyInQuery("user","to_user",innerQuery);
                return query;
            }
        });
    }

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

}
