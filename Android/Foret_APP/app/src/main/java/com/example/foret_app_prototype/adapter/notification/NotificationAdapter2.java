package com.example.foret_app_prototype.adapter.notification;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.helper.CalendarHelper;
import com.example.foret_app_prototype.model.ModelNotify;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter2 extends ArrayAdapter<ModelNotify> {
    Activity activity;
    int resource;

    public NotificationAdapter2(@NonNull Context context, int resource, @NonNull List<ModelNotify> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }
        ModelNotify item = getItem(position);

        if (item != null) {
            ImageView imageView = convertView.findViewById(R.id.imageViewPhoto);
            TextView textViewtype = convertView.findViewById(R.id.textViewFromWhere);
            TextView textViewContent = convertView.findViewById(R.id.textViewContent);
            TextView textViewTime = convertView.findViewById(R.id.textViewTime);

            //이미지셋팅
            Glide.with(convertView).load(item.getIamge()).into(imageView);
            textViewContent.setText(item.getMessage());
            textViewTime.setText(CalendarHelper.getInstance().getRelativeHourAndDaysAndWeek(item.getTime()));
            textViewtype.setText(item.getType());


        }


        return convertView;

    }
}
