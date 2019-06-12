package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.sharemateclient.Activity.MainActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.NoteDetailActivity;
import cn.edu.hebtu.software.sharemateclient.Adapter.CustomAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
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
    private int currentPage=0;
    private SmartRefreshLayout srl;
    private UserBean user;
//    private List<NoteBean> notecurrent=new ArrayList<NoteBean>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.note_fragment, container, false);
        listView = view.findViewById(R.id.list);
        user = (UserBean) getActivity().getIntent().getSerializableExtra("user");
        U=getResources().getString(R.string.server_path);
        okHttpClient = new OkHttpClient();
        Log.e("okhttp","okhttp");
        listView=view.findViewById(R.id.list);
        guanzhuAsyncTask guanzhuAsyncTask=new guanzhuAsyncTask();
        guanzhuAsyncTask.execute();
        srl=view.findViewById(R.id.srl);
        //刷新事件监听器
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                refreshData();
                                srl.finishRefresh();//结束刷新
                            }
                        },2000
                );
            }
        });
        //加载更多事件监听器
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreData();
                srl.finishLoadMore();//结束加载
                if(notes.size()>40){//表示加载完所有数据
                    srl.finishLoadMoreWithNoMoreData();
                }else {
                    srl.setNoMoreData(false);
                }
            }
        });



        return view;
    }



    //    刷新
    private void refreshData(){
        //清空note列表
        notes.clear();
        //开启进程重新获取数据
        guanzhuAsyncTask guanzhu=new guanzhuAsyncTask();
        guanzhu.execute();
        customAdapter.notifyDataSetChanged();
    }

    //    加载更多
    private void loadMoreData(){
        guanzhuAsyncTask guanzhu=new guanzhuAsyncTask();
        guanzhu.execute();

        customAdapter.notifyDataSetChanged();
    }






    class guanzhuAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            int userid = user.getUserId();
            currentPage++;
            String u=U+"/note/allnotelist?userid="+userid;
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
                    UserBean userBean = new UserBean();
                    noteBean.setNoteId(noteList.get(i).getNoteId());
                    noteBean.setNoteImage(noteList.get(i).getNoteImage());
                    noteBean.setNoteVideo(noteList.get(i).getNoteVideo());
                    noteBean.setTitle(noteList.get(i).getTitle());
                    noteBean.setNoteDetail(noteList.get(i).getNoteDetail());
                    noteBean.setNoteLikeCount(noteList.get(i).getNoteLikeCount());
                    noteBean.setNoteCollectionCount(noteList.get(i).getNoteCollectionCount());
                    noteBean.setNoteCommentCount(noteList.get(i).getNoteCommentCount());
                    noteBean.setCommentdetial(noteList.get(i).getCommentdetial());
                    noteBean.setUserImage(noteList.get(i).getUserImage());
                    noteBean.setUserName(noteList.get(i).getUserName());
                    noteBean.setCollectTag(noteList.get(i).getCollectTag());
                    noteBean.setZanTag(noteList.get(i).getZanTag());
                    noteBean.setCommentUserImage(noteList.get(i).getCommentUserImage());
                    userBean.setUserName(noteList.get(i).getUserName());
                    userBean.setUserPhoto(noteList.get(i).getUserImage());
                    noteBean.setUser(userBean);
                    Log.e("tagfollow",noteBean.getZanTag()+"");
                    notes.add(noteBean);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("size",String.valueOf(notes.size()));
            for(int i=0;i<notes.size();i++)
            {
                Log.e("zan",String.valueOf(notes.get(i).getNoteLikeCount()));
                Log.e("id",String.valueOf(notes.get(i).getNoteId()));
                Log.e("colloct",String.valueOf(notes.get(i).getNoteCollectionCount()));

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            customAdapter=new CustomAdapter(getContext(),R.layout.list_item,notes,U,user);
            listView.setAdapter(customAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),NoteDetailActivity.class);
                    NoteBean note = notes.get(position);
                    Log.e("note",note.getNoteId()+"");
                    intent.putExtra("note", (Serializable) note);
                    intent.putExtra("contentUser",user);
                    startActivity(intent);
                }
            });
        }
    }
}
