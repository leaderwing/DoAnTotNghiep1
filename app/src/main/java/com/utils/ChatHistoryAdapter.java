package com.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quy2016.doantotnghiep.R;
import com.hust.chat.Conversation;
import com.model.Post_Info;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 5/10/2016.
 */
