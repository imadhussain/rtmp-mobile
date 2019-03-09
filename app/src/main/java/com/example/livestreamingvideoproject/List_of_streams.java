package com.example.livestreamingvideoproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class List_of_streams extends AppCompatActivity {
    String url = "http://54.173.3.60:3000/streams";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    FloatingActionButton add_user_btn;
    int GetItemPosition ;
    View ChildView ;
    public static String Stream_url;
    List<User_subjects> user_subjectsList;
    ArrayList<String> All_RTMP_links;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_streams);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_all_articals);
        progressBar = (ProgressBar)findViewById(R.id.all_articals_frg_progressBar);
        add_user_btn = (FloatingActionButton)findViewById(R.id.add_user_btn);
        add_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Add_User_act.class));
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(getApplicationContext(), Live_Steam_player.class);
                    Stream_url = All_RTMP_links.get(GetItemPosition);
                    intent.putExtra("id",Stream_url);
//                    Toast.makeText(List_of_streams.this, id, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stream_method();
    }

    private void stream_method(){
        user_subjectsList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        All_RTMP_links = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            JSONArray jsonArray = response.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                User_subjects user_subjects = new User_subjects();
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    user_subjects.set_id(jsonObject.getString("_id"));
                                    user_subjects.setName(jsonObject.getString("name"));
                                    user_subjects.setRtmp(jsonObject.getString("rtmp"));
                                    All_RTMP_links.add(jsonObject.getString("rtmp"));
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }
                                user_subjectsList.add(user_subjects);
                            }
                            recyclerViewadapter = new Recycler_view_for_stream_list(user_subjectsList, getApplicationContext());

                            recyclerView.setAdapter(recyclerViewadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
