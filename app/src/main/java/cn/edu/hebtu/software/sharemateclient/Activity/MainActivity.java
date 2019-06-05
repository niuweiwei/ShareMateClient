package cn.edu.hebtu.software.sharemateclient.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.pili.droid.shortvideo.PLAudioEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLCameraSetting;
import com.qiniu.pili.droid.shortvideo.PLFaceBeautySetting;
import com.qiniu.pili.droid.shortvideo.PLMicrophoneSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordStateListener;
import com.qiniu.pili.droid.shortvideo.PLShortAudioRecorder;
import com.qiniu.pili.droid.shortvideo.PLShortVideoRecorder;
import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Fragment.FollowFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.HomeFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.MessageFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.MyFragment;
import cn.edu.hebtu.software.sharemateclient.R;

public class MainActivity extends AppCompatActivity {

    private TextView indexView;
    private TextView followView;
    private TextView messageView;
    private TextView myView;
    private HomeFragment indexFragment = new HomeFragment();
    private FollowFragment followFragment = new FollowFragment();
    private MessageFragment messageFragment = new MessageFragment();
    private MyFragment myFragment = new MyFragment();
    private FragmentManager manager ;
    private Fragment currentFragment = new Fragment();
    private List<TextView> views = new ArrayList<>();
    //弹出框
    private PopupWindow window=null;
    private Button sharebutton;
    private RelativeLayout root = null;
    //短视频
    public static final String ENCODING_MODE = "EncodingMode";

    public static final String AUDIO_CHANNEL_NUM = "AudioChannelNum";
//    private Spinner mEncodingModeLevelSpinner;
//    private Spinner mAudioChannelNumSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indexView = findViewById(R.id.tv_index);
        followView = findViewById(R.id.tv_follow);
        messageView = findViewById(R.id.tv_message);
        myView = findViewById(R.id.tv_my);
//        mEncodingModeLevelSpinner = (Spinner) findViewById(R.id.EncodingModeLevelSpinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, RecordSettings.ENCODING_MODE_LEVEL_TIPS_ARRAY);
//        mEncodingModeLevelSpinner.setAdapter(adapter);
//        mEncodingModeLevelSpinner.setSelection(0);
//        mAudioChannelNumSpinner = (Spinner) findViewById(R.id.AudioChannelNumSpinner);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, RecordSettings.AUDIO_CHANNEL_NUM_TIPS_ARRAY);
//        mAudioChannelNumSpinner.setAdapter(adapter);
//        mAudioChannelNumSpinner.setSelection(0);



        manager = getSupportFragmentManager();
        //默认显示首页
        showFragment(indexFragment);
        //调用为每个选项绑定事件监听器的方法
        setClickListener();


        //实现发布的popupwindow
        root=findViewById(R.id.root);
        window=new PopupWindow(root,RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        sharebutton=findViewById(R.id.btn_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.isShowing()){
                    window.dismiss();
                }else{
                    showPopupWindow(root);
                    addBackgroundAlpha(0.7f);
                }
            }
        });

    }

    //显示出指定的页面
    private void showFragment(Fragment fragment){
        //创建 fragment 事务
        FragmentTransaction transaction = manager.beginTransaction();
        //判断传入的fragment 是否是当前正在显示的fragment
        if(fragment != currentFragment)
            transaction.hide(currentFragment);
        //判断要展示的 fragment 是否被添加过
        if(!fragment.isAdded())
            transaction.add(R.id.content,fragment);
        transaction.show(fragment);
        //提交事务
        transaction.commit();
        currentFragment = fragment;
    }

    //为每一个 选项卡 (模拟的选项) 添加监听器
    private void setClickListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_index:
                        showFragment(indexFragment);
                        break;
                    case R.id.tv_follow:
                        showFragment(followFragment);
                        break;
                    case R.id.tv_message:
                        showFragment(messageFragment);
                        break;
                    case R.id.tv_my:
                        showFragment(myFragment);
                        break;
                }

                //点击改变效果
                for(TextView view : views){
                    TextView tmp = findViewById(v.getId());
                    if(tmp == view){
                        view.setTextColor(getResources().getColor(R.color.inkGray));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                    }else{
                        view.setTextColor(getResources().getColor(R.color.deepGray));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                    }
                }
            }

        };
        //每个选项绑定点击事件监听器
        indexView.setOnClickListener(listener);
        followView.setOnClickListener(listener);
        messageView.setOnClickListener(listener);
        myView.setOnClickListener(listener);
    }
    //点击按钮后弹出选项框
    private void showPopupWindow(RelativeLayout root){

        //设置显示的视图
        LayoutInflater inflater =getLayoutInflater();
        View view = inflater.inflate(R.layout.share_popupwindow_layout,null);
        Button btnCamera = view.findViewById(R.id.btn_camera);
        Button btnPhoto = view.findViewById(R.id.btn_photos);
        Button btnRadio=view.findViewById(R.id.btn_radio);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        //为弹出框中的每一个按钮
        ClickListener listener = new ClickListener();
        btnCamera.setOnClickListener(listener);
        btnPhoto.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
        btnRadio.setOnClickListener(listener);

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
        window.showAtLocation(root, Gravity.BOTTOM,0,0);
    }

    // 弹出选项框时为背景加上透明度
    private void addBackgroundAlpha(float alpha){
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }



    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_camera:
                    String state = Environment.getExternalStorageState();// 获取内存卡可用状态
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        // 内存卡状态可用
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, 1);
                    } else {
                        // 不可用
                        Log.e("sd卡","内存不可用");
                    }
                    break;
                case R.id.btn_photos:
                    Intent intent1=new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1, 2);
                    break;
                case R.id.btn_radio:

                    //Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                   // startActivityForResult(intent, 3);
                    Intent intent = new Intent(MainActivity.this, VideoRecordActivity.class);
                    intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_RATIO, "4:3");

                    intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_LEVEL, "720P");

                    intent.putExtra(VideoRecordActivity.ENCODING_MODE,"单声道");

                    intent.putExtra(VideoRecordActivity.ENCODING_SIZE_LEVEL, "480*480");

                    intent.putExtra(VideoRecordActivity.ENCODING_BITRATE_LEVEL, "1000kbps");

                    intent.putExtra(VideoRecordActivity.AUDIO_CHANNEL_NUM,"HW");
                    startActivity(intent);

                case R.id.btn_cancel:
                    window.dismiss();
                    break;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String requestCode1 = String.valueOf(requestCode);
        Log.e("requestCode", requestCode1);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            switch (requestCode) {
                case 1:
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    // 获取 SD 卡根目录 生成图片并
                    String saveDir = Environment
                            .getExternalStorageDirectory()
                            + "/DCIM/Camera";
                    // 新建目录
                    File dir = new File(saveDir);
                    if (!dir.exists())
                        dir.mkdir();
                    // 生成文件名
                    SimpleDateFormat t = new SimpleDateFormat(
                            "yyyyMMddssSSS");
                    String filename = "MT" + (t.format(new Date()))
                            + ".jpg";
                    // 新建文件
                    File file = new File(saveDir, filename);
                    Log.e("路径", file.getPath());
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bos.flush();
                        bos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, FabuActivity.class);
                    String code = "1";
                    intent.putExtra("code", code);
                    intent.putExtra("lujing", file.getPath());///storage/emulated/0/DCIM/Camera/MT2018122121428.jpg
//                    intent.putExtra("userId",userId);
//                    intent.putExtra("type",type);
                    startActivity(intent);
                    break;
                case 2:
                    //打开相册并选择照片，这个方式选择单张
                    // 获取返回的数据，这里是android自定义的Uri地址
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    Log.e("PICFILE", picturePath);
                    cursor.close();
                    // 将图片显示到界面上

                    //加上一个动态获取权限
                    Intent intent1 = new Intent();
                    String code1 = "2";
                    intent1.putExtra("pic1", picturePath);
                    Log.e("PICFILE", picturePath);
                    intent1.putExtra("code", code1);
//                    intent1.putExtra("userId",userId);
//                    intent1.putExtra("type",type);
                    intent1.setClass(MainActivity.this, FabuActivity.class);
                    startActivity(intent1);
                    break;
                case 3:
//                        Uri uri = data.getData();
//                        ContentResolver cr = this.getContentResolver();
//                        String Path=null;
//                        /** 数据库查询操作。
//                         * 第一个参数 uri：为要查询的数据库+表的名称。
//                         * 第二个参数 projection ： 要查询的列。
//                         * 第三个参数 selection ： 查询的条件，相当于SQL where。
//                         * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
//                         * 第四个参数 sortOrder ： 结果排序。
//                         */
//                        assert uri != null;
//                        Cursor cursor1= cr.query(uri, null, null, null, null);
//                        if (cursor1 != null) {
//                            if (cursor1.moveToFirst()) {
//                                // 视频ID:MediaStore.Audio.Media._ID1
//                                int videoId = cursor1.getInt(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
//                                // 视频名称：MediaStre.Audio.Media.TITLE
//                                String title = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
//                                // 视频路径：MediaStore.Audio.Media.DATA
//                                String path = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                                Path=path;
//                                // 视频时长：MediaStore.Audio.Media.DURATION
//                                int duration = cursor1.getInt(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
//                                // 视频大小：MediaStore.Audio.Media.SIZE
//                                long size = cursor1.getLong(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
//                                Log.e("size ", size + "");
//                                // 视频缩略图路径：MediaStore.Images.Media.DATA
//                                String imagePath = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//
//                                // 缩略图ID:MediaStore.Audio.Media._ID
//                                int imageId = cursor1.getInt(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
//                                // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例1
//                                // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。
//                                Bitmap bitmap1 = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
//                                // 方法二 ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
//                                // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
//                                Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);
//                                // 如果追求更好的话可以利用 ThumbnailUtils.extractThumbnail 把缩略图转化为的制定大小
//                                if (duration > 11000) {
//                                    Toast.makeText(getApplicationContext(), "视频时长已超过10秒，请重新选择", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                            }
//                            cursor1.close();
//                            Intent intent2=new Intent();
//                            intent2.putExtra("videopath", Path);
//                            Log.e("PICFILE", Path);
//                            intent2.putExtra("code", "3");
////                    intent1.putExtra("userId",userId);
////                    intent1.putExtra("type",type);
//                            intent2.setClass(MainActivity.this, FabuActivity.class);
//                            startActivity(intent2);


                    //七牛短视频


                        }
            }
        }
    }


