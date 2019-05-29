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

import cn.edu.hebtu.software.sharemateclient.Adapter.MCommentListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.CommentAndReply;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcomment);

        listView = findViewById(R.id.lv_comment);
        gson = new GsonBuilder().serializeNulls().create();
        serverPath = getResources().getString(R.string.server_path);

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

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                adapter = new MCommentListAdapter(getApplicationContext(),
                        commentAndReplyList,
                        R.layout.comment_list_item_layout,
                        serverPath,
                        userId);
                listView.setAdapter(adapter);
            }
        }
    }

}
