package cn.edu.hebtu.software.sharemateclient.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import cn.edu.hebtu.software.sharemateclient.Adapter.LikeListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.Like;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LikeActivity extends AppCompatActivity {

    //数据源 当前登录用户获得的所有赞
    private List<Like> likeList = new ArrayList<>();
    private ListView likeListView;
    private LikeListAdapter adapter ;
    //当前用户的用户id
    private int currentUserId=3;
    private String serverPath;
    private Gson gson;
    private LikeHandler handler = new LikeHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        likeListView = findViewById(R.id.lv_like);

        //初识化GSON
        GsonBuilder builder = new GsonBuilder();
        gson = builder.serializeNulls().create();
        //获取服务端url
        serverPath = getResources().getString(R.string.server_path);

        //OkHttp 异步请求当前用户收到的赞
        Log.e("requestPath",serverPath+"like/getLikeList/"+currentUserId);
        Request request = new Request.Builder()
                .url(serverPath+"like/getLikeList/"+currentUserId)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call likeListCall = okHttpClient.newCall(request);
        likeListCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LikeActivity","无法连接服务器端");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream in = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String jsonLikeList = reader.readLine();
                Log.e("likeListJson",jsonLikeList);
                Type likeListType = new TypeToken<List<Like>>(){}.getType();
                likeList = gson.fromJson(jsonLikeList,likeListType);
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        });

    }

    private class LikeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                //初始化adapter
                adapter = new LikeListAdapter(LikeActivity.this,R.layout.like_list_item_layout,likeList,serverPath);
                likeListView.setAdapter(adapter);
            }
        }
    }
}
