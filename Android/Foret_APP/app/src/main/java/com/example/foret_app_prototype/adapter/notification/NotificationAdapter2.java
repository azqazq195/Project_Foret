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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.ChatFragment;
import com.example.foret_app_prototype.activity.free.FreeFragment;
import com.example.foret_app_prototype.activity.home.HomeFragment;
import com.example.foret_app_prototype.helper.CalendarHelper;
import com.example.foret_app_prototype.model.ModelNotify;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter2 extends ArrayAdapter<ModelNotify> {

    final int GROUP_NEW_ITEM = 0;            //내 포레 새글   icon_foet
    final int MESSAGE_NEW_ITEM = 1;          //새로운 메세지  icon_chat
    final int PUBLIC_NOTICE_NEW_ITEM = 2;    //마스터 공지   icon_notice
    final int ANONYMOUS_BOARD_NEW_ITEM = 3;  //내가 쓴 글의 댓글 icon_reply
    final int REPLIED_NEW_ITEM = 4;          //내가 댓글의 대댓글
    final int NEW_ITEM1 = 5;                 //임시 타입 1
    final int NEW_ITEM2 = 6;                 //임시 타입 2
    final int NEW_ITEM3 = 7;                 //임시 타입 3
    final int NEW_ITEM4 = 8;                 //임시 타입 4

    Activity activity;
    int resource;
    Fragment fragment;

    public NotificationAdapter2(@NonNull Context context, int resource, @NonNull List<ModelNotify> objects,Fragment fragment) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
        this.fragment = fragment;
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
            ConstraintLayout layout = convertView.findViewById(R.id.chat_notify_item);

            if(item.isSeen()){
                layout.setBackgroundColor(getContext().getResources().getColor(R.color.unread));
            }else{
                layout.setBackgroundColor(getContext().getResources().getColor(R.color.read));
            }

            if(item.getType().equals("GROUP_NEW_ITEM")){
                Glide.with(convertView).load(R.drawable.icon_foret).into(imageView);
                textViewtype.setText("내 포레 새글 알림");
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.getFragmentManager().beginTransaction().replace(R.id.containerLayout, new HomeFragment()).commit();
                    }
                });

            }else if(item.getType().equals("MESSAGE_NEW_ITEM")){
                Glide.with(convertView).load(R.drawable.icon_chat).into(imageView);
                textViewtype.setText("내 채팅에 새글 알림");
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.getFragmentManager().beginTransaction().replace(R.id.containerLayout, new ChatFragment()).commit();
                    }
                });

            }else if(item.getType().equals("PUBLIC_NOTICE_NEW_ITEM")){
                Glide.with(convertView).load(R.drawable.icon_board).into(imageView);
                textViewtype.setText("새 공지사항 알림");
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.getFragmentManager().beginTransaction().replace(R.id.containerLayout, new HomeFragment()).commit();
                    }
                });
            }else if(item.getType().equals("ANONYMOUS_BOARD_NEW_ITEM")){
                Glide.with(convertView).load(R.drawable.icon_reply).into(imageView);
                textViewtype.setText("내 게시판에 댓글알림");
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.getFragmentManager().beginTransaction().replace(R.id.containerLayout, new FreeFragment()).commit();
                    }
                });
            }else{
                Glide.with(convertView).load(R.drawable.foreticon).into(imageView);
                textViewtype.setText("새 글 알림");
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //fragment.getFragmentManager().beginTransaction().replace(R.id.containerLayout, new ChatFragment()).commit();
                    }
                });
            }
            textViewContent.setText(item.getContent());
            textViewTime.setText(CalendarHelper.getInstance().getRelativeHourAndDaysAndWeek(item.getTime()));

        }
        return convertView;

    }


}
