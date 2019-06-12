package cn.edu.hebtu.software.sharemateclient.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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

import cn.edu.hebtu.software.sharemateclient.Adapter.MFollowListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.Follow;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//"消息"模块中 新增关注的页面
public class MFollowActivity extends AppCompatActivity {

    private List<Follow> followList = new ArrayList<>();
    private ListView followListView;
    private Button backBtn;
    private MFollowListAdapter followListAdapter;
    //Gson
    private Gson gson;
    private String serverPath;
    private UserBean currentUser;//当前用户id
    private FollowHandler followHandler = new FollowHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfollow);

        followListView = findViewById(R.id.lv_followed);
        backBtn = findViewById(R.id.btn_back);

        currentUser = (UserBean) getIntent().getSerializableExtra("currentUser");

        //为返回按钮绑定点击事件监听器
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初识话GSon
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.serializeNulls().create();
        //获取服务器端的URL
        serverPath = getResources().getString(R.string.server_path);
        Request followRequest = new Request.Builder()
                .url(serverPath+"/follow/followList/"+currentUser.getUserId())
                .build();
        OkHttpClient followsClient = new OkHttpClient();
        Call followCall = followsClient.newCall(followRequest);
        followCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MFollowActivity","网络连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String jsonFollowList = reader.readLine();
                //将jsonFollowList字符串转化为follow的List
                Type followListType = new TypeToken<List<Follow>>(){}.getType();
                followList = gson.fromJson(jsonFollowList,followListType);
                Message message = new Message();
                message.what = 0;
                followHandler.sendMessage(message);
            }
        });
    }

    private class FollowHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                followListAdapter = new MFollowListAdapter(MFollowActivity.this,followList,R.layout.mfollow_list_item_layout,serverPath);
                followListView.setAdapter(followListAdapter);
            }
        }
    }
}
