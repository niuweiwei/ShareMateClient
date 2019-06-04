package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMTextMessageBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;


import cn.edu.hebtu.software.sharemateclient.Adapter.MChatListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.Chat;
import cn.edu.hebtu.software.sharemateclient.Bean.User;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MChatListActivity extends AppCompatActivity {

    private Button btnConcact;
    private Button btnBack;
    private User currentUser = new User();
    private String serverPath;
    private Gson gson;
    private Handler handler = new MyHandler();

    private ListView chatListView;
    private List<Chat> chatList = new ArrayList<>();
    private MChatListAdapter chatListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchat_list);

        btnConcact = findViewById(R.id.btn_enterConcat);
        btnBack = findViewById(R.id.btn_back);
        chatListView = findViewById(R.id.lv_chat);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        serverPath = getResources().getString(R.string.server_path);
        gson = new GsonBuilder().serializeNulls().create();


        //登录 试验代码
        EMClient.getInstance().login("" + currentUser.getUserId(), "mff", new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("MChatDetailActivity","登录成功");
            }

            @Override
            public void onError(int i, String s) {
                Log.e("MChatDetailActivity","登录失败");
                Log.e("ErrorCode "+i,s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

        //获取当前用户所开启的所有会话
        final Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                super.run();
                for(String user : conversations.keySet()){
                    User targetUser = null;
                    //获取会话的最新一条的信息
                    EMConversation conversation = conversations.get(user);
                    Date date = new Date(conversation.getLastMessage().getMsgTime());
                    EMTextMessageBody latestMessage = (EMTextMessageBody) conversation.getLastMessage().getBody();
                    String msg = latestMessage.getMessage();
                    Log.e(user,msg);
                    int userId = Integer.parseInt(user);
                    //获取与当前用户通信的用户对象
                    Request userRequest = new Request.Builder()
                            .url(serverPath+"user/getUser/"+userId)
                            .build();
                    OkHttpClient userClient = new OkHttpClient();
                    Call userCall = userClient.newCall(userRequest);

                    try {
                        Response response = userCall.execute();
                        InputStream inputStream = response.body().byteStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String jsonUser = bufferedReader.readLine();
                        targetUser = gson.fromJson(jsonUser,User.class);
                        Chat chat = new Chat(targetUser,msg,date);
                        chatList.add(chat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(chatList,new DateComparator());
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }.start();

        //点击私信图标 跳转到联系人列表
        btnConcact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到关注人列表(即可私信的列表)
                Intent intent = new Intent(MChatListActivity.this,MChatContactsActivity.class);
                intent.putExtra("currentUser",currentUser);
                startActivity(intent);
            }
        });

        //为返回按钮绑定点击事件监听器
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //结束页面
                finish();
            }
        });
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    chatListAdapter = new MChatListAdapter(getApplicationContext(),
                            R.layout.chat_list_item_layout,
                            chatList,serverPath);
                    chatListView.setAdapter(chatListAdapter);
                    //点击每一项  跳转到聊天详情页面
                    chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MChatListActivity.this,MChatDetailActivity.class);
                            intent.putExtra("currentUser",currentUser);
                            intent.putExtra("user",chatList.get(position).getUser());
                            startActivity(intent);
                        }
                    });
                    break;
            }

        }
    }

    private class DateComparator implements Comparator<Chat>{

        @Override
        public int compare(Chat o1, Chat o2) {
            if(o1.getDate().before(o2.getDate()))
                return 1;
            else if(o1.getDate().after(o2.getDate()))
                return -1;
            else
                return 0;
        }
    }
}
