package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.hebtu.software.sharemateclient.Bean.User;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchat_list);

        btnConcact = findViewById(R.id.btn_enterConcat);
        btnBack = findViewById(R.id.btn_back);

        serverPath = getResources().getString(R.string.server_path);
        gson = new GsonBuilder().serializeNulls().create();

        //为私信按钮绑定点击事件监听器
        btnConcact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //异步获取当前用户  暂时代码
                //通过当前 userId 获取当前的user 试验代码
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(serverPath+"user/getUser/"+3)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("MChatDetailActivity","网络连接错误");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream inputStream = response.body().byteStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String jsonUser = bufferedReader.readLine();
                        currentUser = gson.fromJson(jsonUser,User.class);
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                });

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
            if(msg.what == 0){
                //跳转到关注人列表(即可私信的列表)
                Intent intent = new Intent(MChatListActivity.this,MChatContactsActivity.class);
                intent.putExtra("currentUser",currentUser);
                startActivity(intent);
            }
        }
    }
}
