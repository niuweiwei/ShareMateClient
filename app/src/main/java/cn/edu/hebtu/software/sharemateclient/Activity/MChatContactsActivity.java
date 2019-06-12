package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.EaseConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.MContactListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MChatContactsActivity extends AppCompatActivity {

    private List<UserBean> contactList = new ArrayList<>();
    private ListView listView;
    private Button backButton;
    private MContactListAdapter adapter ;
    private String serverPath;
    private UserBean currentUser;
    private Gson gson;
    private MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchat_contacts);

        listView = findViewById(R.id.lv_contacts);
        backButton = findViewById(R.id.btn_back);

        currentUser = (UserBean) getIntent().getSerializableExtra("currentUser");

        //为返回按钮绑定事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化gson对象
        gson = new GsonBuilder().serializeNulls().create();
        //获取服务器的url
        serverPath = getResources().getString(R.string.server_path);
        //开启线程 向服务器端获取 contact列表
        Request contactRequest = new Request.Builder()
                .url(serverPath+"/follow/contactList/"+currentUser.getUserId())
                .build();
        OkHttpClient contactClient = new OkHttpClient();
         Call contactCall = contactClient.newCall(contactRequest);
          contactCall.enqueue(new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {

              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                  InputStream inputStream = response.body().byteStream();
                  BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                  String jsonContactList = reader.readLine();
                  Type contactListType = new TypeToken<List<UserBean>>(){}.getType();
                  contactList = gson.fromJson(jsonContactList,contactListType);
                  Message message = new Message();
                  message.what = 0;
                  handler.sendMessage(message);
              }
          });
    }

    private class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                //初始化adpter
                adapter = new MContactListAdapter(MChatContactsActivity.this,contactList,R.layout.mcontact_list_item_layout,serverPath);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserBean user = contactList.get(position);
                        Intent intent = new Intent(MChatContactsActivity.this,MChatDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_SINGLE);
                        bundle.putString(EaseConstant.EXTRA_USER_ID,user.getUserId()+"");
                        intent.putExtras(bundle);
                        intent.putExtra("currentUser",currentUser);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
