package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
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
        okHttpClient = new OkHttpClient();
        getGuanzhuResouce();
        Log.e("okhttp","okhttp");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listView=view.findViewById(R.id.list);
//        NoteBean notebean =new NoteBean();
//        notebean.setTitle("123");
//        notebean.setZancount(5);
//        notebean.setNoteDetail("123456");
//        notes.add(notebean);
        customAdapter=new CustomAdapter(getContext(),R.layout.list_item,notes);
        listView.setAdapter(customAdapter);
        Log.e("listview","liatview");
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
                String u=U+"/note/allnotelist";
                Log.e("URL",u);
                Request request=new Request.Builder()
                        .url(u)
                        .build();
                Call call=okHttpClient.newCall(request);
                try {
                    String notejson=call.execute().body().string();
                    Log.e("notelist" ,notejson);
                    JSONObject noteObject=new JSONObject(notejson);
                    JSONArray jsonArray=noteObject.getJSONArray("notelist");
                    String notestr=jsonArray.toString();
                    Gson gson=new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                    Type type=new TypeToken<List<NoteBean>>(){}.getType();
                    List<NoteBean> noteList=gson.fromJson(notestr,type);
                    Log.e("noteList",noteList.get(0).getNoteTitle());
                    for(int i=0;i<noteList.size();i++){
                            NoteBean noteBean=new NoteBean();
                            noteBean.setNoteImage(noteList.get(i).getNoteImage());
                            noteBean.setTitle(noteList.get(i).getTitle());
                            noteBean.setNoteDetail(noteList.get(i).getNoteDetail());
                            noteBean.setZancount(noteList.get(i).getZancount());
                            noteBean.setCollectcount(noteList.get(i).getCollectcount());
                            noteBean.setPingluncount(noteList.get(i).getPingluncount());
                            notes.add(noteBean);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("size",String.valueOf(notes.size()));
            }
        }.start();

    }
}
