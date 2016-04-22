package com.hust.friend_find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Utils.FriendListAdapter;
import com.example.quy2016.doantotnghiep.R;
import com.model.ProfileUser;
import com.parse.ParseQueryAdapter;

/**
 * Created by Administrator on 4/21/2016.
 */
public class FriendListFragment extends ListFragment {
    private FriendListAdapter friendListAdapter;
    private ParseQueryAdapter<ProfileUser> mainAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.list_item_friends,null);
        friendListAdapter = new FriendListAdapter(getContext());
        friendListAdapter.loadObjects();
        setListAdapter(friendListAdapter);
        return view;
    }
}
