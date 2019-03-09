package com.example.livestreamingvideoproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class Add_User_act extends AppCompatActivity {
    String user_url="http://54.173.3.60:3000/user/register";
    ProgressDialog progressDialog;
    EditText txt_user_name, txt_url;
    Button btn_submit;
    String username_Holder,urlHolder;
    Boolean CheckEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_act);
        txt_user_name = (EditText) findViewById(R.id.txt_user_name);
        txt_url = (EditText) findViewById(R.id.txt_url);
        progressDialog = new ProgressDialog(Add_User_act.this);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_user_method();
            }
        });
    }
    private void add_user_method() {
        CheckEditTextIsEmptyOrNot();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", username_Holder);
            jsonObject.put("rtmp", urlHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                user_url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        saveData(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void saveData(String response) {
        try {
            JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
            boolean condition = json.getBoolean("success");
            if (condition == true){
                JSONObject json2 = json.getJSONObject("result");
                String test = (String) json2.get("name");
                Toast.makeText(this, "Add Stream Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                txt_user_name.setText("");
                txt_url.setText("");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void CheckEditTextIsEmptyOrNot() {
        username_Holder = txt_user_name.getText().toString();
        urlHolder = txt_url.getText().toString();

        if (TextUtils.isEmpty(username_Holder) || TextUtils.isEmpty(urlHolder)) {
            CheckEditText = false;
        } else {
            CheckEditText = true;
        }
    }
}
