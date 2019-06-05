package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 个人资料页
 * @author fengjiaxing
 * @date 2019/5/13
 */
public class PerPersonalActivity extends AppCompatActivity {
    //相机拍摄的头像文件(本次演示存放在SD卡根目录下)
    private static final File USER_ICON = new File(Environment.getExternalStorageDirectory() + "/CoolImage/", System.currentTimeMillis()+".jpg");
    //请求识别码(分别为本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_id;
    private TextView tv_address;
    private TextView tv_birth;
    private TextView tv_introduce;
    private ImageView iv_back;
    private ImageView iv_head;//头像
    private LinearLayout layoutHead;
    private LinearLayout layoutName;
    private LinearLayout layoutSex;
    private LinearLayout layoutBirth;
    private LinearLayout layoutAddress;
    private LinearLayout layoutIntro;
    private LinearLayout rootLayout;
    private PopupWindow popupWindow;
    private ArrayList<Integer> type;
    private String path;
    private UserBean user;
    private String sex;
    private String birth;
    private TextView finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        type = getIntent().getIntegerArrayListExtra("type");
        path = getResources().getString(R.string.server_path);
        user = (UserBean) getIntent().getSerializableExtra("user");

        findView();
        setListener();
        setContent();
    }

    private void findView() {
        finish = findViewById(R.id.finish);
        iv_back = findViewById(R.id.back);
        iv_head = findViewById(R.id.head);
        tv_name = findViewById(R.id.user);
        tv_id = findViewById(R.id.num);
        tv_sex = findViewById(R.id.sex);
        tv_birth = findViewById(R.id.birth);
        tv_address = findViewById(R.id.address);
        tv_introduce = findViewById(R.id.introduction);
        layoutHead = findViewById(R.id.ly_head);
        layoutName = findViewById(R.id.ly_name);
        layoutAddress = findViewById(R.id.ly_address);
        layoutBirth = findViewById(R.id.ly_birth);
        layoutIntro = findViewById(R.id.ly_intro);
        layoutSex = findViewById(R.id.ly_sex);
        rootLayout = findViewById(R.id.root);
    }

    public void setListener() {
        PerOnClickListener listener = new PerOnClickListener();
        iv_head.setOnClickListener(listener);
        iv_back.setOnClickListener(listener);
        layoutName.setOnClickListener(listener);
        layoutSex.setOnClickListener(listener);
        layoutIntro.setOnClickListener(listener);
        layoutBirth.setOnClickListener(listener);
        layoutAddress.setOnClickListener(listener);
        layoutHead.setOnClickListener(listener);
        finish.setOnClickListener(listener);
    }
    public void setContent(){
        tv_address.setText(user.getUserAddress());
        tv_birth.setText(user.getUserBirth());
        tv_sex.setText(user.getUserSex());
        String userId = String.format("%06d",user.getUserId());
        tv_id.setText(userId);
        tv_name.setText(user.getUserName());
        String photoPath = path+"/"+user.getUserPhoto();
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(this).load(photoPath).apply(mRequestOptions).into(iv_head);
        if (user.getUserIntroduce() == null || user.getUserIntroduce().length() < 7) {
            tv_introduce.setText(user.getUserIntroduce());
        } else {
            tv_introduce.setText(user.getUserIntroduce().substring(0, 6) + "...");
        }
        //完成
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUser updateUser = new UpdateUser();
                updateUser.execute(user);
            }
        });
    }
    public class PerOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head://修改头像
                    showPopupWindow();
                    break;
                case R.id.ly_head://修改头像
                    showPopupWindow();
                    break;
                case R.id.ly_name:
                    Intent userIntent = new Intent();
                    userIntent.setClass(PerPersonalActivity.this, PerNameActivity.class);
                    userIntent.putExtra("user", user);
                    startActivityForResult(userIntent,3);
                    break;
                case R.id.ly_sex:
                    showSexDialog();
                    break;
                case R.id.ly_birth:
                    showBirthDialog();
                    break;
                case R.id.ly_address:
                    Intent addIntent = new Intent();
                    addIntent.setClass(PerPersonalActivity.this, PerAddressActivity.class);
                    addIntent.putExtra("user", user);
                    addIntent.putExtra("msg", "常住地");
                    startActivityForResult(addIntent,4);
                    break;
                case R.id.ly_intro:
                    Intent introIntent = new Intent();
                    introIntent.setClass(PerPersonalActivity.this, PerAddressActivity.class);
                    introIntent.putExtra("user", user);
                    introIntent.putExtra("msg", "个性签名");
                    startActivityForResult(introIntent,5);
                    break;
                case R.id.back://返回
                    if("my".equals(getIntent().getStringExtra("flag"))){
                        Intent myIntent = new Intent(PerPersonalActivity.this,MainActivity.class);
                        myIntent.putExtra("flag","my");
                        startActivity(myIntent);
                    }
                    break;
            }
        }
    }
    //性别选择器
    private void showSexDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择你的性别");
        View v = getLayoutInflater().inflate(R.layout.layout_sex, null);
        final ImageView manView = v.findViewById(R.id.iv_man);
        final ImageView womanView = v.findViewById(R.id.iv_woman);
        final TextView manText = v.findViewById(R.id.man);
        final TextView womanText = v.findViewById(R.id.woman);
        manView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manView.setImageResource(R.drawable.mans);
                womanView.setImageResource(R.drawable.woman);
                sex = manText.getText().toString();
            }
        });
        womanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manView.setImageResource(R.drawable.man);
                womanView.setImageResource(R.drawable.womans);
                sex = womanText.getText().toString();
            }
        });
        builder.setView(v);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_sex.setText(sex);
                user.setUserSex(sex);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //生日日期选择器
    private void showBirthDialog() {
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ConstraintLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ConstraintLayout.LayoutParams.MATCH_PARENT);
        View view = getLayoutInflater().inflate(R.layout.layout_birth, null);
        TextView okText = view.findViewById(R.id.tv_ok);
        TextView canaleText = view.findViewById(R.id.tv_cancle);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd");
                birth = format.format(calendar.getTime());
            }
        });
        popupWindow.setContentView(view);
        addBackgroundAlpha((float) 0.50);
        popupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_birth.setText(birth);
                user.setUserBirth(birth);
                popupWindow.dismiss();
                addBackgroundAlpha((float) 1);
            }
        });
        canaleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                addBackgroundAlpha((float) 1);
            }
        });
    }

    //修改activity的透明度
    private void addBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }

    public void showPopupWindow() {
        popupWindow = new PopupWindow(PerPersonalActivity.this);
        popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        View view = getLayoutInflater().inflate(R.layout.upload_head, null);
        Button openAlbum = view.findViewById(R.id.btn_openAlbum);
        Button openCam = view.findViewById(R.id.btn_openCam);
        Button cancle = view.findViewById(R.id.btn_cancle);
        //打开手机相册
        openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // 获取本地相册方法一
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
                //获取本地相册方法二
                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CODE_PHOTO_REQUEST);
                popupWindow.dismiss();
            }
        });
        //打开相机
        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(USER_ICON));
                startActivityForResult(intent, CODE_CAMERA_REQUEST);
                popupWindow.dismiss();
            }
        });
        //取消
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //用户没有进行有效地设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_CAMERA_REQUEST://相机返回值
                if (USER_ICON.exists()) {
                    photoClip(Uri.fromFile(USER_ICON));
                }
                break;
            case CODE_PHOTO_REQUEST://相册返回值
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case CODE_PHOTO_CLIP://裁剪返回值
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
        if(requestCode == 3 && resultCode == 200){
            user = (UserBean)data.getSerializableExtra("responseUser");
            setContent();
        }
        if(requestCode == 4 && resultCode == 200){
            user = (UserBean)data.getSerializableExtra("responseUser");
            setContent();
        }
        if(requestCode == 5 && resultCode == 200){
            user = (UserBean)data.getSerializableExtra("responseUser");
            setContent();
        }
    }

    /**
     * 对图片进行裁剪
     *
     * @param uri
     */
    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        /*outputX outputY 是裁剪图片宽高
         *这里仅仅是头像展示，不建议将值设置过高
         * 否则超过binder机制的缓存大小的1M限制
         * 报TransactionTooLargeException
         */
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_PHOTO_CLIP);
    }
    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            Glide.with(this).load(photo).apply(mRequestOptions).into(iv_head);
        }
    }

    public class UpdateUser extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean user = (UserBean) objects[0];
            user.setUserPhotoPath(path+"/"+user.getUserPhoto());
            Gson gson = new Gson();
            String str = gson.toJson(user);
            Log.e("gson_user",str);
            String url = path +"/user/updateUser";
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType type = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(type,str);
            Request request = new Request.Builder().url(url).post(body).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("UpdateResult---",result);
                if ("更新成功".equals(result)){
                    Log.e("update","更新用户信息成功");
                }else {
                    Log.e("update","更新用户信息失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
