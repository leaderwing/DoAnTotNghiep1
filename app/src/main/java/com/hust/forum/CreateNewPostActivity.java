package com.hust.forum;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quy2016.doantotnghiep.R;
import com.model.Post_Info;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jsoup.helper.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 4/26/2016.
 */
public class CreateNewPostActivity extends AppCompatActivity {
    Button btnPost , btnCancel , btnUploadFile;
    EditText editTitle , editContent;
    Spinner spinnerCat;
    TextView txtFile;
    String course, category;
    ProgressDialog progress;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_create_post);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Bài viết mới");
        btnPost = (Button) findViewById(R.id.btnPost);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnUploadFile = (Button) findViewById(R.id.btnUploadFile);
        editTitle = (EditText) findViewById(R.id.txtDescribe);
        editContent = (EditText) findViewById(R.id.txtContent);
        txtFile  = (TextView) findViewById(R.id.txtfile);
        spinnerCat = (Spinner) findViewById(R.id.spinnerCat);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext() ,R.array.category, R.layout.spinner_item );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(spinnerAdapter);
        Intent i = getIntent();
        course = i.getStringExtra("khoa_hoc");
        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
                query.whereEqualTo("user" , ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (ParseObject object : objects) {

                        }
                    }
                });
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void SaveData()
    {
        progress = ProgressDialog.show(this, "",
                "LƯU DỮ LIỆU...", true);
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
        final Post_Info post_info = new Post_Info();
        post_info.setUser(ParseUser.getCurrentUser());
        post_info.setCourse(course);
        post_info.setTitle(editTitle.getText().toString());
        post_info.setDescribe(editContent.getText().toString());
        post_info.setCategory(spinnerCat.getSelectedItem().toString());
        post_info.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    if(post_info.getCategory() == null)
                    {
                        Toast.makeText(getApplicationContext() ,"Bạn chưa chọn danh muc bài viêt",Toast.LENGTH_SHORT).show();
                    }
                    else if(StringUtil.isBlank(post_info.getTitle().trim()))
                    {
                        Toast.makeText(getApplicationContext(),"Bạn chưa đặt tiêu đề cho bài viết",Toast.LENGTH_SHORT).show();
                    }
                    else if(StringUtil.isBlank(post_info.getDescribe().trim()))
                    {
                        Toast.makeText(getApplicationContext(),"Bạn chưa viết nội dung cho bài viết",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        setResult(RESULT_OK);
                        finish();
                        progress.dismiss();
                    }
                }
                else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
