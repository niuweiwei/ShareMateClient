package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.UpLoadUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 个人资料页
 *
 * @author fengjiaxing
 * @date 2019/5/13
 */
public class PerPersonalActivity extends AppCompatActivity {
    //相机拍摄的头像文件(本次演示存放在SD卡根目录下)
    private static final File file = new File(Environment.getExternalStorageDirectory() + "/CoolImage/", System.currentTimeMillis() + ".jpg");
    //请求识别码(分别为本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;


    private TextView tv_name, tv_sex, tv_id, tv_address, tv_birth, tv_introduce;
    private ImageView iv_back;
    private ImageView iv_head;//头像
    private LinearLayout layoutHead, layoutName, layoutSex, layoutBirth, layoutAddress, layoutIntro, rootLayout;
    private PopupWindow popupWindow;
    private ArrayList<Integer> type;
    private String path;
    private UserBean user;
    private String sex;
    private String birth;
    private TextView finish;//完成
    private boolean update = false;
    private String filePath ;

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

    public void setContent() {
        tv_address.setText(user.getUserAddress());
        tv_birth.setText(user.getUserBirth());
        tv_sex.setText(user.getUserSex());
        String userId = String.format("%06d", user.getUserId());
        tv_id.setText(userId);
        tv_name.setText(user.getUserName());
        final String photoPath = path + "/" + user.getUserPhoto();
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(this).load(photoPath).apply(mRequestOptions).into(iv_head);
        if (user.getUserIntro() == null || user.getUserIntro().length() < 7) {
            tv_introduce.setText(user.getUserIntro());
        } else {
            tv_introduce.setText(user.getUserIntro().substring(0, 6) + "...");
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
                    update = true;
                    showPopupWindow();
                    break;
                case R.id.ly_head://修改头像
                    update = true;
                    showPopupWindow();
                    break;
                case R.id.ly_name:
                    Intent userIntent = new Intent();
                    userIntent.setClass(PerPersonalActivity.this, PerNameActivity.class);
                    userIntent.putExtra("user", user);
                    startActivityForResult(userIntent, 3);
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
                    startActivityForResult(addIntent, 4);
                    break;
                case R.id.ly_intro:
                    Intent introIntent = new Intent();
                    introIntent.setClass(PerPersonalActivity.this, PerAddressActivity.class);
                    introIntent.putExtra("user", user);
                    introIntent.putExtra("msg", "个性签名");
                    startActivityForResult(introIntent, 5);
                    break;
                case R.id.back://返回
                    Intent myIntent = new Intent(PerPersonalActivity.this, MainActivity.class);
                    myIntent.putExtra("flag", "my");
                    myIntent.putExtra("user",user);
                    startActivity(myIntent);
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
//        Button openCam = view.findViewById(R.id.btn_openCam);
        Button cancle = view.findViewById(R.id.btn_cancle);
        //打开手机相册
        openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, 2);
                popupWindow.dismiss();
            }
        });
        //打开相机
//        openCam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String state = Environment.getExternalStorageState();// 获取内存卡可用状态
//                if (state.equals(Environment.MEDIA_MOUNTED)) {
//                    // 内存卡状态可用
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    startActivityForResult(intent, 1);
//                } else {
//                    // 不可用
//                    Log.e("sd卡","内存不可用");
//                }
//                popupWindow.dismiss();
//            }
//        });
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
                filePath = file.getPath();
                photoClip(Uri.fromFile(file));
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
                filePath = picturePath;
                cursor.close();
                photoClip(data.getData());
                break;
            case CODE_PHOTO_CLIP://裁剪返回值
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
        if (requestCode == 3 && resultCode == 200) {
            user = (UserBean) data.getSerializableExtra("responseUser");
            setContent();
        }
        if (requestCode == 4 && resultCode == 200) {
            user = (UserBean) data.getSerializableExtra("responseUser");
            setContent();
        }
        if (requestCode == 5 && resultCode == 200) {
            user = (UserBean) data.getSerializableExtra("responseUser");
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
            String url = path +"/user/updatePhoto?userId="+user.getUserId();
            UpLoadPhoto upLoadPhoto = new UpLoadPhoto();
            upLoadPhoto.execute(url,filePath);
        }
    }

    public class UpdateUser extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean user = (UserBean) objects[0];
            user.setUserPhotoPath(path + "/" + user.getUserPhoto());
            Gson gson = new Gson();
            String str = gson.toJson(user);
            Log.e("gson_user", str);
            String url = path + "/user/updateUser";
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType type = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(type, str);
            Request request = new Request.Builder().url(url).post(body).build();
            Call call = okHttpClient.newCall(request);
            UserBean u = null;
            try {
                String result = call.execute().body().string();
                Log.e("UpdateResult---", result);
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonUser = jsonObject.getJSONObject("user");
                Gson gson1 = new Gson();
                u = gson1.fromJson(jsonUser.toString(),UserBean.class);
                Log.e("PersonalActivity",u.getUserPhone());
                if (u != null){
                    Log.e("PersonActivity","用户更新成功");
                }else{
                    Log.e("PersonActivity","用户更新失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return u;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            UserBean user = (UserBean) o;
            Intent intent = new Intent(PerPersonalActivity.this,MainActivity.class);
            intent.putExtra("flag","my");
            intent.putExtra("user",user);
            startActivity(intent);
//            String url = path +"/user/updatePhoto?userId="+user.getUserId();
//            UpLoadUtil upLoadUtil = new UpLoadUtil(url,getApplicationContext(),user);
//            upLoadUtil.execute(filePath);
        }
    }

    public class UpLoadPhoto extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String path = (String) objects[0];
            String BOUNDARY = UUID.randomUUID().toString();
            String PREFIX = "--", LINE_END = "\r\n";
            String CONTENT_TYPE = "multipart/form-data";
            File file = new File((String) objects[1]);
            try {
                Log.e("path",path);
                URL url = new URL(path);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true); // 允许输入流
                con.setDoOutput(true); // 允许输出流
                con.setUseCaches(false); // 不允许使用缓存
                con.setRequestMethod("POST"); // 请求方式
                con.setRequestProperty("connection", "keep-alive");
                con.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
                con.setRequestProperty("Charset", "UTF-8");
                Log.e("con","con");
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=utf-8" + LINE_END);
                sb.append(LINE_END);
                Log.e("sb","sb");
                dos.write(sb.toString().getBytes());
                Log.e("is","is");
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                Log.e("dos","dos");
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                int res = con.getResponseCode();
                if (res == 200) {
                    Log.e("test","上传成功");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
