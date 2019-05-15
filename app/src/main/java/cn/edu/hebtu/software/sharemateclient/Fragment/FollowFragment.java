package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.sharemateclient.Adapter.CustomAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FollowFragment extends Fragment{
    private List<NoteBean> notes=new ArrayList<NoteBean>();
    private ListView listView;
    private CustomAdapter customAdapter = null;
    private int userId;
    private OkHttpClient okHttpClient;
    private String U;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.note_fragment, container, false);
        listView = view.findViewById(R.id.list);
        U=getResources().getString(R.string.server_path);
//        NoteBean note1=new NoteBean();
//        note1.setNoteImage(R.drawable.a1);
//        note1.setNoteDetail("12345789");
//        note1.setZancount(1);
//        note1.setCollectcount(2);
//        note1.setPingluncount(5);
//        notes.add(note1);
//        NoteBean note2=new NoteBean();
//        note2.setNoteImage(R.drawable.a1);
//        note2.setNoteDetail("12345789");
//        note2.setZancount(1);
//        note2.setCollectcount(2);
//        note2.setPingluncount(5);
//        notes.add(note2);
        okHttpClient = new OkHttpClient();
        getGuanzhuResouce();
        Log.e("okhttp","okhttp");
        listView=view.findViewById(R.id.list);
        customAdapter=new CustomAdapter(getContext(),R.layout.list_item,notes);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //转跳至详情页面
            }
        });
        return view;
    }

    //通过okhttp请求数据
    public void getGuanzhuResouce(){
        new Thread(){
            public void run(){
                String userid="1";
                String u=String.valueOf(U);
                Log.e("URL",u);
                Request request=new Request.Builder()
                        .url(U+"/note/allnotelist")
                        .build();
                Call call=okHttpClient.newCall(request);
                try {
                    Response response =call.execute();
                    Log.e("fr",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
