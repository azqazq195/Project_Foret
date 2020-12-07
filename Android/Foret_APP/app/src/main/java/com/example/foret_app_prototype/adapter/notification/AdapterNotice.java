package com.example.foret_app_prototype.adapter.notification;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.ModelNotice;

import java.util.List;

public class AdapterNotice extends ArrayAdapter<ModelNotice> {

    Activity activity;
    int resource;

    public AdapterNotice(@NonNull Context context, int resource, @NonNull List<ModelNotice> objects) {
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

        ModelNotice item = getItem(position);

        if(item != null){
            TextView title = convertView.findViewById(R.id.textViewTitle);
            TextView writer = convertView.findViewById(R.id.textViewWriter);
            TextView content = convertView.findViewById(R.id.textViewContent);
            TextView time = convertView.findViewById(R.id.textViewTime);
            LinearLayout linearLayout = convertView.findViewById(R.id.notice_row);

            content.setText(item.getContent());
            writer.setText(item.getWriter());
            title.setText(item.getTitle());
            time.setText(item.getTime());

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,item.getTitle()+" 클릭 됨",Toast.LENGTH_SHORT).show();
                }
            });

        }
        return convertView;

    }
}
