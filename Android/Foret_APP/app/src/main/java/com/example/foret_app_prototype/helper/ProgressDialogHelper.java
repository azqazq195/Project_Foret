package com.example.foret_app_prototype.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogHelper {

    ProgressDialog progressDialog;

    private static ProgressDialogHelper instance = null;

    public static ProgressDialogHelper getInstance() {
        if (instance == null) instance = new ProgressDialogHelper();
        return instance;
    }
    private ProgressDialogHelper(){}

    public void getProgressbar(Context context, String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void removeProgressbar(){
         progressDialog.dismiss();
    }


}
