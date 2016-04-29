package com.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quy2016.doantotnghiep.R;
import com.model.ProfileUser;
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
                ParseQuery query = new ParseQuery("user_details");
                query.whereNotEqualTo("Email",ParseUser.getCurrentUser().getEmail());
                //query.whereContainedIn("rating", Arrays.asList("5", "4"));
                //query.orderByDescending("rating");
                return query;
            }
        });
    }

    @Override
    public View getItemView(ProfileUser object, View v, ViewGroup parent) {
        //return super.getItemView(object, v, parent);
        LayoutInflater inflater;
        ViewHolder viewHolder;
         inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        if(v==null)
        {
            v = inflater.inflate(R.layout.list_item_friend_row , parent , false);
            viewHolder = new ViewHolder();
            viewHolder.userName = (TextView) v.findViewById(R.id.user_name);
            viewHolder.school = (TextView) v.findViewById(R.id.user_school);
            viewHolder.photoFile = (ParseImageView) v.findViewById(R.id.avatar);
            v.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) v.getTag();
        }
        TextView userName = viewHolder.userName;
        userName.setText(object.getAuthorName());
        TextView school = viewHolder.school;
        school.setText(object.getSchool());
        ParseImageView avatar = viewHolder.photoFile;
        ParseFile photoFile = object.getPhotoFile();
        if (photoFile != null) {
            avatar.setParseFile(photoFile);
            avatar.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }
//        if(object.getBoolean("Online"))
//            status.setImageResource(R.drawable.online);
//        else
//            status.setImageResource(R.drawable.off);

        return v;
    }
    private static class ViewHolder{
        TextView userName;
        TextView school;
        ParseImageView photoFile;


    }
}
