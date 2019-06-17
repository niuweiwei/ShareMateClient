package cn.edu.hebtu.software.sharemateclient.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.UpLoadUtil;
import cn.edu.hebtu.software.sharemateclient.tools.UpLoadUtil1;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FabuActivity extends AppCompatActivity {
    private PopupWindow window = null;
    private LinearLayout root = null;
    private String path = null;
    private int typeid;
    private int userId;
    private List<Integer> type;
    private LinearLayout addposition;
    private TextView position;
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;
    private String address;
    private String title;
    private String detial;
    private JSONObject jsonObject;
    private OkHttpClient okHttpClient;
    private String code;
    private String videoPath;
    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_fabu);
        //显示图片
        final Intent intent = getIntent();
        code = intent.getStringExtra("code");
        user = (UserBean) intent.getSerializableExtra("user");
        userId = user.getUserId();
        Log.e("userid",userId+"");
        ImageView imageView = findViewById(R.id.imageView);
        VideoView vv = findViewById(R.id.videoview);

        //定位信息
        addposition = findViewById(R.id.add_position);
        position = findViewById(R.id.wenzi_postion);

        if (code != null) {
            if (code.equals("1")) {
                vv.setVisibility(View.INVISIBLE);
                String picturePath = intent.getStringExtra("lujing");
                path = picturePath;
                Bitmap bitmap1 = BitmapFactory.decodeFile(picturePath);
                imageView.setImageBitmap(bitmap1);
            } else if (code.equals("2")) {
                vv.setVisibility(View.INVISIBLE);
                String picturePath = intent.getStringExtra("pic1");
                path = picturePath;
                Log.e("code", code);
                Bitmap bitmap2 = BitmapFactory.decodeFile(picturePath);
                imageView.setImageBitmap(bitmap2);

            } else if (code.equals("3")) {
                imageView.setVisibility(View.INVISIBLE);

                videoPath = intent.getStringExtra("videopath");
                vv.setVideoURI(Uri.parse(videoPath));
                vv.start();
            } else {
                Log.e("picture", "图片未获取到");
            }
        }
        //退出发布页面
        root = findViewById(R.id.fabu_root);
        window = new PopupWindow(root, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Button btnShare = findViewById(R.id.guanbi);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.isShowing()) {
                    window.dismiss();
                } else {
                    showPopupWindow(root);
                    addBackgroundAlpha(0.7f);
                }



            }
        });


        //获取话题
        LinearLayout linearLayout = findViewById(R.id.btn_topic);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTopicPopupWindow();
            }
        });


        //获取位置
        LinearLayout linearLayout1 = findViewById(R.id.add_position);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                initLocationOption();
                Log.e("fr", "开始定位");
            }
        });

        //发布
        Button btn = findViewById(R.id.fabu);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserBean userbean = new UserBean(userId);
                EditText EDdetial = (EditText) findViewById(R.id.detial);
                String detial1 = EDdetial.getText().toString();
                detial = detial1;
                EditText EDtitle = (EditText) findViewById(R.id.wenzi_title);
                String title1 = EDtitle.getText().toString();
                title = title1;
                Log.e("fr", detial);
                Log.e("fr", title);
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("detial", detial);
                    jsonObject.put("title", title);
                    jsonObject.put("typeid", String.valueOf(typeid));
                    jsonObject.put("userid", String.valueOf(userId));
                    jsonObject.put("position",address);
                    if(code.equals("3")){
                        jsonObject.put("videoPath",videoPath);
                    }
                    else{
                        jsonObject.put("videoPath","1");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("fr", jsonObject.toString());
                okHttpClient = new OkHttpClient();
                fabuAsyncTask fabuAsyncTask=new fabuAsyncTask();
                fabuAsyncTask.execute();
                //发布提示
                showtoast();
                //显示分享界面
                //sharepopupWindow(path);
                addBackgroundAlpha(0.7f);
                Intent intent1=new Intent();
                intent1.putExtra("user",user);
                intent1.setClass(FabuActivity.this,MainActivity.class);
                startActivity(intent1);
            }


        });


    }

    //展示位置
    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
//开始定位
        locationClient.start();
    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取位置
            String address1 = location.getAddrStr();
            Log.e("fr", address1);
            address = address1;
            position.setText(address1);
        }
    }


    //展示topicpopupwindow
    private void showTopicPopupWindow() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.topic_popupwindow, null);
        window.setContentView(view);
        final Button button1 = view.findViewById(R.id.meizhuang1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button1.getText().toString();
                textView.setText(tips);
                typeid = 1;
                window.dismiss();
            }
        });
        final Button button2 = view.findViewById(R.id.meizhuang2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button2.getText().toString();
                textView.setText(tips);
                typeid = 1;
                window.dismiss();
            }
        });
        final Button button3 = view.findViewById(R.id.meizhuang3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button3.getText().toString();
                textView.setText(tips);
                typeid = 1;
                window.dismiss();
            }
        });
        final Button button4 = view.findViewById(R.id.meizhuang4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button4.getText().toString();
                textView.setText(tips);
                typeid = 1;
                window.dismiss();
            }
        });
        final Button button5 = view.findViewById(R.id.lvxing1);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button5.getText().toString();
                textView.setText(tips);
                typeid = 2;
                window.dismiss();
            }
        });
        final Button button6 = view.findViewById(R.id.lvxing2);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button6.getText().toString();
                textView.setText(tips);
                typeid = 2;
                window.dismiss();
            }
        });
        final Button button7 = view.findViewById(R.id.lvxing3);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button7.getText().toString();
                textView.setText(tips);
                typeid = 2;
                window.dismiss();
            }
        });
        final Button button8 = view.findViewById(R.id.lvxing4);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button8.getText().toString();
                textView.setText(tips);
                typeid = 2;
                window.dismiss();
            }
        });
        final Button button9 = view.findViewById(R.id.yundong1);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button9.getText().toString();
                textView.setText(tips);
                typeid = 3;
                window.dismiss();
            }
        });
        final Button button10 = view.findViewById(R.id.yundong2);
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button10.getText().toString();
                textView.setText(tips);
                typeid = 3;
                window.dismiss();
            }
        });
        final Button button11 = view.findViewById(R.id.yundong3);
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button11.getText().toString();
                textView.setText(tips);
                typeid = 3;
                window.dismiss();
            }
        });

        final Button button12 = view.findViewById(R.id.meishi1);
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button12.getText().toString();
                textView.setText(tips);
                typeid = 4;
                window.dismiss();
            }
        });
        final Button button13 = view.findViewById(R.id.meishi2);
        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button13.getText().toString();
                textView.setText(tips);
                typeid = 4;
                window.dismiss();
            }
        });
        final Button button14 = view.findViewById(R.id.meishi3);
        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button14.getText().toString();
                textView.setText(tips);
                typeid = 4;
                window.dismiss();
            }
        });
        final Button button15 = view.findViewById(R.id.meishi4);
        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button15.getText().toString();
                textView.setText(tips);
                typeid = 4;
                window.dismiss();
            }
        });
        final Button button16 = view.findViewById(R.id.keji1);
        button16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button16.getText().toString();
                textView.setText(tips);
                typeid = 5;
                window.dismiss();
            }
        });
        final Button button17 = view.findViewById(R.id.keji2);
        button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button17.getText().toString();
                textView.setText(tips);
                typeid = 5;
                window.dismiss();
            }
        });
        final Button button18 = view.findViewById(R.id.keji3);
        button18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button18.getText().toString();
                textView.setText(tips);
                typeid = 5;
                window.dismiss();
            }
        });
        final Button button19 = view.findViewById(R.id.keji4);
        button19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button19.getText().toString();
                textView.setText(tips);
                typeid = 5;
                window.dismiss();
            }
        });
        final Button button20 = view.findViewById(R.id.dongman1);
        button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button20.getText().toString();
                textView.setText(tips);
                typeid = 6;
                window.dismiss();
            }
        });
        final Button button21 = view.findViewById(R.id.dongman2);
        button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button21.getText().toString();
                textView.setText(tips);
                typeid = 6;
                window.dismiss();
            }
        });
        final Button button22 = view.findViewById(R.id.dongman3);
        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button22.getText().toString();
                textView.setText(tips);
                typeid = 6;
                window.dismiss();
            }
        });
        final Button button23 = view.findViewById(R.id.dongman4);
        button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.wenzi_topic);
                String tips = button23.getText().toString();
                textView.setText(tips);
                typeid = 6;
                window.dismiss();
            }
        });

        window.showAtLocation(root, Gravity.BOTTOM, 0, 0);

    }


    //保存退出popupwindow
    private void showPopupWindow(LinearLayout root) {

        //设置显示的视图
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.back_popupwindow_layout, null);
        Button btnCamera = view.findViewById(R.id.btn_save);
        Button btnPhoto = view.findViewById(R.id.btn_dissave);
        Button btnCancel = view.findViewById(R.id.btn_cancel1);
        //为弹出框中的每一个按钮
        ClickListener listener = new ClickListener();
        btnCamera.setOnClickListener(listener);
        btnPhoto.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        //将自定义的视图添加到 popupWindow 中
        window.setContentView(view);
        //控制 popupwindow 再点击屏幕其他地方时自动消失
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在弹窗消失时调用
                addBackgroundAlpha(1f);
            }
        });
        //显示 popupWindow 设置 弹出框的位置
        window.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    addBackgroundAlpha(1f);

                    break;
                case R.id.btn_dissave:
                    addBackgroundAlpha(1f);

                    break;

                case R.id.btn_cancel1:

                    break;
            }
        }
    }

    // 弹出选项框时为背景加上透明度
    private void addBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }


    //显示toast
    public void showtoast() {
        final Toast toastTip1
                = Toast.makeText(FabuActivity.this,
                "后台上传中，请稍后……",
                Toast.LENGTH_LONG);
        final Toast toastTip2
                = Toast.makeText(FabuActivity.this,
                "笔记已成功发布喽",
                Toast.LENGTH_LONG);
        toastTip1.setGravity(Gravity.CENTER, 0, 0);
        toastTip2.setGravity(Gravity.CENTER, 0, 0);
        toastTip1.show();
        toastTip2.show();
    }

    class fabuAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String urlbase = getResources().getString(R.string.server_path) + "/note/addBaseNote";
            Log.e("fr", urlbase);
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),
                    jsonObject.toString());
            Log.e("fr",jsonObject.toString());
            Request request = new Request.Builder()
                    .post(requestBody)
                    .url(urlbase).build();
            Call call = okHttpClient.newCall(request);
            Log.e("fr","开始执行");
            try {
                call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (code.equals("1") || code.equals("2")) {
                //传图片
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        String url=getResources().getString(R.string.server_path)+"/note/addPic";
                        UpLoadUtil1 upLoadUtil = new UpLoadUtil1(url);
                        upLoadUtil.execute(path);
                        Log.e("upLoadImage","uploadimage");
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 3000);
                Log.e("fabu_btn",path);
            }
        }
    }

}













