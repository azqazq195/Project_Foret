package com.example.foret_app_prototype.adapter.search;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.ForetDTO;
import com.example.foret_app_prototype.model.MemberDTO;

import java.util.Arrays;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<ForetDTO> {

    Activity activity;
    int resource;
    MemberDTO memberDTO;

    public SearchAdapter(@NonNull Context context, int resource, @NonNull List<ForetDTO> objects,MemberDTO memberDTO) {
        super(context, resource, objects);
        activity = (Activity)context;
        this.resource = resource;
        this.memberDTO = memberDTO;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }
        ForetDTO foret = getItem(position);
        if(foret != null) {
            String [] tag_name = foret.getForet_tag().toArray(new String[foret.getForet_tag().size()]);
            String [] si = foret.getForet_region_si().toArray(new String[foret.getForet_region_si().size()]);
            String [] gu = foret.getForet_region_gu().toArray(new String[foret.getForet_region_gu().size()]);
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView textView1 = convertView.findViewById(R.id.textView1);
            TextView textView2 = convertView.findViewById(R.id.textView2);
            TextView textView3 = convertView.findViewById(R.id.textView3);
            TextView textView4 = convertView.findViewById(R.id.textView4);
            TextView textView5 = convertView.findViewById(R.id.textView5);

            textView1.setText(foret.getForet_name());
            textView2.setText(Arrays.toString(tag_name));
            textView3.setText(foret.getIntroduce());
            textView4.setText(foret.getReg_date());
            textView5.setText(Arrays.toString(si)+Arrays.toString(gu));


            Glide.with(convertView).load(foret.getForet_photo()) .fallback(R.drawable.icon2)
                    .into(imageView);


            String result = foret.getForet_photo().substring(foret.getForet_photo().lastIndexOf("/")+1);
            Log.e("[test]", "셀렉트 섭스트링값?" +result);

            if(result.equals("null")){
                Glide.with(convertView).load(R.drawable.icon_foret)
                        .into(imageView);
            }else {

                Glide.with(convertView).load(foret.getForet_photo())
                        .placeholder(R.drawable.icon4)
                        .fallback(R.drawable.icon_foret)
                        .into(imageView);
            }

        }
        return convertView;
    }
}
