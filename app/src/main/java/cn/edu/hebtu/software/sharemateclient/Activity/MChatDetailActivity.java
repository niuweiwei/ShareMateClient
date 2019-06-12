package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;

public class MChatDetailActivity extends AppCompatActivity {

    private EaseTitleBar titleBar;
    private FragmentManager manager ;
    private UserBean currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchat_detail);

        currentUser = (UserBean) getIntent().getSerializableExtra("currentUser");

        //加载chatFragment
        manager = getSupportFragmentManager();
        EaseChatFragment chatFragment = (EaseChatFragment) manager.findFragmentById(R.id.chat_fragment);
        FragmentTransaction transaction = manager.beginTransaction();
        //将 通信的用户名以及 聊天类型作为参数传入
        chatFragment.setArguments(getIntent().getExtras());
        if(!chatFragment.isAdded()){
            transaction.add(R.id.chat_fragment, chatFragment);
        }
        titleBar = chatFragment.getView().findViewById(R.id.title_bar);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MChatDetailActivity.this,MChatListActivity.class);
                intent.putExtra("currentUser",currentUser);
                startActivity(intent);
            }
        });
        transaction.commit();


    }
}
