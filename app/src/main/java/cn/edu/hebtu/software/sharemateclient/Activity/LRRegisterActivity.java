package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.IdentifyingCode;
import cn.edu.hebtu.software.sharemateclient.tools.PasswordUtils;
import cn.edu.hebtu.software.sharemateclient.tools.TelephoneUtils;
import cn.edu.hebtu.software.sharemateclient.tools.UsernameUtils;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LRRegisterActivity extends AppCompatActivity {
    private ImageView back;
    private Button btnTrue;
    private ImageView ivCode;
    private EditText etCode;
    private String realCode;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etPhone;
    private EditText etConfirmPawd;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String confirmPawd;
    private UserBean user;
    private String path;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        path = getResources().getString(R.string.server_path);
        findViews();
        back.setOnClickListener(new backClickListener());
        ivCode.setOnClickListener(new idtfCodeClickListener());
        judgeForm();
    }
    private void findViews() {
        back = findViewById(R.id.iv_back);
        btnTrue = findViewById(R.id.btn_true);
        ivCode = findViewById(R.id.iv_showCode);
        etCode = findViewById(R.id.et_phoneCodes);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etPhone = findViewById(R.id.et_phone);
        etConfirmPawd = findViewById(R.id.et_confirm_password);
    }
    /**
     * 判断输入的数据格式是否正确
     */
    private void judgeForm() {
        user = new UserBean();
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    userName = etUsername.getText().toString();
                    //判断用户名格式,限16个字符，支持中英文、数字、减号或下划线
                    boolean resultName = UsernameUtils.isName(userName);
                    Log.e("userName",userName);
                    Log.e("resultName",resultName+"");
                    if (resultName == false) {
                        Toast.makeText(LRRegisterActivity.this,"请输入16个字符以内的用户名，支持中英文、数字、减号或下划线",
                                Toast.LENGTH_SHORT).show();
//                        etUsername.setError("有错误信息");
                    } else {
                        user.setUserName(userName);
                    }
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    userPassword = etPassword.getText().toString();
                    //判断密码格式,8-16位数字和字母
                    boolean resultPassword = PasswordUtils.isPassword(userPassword);
                    Log.e("userPassword",userPassword);
                    Log.e("resultPassword",resultPassword+"");
                    if (resultPassword == false) {
                        Toast.makeText(LRRegisterActivity.this, "请输入8-16位由数字和字母组成的密码",
                                Toast.LENGTH_SHORT).show();
//                        etPassword.setError("密码错误");
                    } else {
                        user.setUserPassword(userPassword);
                    }
                }
            }
        });
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    userPhone = etPhone.getText().toString();
                    //判断手机号码格式,11位数字
                    boolean resultPhone = TelephoneUtils.isPhone(userPhone);
                    Log.e("userPhone",userPhone);
                    Log.e("resultPhone",resultPhone+"");
                    if (resultPhone == false) {
                        Toast.makeText(LRRegisterActivity.this, "请输入正确格式的手机号",
                                Toast.LENGTH_SHORT).show();
//                        etPhone.setError("手机号码错误");
                    } else {
                        user.setUserPhone(userPhone);
                    }
                }
            }
        });
        etConfirmPawd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    confirmPawd = etConfirmPawd.getText().toString();
                    Log.e("confirmPawd",confirmPawd);
                    if (userPassword.equals(confirmPawd)) {
                        btnTrue.setOnClickListener(new ButtonClickListener());
                    } else {
                        Toast.makeText(LRRegisterActivity.this, "两次密码输入不一样", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    /**
     * 异步任务
     */
    public class RegisterUtil extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("RegisterUtil", "异步任务");
            UserBean user = (UserBean) objects[0];
            Gson gson = new Gson();
            String userStr = gson.toJson(user);
            String url = path +"/user/register";
            MediaType type = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(type,userStr);
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().post(body).url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                result = call.execute().body().string();
                Log.e("RegisterResult",result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            String result = (String)o;
            Log.e("RegisterUtil",result);
            if(result.equals("注册成功")){
                Log.e("RegisterUtil", "文字上传成功");
                Intent intent = new Intent(LRRegisterActivity.this, LRStartActivity.class);
                startActivity(intent);
            }else if (result.equals("该用户已经注册")){
                Toast.makeText(LRRegisterActivity.this,"该用户已注册",Toast.LENGTH_SHORT).show();
            }else if (result.equals("注册失败")){
                Toast.makeText(LRRegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 点击返回
     */
    private class backClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRRegisterActivity.this, LRLoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 点击确定按钮
     */
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String ImageCode = etCode.getText().toString().toLowerCase();
            if (ImageCode.equals(realCode)) {
//                Toast.makeText(LRRegisterActivity.this, ImageCode + "验证码正确", Toast.LENGTH_SHORT).show();

                //上传数据
                btnTrue.setFocusable(true);//设置可以获取焦点，但不一定获得
                btnTrue.setFocusableInTouchMode(true);
                btnTrue.requestFocus();//要获取焦点
                if (!userName.equals("") && !userPassword.equals("") && !userPhone.equals("")) {
                    RegisterUtil registerUtil = new RegisterUtil();
                    registerUtil.execute(user);
                    Log.e("LRRegisterActivity", "上传数据");
                } else {
                    Toast.makeText(LRRegisterActivity.this, "请输入信息", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LRRegisterActivity.this, ImageCode + "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 生成随机验证码图片
     */
    private class idtfCodeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ivCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
            realCode = IdentifyingCode.getInstance().getCode().toLowerCase();
        }
    }
}
