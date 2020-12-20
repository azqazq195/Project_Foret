package com.example.foret.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogHelper {

    ProgressDialog progressDialog;
    Context context;
    private static ProgressDialogHelper instance = null;

    public static ProgressDialogHelper getInstance() {
        if (instance == null) {
            instance = new ProgressDialogHelper();
        }
        return instance;
    }
    private ProgressDialogHelper(){}

    public void getProgressbar(Context context, String message){
        this.context = context;
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void removeProgressbar(){
         progressDialog.dismiss();
    }


}
