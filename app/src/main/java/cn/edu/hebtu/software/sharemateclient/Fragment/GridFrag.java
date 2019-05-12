package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Activity.MainActivity;
import cn.edu.hebtu.software.sharemateclient.Adapter.GridViewAdapter;
import cn.edu.hebtu.software.sharemateclient.Entity.Goods;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GridFrag extends Fragment {
    private GridViewAdapter gridViewAdapter=null;
    private List<Goods>goods = new ArrayList<Goods>();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout,null);
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
        goods.clear();
        if(index<=7){
            index++;
        }else {
            index=1;
        }
        listTask = new ListTask();
        listTask.execute(index);
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
        listMoreTask.execute(index);
        gridViewAdapter.notifyDataSetChanged();
    }

    //请求数据并初始化数组
    private class ListTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            int id = (int) objects[0];
            String url = U+"/cakeshop/cake/index1/"+id;
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
                String array = jsonObject.getJSONArray("cake").toString();
                Gson gson = new Gson();
                Type goodListType = new TypeToken<ArrayList<Goods>>(){}.getType();
                goods = gson.fromJson(array, goodListType);
                for (Goods g: goods){
                    //开启新的数据访问图片
                    String img = g.getCakeImageUrl();
                    String url1 =U+img;
                    Request request2 = new Request.Builder()
                            .url(url1)
                            .build();
                    // 3.创建Call对象
                    Call call2 = okHttpClient.newCall(request2);
                    Response response1 = call2.execute();
                    BufferedInputStream ins = new BufferedInputStream(response1.body().byteStream());
                    Bitmap a= BitmapFactory.decodeStream(ins);
                    g.setBitmap(a);
                }
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
        gridViewAdapter = new GridViewAdapter(getActivity(),R.layout.grid_item,goods);
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
                if(goods.size()>20){//表示加载完所有数据
                    srl.finishLoadMoreWithNoMoreData();
                }else {
                    srl.setNoMoreData(false);
                }
            }
        });
        //item点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Goods good = goods.get(position);
                intent.setClass(getContext(), MainActivity.class);
                intent.putExtra("cakeId",good.getCakeId());
                intent.putExtra("name",good.getCakeName());
                intent.putExtra("price",good.getCakePrice());
                intent.putExtra("img",good.getCakeImageUrl());
                startActivity(intent);
            }
        });
    }

    //请求数据并初始化数组
    public class ListMoreTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            int id = (int) objects[0];
            String url = U+"/cakeshop/cake/index1/"+id;
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
                String array = jsonObject.getJSONArray("cake").toString();
                Gson gson = new Gson();
                Type goodListType = new TypeToken<ArrayList<Goods>>(){}.getType();
                List<Goods> goo = new ArrayList<Goods>();
                goo = gson.fromJson(array, goodListType);
                for (Goods g: goo){
                    //开启新的数据访问图片
                    String img = g.getCakeImageUrl();
                    String url1 =U+img;
                    Request request2 = new Request.Builder()
                            .url(url1)
                            .build();
                    // 3.创建Call对象
                    Call call2 = okHttpClient.newCall(request2);
                    Response response1 = call2.execute();
                    BufferedInputStream ins = new BufferedInputStream(response1.body().byteStream());
                    Bitmap a= BitmapFactory.decodeStream(ins);
                    g.setBitmap(a);
                    goods.add(g);
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
