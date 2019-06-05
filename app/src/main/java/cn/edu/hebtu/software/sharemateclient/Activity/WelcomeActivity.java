package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WelcomeActivity extends AppCompatActivity {
    private UserBean user = new UserBean();
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        path = getResources().getString(R.string.server_path);
        /**
         * Timer是一种定时器工具，用来在一个后台线程计划执行指定任务。它可以计划执行一个任务一次或反复多次。
         TimerTask一个抽象类，它的子类代表一个可以被Timer计划的任务。
         */
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String phone = readInfo("phoneStr");
                Log.e("phone",phone);
                if (phone!=null && !phone.equals("")){
                    user.setUserPhone(phone);
                    Log.e("userPhone",user.getUserPhone());
                    LoginUtil loginUtil = new LoginUtil();
                    loginUtil.execute(user);
//                    Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    WelcomeActivity.this.finish();
                }else {
                    Intent intent = new Intent(WelcomeActivity.this,LRLoginActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        };
        timer.schedule(timerTask,1000*2);
    }
    /**
     * 从SharedPreferences中根据账号读取密码
     */
    private String readInfo(String phoneStr) {
        //"loginInfo",mode_private; MODE_PRIVATE表示可以继续写入
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //sp.getString() phone, "";
        String phone = sp.getString(phoneStr, "");
        return phone;
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
            String url = path + "/user/isLogin?userPhone=" + userPhone;
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            ArrayList<Integer> typeList=new ArrayList<>();
            List<Object> objectList = new ArrayList<>();
            UserBean u = null;
            try {
                result = call.execute().body().string();
                Log.e("isLoginResult---", result);
                JSONObject jsonObject = new JSONObject(result);
                JSONObject mapObject = jsonObject.getJSONObject("map");
                String typeStr =  mapObject.get("typeList").toString();
                Gson gson = new Gson();
                typeList = gson.fromJson(typeStr,new TypeToken<List<Integer>>(){}.getType());
                String uStr = mapObject.get("user").toString();
                u = gson.fromJson(uStr,UserBean.class);
                objectList.add(typeList);
                objectList.add(u);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return objectList;
        }

        @Override
        protected void onPostExecute(Object o) {
            List<Object> objectList = (List<Object>) o;
            ArrayList<Integer> typeList = (ArrayList<Integer>) objectList.get(0);
            UserBean u = (UserBean) objectList.get(1);
            if (u != null) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("user", u);
                intent.putIntegerArrayListExtra("type", typeList);
                startActivity(intent);
                WelcomeActivity.this.finish();
            } else {
                Toast.makeText(WelcomeActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
