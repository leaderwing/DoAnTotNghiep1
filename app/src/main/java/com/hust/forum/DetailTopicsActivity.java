package com.hust.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.Utils.DividerItemDecoration;
import com.Utils.PostInfoAdapter;
import com.example.quy2016.doantotnghiep.R;
import com.model.Post_Info;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/20/2016.
 */
public class DetailTopicsActivity extends AppCompatActivity{
    private List<Post_Info> post_infos = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostInfoAdapter postInfoAdapter;
    public int newInt;
    private String course ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_list);
        Intent i = getIntent();
        if (savedInstanceState == null) {
            Bundle extras = i.getExtras();
            if(extras == null) {
                newInt= -1;
            } else {
                newInt= extras.getInt("position");
            }
        } else {
            newInt= (Integer) savedInstanceState.getSerializable("position");
        }
        switch (newInt)
        {
            case 0:
                course = "k56";
                break;
            case 1:
                course = "k57";
                break;
            case 2:
                course = "k58";
                break;
            case  3:
                course = "k59";
                break;
            case 4:
                course = "k60";
                break;
            default:
                break;
        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        postInfoAdapter = new PostInfoAdapter(post_infos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this , LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(postInfoAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Post_Info post_info = post_infos.get(position);
                Toast.makeText(getApplicationContext(),post_info.getTitle().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareDataPost();
    }
    private void prepareDataPost()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
        query.whereEqualTo("Course",course);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject post:objects)
                {
                     Post_Info  post_info = new Post_Info();
                    post_info.setTitle(post.getString("Title"));
                    post_info.setAuthorName(post.getString("AuthorName"));
                    post_info.setDate(post.getUpdatedAt());
                    post_info.setNumberView(post.getInt("Post_Num_View"));
                    post_info.setNumberPost(post.getInt("Post_Num_Comment"));
                    post_infos.add(post_info);

                }
            }
        });
        postInfoAdapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private DetailTopicsActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DetailTopicsActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.create_new:
                return true;
            case R.id.filter:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
