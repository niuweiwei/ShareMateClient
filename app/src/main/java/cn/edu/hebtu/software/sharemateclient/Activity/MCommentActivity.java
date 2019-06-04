package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.MCommentListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.CommentAndReply;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MCommentActivity extends AppCompatActivity {

    private MCommentListAdapter adapter;
    private String serverPath;
    private int userId = 5;//为当前用户的id
    private List<CommentAndReply> commentAndReplyList =  new ArrayList<>();
    private ListView listView;
    private Gson gson;
    private MyHandler myHandler = new MyHandler();

    private RelativeLayout relativeLayout;//整个的MCommentActivity的父布局
    private PopupWindow window;
    private View view;//代表popupWindow所在的view
    private InputMethodManager methodManager;//管理软键盘
    private RelativeLayout replyLayout;
    private Button sendReply;
    private EditText replyText;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcomment);

        relativeLayout = findViewById(R.id.root);
        listView = findViewById(R.id.lv_comment);
        sendReply = findViewById(R.id.btn_send);
        replyText = findViewById(R.id.et_reply);
        btnBack = findViewById(R.id.btn_back);
        replyLayout = findViewById(R.id.rl_reply);

        gson = new GsonBuilder().serializeNulls().create();
        serverPath = getResources().getString(R.string.server_path);

        //为返回按钮绑定事件监听器
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //开启系统中软键盘的输入服务
        methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //开启异步任务获取当前用户收到的评论及回复
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(serverPath+"CommentAndReply/getCAndRList/"+userId)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String listJsonStr = reader.readLine();
                Log.e("MCommentActivity",listJsonStr);
                Type listType = new TypeToken<List<CommentAndReply>>(){}.getType();
                commentAndReplyList = gson.fromJson(listJsonStr,listType);
                Message message = new Message();
                message.what = 0;
                myHandler.sendMessage(message);
            }
        });

    }

        //显示 popupwindow
        private void showPopupWindow(RelativeLayout root, final int position){
            //获取到要显示的视图
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.popupwindow_mcomments_layout,null);
            }
            //通过view获取到各个按钮 并绑定事件监听器
            final Button like = view.findViewById(R.id.btn_like);
            //根据该用户是否对该评论赞而现实button 中的数据
            if(commentAndReplyList.get(position).isLike()){
                //表示该用户对该评论点赞过
                like.setText("取消赞");
            }else
                like.setText("赞");

            final Button reply = view.findViewById(R.id.btn_reply);
            Button detail = view.findViewById(R.id.btn_detail);
            Button delete = view.findViewById(R.id.btn_delete);
            Button cancel = view.findViewById(R.id.btn_cancel);

            //为popupwindow中的按钮绑定监听器

            //为赞或取消赞绑定监听器
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentAndReply commentAndReply = commentAndReplyList.get(position);
                    if(like.getText().equals("赞")){
                       //开启异步任务点赞
                        FormBody formBody = new FormBody.Builder()
                                .add("tag",commentAndReply.getTag()+"")
                                .add("userId",userId+"")
                                .add("id",commentAndReply.getId()+"")
                                .build();
                        Request likeRequest = new Request.Builder()
                                .url(serverPath+"like/likeCommentOrReply")
                                .post(formBody)
                                .build();
                        OkHttpClient likeClient = new OkHttpClient();
                        final Call likeCall = likeClient.newCall(likeRequest);
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    likeCall.execute();
                                    commentAndReplyList.get(position).setLike(true);
                                    Message msg = new Message();
                                    msg.what = 1;
                                    myHandler.sendMessage(msg);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    }else{
                        //开启异步任务取消赞
                        FormBody formBody = new FormBody.Builder()
                                .add("tag",commentAndReply.getTag()+"")
                                .add("userId",userId+"")
                                .add("id",commentAndReply.getId()+"")
                                .build();
                        Request cancelLikeRequest = new Request.Builder()
                                .url(serverPath+"like/cancelLike")
                                .post(formBody)
                                .build();
                        OkHttpClient cancelClient = new OkHttpClient();
                        final Call cancelLikeCall = cancelClient.newCall(cancelLikeRequest);
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    cancelLikeCall.execute();
                                    commentAndReplyList.get(position).setLike(false);
                                    Message message = new Message();
                                    message.what = 2;
                                    myHandler.sendMessage(message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                    window.dismiss();
                }
            });

            //为回复选项绑定监听器
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    replyLayout.setVisibility(View.VISIBLE);
                    replyText.setText("");
                    replyText.setHint("回复 "+commentAndReplyList.get(position).getUser().getUserName()+":");

                    //使得回复输入框自动获取焦点
                    replyText.setFocusable(true);
                    replyText.setFocusableInTouchMode(true);
                    replyText.requestFocus();
                    //自动弹出软键盘
                   methodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });

            //为发送回复按钮绑定监听器
            sendReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = replyText.getText().toString();
                    if(msg.trim().isEmpty()){
                        //nothing to do
                    }else{
                        //获取回复的内容
                        String replyContent = replyText.getText().toString();
                        //开启异步任务 添加回复
                        CommentAndReply commentAndReply = commentAndReplyList.get(position);
                        FormBody formBody = new FormBody.Builder()
                                .add("tag",commentAndReply.getTag()+"")
                                .add("userId",userId+"")
                                .add("id",commentAndReply.getId()+"")
                                .add("replyContent",replyContent)
                                .build();
                        Request replyRequest = new Request.Builder()
                                .url(serverPath+"CommentAndReply/addReply")
                                .post(formBody)
                                .build();
                        OkHttpClient replyClient = new OkHttpClient();
                        final Call replyCall = replyClient.newCall(replyRequest);
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    replyCall.execute();
                                    Message message = new Message();
                                    message.what = 3;
                                    myHandler.sendMessage(message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                    //回复框隐藏 软键盘消失
                    if(replyLayout.getVisibility()==View.VISIBLE) {
                        replyLayout.setVisibility(View.GONE);
                        methodManager.hideSoftInputFromWindow(MCommentActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }
                }
            });

            //为 delete按钮绑定事件 弹出对话框 提示用户是否删除该条评论
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //popupwindow 消失
                    window.dismiss();
                    //弹出提示
                    AlertDialog.Builder builder = new AlertDialog.Builder(MCommentActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确认删除该评论");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //nothing to do
                        }
                    });
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //开启异步任务 数据库删除该评论
                            FormBody formBody = new FormBody.Builder()
                                    .add("tag",commentAndReplyList.get(position).getTag()+"")
                                    .add("id",commentAndReplyList.get(position).getId()+"")
                                    .build();
                            Request deleteRequest = new Request.Builder()
                                    .url(serverPath+"CommentAndReply/deleteCommentOrReply")
                                    .post(formBody)
                                    .build();
                            OkHttpClient deleteClient = new OkHttpClient();
                            final Call deleteCall = deleteClient.newCall(deleteRequest);
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        deleteCall.execute();
                                        commentAndReplyList.remove(position);
                                        Message message = new Message();
                                        message.what = 4;
                                        myHandler.sendMessage(message);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            //为查看笔记 绑定事件监听器
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到笔记详情页面
                }
            });
            //为取消按钮绑定事件监听器
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //popupWindow消失
                    window.dismiss();
                }
            });

            //将视图设置到 window
            window.setContentView(view);
            //控制 popupwindow 再点击屏幕其他地方时自动消失
            window.setFocusable(true);
            window.setOutsideTouchable(true);
            window.showAtLocation(root, Gravity.BOTTOM,0,0);
        }

        //调节背景的透明度
        private void addBackgroundAlpha(float alpha){
            //获取到当前屏幕的一系列参数
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = alpha;
            //设置参数
            getWindow().setAttributes(params);
        }

        private class MyHandler extends Handler{
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        adapter = new MCommentListAdapter(getApplicationContext(),
                                commentAndReplyList,
                                R.layout.mcomment_list_item_layout,
                                serverPath,
                                userId);
                        listView.setAdapter(adapter);
                        //为listView每一项绑定事件监听器
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //点击后 弹出popupWindow 关闭软键盘
                                methodManager.hideSoftInputFromWindow(MCommentActivity.this.getCurrentFocus().getWindowToken(), 0);
                                //将回复框隐藏
                                if(replyLayout.getVisibility()==View.VISIBLE) {
                                    replyLayout.setVisibility(View.GONE);
                                }
                                window = new PopupWindow(relativeLayout,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
                                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        addBackgroundAlpha(1f);
                                        window.dismiss();
                                    }
                                });
                                if(window.isShowing()){
                                    window.dismiss();
                                }else{
                                    showPopupWindow(relativeLayout,position);
                                    addBackgroundAlpha(0.7f);
                                }
                            }
                        });
                        break;
                    case 1:
                        adapter.notifyDataSetChanged();
                        Toast likeToast = Toast.makeText(MCommentActivity.this,"点赞成功",Toast.LENGTH_SHORT);
                        likeToast.setGravity(Gravity.TOP,0,300);
                        likeToast.show();
                        break;
                    case 2:
                        adapter.notifyDataSetChanged();
                        Toast cancelToast = Toast.makeText(MCommentActivity.this,"取消赞成功",Toast.LENGTH_SHORT);
                        cancelToast.setGravity(Gravity.TOP,0,300);
                        cancelToast.show();
                        break;
                    case 3:
                        //弹出toast提示用户回复成功或失败
                        Toast replySuccess = Toast.makeText(MCommentActivity.this,"发表评论成功",Toast.LENGTH_SHORT);
                        replySuccess.setGravity(Gravity.TOP,0,300);
                        replySuccess.show();
                        break;
                    case 4:
                        adapter.notifyDataSetChanged();
                }
            }
    }



}
