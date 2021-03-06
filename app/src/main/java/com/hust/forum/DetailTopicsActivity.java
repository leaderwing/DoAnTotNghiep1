package com.hust.forum;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.utils.DividerItemDecoration;
import com.utils.PostInfoAdapter;
import com.example.quy2016.doantotnghiep.R;
import com.model.Post_Info;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/20/2016.
 */
public class DetailTopicsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ArrayList<Post_Info> post_infos = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostInfoAdapter postInfoAdapter;
    public int newInt;
    private String course;
    public static final int REQUEST_CODE = 111;
    ProgressDialog progress;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_post);
        Intent i = getIntent();
        if (savedInstanceState == null) {
            Bundle extras = i.getExtras();
            if (extras == null) {
                newInt = -1;
            } else {
                newInt = extras.getInt("position");
            }
        } else {
            newInt = (Integer) savedInstanceState.getSerializable("position");
        }
        switch (newInt) {
            case 0:
                course = "k56";
                break;
            case 1:
                course = "k57";
                break;
            case 2:
                course = "k58";
                break;
            case 3:
                course = "k59";
                break;
            case 4:
                course = "k60";
                break;
            case 5:
                course = "k";
            default:
                break;
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        postInfoAdapter = new PostInfoAdapter(this,post_infos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(postInfoAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(getApplicationContext(), MainPostActivity.class);
                String objectId = post_infos.get(position).getObjectId();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
                query.whereEqualTo("objectId",objectId);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.increment("Post_Num_View");
                    }
                });
                //Toast.makeText(getApplicationContext(),objectId,Toast.LENGTH_SHORT).show();
                intent.putExtra("sendData", objectId);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareDataPost();
    }


    private void prepareDataPost() {
        progress = ProgressDialog.show(this, "",
                " LẤY DỮ LIỆU...", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post_Info");
        query.whereEqualTo("Course", course);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                progress.dismiss();
                if (objects != null && objects.size() > 0) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject po = objects.get(i);
                        Post_Info post_info = new Post_Info();
                        post_info.setUser(po.getParseUser("user"));
                        post_info.setObjectId(po.getObjectId());
                        post_info.setDate(po.getCreatedAt());
                        post_info.setDescribe(po.getString("Describe"));
                        post_info.setTitle(po.getString("Title"));
                        post_info.setNumberPost(po.getInt("Post_Num_Comment"));
                        post_info.setNumberView(po.getInt("Post_Num_View"));
                        post_infos.add(post_info);
                        postInfoAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext() , "Chưa có bài đăng nào" ,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

       // postInfoAdapter.getFilter().filter(newText);
        final List<Post_Info> filteredModelList = filter(post_infos, newText);
        postInfoAdapter.setFilter(filteredModelList);

        return true;
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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
        inflater.inflate(R.menu.menu_post, menu);
        final MenuItem item = menu.findItem(R.id.filter);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        postInfoAdapter.setFilter(post_infos);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new:
                CreateNewPost();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CreateNewPost() {
        Intent data = new Intent(getApplicationContext(), CreateNewPostActivity.class);
        data.putExtra("khoa_hoc", course);
        startActivityForResult(data, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                clearData();
                prepareDataPost();

            }
        }
    }
    public void clearData() {
        int size = this.post_infos.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.post_infos.remove(0);
            }

            postInfoAdapter.notifyDataSetChanged();
        }
    }
    private List<Post_Info> filter(List<Post_Info> posts, String query) {
        query = query.toLowerCase();

        final List<Post_Info> filteredList = new ArrayList<>();
        for (Post_Info post : posts) {
            final String text = post.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredList.add(post);
            }
        }
        return filteredList;
    }

}
