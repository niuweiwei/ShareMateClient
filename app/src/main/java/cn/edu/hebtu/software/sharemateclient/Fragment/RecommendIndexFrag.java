package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Activity.NoteDetailActivity;
import cn.edu.hebtu.software.sharemateclient.Adapter.GridViewAdapter;
import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.Entity.User;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecommendIndexFrag extends Fragment{
    private int typeId;
    private GridViewAdapter gridViewAdapter=null;
    private List<Note> notes = new ArrayList<Note>();
    private GridView gridView;
    private SmartRefreshLayout srl;
    private OkHttpClient okHttpClient;
    private String U;
    private View view;
    private ListTask listTask;
    private int currentPage;
    private int pages;
    private ListMoreTask listMoreTask;
    private User contentUser;
    private int userId;

    //RecommendIndexFrag的new（）方法 id用来fragment传递和保存typeId
    public static RecommendIndexFrag newInstance(int id){
        RecommendIndexFrag frag = new RecommendIndexFrag();
        Bundle args = new Bundle();
        args.putInt("typeId", id);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_viewpager,null);
        //初始化控件
        findViews();
        //开启进程向服务器端获取note的列表数据
        listTask.execute();
        return view;
    }

    //   初始化控件
    public void findViews(){
        okHttpClient = new OkHttpClient();
        U = getResources().getString(R.string.server_path);
        //当前页默认为1;
        currentPage = 1;
        //typeId为空时默认为0
        typeId = getArguments() != null ? getArguments().getInt("typeId") : 0;
        gridView = view.findViewById(R.id.root);
        listTask = new ListTask();
        srl=view.findViewById(R.id.srl);
        //待修改
        contentUser = contentUser = new User(17,"狗蛋","gou",
                "images/userPhotos/17.jpg","女","15852160982",
                "上海市", "1985-09-04","做梦都想发家致富");
        userId = contentUser.getUserId();
    }

    //    刷新
    private void refreshData(){
        //请空note列表
        notes.clear();
        //开启进程重新获取数据
        listTask = new ListTask();
        listTask.execute();
        gridViewAdapter.notifyDataSetChanged();
    }

    //    加载更多
    private void loadMoreData(){
        listMoreTask = new ListMoreTask();
        if(currentPage==pages){
            currentPage=1;
        }else {
            currentPage++;
            listMoreTask.execute();
        }
        gridViewAdapter.notifyDataSetChanged();
    }


    //请求数据并初始化数组
    private class ListTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            String url = U+"/note/recommend/"+currentPage+"?typeId="+typeId+"&userId="+userId;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            // 3.创建Call对象
            Call call = okHttpClient.newCall(request);
            // 4.提交请求，返回响应
            try {
                Response response = call.execute();
                String rel = response.body().string();
                JSONObject jsonObject = new JSONObject(rel);
                JSONObject noteObject = jsonObject.getJSONObject("map");
                String array = noteObject.getJSONArray("note").toString();
                Gson gson = new Gson();
                Type noteListType = new TypeToken<ArrayList<Note>>(){}.getType();
                notes = gson.fromJson(array, noteListType);
                pages = noteObject.getInt("pages");
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setGrid();
        }
    }

    //    gridView事件
    private void setGrid(){
        // 创建Adapter对象
        gridViewAdapter = new GridViewAdapter(getActivity(),R.layout.item_recomgrid,notes);
        // 设置Adapter
        gridView.setAdapter(gridViewAdapter);
        //gridItem点击事件监听  跳转至笔记详情页面
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NoteDetailActivity.class);
                Note note = notes.get(position);
                intent.putExtra("note",note);
                intent.putExtra("contentUser",contentUser);
                Log.e("noteinfo",note.getNoteImage());
                startActivity(intent);
            }
        });

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

    }

    //请求数据并初始化数组
    public class ListMoreTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            String url = U+"/note/recommend/"+currentPage+"?typeId="+typeId+"&userId="+userId;

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            // 3.创建Call对象
            Call call = okHttpClient.newCall(request);
            // 4.提交请求，返回响应
            try {
                Response response = call.execute();
                String rel = response.body().string();
                JSONObject jsonObject = new JSONObject(rel);
                JSONObject noteObject = jsonObject.getJSONObject("map");
                String array = noteObject.getJSONArray("note").toString();
                Gson gson = new Gson();
                Type noteListType = new TypeToken<ArrayList<Note>>(){}.getType();
                List<Note> noo=gson.fromJson(array, noteListType);
                for (Note n:noo ){
                    notes.add(n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( listTask != null && listTask.getStatus() == ListTask.Status.RUNNING ){
            listTask.cancel(true);
            listTask = null;
        }
        if ( listMoreTask != null && listMoreTask.getStatus() == ListMoreTask.Status.RUNNING ){
            listMoreTask.cancel(true);
            listMoreTask = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        listTask = new ListTask();
//        listTask.execute();
    }
}