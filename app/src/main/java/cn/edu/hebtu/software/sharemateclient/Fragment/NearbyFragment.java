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


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Activity.NoteDetailActivity;
import cn.edu.hebtu.software.sharemateclient.Adapter.GridViewAdapter;
import cn.edu.hebtu.software.sharemateclient.Adapter.NearbyViewAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NearbyFragment extends Fragment {
    private NearbyViewAdapter gridViewAdapter=null;
    private List<NoteBean> notes = new ArrayList<NoteBean>();
    private GridView gridView;
    private SmartRefreshLayout srl;
    private OkHttpClient okHttpClient;
    private String U;
    private View view;
    private ListTask listTask;
    private ListMoreTask listMoreTask;
    private UserBean contentUser;
    private int userId;
    private int typeId=1;
    private int currentPage;
    private int pages;
    private String address;
    private LocationClient locationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_nearby,null);
        findViews();
        initLocationOption();
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
        contentUser= (UserBean) getActivity().getIntent().getSerializableExtra("user");
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
            Log.e("contentUserAddress",address);
            String url = U+"/note/nearby/"+currentPage+
                    "?address="+address+"&userId="+userId;
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
                Type noteListType = new TypeToken<ArrayList<NoteBean>>(){}.getType();
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
            locationClient.stop();
            setGrid();
        }
    }

    //    gridView事件
    private void setGrid(){
        // 创建Adapter对象
        gridViewAdapter = new NearbyViewAdapter(getActivity(),
                R.layout.item_nearbygrid,notes,userId);
        // 设置Adapter
        gridView.setAdapter(gridViewAdapter);
        //gridItem点击事件监听  跳转至笔记详情页面
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NoteDetailActivity.class);
                NoteBean note = notes.get(position);
                intent.putExtra("note", (Serializable) note);
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
//        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                loadMoreData();
//                srl.finishLoadMore();//结束加载
//                if(notes.size()>40){//表示加载完所有数据
//                    srl.finishLoadMoreWithNoMoreData();
//                }else {
//                    srl.setNoMoreData(false);
//                }
//            }
//        });

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
                Type noteListType = new TypeToken<ArrayList<NoteBean>>(){}.getType();
                List<NoteBean> noo=gson.fromJson(array, noteListType);
                for (NoteBean n:noo ){
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

    //展示位置
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getContext().getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("wgs84");
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
        //开始定位
        locationClient.start();
        Log.e("start" ,"开始定位");
    }
    /**
     * 实现定位回调
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取位置
            address = location.getAddrStr();
            Log.e("contentgetUser",address);
            listTask.execute();
        }
    }
}
