package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.VerifyCodeView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LRInputCodeActivity extends AppCompatActivity {
    private int userId;
    private String userPhone;
    private String code;
    private boolean flag;//操作是否成功
    private final String AppKey = "29664f538c740";
    private final String AppSecret = "779a252740ce0b876316a2c8152d1c7d";
    private VerifyCodeView verifyCodeView;
    private ImageView back;
    private Button btnTrue;
    private TextView tvResend;
    //    private TextView tvPassword;
    private TimeCount time;
    private boolean isCode = false;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        path = getResources().getString(R.string.server_path);
        findViews();
        userId = getIntent().getIntExtra("userId",0);

        time = new TimeCount(30000, 1000);
        userPhone = getIntent().getStringExtra("userPhone");
        SMSSDK.getVerificationCode("86", userPhone);// 发送验证码给号码的 phoneNum 的手机
        Log.e("phoneNum",userPhone);
        MobSDK.init(LRInputCodeActivity.this,AppKey,AppSecret);
//        SMSSDK.initSdk(ForgetPasswordActivity.this,AppKey,AppSecret);//初始化 SDK 单例，可以多次调用
        EventHandler eventHandler = new EventHandler(){//操作回调

            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);//注册回调接口

        back.setOnClickListener(new backClickListener());
        btnTrue.setOnClickListener(new ButtonClickListener());
//        tvPassword.setOnClickListener(new passwordClickListener());
        verifyCodeView.setInputCompleteListener(new inputCompleteListener());
        tvResend.setOnClickListener(new ResendClickListener());
    }
    private void findViews(){
        back = findViewById(R.id.iv_back);
        btnTrue = findViewById(R.id.btn_true);
        tvResend = findViewById(R.id.tv_resend);
//        tvPassword = findViewById(R.id.tv_password);
        verifyCodeView = findViewById(R.id.verify_code_view);

    }
    //输入验证码
    private class inputCompleteListener implements VerifyCodeView.InputCompleteListener{

        @Override
        public void inputComplete() {
//            Toast.makeText(LRInputCodeActivity.this,"inputComplete:" +
//                    verifyCodeView.getEditContent(),Toast.LENGTH_SHORT).show();
            code = verifyCodeView.getEditContent();
            Log.e("验证码",code);
            if (code.length() == 4){
                SMSSDK.submitVerificationCode("86",userPhone,code);
                flag = false;
                Log.e("flag",flag+"");
            }else {
                Toast.makeText(LRInputCodeActivity.this,"请输入完整的验证码",Toast.LENGTH_SHORT).show();
                verifyCodeView.requestFocus();
            }
        }

        @Override
        public void invalidContent() {

        }
    }

    /**
     * 重新发送验证码
     */
    private class ResendClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SMSSDK.getVerificationCode("86", userPhone);// 发送验证码给号码的 phoneNum 的手机
            time.start();
        }
    }
    /**
     * 实现验证码倒计时
     */
    public class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvResend.setText("重新发送(" + millisUntilFinished / 1000 + ")秒");
        }

        @Override
        public void onFinish() {
            tvResend.setText("重新发送");
        }
    }
    //点击返回
    private class backClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRInputCodeActivity.this,LRLoginActivity.class);
            startActivity(intent);
        }
    }
    //点击确定按钮
    private class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (!code.equals("")) {
                if (isCode == true){
                    //跳到首页
                    InputCodeUtil inputCodeUtil = new InputCodeUtil();
                    inputCodeUtil.execute(userId);

                }
            }else {
                Toast.makeText(LRInputCodeActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                verifyCodeView.requestFocus();
            }

        }
    }

    /**
     * 异步任务
     */
    private class InputCodeUtil extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            int userId = (Integer)objects[0];
            List<Integer> type=null;
            String url = path + "/title/findTypeByUserId?userId="+userId;
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("InputCodeResult",result);
                type=new ArrayList<>();
                JSONArray jsonArray=new JSONArray(result);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    int typeId=jsonObject.getInt("typeId");
                    Log.e("typeId",typeId+"");
                    type.add(typeId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return type;
        }

        @Override
        protected void onPostExecute(Object o) {
            ArrayList<Integer> type=(ArrayList<Integer>)o;
            Intent intent = new Intent(LRInputCodeActivity.this,MainActivity.class);
            intent.putExtra("userId",userId);
            intent.putIntegerArrayListExtra("type",type);
            intent.putExtra("flag","main");
            startActivity(intent);
        }
    }
    //密码登录
    private class passwordClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRInputCodeActivity.this,LRPswdLoginActivity.class);
            startActivity(intent);
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE){
                //如果操作成功
                if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    //校验验证码，返回校验的手机和国家代码
//                    Toast.makeText(LRInputCodeActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    isCode = true;
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功，true为智能验证，false为普通下发短信
                    Toast.makeText(LRInputCodeActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                }
            }else {
                //如果操作失败
                if (flag){
                    Toast.makeText(LRInputCodeActivity.this,"验证码获取失败，请重新获取",Toast.LENGTH_SHORT).show();
                }else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(LRInputCodeActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
//                    isCode = false;
                }
            }
        }
    };
    //注销回调接口
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
