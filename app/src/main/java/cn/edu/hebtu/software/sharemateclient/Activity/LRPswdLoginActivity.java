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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private TextView tvSpinner;
    private Button btnSpinner;
    private ImageView back;
    private Button btnLogin;
    private TextView forgetPswd;
    private TextView codeLogin;
    private EditText etPhone;
    private String phone;
    private EditText etPassword;
    private String password;
    private UserBean user = new UserBean();
    private boolean resultPhone;
    private boolean resultPassword;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pswd_login);
        path = getResources().getString(R.string.server_path);
        findViews();
        back.setOnClickListener(new backClickListener());
        btnLogin.setOnClickListener(new ButtonClickListener());
        forgetPswd.setOnClickListener(new forgetPswdClickListener());
//        codeLogin.setOnClickListener(new codeLoginClickListener());
        btnSpinner.setOnClickListener(new SpinnerClickListener());
        tvSpinner.setOnClickListener(new SpinnerClickListener());
        etPhone.setOnFocusChangeListener(new FocusChangeListener());
        etPassword.setOnFocusChangeListener(new FocusChangeListener());
    }
    private void findViews(){
        back = findViewById(R.id.iv_back);
        btnLogin = findViewById(R.id.btn_login);
        forgetPswd = findViewById(R.id.tv_forget_password);
//        codeLogin = findViewById(R.id.tv_code_login);
        btnSpinner = findViewById(R.id.btn_spinner);
        tvSpinner = findViewById(R.id.tv_spinner);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
    }

    /**
     * 根据手机号和密码判断该用户是否存在
     */
    private class FocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case R.id.et_phone:
                    if (hasFocus){

                    }else {
                        phone = etPhone.getText().toString();
                        Log.e("phone",phone);
                        //判断手机号码格式,11位数字
                        resultPhone = TelephoneUtils.isPhone(phone);
                        if (resultPhone == true){
                            user.setUserPhone(phone);
                        }else {
                            Toast.makeText(LRPswdLoginActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.et_password:
                    if (hasFocus){

                    }else {
                        password = etPassword.getText().toString();
                        Log.e("password",password);
                        //判断密码格式,8-16位数字和字母
                        resultPassword = PasswordUtils.isPassword(password);
                        if (resultPassword == true){
                            user.setUserPassword(password);
                        }else {
                            Toast.makeText(LRPswdLoginActivity.this,"请输入正确的密码",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
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
    //点击登录按钮
    private class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            btnLogin.setFocusable(true);//设置可以获取焦点，但不一定获得
            btnLogin.setFocusableInTouchMode(true);
            btnLogin.requestFocus();//要获取焦点
            if (!phone.equals("") && !password.equals("")){
                PhonePswdLoginUtil phonePswdLoginUtil = new PhonePswdLoginUtil();
                phonePswdLoginUtil.execute(user);
            }else {
                Toast.makeText(LRPswdLoginActivity.this,"请输入手机号或密码",Toast.LENGTH_SHORT).show();
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
            List<Integer> typeList=null;
            String msg="";
            int userId=0;
            List<Object> objectList=new ArrayList<>();
            try {
                result = call.execute().body().string();
                Log.e("PwsdResult--",result);
                typeList=new ArrayList<>();
                JSONArray jsonArray= new JSONArray(result);
                Log.e("jsonArray",jsonArray.toString());
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject obj=jsonArray.getJSONObject(i);
                    int typeId=obj.getInt("typeId");
                    typeList.add(typeId);
                    msg=obj.getString("msg");
                    userId=obj.getInt("userId");
                }
                objectList.add(typeList);
                objectList.add(msg);
                objectList.add(userId);
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
            String result =  (String)objectList.get(1);
            Log.e("result", result);
            if (result.equals("该用户存在")) {
                int userId = (Integer)objectList.get(2);
                Log.e("userId",userId+"");
                Intent intent = new Intent(LRPswdLoginActivity.this, MainActivity.class);
                intent.putExtra("userId", userId);
                intent.putIntegerArrayListExtra("type",(ArrayList<Integer>)objectList.get(0));
                intent.putExtra("flag","main");
                startActivity(intent);
            } else if (result.equals("该用户不存在")) {
                Toast.makeText(LRPswdLoginActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
            }
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
}
