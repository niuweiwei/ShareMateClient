package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Activity.MCommentActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.MLikeActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.MFollowActivity;
import cn.edu.hebtu.software.sharemateclient.Adapter.MessageListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.MessageFragmentTitle;
import cn.edu.hebtu.software.sharemateclient.Activity.MChatListActivity;
import cn.edu.hebtu.software.sharemateclient.Bean.User;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 消息Fragment
 * */
public class MessageFragment extends Fragment {

    private List<MessageFragmentTitle> titles = new ArrayList<>();
    private ListView listView;
    private User currentUser = null;
    private String serverPath;
    private Gson gson;
    private Handler handler = new MyHandler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_message,container,false);
        //准备数据源
        titles.add(new MessageFragmentTitle(R.mipmap.like,"收到的赞"));
        titles.add(new MessageFragmentTitle(R.mipmap.comment,"收到的评论"));
        titles.add(new MessageFragmentTitle(R.mipmap.followed,"新增关注"));
        titles.add(new MessageFragmentTitle(R.mipmap.message,"收到的私信"));
        //初始化监听器
        MessageListAdapter listAdapter = new MessageListAdapter(titles,getActivity(),R.layout.mmessage_list_item_layout);
        listView = view.findViewById(R.id.lv_message);
        //绑定监听器
        listView.setAdapter(listAdapter);


        //异步获取当前用户  暂时代码
        serverPath = getResources().getString(R.string.server_path);
        gson = new GsonBuilder().serializeNulls().create();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(serverPath+"user/getUser/"+15)
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

        return view;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    //为listView的每一个item绑定点击事件监听器
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position){
                                case 0:
                                    //点击了收到的赞
                                    Intent likeIntent = new Intent(getActivity(),MLikeActivity.class);
                                    startActivity(likeIntent);
                                    break;
                                case 1:
                                    //点击了收到的关注
                                    Intent commentIntent = new Intent(getActivity(),MCommentActivity.class);
                                    startActivity(commentIntent);
                                    break;
                                case 2:
                                    //点击了新增关注
                                    Intent fanIntent = new Intent(getActivity(),MFollowActivity.class);
                                    startActivity(fanIntent);
                                    break;
                                case 3:
                                    //点击了收到的消息页面
                                    Intent chatIntent = new Intent(getActivity(),MChatListActivity.class);
                                    chatIntent.putExtra("currentUser",currentUser);
                                    startActivity(chatIntent);
                                    break;
                            }
                        }
                    });
                    break;
            }
        }
    }
}
