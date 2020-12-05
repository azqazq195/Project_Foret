package com.example.foret_app_prototype.adapter.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.ModelNotify;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    //알림 타입
    private static final int GROUP_NEW_ITEM = 0;            //내 포레 새글
    private static final int MESSAGE_NEW_ITEM = 1;          //새로운 메세지
    private static final int PUBLIC_NOTICE_NEW_ITEM = 2;    //마스터 공지
    private static final int ANONYMOUS_BOARD_NEW_ITEM = 3;  //내가 쓴 글의 댓글
    private static final int REPLIED_NEW_ITEM = 4;          //내가 댓글의 대댓글
    private static final int NEW_ITEM1 = 5;                 //임시 타입 1
    private static final int NEW_ITEM2 = 6;                 //임시 타입 2
    private static final int NEW_ITEM3 = 7;                 //임시 타입 3
    private static final int NEW_ITEM4 = 8;                 //임시 타입 4

    Context context;
    private List<ModelNotify> list; //아이템 리스트 데이터 셋팅할꺼 임시

    public NotificationAdapter(Context context, List<ModelNotify> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_notification, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        ModelNotify model = list.get(position);
        String iamge = model.getIamge();
        String message = model.getMessage();
        int typeis = getItemViewType(position);
        String time = model.getTime();

        holder.imageViewPhoto.setImageResource(Integer.parseInt(iamge));
        holder.textViewContent.setText(message);
        holder.textViewFromWhere.setText(model.getType());
        holder.textViewTime.setText(time);
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면 이동
            }
        });

        switch (typeis) {
            case 0:
                //내 포레 새글

                break;
            case 1:
                //새로운 메세지

                break;
            case 2:
                //마스터 공지

                break;
            case 3:
                //내가 쓴 글의 댓글

                break;
            case 4:
                //내가 댓글의 대댓글
                break;
            case 5:
                //임시 타입 1

                break;
            case 6:
                //임시 타입 2

                break;
            case 7:
                //임시 타입 3

                break;
            case 8:
                //임시 타입 4

                break;

            case -1:
                //-1 디폴트값

                break;

        }


        //데이터 셋팅
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewPhoto;
        TextView textViewFromWhere, textViewTime, textViewContent;
        LinearLayout messageLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewFromWhere = itemView.findViewById(R.id.textViewFromWhere);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewContent = itemView.findViewById(R.id.textViewContent);

            messageLayout = itemView.findViewById(R.id.chat_notify_item);

        }
    }

    @Override
    public int getItemViewType(int position) {
//        int GROUP_NEW_ITEM = 0;            //내 포레 새글
//        int MESSAGE_NEW_ITEM = 1;          //새로운 메세지
//        int PUBLIC_NOTICE_NEW_ITEM = 2;    //마스터 공지
//        int ANONYMOUS_BOARD_NEW_ITEM = 3;  //내가 쓴 글의 댓글
//        int REPLIED_NEW_ITEM = 4;          //내가 댓글의 대댓글
//        int NEW_ITEM1 = 5;                 //임시 타입 1
//        int NEW_ITEM2 = 6;                 //임시 타입 2
//        int NEW_ITEM3 = 7;                 //임시 타입 3
//        int NEW_ITEM4 = 8;                 //임시 타입 4
        String type = list.get(position).getType();
        int getitem = -1;
        switch (type) {
            case "GROUP_NEW_ITEM":
                getitem = GROUP_NEW_ITEM;
                break;

            case "MESSAGE_NEW_ITEM":
                getitem = MESSAGE_NEW_ITEM;
                break;

            case "PUBLIC_NOTICE_NEW_ITEM":
                getitem = PUBLIC_NOTICE_NEW_ITEM;
                break;

            case "ANONYMOUS_BOARD_NEW_ITEM":
                getitem = ANONYMOUS_BOARD_NEW_ITEM;
                break;

            case "REPLIED_NEW_ITEM":
                getitem = REPLIED_NEW_ITEM;
                break;

            case "NEW_ITEM1":
                getitem = NEW_ITEM1;
                break;

            case "NEW_ITEM2":
                getitem = NEW_ITEM2;
                break;

            case "NEW_ITEM3":
                getitem = NEW_ITEM3;
                break;
            case "NEW_ITEM4":
                getitem = NEW_ITEM4;
                break;
        }
        return getitem;
    }

    public void addNewItem(ModelNotify notificationList, Context context) {
        list.add(notificationList);
        this.context = context;
        notifyItemInserted(list.size() - 1); //갱신
    }
}
