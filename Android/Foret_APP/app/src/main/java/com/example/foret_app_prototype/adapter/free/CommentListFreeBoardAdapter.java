package com.example.foret_app_prototype.adapter.free;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.ForetBoardComment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CommentListFreeBoardAdapter extends RecyclerView.Adapter<CommentListFreeBoardAdapter.CommentView> {

    List<ForetBoardComment> list;
    Activity activity;
    int memberID;
    CommentClickListener commentClickListener = null;
    InputMethodManager inputMethodManager;
    AsyncHttpClient client;
    ModifyCommentResponse modifyResponse;
    DeleteCommentResponse deleteResponse;

    public CommentListFreeBoardAdapter(List<ForetBoardComment> list, Context context, int memberID) {
        this.list = list;
        this.activity = (Activity)context;
        this.memberID = memberID;
        client = new AsyncHttpClient();
        modifyResponse = new ModifyCommentResponse();
        deleteResponse = new DeleteCommentResponse();
    }

    public void setCommentClickListener(CommentClickListener commentClickListener) {
        this.commentClickListener = commentClickListener;
    }

    @NonNull
    @Override
    public CommentView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentView(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentView holder, int position) {
        ForetBoardComment comment = list.get(position);
        holder.textView1.setText(comment.getWriter());
        if (String.valueOf(memberID).equals(comment.getWriter())) {
            holder.layout.setVisibility(View.VISIBLE);
            holder.imageView6.setVisibility(View.INVISIBLE);
            holder.button1.setVisibility(View.INVISIBLE);
            holder.textView1.setText("내가 작성한 댓글");
        } else {
            holder.layout.setVisibility(View.GONE);
        }
        holder.textView2.setText(comment.getContent());
        holder.textView3.setText(comment.getReg_date());
        holder.button1.setOnClickListener(new View.OnClickListener() { //답글쓰기
            @Override
            public void onClick(View v) {
                String target = comment.getWriter();
                commentClickListener.onReplyButtonClick(v, target, true);
            }
        });
        holder.button2.setOnClickListener(new View.OnClickListener() { //수정하기 //엔터 누를시 수정한다
            @Override
            public void onClick(View v) {
                String content = comment.getContent();
                holder.editText.setText(content);
                holder.textView2.setVisibility(View.GONE);
                holder.editText.setVisibility(View.VISIBLE);
                holder.button4.setVisibility(View.VISIBLE);
                holder.editText.requestFocus();
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                boolean modify = true;
                commentClickListener.onModifyButtonClick(v, modify);
            }
        });
        holder.button3.setOnClickListener(new View.OnClickListener() { //삭제하기
            @Override
            public void onClick(View v) {
                showDialog(holder, list, position, comment.getId());
            }
        });
        holder.button4.setOnClickListener(new View.OnClickListener() { //수정취소
            @Override
            public void onClick(View v) {
                holder.editText.setVisibility(View.GONE);
                holder.button4.setVisibility(View.GONE);
                holder.textView2.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(holder.editText.getWindowToken(), 0);
                boolean modify = false;
                commentClickListener.onModifyButtonClick(v, modify);
            }
        });
        holder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                RequestParams params = new RequestParams();
                params.put("comment_id", comment.getId());
                params.put("content", holder.editText.getText().toString());
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND : //수정하기
                        client.post("http://34.72.240.24:8085/foret/comment/comment_modify.do", params, modifyResponse);
                        holder.textView2.setText(holder.editText.getText().toString().trim());
                        holder.textView2.setVisibility(View.VISIBLE);
                        holder.editText.setVisibility(View.GONE);
                        holder.button4.setVisibility(View.GONE);
                        inputMethodManager.hideSoftInputFromWindow(holder.editText.getWindowToken(), 0);
                        boolean modify = false;
                        commentClickListener.onModifyButtonClick(v, modify);
                        break;
                    default :
                        Toast.makeText(activity, "엔터쳤을때", Toast.LENGTH_SHORT).show();
                        client.post("http://34.72.240.24:8085/foret/comment/comment_modify.do", params, modifyResponse);
                        break;
                }
                return false;
            }
        });
    }

    private void showDialog(CommentView holder, List<ForetBoardComment> list, int positon, int commentID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("삭제 하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    list.remove(positon);
                    notifyItemRemoved(positon);
                    if(list.size() > 0) {
                    notifyItemRangeChanged(positon, list.size() - 1);
                    }
                    commentClickListener.onDeleteButtonClick(true);
                    RequestParams params = new RequestParams();
                    params.put("comment_id", commentID);
                    client.post("http://34.72.240.24:8085/foret/comment/comment_delete.do", params, deleteResponse);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentView extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3, button1, button2, button3, button4;
        LinearLayout layout;
        EditText editText;
        FrameLayout commentLayout;
        ImageView imageView6;

        public CommentView(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            editText = itemView.findViewById(R.id.editText); //수정하기
            button1 = itemView.findViewById(R.id.button1);
            button2 = itemView.findViewById(R.id.button2);
            button3 = itemView.findViewById(R.id.button3);
            button4 = itemView.findViewById(R.id.button4); //수정취소버튼
            layout = itemView.findViewById(R.id.layout);
            commentLayout = itemView.findViewById(R.id.commentLayout);
            layout.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            button4.setVisibility(View.GONE);
            imageView6 = itemView.findViewById(R.id.imageView6);

        }
    }

    public interface CommentClickListener {
        public void onReplyButtonClick(View v, String target, boolean reply);
        public void onModifyButtonClick(View v, boolean modify);
        public void onDeleteButtonClick(boolean delete);
    }

    class ModifyCommentResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("commentRT").equals("OK")) {
                    Toast.makeText(activity, "댓글 수정 성공", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "수정할 수 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    class DeleteCommentResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("commentRT").equals("OK")) {
                    Toast.makeText(activity, "댓글 삭제 성공", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "삭제할 수 없습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
