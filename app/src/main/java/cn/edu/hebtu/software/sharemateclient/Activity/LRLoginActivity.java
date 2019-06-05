package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.Fragment.FollowFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.HomeFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.MessageFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.MyFragment;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.TelephoneUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LRLoginActivity extends AppCompatActivity {

    private TextView tvSpinner, pswdLogin, register;
    private Button btnSpinner, btnTrue;
    private EditText etPhone;
    private String phone, path;
    private UserBean user = new UserBean();
    private OkHttpClient okHttpClient;
    private boolean isLogin = false, resultPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            findViews();
            setListener();
            path = getResources().getString(R.string.server_path);
    }

    /**
     * 保存账号和密码到SharedPreferences中
     */
    private void saveRegisterInfo(String phoneStr, String phone) {
        //"loginInfo" ：文件名；mode_private SharedPreferences sp = getSharedPreferences( );
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_MULTI_PROCESS);
        //获取编译器
        SharedPreferences.Editor editor = sp.edit();
        //以用户名为key，密码为value 保存在SharedPreferences中
        editor.putString(phoneStr, phone);
        //提交修改
        editor.commit();
    }

    private void findViews() {
        pswdLogin = findViewById(R.id.pswd_login);
        register = findViewById(R.id.register);
        btnTrue = findViewById(R.id.btn_true);
        tvSpinner = findViewById(R.id.tv_spinner);
        btnSpinner = findViewById(R.id.btn_spinner);
        etPhone = findViewById(R.id.et_phone);
    }

    private void setListener() {
        pswdLogin.setOnClickListener(new pswdClickListener());
        btnTrue.setOnClickListener(new trueClickListener());
        register.setOnClickListener(new registerClickListener());
        tvSpinner.setOnClickListener(new SpinnerClickListener());
        btnSpinner.setOnClickListener(new SpinnerClickListener());
    }

    /**
     * 选择地区和地区代码
     */
    private class SpinnerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRLoginActivity.this, LRCountryActivity.class);
            startActivityForResult(intent, 12);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 12:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String countryNumber = bundle.getString("countryNumber");
                    tvSpinner.setText(countryNumber);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 点击密码登录
     */
    private class pswdClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRLoginActivity.this, LRPswdLoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 点击确定按钮
     */
    private class trueClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            phone = etPhone.getText().toString();
            Log.e("phone", phone);
            if (!"".equals(phone)) {
                //判断手机号码格式,11位数字
                resultPhone = TelephoneUtils.isPhone(phone);
                if (resultPhone == true) {
                    user.setUserPhone(phone);
                    saveRegisterInfo("phoneStr", phone);
                    LoginUtil loginUtil = new LoginUtil();
                    loginUtil.execute(user);
                } else {
                    Toast.makeText(LRLoginActivity.this, "请输入正确格式的手机号",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LRLoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 异步任务
     */
    private class LoginUtil extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("LoginUtil", "异步任务");
            UserBean user = (UserBean) objects[0];
            String userPhone = user.getUserPhone();
            String url = path + "/user/login?userPhone=" + userPhone;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            JSONObject jsonObject = null;
            UserBean u = null;
            try {
                result = call.execute().body().string();
                Log.e("LoginResult---", result);
                JSONObject object = new JSONObject(result);
                String str = object.getString("user");
                Gson gson = new Gson();
                u = gson.fromJson(str,UserBean.class);
                Log.e("u",u.getUserPhone());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return u;
        }

        @Override
        protected void onPostExecute(Object o) {
            UserBean u = (UserBean) o;
            if (u != null) {
                Intent intent = new Intent(LRLoginActivity.this, LRInputCodeActivity.class);
                intent.putExtra("user", u);
                startActivity(intent);
            } else {
                Log.e("login","该用户不存在");
            }

        }
    }

    /**
     * 注册
     */
    private class registerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRLoginActivity.this, LRRegisterActivity.class);
            startActivity(intent);
        }
    }
}
