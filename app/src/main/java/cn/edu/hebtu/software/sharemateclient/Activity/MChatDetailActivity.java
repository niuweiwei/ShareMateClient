package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Context;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.hebtu.software.sharemateclient.Adapter.MChatContentAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.Chat;
import cn.edu.hebtu.software.sharemateclient.Bean.User;
import cn.edu.hebtu.software.sharemateclient.R;


public class MChatDetailActivity extends AppCompatActivity {

    private TextView chatTitle;
    private EditText replyText;
    private Button btnSend;
    private Button btnBack;
    private ListView chatListView;
    private MChatContentAdapter chatAdapter;
    private InputMethodManager inputMethodManager;
    private List<Chat> chatList = new ArrayList<>();
    private User currentUser ;//当前用户
    private User anotherUser;//与当前用户进行通信的用户
    private ReceiveMessageListener messageListener = new ReceiveMessageListener();
    private String serverPath;

    protected int pagesize = 20;
    protected boolean isRoaming = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchat_detail);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        anotherUser = (User) getIntent().getSerializableExtra("user");

        chatTitle = findViewById(R.id.tv_userName);
        replyText = findViewById(R.id.et_reply);
        btnSend = findViewById(R.id.btn_send);
        chatListView = findViewById(R.id.lv_content);
        btnBack = findViewById(R.id.btn_back);

        serverPath = getResources().getString(R.string.server_path);

        //开启系统中软键盘的输入服务
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        chatTitle.setText(anotherUser.getUserName());
        //使得消息输入框自动获取焦点
        replyText.setFocusable(true);
        replyText.setFocusableInTouchMode(true);
        replyText.requestFocus();
        //自动弹出软键盘
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        //获取与当前用户之前的聊天记录
        final EMConversation conversation = EMClient.getInstance().chatManager().getConversation(anotherUser.getUserId()+"",EMConversation.EMConversationType.Chat,true);
        if (!isRoaming) {
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            }else {
                ExecutorService fetchQueue = Executors.newSingleThreadExecutor();
                fetchQueue.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().chatManager().fetchHistoryMessages(
                                    anotherUser.getUserId()+"",EMConversation.EMConversationType.Chat, pagesize, "");
                            final List<EMMessage> msgs = conversation.getAllMessages();
                            int msgCount = msgs != null ? msgs.size() : 0;
                            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                                String msgId = null;
                                if (msgs != null && msgs.size() > 0) {
                                    msgId = msgs.get(0).getMsgId();
                                }
                                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
                            }
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        List<EMMessage> messages = conversation.getAllMessages();
        for(EMMessage message : messages){
            Date date = new Date(message.getMsgTime());
            EMTextMessageBody textMessage = (EMTextMessageBody) message.getBody();
            Chat chat;
            //判断每一条消息 是谁发的
            if(message.getFrom().equals(currentUser.getUserId()+"")){
                chat = new Chat(currentUser,textMessage.getMessage(),date);
            }else{
                chat = new Chat(anotherUser,textMessage.getMessage(),date);
            }
            chatList.add(chat);
        }

        chatAdapter = new MChatContentAdapter(this,
                chatList,
                R.layout.chat_left_item_layout,
                R.layout.chat_right_item_layout,
                currentUser.getUserId(),
                serverPath);
        //绑定适配器
        chatListView.setAdapter(chatAdapter);

        //为点击发送消息按钮绑定事件监听器
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = replyText.getText().toString();
                if(msg.trim().isEmpty()){
                    //nothing to do
                }else{

                    //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id
                    EMMessage message = EMMessage.createTxtSendMessage(msg, anotherUser.getUserId()+"");
                    message.setFrom(currentUser.getUserId()+"");
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(message);

                   Chat chat = new Chat(currentUser,msg,new Date());
                    chatList.add(chat);
                    chatAdapter.notifyDataSetChanged();
                    //清除输入框的文字
                    replyText.setText("");
                    replyText.setHint("发消息......");

                }
            }
        });

        //绑定监听接收消息的时间监听器
        EMClient.getInstance().chatManager().addMessageListener(messageListener);

        //为返回按钮绑定事件监听器
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MChatDetailActivity.this,MChatListActivity.class);
                intent.putExtra("currentUser",currentUser);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }


    private class ReceiveMessageListener implements EMMessageListener{

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for(EMMessage message : list){
                if(Integer.parseInt(message.getFrom()) == anotherUser.getUserId()){
                    String messageContent = message.getBody().toString();
                    Chat chat = new Chat(anotherUser,messageContent);
                    chatList.add(chat);
                    chatAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    }
}
