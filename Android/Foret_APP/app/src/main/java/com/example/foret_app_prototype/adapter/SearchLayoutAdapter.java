package com.example.foret_app_prototype.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foret_app_prototype.model.Test;

public class SearchLayoutAdapter extends ArrayAdapter<Test> {

    Activity activity;
    int resource;

    public SearchLayoutAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        activity = (Activity)context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Test item = getItem(position);

        if(item != null) {

        }

        return convertView;
    }
}
