package com.example.foret_app_prototype.response;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CUDResponse extends AsyncHttpResponseHandler {

    Activity activity;
    String responce="";

    public CUDResponse(Context context) {
        this.activity = (Activity)context;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        activity.finish();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Toast.makeText(activity, responce+" 실패", Toast.LENGTH_SHORT).show();
    }
}
