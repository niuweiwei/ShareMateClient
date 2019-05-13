package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

import cn.edu.hebtu.software.sharemateclient.Activity.MainActivity;
import cn.edu.hebtu.software.sharemateclient.Adapter.GridViewAdapter;
import cn.edu.hebtu.software.sharemateclient.Entity.Goods;
import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NearbyFragment extends Fragment {
    private GridViewAdapter gridViewAdapter=null;
    private List<Note> notes = new ArrayList<Note>();
    private GridView gridView;
    private SmartRefreshLayout srl;
    private OkHttpClient okHttpClient;
    private String U;
    private View view;
    private ListTask listTask;
    private int index =1;
    private ListMoreTask listMoreTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_nearby,null);
        findViews();
        listTask.execute(index);
        return view;

    }
    //   初始化控件
    public void findViews(){
        okHttpClient = new OkHttpClient();
        U = getResources().getString(R.string.url);
        gridView = view.findViewById(R.id.root);
        listTask = new ListTask();
        srl=view.findViewById(R.id.srl);
    }

    //    刷新
    private void refreshData(){
        notes.clear();
        if(index<=7){
            index++;
        }else {
            index=1;
        }
        listTask = new ListTask();
        listTask.execute();
        gridViewAdapter.notifyDataSetChanged();
    }

    //    加载更多
    private void loadMoreData(){
        listMoreTask = new ListMoreTask();
        if(index<=7){
            index++;
        }else {
            index=1;
        }
        listMoreTask.execute();
        gridViewAdapter.notifyDataSetChanged();
    }

    //请求数据并初始化数组
    private class ListTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            String url = U+"/note/recommend/"+index;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            // 3.创建Call对象
            Call call = okHttpClient.newCall(request);
            // 4.提交请求，返回响应
            try {
                Response response = call.execute();
                String rel = response.body().string();
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(rel);
                String array = jsonObject.getJSONArray("note").toString();
                Gson gson = new Gson();
                Type noteListType = new TypeToken<ArrayList<Note>>(){}.getType();
                notes = gson.fromJson(array, noteListType);
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
        gridViewAdapter = new GridViewAdapter(getActivity(),R.layout.grid_item,notes);
        // 设置Adapter
        gridView.setAdapter(gridViewAdapter);
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
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreData();
                srl.finishLoadMore();//结束加载
                if(notes.size()>30){//表示加载完所有数据
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
            String url = U+"/note/recommend/"+index;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            // 3.创建Call对象
            Call call = okHttpClient.newCall(request);
            // 4.提交请求，返回响应
            try {
                Response response = call.execute();
                String rel = response.body().string();
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(rel);
                String array = jsonObject.getJSONArray("note").toString();
                Gson gson = new Gson();
                Type noteListType = new TypeToken<ArrayList<Note>>(){}.getType();
                List<Note> noo = new ArrayList<Note>();
                noo = gson.fromJson(array, noteListType);
                for (Note n:noo ){
                    noo.add(n);
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
    }
}
