package com.hust.forum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.utils.UserCommentAdapter;
import com.example.quy2016.doantotnghiep.R;
import com.model.Comments;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/27/2016.
 */
public class MainPostActivity extends AppCompatActivity {
    TextView txtname ,txtCreated,txtSchool, txtTitle ,txtContent;
    Button btnAddCmt;
    EditText addComment;
    private List<Comments> commentsList = new ArrayList<>();
    public String id,school;
    private RecyclerView recyclerView;
    private UserCommentAdapter commentAdapter;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_fragment);
        Intent getIntent = getIntent();
        id = getIntent.getStringExtra("sendData");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comment);
        txtname = (TextView) findViewById(R.id.postUser);
        txtCreated = (TextView) findViewById(R.id.createdDatePost);
        txtSchool = (TextView) findViewById(R.id.schoolPost);
        txtTitle = (TextView) findViewById(R.id.titlePost);
        txtContent = (TextView) findViewById(R.id.contentPost);
        addComment = (EditText) findViewById(R.id.add_cmt);
        btnAddCmt = (Button) findViewById(R.id.btSend_cmt);
        final View view = (View)findViewById(R.id.content_fragment);
        addComment.setVisibility(View.INVISIBLE);
        btnAddCmt.setVisibility(View.INVISIBLE);
        commentAdapter = new UserCommentAdapter(commentsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(commentAdapter);

        LoadMainPost();
        LoadComments();
        final FloatingActionButton create_cmt = (FloatingActionButton) findViewById(R.id.create_cmt);
        create_cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_cmt.setVisibility(View.INVISIBLE);
            addComment.setVisibility(View.VISIBLE);
               addComment.setOnTouchListener(new View.OnTouchListener() {
                   @Override
                   public boolean onTouch(View v, MotionEvent event) {
                       view.setVisibility(View.GONE);
                       return false;
                   }
               });
            view.setVisibility(View.VISIBLE);
                btnAddCmt.setVisibility(View.VISIBLE);
                btnAddCmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
                        query.whereEqualTo("objectId", id);
                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if(addComment.getText().toString().trim().length() == 0)
                                    return;
                                object.increment("Post_Num_Comment");
                                final ParseObject newCmt =  ParseObject.create("User_comments");
                                newCmt.put("user", ParseUser.getCurrentUser());
                                newCmt.put("Comment", addComment.getText().toString().trim());
                                newCmt.put("post",object);
                                newCmt.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        addComment.setVisibility(View.INVISIBLE);
                                        btnAddCmt.setVisibility(View.INVISIBLE);
                                        create_cmt.setVisibility(View.VISIBLE);
                                       // commentAdapter.insert(commentAdapter.getItemCount(),commentsList.get(commentAdapter.getItemCount()));
                                        clearData();
                                        LoadComments();
                                    }
                                });
                            }
                        });

                    }
                });
                LoadComments();
            }
        });

    }
    private void LoadMainPost()
    {
        progress = ProgressDialog.show(this, "",
                " LOADING...", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
        query.whereEqualTo("objectId",id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null) {
                    for (ParseObject object : objects) {
                        try {
                            txtname.setText(object.getParseUser("user").fetchIfNeeded().getString("name"));
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        txtTitle.setText(object.getString("Title"));
                        txtContent.setText(object.getString("Describe"));

                    }

                }
            }
        });
        progress.dismiss();
    }
    private void LoadComments()
    {   ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
        query.whereEqualTo("objectId",id);
        ParseQuery<ParseObject> newquerry = ParseQuery.getQuery("User_comments");
        newquerry.whereMatchesQuery("post",query);
        newquerry.addDescendingOrder("createdAt");
        newquerry.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() != 0) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject po = objects.get(i);
                        Comments comments = new Comments();
                        comments.setUser(po.getParseUser("user"));
                        comments.setComment(po.getString("Comment"));
                        comments.setPost(po.getParseObject("post"));
                        commentsList.add(comments);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
                else
                {

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
        }
        return super.onOptionsItemSelected(item);
    }
    public void clearData() {
        int size = this.commentsList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.commentsList.remove(0);
            }

            commentAdapter.notifyItemRangeRemoved(0,size);
        }
    }
}
