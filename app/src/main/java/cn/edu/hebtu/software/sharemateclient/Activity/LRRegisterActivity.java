package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

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
    private UserBean user = new UserBean();
    private String path;
    private OkHttpClient okHttpClient;
    //输入错误时提示文字
    private TextView erName,erPassword,erConfirmPwsd,erPhone;
    private boolean resultName,resultPwsd,resultPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        path = getResources().getString(R.string.server_path);
        findViews();
        setListeners();
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
        erConfirmPwsd = findViewById(R.id.error_confirm_pwsd);
        erName = findViewById(R.id.error_name);
        erPassword = findViewById(R.id.error_password);
        erPhone = findViewById(R.id.error_phone);
    }
    private void setListeners(){
        back.setOnClickListener(new backClickListener());
        ivCode.setOnClickListener(new idtfCodeClickListener());
        btnTrue.setOnClickListener(new ButtonClickListener());
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
            //清除错误信息
            erConfirmPwsd.setText(null);
            erPhone.setText(null);
            erPassword.setText(null);
            erName.setText(null);
            //获取输入信息
            String ImageCode = etCode.getText().toString().toLowerCase();
            userName = etUsername.getText().toString().trim();
            userPassword = etPassword.getText().toString().trim();
            confirmPawd = etConfirmPawd.getText().toString().trim();
            userPhone = etPhone.getText().toString().trim();
            Log.e("message", userName+" "+userPassword+" "+confirmPawd+" "+userPhone);
            //判断
            if (ImageCode.equals(realCode)) {
//                Toast.makeText(LRRegisterActivity.this, ImageCode + "验证码正确", Toast.LENGTH_SHORT).show();

                if (!userName.equals("")&&!userPassword.equals("")&&!userPhone.equals("")){
                    resultName = UsernameUtils.isName(userName);
                    resultPhone = TelephoneUtils.isPhone(userPhone);
                    resultPwsd = PasswordUtils.isPassword(userPassword);
                    if (resultName == true){
                        user.setUserName(userName);
                    }else{
                        erName.setText("请输入10个字符以内的用户名，支持中英文、数字、减号或下划线");
                    }
                    if (resultPwsd == true){
                        if (confirmPawd.equals(userPassword)){
                            user.setUserPassword(userPassword);
                        }else{
                            erConfirmPwsd.setText("两次密码不一致");
                        }
                    }else {
                        erPassword.setText("请输入8-16位由数字和字母组成的密码");
                    }
                    if (resultPhone == true){
                        user.setUserPhone(userPhone);
                    }else {
                        erPhone.setText("请输入正确格式的手机号");
                    }
                    Log.e("user",user.getUserName()+" "+user.getUserPassword()+" "+user.getUserPhone());
                    RegisterUtil registerUtil = new RegisterUtil();
                    registerUtil.execute(user);
                }else{
                    Toast.makeText(LRRegisterActivity.this, ImageCode + "输入信息不能为空", Toast.LENGTH_SHORT).show();
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
            }
            if (result.equals("该用户已经注册")){
                Toast.makeText(LRRegisterActivity.this,"该用户已注册",Toast.LENGTH_SHORT).show();
            }
            if (result.equals("注册失败")){
                Toast.makeText(LRRegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
