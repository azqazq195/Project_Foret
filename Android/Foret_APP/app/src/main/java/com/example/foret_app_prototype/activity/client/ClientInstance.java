package com.example.foret_app_prototype.activity.client;

import android.app.Activity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ClientInstance extends AsyncHttpResponseHandler {

    private static ClientInstance instance = null;

    private Activity activity;

    public ClientInstance() {
    }

    public static ClientInstance getInstance() {
        if (instance == null) {
            instance = new ClientInstance();
        }
        return instance;
    }

    public void injection(Activity activity){
        this.activity = activity;
    }



    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String result = new String(responseBody);

        try {
            JSONObject json = new JSONObject(result);

            String rt = json.getString("rt");

            if(rt.equals("OK")){




            }else{
                Toast.makeText(activity,"리절트 fail",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Toast.makeText(activity, "통신실패, 원인 : "+error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
