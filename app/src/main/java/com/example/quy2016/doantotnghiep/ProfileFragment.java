package com.example.quy2016.doantotnghiep;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.model.ProfileUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.utils.Utils;

/**
 * Created by Ratan on 7/29/2015.
 */
public class ProfileFragment extends Fragment
{
    EditText tvName ,tvHobby, tvEmail,tvCharacter,tvBirthday,tvAddress;
    AutoCompleteTextView tvCourse , tvSchool ;
    ToggleButton btnName,btnEmail,btnBirth,btnCourse,btnSchool,btnHobby,btnCharacter,btnAddress;
    Button btnSave;
    ParseImageView avatar;
    ParseFile image , thumbnailImg;
    ImageButton imgUpload;
    ProfileUser profileUser;
    public  static  final  int SELECT_PICTURE = 1000;
    public Uri selectedImageUri;
    public  String selectedImagePath;
    public String user_email, user_name;
    Date dateFinish;
    Calendar cal;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.profile_layout,null);

        tvName = (EditText) view.findViewById(R.id.tvName);
        tvEmail = (EditText) view.findViewById(R.id.tvEmail);
        tvCourse = (AutoCompleteTextView) view.findViewById(R.id.tvCourse);
        tvBirthday = (EditText) view.findViewById(R.id.tvBirth);
        tvSchool = (AutoCompleteTextView) view.findViewById(R.id.tvSchool);
        tvHobby = (EditText) view.findViewById(R.id.tvHobby);
        tvCharacter = (EditText) view.findViewById(R.id.tvCharacter);
        avatar = (ParseImageView) view.findViewById(R.id.avatar_profile);
        tvAddress = (EditText) view.findViewById(R.id.tvAddress);
        btnEmail = (ToggleButton) view.findViewById(R.id.btnEmail);
        btnBirth = (ToggleButton) view.findViewById(R.id.btnBirthday);
        btnCourse = (ToggleButton) view.findViewById(R.id.btnCourse);
        btnSchool = (ToggleButton) view.findViewById(R.id.btnSchool);
        btnHobby = (ToggleButton) view.findViewById(R.id.btnHobby);
        btnAddress = (ToggleButton) view.findViewById(R.id.btnAddress);
        btnCharacter = (ToggleButton) view.findViewById(R.id.btnCharacter);
        btnName = (ToggleButton) view.findViewById(R.id.btnName);
        imgUpload = (ImageButton) view.findViewById(R.id.upload_avatar);
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInGallery();

            }
        });
        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;
        //Định dạng ngày / tháng /năm
        dft=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final String strDate=dft.format(cal.getTime());
        //hiển thị lên giao diện

        ParseUser user = ParseUser.getCurrentUser();
        user_email = user.getEmail().toString();
        user_name = user.getUsername().toString();
        tvName.setText(user.get("name").toString());
        tvEmail.setText(user_email);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("user_details");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    profileUser = new ProfileUser();
                   if (object.getString("user_birthday") != null )
                    profileUser.setBirthday(object.getString("user_birthday"));
                    else
                   profileUser.setBirthday(strDate);
                    if (object.getString("course") != null )
                    profileUser.setCourse(object.getString("course"));
                    if (object.getString("user_char") != null )
                    profileUser.setCharacter(object.getString("user_char"));
                    if (object.getString("user_school") != null )
                    profileUser.setSchool(object.getString("user_school"));
                    if (object.getString("user_hobbies") != null )
                    profileUser.setHobbies(object.getString("user_hobbies"));
                    profileUser.setObjectId(object.getObjectId());
                   // profileUser.setbjectId(object.getObjectId());
                    if(object.getParseFile("user_avatar")!= null) {
                        profileUser.setPhotoFile(object.getParseFile("user_avatar"));
                        avatar.setParseFile(profileUser.getPhotoFile());
                    }
                    else {
                        avatar.setImageResource(R.drawable.default_avatar);
                    }
                    tvBirthday.setText(profileUser.getBirthday());
                    tvCourse.setText(profileUser.getCourse());
                    tvSchool.setText(profileUser.getSchool());
                    tvHobby.setText(profileUser.getHobbies());
                    tvCharacter.setText(profileUser.getCharacter());
                   // avatar.setImageBitmap(profileUser.getPhotoFile().);
                }
            }
        });

        btnName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvName.setFocusableInTouchMode(true);

                } else {
                    tvName.setFocusableInTouchMode(false);
                    tvName.setFocusable(false);
                }
                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.put("name" ,tvName.getText().toString());
                        object.saveInBackground();
                    }
                });

            }
        });
        btnEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvEmail.setFocusableInTouchMode(true);

                } else {
                    tvEmail.setFocusableInTouchMode(false);
                    tvEmail.setFocusable(false);
                }
                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.put("email" ,tvEmail.getText().toString());
                        object.saveInBackground();
                    }
                });

            }
        });
        btnBirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvBirthday.setFocusableInTouchMode(true);
                    showDatePickerDialog();
                } else {
                    tvBirthday.setFocusableInTouchMode(false);
                    tvBirthday.setFocusable(false);
                }

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("user_details");
                query1.whereEqualTo("user",ParseUser.getCurrentUser());
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (ParseObject object : objects) {
                            profileUser = new ProfileUser();
                            profileUser.setObjectId(object.getObjectId());
                            ParseQuery<ParseObject> query11 = ParseQuery.getQuery("user_details");
                            query11.getInBackground(profileUser.getObjectId(), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    object.put("user_birthday", tvBirthday.getText().toString());
                                    object.saveInBackground();
                                }
                            });
                        }
                    }
                });

            }
        });
        btnAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvAddress.setFocusableInTouchMode(true);
                } else {
                    tvAddress.setFocusableInTouchMode(false);
                    tvAddress.setFocusable(false);
                }

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("user_details");
                query1.whereEqualTo("user",ParseUser.getCurrentUser());
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (ParseObject object : objects) {
                            profileUser = new ProfileUser();
                            profileUser.setObjectId(object.getObjectId());
                            ParseQuery<ParseObject> query11 = ParseQuery.getQuery("user_details");
                            query11.getInBackground(profileUser.getObjectId(), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    object.put("user_address", tvAddress.getText().toString());
                                    object.saveInBackground();
                                }
                            });
                        }
                    }
                });

            }
        });
        btnHobby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvHobby.setFocusableInTouchMode(true);
                } else {
                    tvHobby.setFocusableInTouchMode(false);
                    tvHobby.setFocusable(false);
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("user_details");
                    query2.whereEqualTo("user", ParseUser.getCurrentUser());
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            for (ParseObject objective : objects) {
                                profileUser = new ProfileUser();
                                profileUser.setObjectId(objective.getObjectId());
                                ParseQuery<ParseObject> query21 = ParseQuery.getQuery("user_details");
                                query21.getInBackground(profileUser.getObjectId(), new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        object.put("user_hobbies", tvHobby.getText().toString());
                                        object.saveInBackground();
                                    }
                                });
                            }
                        }
                    });
                }


            }
        });
        btnCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvCourse.setFocusableInTouchMode(true);
                    //tvCourse.addTextChangedListener((TextWatcher) getContext());
                    tvCourse.setAdapter(new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.course)));
                    tvCourse.setThreshold(1);
                } else {
                    tvCourse.setFocusableInTouchMode(false);
                    tvCourse.setFocusable(false);
                    ParseQuery<ParseObject> query3 = ParseQuery.getQuery("user_details");
                    query3.whereEqualTo("user", ParseUser.getCurrentUser());
                    query3.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            for (ParseObject objective : objects) {
                                profileUser = new ProfileUser();
                                profileUser.setObjectId(objective.getObjectId());
                                ParseQuery<ParseObject> query31 = ParseQuery.getQuery("user_details");
                                query31.getInBackground(profileUser.getObjectId(), new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        object.put("course", tvCourse.getText().toString());
                                        object.saveInBackground();
                                    }
                                });
                            }
                        }
                    });
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
                    ParseQuery<ParseObject> query4 = ParseQuery.getQuery("user_details");
                    query4.whereEqualTo("user", ParseUser.getCurrentUser());
                    query4.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            for (ParseObject objective : objects) {
                                profileUser = new ProfileUser();
                                profileUser.setObjectId(objective.getObjectId());
                                ParseQuery<ParseObject> query41 = ParseQuery.getQuery("user_details");
                                query41.getInBackground(profileUser.getObjectId(), new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        object.put("user_char", tvCharacter.getText().toString());
                                        object.saveInBackground();
                                    }
                                });
                            }
                        }
                    });
                }


            }
        });

        btnSchool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvSchool.setFocusableInTouchMode(true);
                    //tvSchool.addTextChangedListener((TextWatcher) getContext());
                    tvSchool.setAdapter(new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.school)));
                    tvSchool.setThreshold(1);
                } else {
                    tvSchool.setFocusableInTouchMode(false);
                    tvSchool.setFocusable(false);
                    ParseQuery<ParseObject> query5 = ParseQuery.getQuery("user_details");
                    query5.whereEqualTo("user", ParseUser.getCurrentUser());
                    query5.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            for (ParseObject objective : objects) {
                                profileUser = new ProfileUser();
                                profileUser.setObjectId(objective.getObjectId());
                                ParseQuery<ParseObject> query51 = ParseQuery.getQuery("user_details");
                                query51.getInBackground(profileUser.getObjectId(), new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        object.put("user_school", tvSchool.getText().toString());
                                        object.saveInBackground();
                                    }
                                });
                            }
                        }
                    });
                }


            }
        });

        return view;
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
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == getActivity().RESULT_OK) {
                selectedImageUri = data.getData();
                selectedImageUri = data == null ? null : data.getData();
                if(selectedImageUri != null)
                {
                    String realPath = getPathFromUri(getContext(), selectedImageUri);
                    savePhotoFile(realPath);
                    return;
                }
                imgUpload.setImageURI(selectedImageUri);

            }
        }
    }

    private void savePhotoFile(String realPath) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.dialog_title));
        Bitmap bitmap = Utils.decodeSampledBitmapFromFile(realPath ,250,250);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedImage = Bitmap.createBitmap(bitmap, 0,
                0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        Bitmap thumbnail = Bitmap.createScaledBitmap(rotatedImage, 86, 86, false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] rotatedData = bos.toByteArray();

        bos.reset(); // reset the stream to prepare for the thumbnail
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] thumbnailData = bos.toByteArray();

        try {
            // close the byte array output stream
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = new ParseFile("user_avatar.png", rotatedData);
        thumbnailImg = new ParseFile("thumbnail_avatar.png",thumbnailData);
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("user_details");
        query1.whereEqualTo("user",ParseUser.getCurrentUser());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    profileUser = new ProfileUser();
                    profileUser.setObjectId(object.getObjectId());
                    ParseQuery<ParseObject> query11 = ParseQuery.getQuery("user_details");
                    query11.getInBackground(profileUser.getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            object.put("user_avatar", image);
                            object.put("thumbnail_avatar", thumbnailImg);
                            object.saveInBackground();
                        }
                    });
                }
                dialog.dismiss();
            }
        });

    }

    public void showDatePickerDialog()
    {
        DatePickerDialog.OnDateSetListener callback=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                tvBirthday.setText(
                        (dayOfMonth) + "/" + (monthOfYear+1)+"/"+year);
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=tvBirthday.getText()+"";
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam= Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                getActivity(),
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày sinh");
        pic.show();
    }
    public void onTextChanged(CharSequence arg0, int arg1,
                              int arg2, int arg3) {
    }
    public void afterTextChanged(Editable arg0) {
    }
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
    }
    @SuppressLint("NewApi")
    public String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



}