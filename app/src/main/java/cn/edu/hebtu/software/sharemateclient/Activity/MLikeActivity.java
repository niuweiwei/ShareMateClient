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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.MLikeListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.Like;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MLikeActivity extends AppCompatActivity {

    //数据源 当前登录用户获得的所有赞
    private List<Like> likeList = new ArrayList<>();
    private ListView likeListView;
    private MLikeListAdapter adapter ;
    private Button backButton;
    //当前用户的用户id
    private UserBean currentUser;
    private String serverPath;
    private Gson gson;
    private LikeHandler handler = new LikeHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlike);

        likeListView = findViewById(R.id.lv_like);
        backButton = findViewById(R.id.btn_back);
        //为返回按钮绑定页面结束的时间监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        currentUser = (UserBean) getIntent().getSerializableExtra("currentUser");

        //初识化GSON
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        gson = builder.serializeNulls().create();
        //获取服务端url
        serverPath = getResources().getString(R.string.server_path);

        //OkHttp 异步请求当前用户收到的赞
        Request request = new Request.Builder()
                .url(serverPath+"/like/getLikeList/"+currentUser.getUserId())
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Call likeListCall = okHttpClient.newCall(request);
        likeListCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MLikeActivity","无法连接服务器端");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream in = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String jsonLikeList = reader.readLine();
                Log.e("MLikeActivity",jsonLikeList);
                //用gson将接收到的字符串转换为Like类的list
                Type likeListType = new TypeToken<List<Like>>(){}.getType();
                likeList = gson.fromJson(jsonLikeList,likeListType);
                Log.e("MLikeActivity",likeList.toString());
                for(Like like : likeList){
                    if(like.getNote()!=null && like.getComment()==null && like.getReply()==null)
                        like.setLikeType(Like.NOTE);
                    else if(like.getComment()!=null && like.getNote()==null && like.getReply()==null)
                        like.setLikeType(Like.COMMENT);
                    else if(like.getReply()!=null && like.getNote()==null && like.getComment()==null)
                        like.setLikeType(Like.REPLY);
                    like.getUser().setFollowCount(like.getFollowCount());
                    like.getUser().setFanCount(like.getFanCount());
                    like.getUser().setLikeCount(like.getLikeCount());
                    Log.e("MLikeActivity",like.toString());
                }


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
                adapter = new MLikeListAdapter(MLikeActivity.this,R.layout.mlike_list_item_layout,likeList,serverPath,currentUser);
                likeListView.setAdapter(adapter);
                //点击每一项绑定事件监听器
                likeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(likeList.get(position).getLikeType() == Like.NOTE){
                            //如果点赞的是某一条笔记 则跳转到该笔记的详情页
                            Intent noteIntent = new Intent(MLikeActivity.this,NoteDetailActivity.class);
                            noteIntent.putExtra("note",likeList.get(position).getNote());
                            noteIntent.putExtra("contentUser",currentUser);
                            startActivity(noteIntent);
                        } else if(likeList.get(position).getLikeType() == Like.COMMENT || likeList.get(position).getLikeType() == Like.REPLY){
                            //如果点赞的是某一条评论 则跳转到评论列表
                            Intent commentIntent = new Intent(MLikeActivity.this,NoteDetailActivity.class);
                            commentIntent.putExtra("note",likeList.get(position).getComment().getNote());
                            commentIntent.putExtra("contentUser",currentUser);
                            startActivity(commentIntent);
                        }
                    }
                });
            }
        }
    }
}
