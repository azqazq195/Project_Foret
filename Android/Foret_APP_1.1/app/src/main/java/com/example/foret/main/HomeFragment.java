package com.example.foret.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foret.R;
import com.example.foret.SplashActivity;
import com.example.foret.login.LoginActivity;
import com.example.foret.login.SessionManager;
import com.example.foret.model.MemberDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String url = "http://54.180.219.200:8085/get/member";

    SessionManager sessionManager;
    AsyncHttpClient client;
    HttpResponse response;
    Context context;

    Toolbar toolbar;

    MemberDTO memberDTO;

    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8;

    public HomeFragment() {
    }

    public HomeFragment(Context context) {
        this.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        client = new AsyncHttpClient();
        response = new HttpResponse();
        sessionManager = new SessionManager(context);
        RequestParams params = new RequestParams();
        params.put("id", sessionManager.getSession());
        client.post(url, params, response);

//        memberDTO = (MemberDTO) getArguments().getSerializable("memberDTO");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = rootView.findViewById(R.id.home_toolbar);

//        textView1 = rootView.findViewById(R.id.textView1);
//        textView2 = rootView.findViewById(R.id.textView2);
//        textView3 = rootView.findViewById(R.id.textView3);
//        textView4 = rootView.findViewById(R.id.textView4);
//        textView5 = rootView.findViewById(R.id.textView5);
//        textView6 = rootView.findViewById(R.id.textView6);
//        textView7 = rootView.findViewById(R.id.textView7);
//        textView8 = rootView.findViewById(R.id.textView8);
//
//        textView1.setText(String.valueOf(memberDTO.getId()));
//        textView2.setText(memberDTO.getName());
//        textView3.setText(memberDTO.getEmail());
//        textView4.setText(memberDTO.getPassword());
//        textView5.setText(memberDTO.getNickname());
//        textView6.setText(memberDTO.getBirth());
//        textView7.setText(memberDTO.getReg_date());
//        textView8.setText(memberDTO.getDeviceToken());

        return rootView;
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e("[HomeFragment]", "onSuccess");
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show();
            Log.e("[HomeFragment]", "onFailure");
        }
    }
}