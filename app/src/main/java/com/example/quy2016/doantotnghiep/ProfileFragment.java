package com.example.quy2016.doantotnghiep;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Ratan on 7/29/2015.
 */
public class ProfileFragment extends Fragment
{
    EditText tvName , tvEmail,tvCourse,tvSchool,tvHobby,tvCharacter,tvBirthday;
    ToggleButton btnName,btnEmail,btnBirth,btnCourse,btnSchool,btnHobby,btnCharacter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.profile_layout,null);

        tvName = (EditText) view.findViewById(R.id.tvName);
        tvEmail = (EditText) view.findViewById(R.id.tvEmail);
        tvCourse = (EditText) view.findViewById(R.id.tvCourse);
        tvBirthday = (EditText) view.findViewById(R.id.tvBirth);
        tvSchool = (EditText) view.findViewById(R.id.tvSchool);
        tvHobby = (EditText) view.findViewById(R.id.tvHobby);
        tvCharacter = (EditText) view.findViewById(R.id.tvCharacter);
       // btnEmail = (ToggleButton) view.findViewById(R.id.btnEmail);
        btnBirth = (ToggleButton) view.findViewById(R.id.btnBirthday);
        btnCourse = (ToggleButton) view.findViewById(R.id.btnCourse);
        btnSchool = (ToggleButton) view.findViewById(R.id.btnSchool);
        btnHobby = (ToggleButton) view.findViewById(R.id.btnHobby);
        btnCharacter = (ToggleButton) view.findViewById(R.id.btnCharacter);
        final ParseUser user = ParseUser.getCurrentUser();
        String name = user.getString("name");
        if(user != null)
        {
            tvName.setText(name);
            tvEmail.setText(user.getEmail().toString());
        }
         ParseQuery<ParseObject> query = ParseQuery.getQuery("user_details");
        query.whereEqualTo("Email", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        tvBirthday.setText(object.getString("user_birthday"));
                        tvSchool.setText(object.getString("user_school"));
                        tvCourse.setText(object.getString("course"));
                        tvHobby.setText(object.getString("user_hobbies"));
                        tvCharacter.setText(object.getString("user_char"));

                    }
                } else {
                    Log.d("User_details", "Error: " + e.getMessage());
                }
            }
        });

        btnName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvName.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    tvName.setFocusableInTouchMode(false);
                    tvName.setFocusable(false);
                }
            }
        });
        btnBirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvBirthday.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    tvBirthday.setFocusableInTouchMode(false);
                    tvBirthday.setFocusable(false);
                }
            }
        });
        btnHobby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnHobby.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    btnHobby.setFocusableInTouchMode(false);
                    btnHobby.setFocusable(false);
                }
            }
        });
        btnCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnCourse.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    btnCourse.setFocusableInTouchMode(false);
                    btnCourse.setFocusable(false);
                }
            }
        });
        btnCharacter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnCharacter.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    btnCharacter.setFocusableInTouchMode(false);
                    btnCharacter.setFocusable(false);
                }
            }
        });
        btnSchool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnSchool.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    btnSchool.setFocusableInTouchMode(false);
                    btnSchool.setFocusable(false);
                }
            }
        });
        ParseQuery<ParseObject> saveData = ParseQuery.getQuery("user_details");
        saveData.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.put("user_school", tvSchool.getText().toString());
                object.put("user_birthday", tvBirthday.getText().toString());
                object.put("course", tvCourse.getText().toString());
                object.put("user_char", tvCharacter.getText().toString());
                object.put("user_hobbies", tvHobby.getText().toString());
            }
        });

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
