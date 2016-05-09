package com.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.quy2016.doantotnghiep.ProfileFragment;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 4/28/2016.
 */
public class Utils {

    public  static ParseUser user;

    public  static void UserStatus(boolean online)
    {
        user.put("online" , online);
        user.saveEventually();
    }
    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static ParseObject uploadAudioToParse(File audioFile, ParseObject po, String columnName) {

        if (audioFile != null) {
            Log.d("EB", "audioFile is not NULL: " + audioFile.toString());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(audioFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int read;
            byte[] buff = new byte[1024];
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] audioBytes = out.toByteArray();

            // Create the ParseFile
            ParseFile file = new ParseFile(audioFile.getName(), audioBytes);
            po.put(columnName, file);

            // Upload the file into Parse Cloud
            file.saveInBackground();
            po.saveInBackground();
        }
        return po;
    }


}
