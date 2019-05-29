package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.TelephoneUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LRLoginActivity extends AppCompatActivity {

    private TextView tvSpinner;
    private Button btnSpinner;
    private TextView pswdLogin;
    private Button btnTrue;
    private TextView register;
    private EditText etPhone;
    private String phone;
    private UserBean user = new UserBean();
    private boolean resultPhone;
    private LoginUtil loginUtil;
    private String path;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setListener();
        path = getResources().getString(R.string.server_path);

    }
    private void findViews(){
        pswdLogin = findViewById(R.id.pswd_login);
        register = findViewById(R.id.register);
        btnTrue = findViewById(R.id.btn_true);
        tvSpinner = findViewById(R.id.tv_spinner);
        btnSpinner = findViewById(R.id.btn_spinner);
        etPhone = findViewById(R.id.et_phone);
    }
    private void setListener(){
        pswdLogin.setOnClickListener(new pswdClickListener());
        btnTrue.setOnClickListener(new trueClickListener());
        register.setOnClickListener(new registerClickListener());
        tvSpinner.setOnClickListener(new SpinnerClickListener());
        btnSpinner.setOnClickListener(new SpinnerClickListener());
//        etPhone.setOnFocusChangeListener(new FocusChangeListener());
    }

    /**
     * 选择地区和地区代码
     */
    private class SpinnerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRLoginActivity.this,LRCountryActivity.class);
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
    private class pswdClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRLoginActivity.this,LRPswdLoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 点击确定按钮
     */
    private class trueClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            phone = etPhone.getText().toString();
            Log.e("phone",phone);
            if (!"".equals(phone)){
                //判断手机号码格式,11位数字
                resultPhone = TelephoneUtils.isPhone(phone);
                if (resultPhone == true){
                    user.setUserPhone(phone);
                    loginUtil = new LoginUtil();
                    loginUtil.execute(user);
                }else {
                    Toast.makeText(LRLoginActivity.this, "请输入正确格式的手机号",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(LRLoginActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 异步任务
     */
    private class LoginUtil extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("LoginUtil","异步任务");
            UserBean user = (UserBean) objects[0];
            String userPhone = user.getUserPhone();
            String url = path +"/user/login?userPhone="+userPhone;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                result = call.execute().body().string();
                Log.e("LoginResult---",result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            JSONObject back = (JSONObject) o;
            String result;
            try {
                result = back.getString("msg");
                Log.e("result",result);
                if (result.equals("用户存在")){
                    int userId = back.getInt("userId");
                    String userPhone = back.getString("userPhone");
                    Intent intent = new Intent(LRLoginActivity.this,LRInputCodeActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("userPhone",userPhone);
                    startActivity(intent);
                }else{
                    Toast.makeText(LRLoginActivity.this,"该用户不存在",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册
     */
    private class registerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRLoginActivity.this,LRRegisterActivity.class);
            startActivity(intent);
        }
    }
}
