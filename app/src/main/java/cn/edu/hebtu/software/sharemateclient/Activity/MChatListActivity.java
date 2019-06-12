package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;

public class MChatListActivity extends AppCompatActivity {

    private Button btnConcact;
    private Button btnBack;
    private UserBean currentUser ;
    private EaseConversationList conversationListView;
    private List<EMConversation> conversationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchat_list);

        btnConcact = findViewById(R.id.btn_enterConcat);
        btnBack = findViewById(R.id.btn_back);
        conversationListView = findViewById(R.id.list);

        currentUser = (UserBean) getIntent().getSerializableExtra("currentUser");


        conversationListView = findViewById(R.id.list);

        conversationList.addAll(loadConversationList());
        //初始化，参数为会话列表集合
        conversationListView.init(conversationList);
        //刷新列表
        conversationListView.refresh();
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MChatListActivity.this,MChatDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                bundle.putString(EaseConstant.EXTRA_USER_ID,conversationList.get(position).conversationId());
                intent.putExtras(bundle);
                intent.putExtra("currentUser",currentUser);
                startActivity(intent);
            }
        });




        //为私信按钮绑定点击事件监听器
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
                //返回到主页中
                Intent intent = new Intent(MChatListActivity.this,MainActivity.class);
                intent.putExtra("flag","MessageFragment");
                intent.putExtra("user",currentUser);
                startActivity(intent);
            }
        });
    }


    /**
     * load conversation list
     *
     * @return
    +    */
    protected List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

}
