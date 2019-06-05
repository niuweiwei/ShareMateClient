package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.PasswordUtils;
import cn.edu.hebtu.software.sharemateclient.tools.TelephoneUtils;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LRPswdLoginActivity extends AppCompatActivity {
    private TextView error;
    private TextView tvSpinner;
    private Button btnSpinner;
    private ImageView back;
    private Button btnLogin;
    private TextView forgetPswd;
    private EditText etPhone;
    private String phone;
    private EditText etPassword;
    private String password;
    private UserBean user = new UserBean();
    private boolean resultPhone;
    private boolean resultPassword;
    private String path;
    private RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pswd_login);
        path = getResources().getString(R.string.server_path);
        findViews();
        setListeners();
    }
    private void findViews(){
        back = findViewById(R.id.iv_back);
        btnLogin = findViewById(R.id.btn_login);
        forgetPswd = findViewById(R.id.tv_forget_password);
        btnSpinner = findViewById(R.id.btn_spinner);
        tvSpinner = findViewById(R.id.tv_spinner);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        error = findViewById(R.id.error);
        root = findViewById(R.id.root);
    }
    private void setListeners(){
        //返回按钮
        back.setOnClickListener(new backClickListener());
        //登录按钮
        btnLogin.setOnClickListener(new ButtonClickListener());
        //忘记密码
        forgetPswd.setOnClickListener(new forgetPswdClickListener());
        //下拉菜单
        btnSpinner.setOnClickListener(new SpinnerClickListener());
        tvSpinner.setOnClickListener(new SpinnerClickListener());
    }
    //选择地区和地区代码
    private class SpinnerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRPswdLoginActivity.this,LRCountryActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case 12:
                if (resultCode == RESULT_OK)
                {
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
    //点击返回
    private class backClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRPswdLoginActivity.this,LRLoginActivity.class);
            startActivity(intent);
        }
    }
    //忘记密码
    private class forgetPswdClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRPswdLoginActivity.this,LRForgetPwsdActivity.class);
            startActivity(intent);
        }
    }
    //点击登录按钮
    private class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            phone = etPhone.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            Log.e("phone&password",phone+"  "+password);
            resultPhone = TelephoneUtils.isPhone(phone);
            resultPassword = PasswordUtils.isPassword(password);
            if (phone != null && !phone.equals("") && password != null && !password.equals("")) {
                if (resultPhone == true && resultPassword == true) {
                    user.setUserPhone(phone);
                    user.setUserPassword(password);
                    saveRegisterInfo("phoneStr", phone);
                    PhonePswdLoginUtil phonePswdLoginUtil = new PhonePswdLoginUtil();
                    phonePswdLoginUtil.execute(user);
                } else {
                    error.setText("手机号或密码格式不正确");
                }
            }else{
                error.setText("手机号或密码不能为空");
            }
        }
    }

    /**
     * 异步任务
     */
    public class PhonePswdLoginUtil extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e("PhonePswdLoginUtil", "异步任务");
            UserBean user = (UserBean) objects[0];
            Gson gson = new Gson();
            String userStr = gson.toJson(user);
            String url = path + "/user/login2";
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,userStr);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().post(body).url(url).build();
            Call call = okHttpClient.newCall(request);

            String result=null;
            List<Integer> typeList=new ArrayList<>();
            String msg="";
            UserBean u = null;
            List<Object> objectList=new ArrayList<>();
            try {
                result = call.execute().body().string();
                Log.e("PwsdResult--",result);
                JSONObject jsonObject = new JSONObject(result);
                JSONObject mapObject = jsonObject.getJSONObject("map");
                String typeStr =  mapObject.get("typeList").toString();
                typeList = gson.fromJson(typeStr,new TypeToken<List<Integer>>(){}.getType());
                String uStr = mapObject.get("user").toString();
                u = gson.fromJson(uStr,UserBean.class);
                msg = (String) mapObject.get("msg");
                objectList.add(typeList);
                objectList.add(u);
                objectList.add(msg);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return objectList;
        }

        @Override
        protected void onPostExecute(Object o) {
            List<Object> objectList = (List<Object>)o;
            Log.e("onPostExecute",objectList.toString());
            UserBean u = (UserBean) objectList.get(1);
            String msg = (String) objectList.get(2);
            if (msg.equals("该用户存在")) {
                Log.e("userId",u.getUserId()+"");

                Intent intent = new Intent(LRPswdLoginActivity.this, MainActivity.class);
                intent.putExtra("user", u);
                intent.putIntegerArrayListExtra("type",(ArrayList<Integer>)objectList.get(0));
                intent.putExtra("flag","main");
                startActivity(intent);
            }
            if (msg.equals("该用户不存在")) {
                Log.e("error1",msg);
                error.setText(msg);
            }
            if(msg.equals("密码输入错误")){
                Log.e("error2",msg);
                error.setText(msg);
            }
        }
    }
    /**
     * 保存账号和密码到SharedPreferences中
     */
    private void saveRegisterInfo(String phoneStr, String phone) {
        //"loginInfo" ：文件名；mode_private SharedPreferences sp = getSharedPreferences( );
        //MODE_PRIVATE:代表私有访问模式,在Android 2.3及以前这个访问模式是可以跨进程的,之后的版本这个模式就只能访问同一进程下的数据.
        //MODE_MULTI_PROCESS:在Android 2.3及以前，这个标志位都是默认开启的，允许多个进程访问同一个SharedPrecferences对象。而以后的Android版本，必须通过明确的将MODE_MULTI_PROCESS这个值传递给mode参数，才能开启多进程访问。
        //MODE_WORLD_READABLE: 表示当前文件可以被其他应用读取
        //MODE_WORLD_WRITEABLE: 表示当前文件可以被其他应用写入
        //MODE_APPEND: 追加方式存储
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_MULTI_PROCESS);
        //获取编译器
        SharedPreferences.Editor editor = sp.edit();
        //以用户名为key，密码为value 保存在SharedPreferences中
        editor.putString(phoneStr, phone);
        //提交修改
        editor.commit();
    }
}
