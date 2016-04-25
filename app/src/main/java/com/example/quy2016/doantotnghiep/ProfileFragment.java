package com.example.quy2016.doantotnghiep;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.quy2016.doantotnghiep.R;
import com.model.ProfileUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Ratan on 7/29/2015.
 */
public class ProfileFragment extends Fragment
{
    EditText tvName , tvEmail,tvCourse,tvSchool,tvHobby,tvCharacter,tvBirthday;
    ToggleButton btnName,btnEmail,btnBirth,btnCourse,btnSchool,btnHobby,btnCharacter;
    Button btnSave;
    ImageButton imgUpload;
    ProfileUser profileUser;
    public  static  final  int SELECT_PICTURE = 1000;
    public Uri selectedImageUri;
    public  String selectedImagePath;
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
        btnName = (ToggleButton) view.findViewById(R.id.btnName);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        imgUpload = (ImageButton) view.findViewById(R.id.upload_avatar);
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInGallery();
                Toast.makeText(getContext() , "upload" , Toast.LENGTH_SHORT).show();
            }
        });
        ParseUser user = ParseUser.getCurrentUser();
        String name = user.getUsername();
        tvName.setText(name);
        tvEmail.setText(user.getEmail().toString());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("user_details");
        query.whereEqualTo("Email", ParseUser.getCurrentUser().getEmail());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {

                    profileUser = new ProfileUser();
                    profileUser.setBirthday(object.getDate("user_birthday").toString());
                    profileUser.setCourse(object.getString("course"));
                    profileUser.setCharacter(object.getString("user_char"));
                    profileUser.setSchool(object.getString("user_school"));
                    profileUser.setHobbies(object.getString("user_hobbies"));
                    tvBirthday.setText(profileUser.getBirthday().toString());
                    tvCourse.setText(profileUser.getCourse().toString());
                    tvSchool.setText(profileUser.getSchool().toString());
                    tvHobby.setText(profileUser.getHobbies().toString());
                    tvCharacter.setText(profileUser.getCharacter().toString());

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
                    tvHobby.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    tvHobby.setFocusableInTouchMode(false);
                    tvHobby.setFocusable(false);
                }
            }
        });
        btnCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvCourse.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    tvCourse.setFocusableInTouchMode(false);
                    tvCourse.setFocusable(false);
                }
            }
        });
        btnCharacter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvCharacter.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    tvCharacter.setFocusableInTouchMode(false);
                    tvCharacter.setFocusable(false);
                }
            }
        });
        btnSchool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvSchool.setFocusableInTouchMode(true);
                    isChecked = false;
                } else {
                    tvSchool.setFocusableInTouchMode(false);
                    tvSchool.setFocusable(false);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("user_details");
//                query1.getInBackground("Ayy8nYM2FM", new GetCallback<ParseObject>() {
//                    @Override
//                    public void done(ParseObject object, ParseException e) {
//                        if (e == null) {
//                            object.put("user_school", String.valueOf(tvSchool.getText()));
//                            object.put("user_birthday", String.valueOf(tvBirthday.getText()));
//                            object.put("course", String.valueOf(tvCourse.getText()));
//                            object.put("user_char", tvCharacter.getText().toString());
//                            object.put("user_hobbies", tvHobby.getText().toString());
//                            object.saveEventually(new SaveCallback() {
//                                @Override
//                                public void done(ParseException e) {
//                                    if (e == null) {
//                                        Toast.makeText(getContext(), "Luu thanh cong", Toast.LENGTH_LONG).show();
//
//                                    }
//                                }
//                            });
//                        } else
//                            Toast.makeText(getContext(), "Xay ra loi ", Toast.LENGTH_LONG).show();
//
//                    }
//                });
                SaveData();

            }
        });



        return view;
    }
    private  void SaveData()
    {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.dialog_title));
        dialog.show();
        InputStream imageStream = null;
        try {
            imageStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ParseFile file = new ParseFile("user_avatar", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();
        Log.d("File======", "" + file);
        ProfileUser user = new ProfileUser();
        user.setEmail(ParseUser.getCurrentUser());
        user.setAuthorName(String.valueOf(tvName.getText()));
        user.setBirthday(String.valueOf(tvBirthday.getText()));
        user.setCourse(String.valueOf(tvCourse.getText()));
        user.setCharacter(String.valueOf(tvCharacter.getText()));
        user.setHobbies(String.valueOf(tvHobby.getText()));
        user.setSchool(String.valueOf(tvSchool.getText()));
        user.setPhotoFile(file);
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        user.setACL(acl);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();

            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void openInGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Error :" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();

                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                imgUpload.setImageURI(selectedImageUri);
            }
        }
    }
    @SuppressWarnings("deprecation")
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }
}