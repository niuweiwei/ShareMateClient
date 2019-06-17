package cn.edu.hebtu.software.sharemateclient.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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
    private String path,phone;
    private ImageView image1,image2,image3,image4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        path = getResources().getString(R.string.server_path);
        findViews();

        phone = readInfo("phoneStr");
        Log.e("phone",phone);
        if (phone!=null && !phone.equals("")){
            startAnimator();
        }else {
            startAnimators();
        }

        /**
         * Timer是一种定时器工具，用来在一个后台线程计划执行指定任务。它可以计划执行一个任务一次或反复多次。
         TimerTask一个抽象类，它的子类代表一个可以被Timer计划的任务。
         */
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                String phone = readInfo("phoneStr");
//                Log.e("phone",phone);
//                if (phone!=null && !phone.equals("")){
//                    user.setUserPhone(phone);
//                    Log.e("userPhone",user.getUserPhone());
//                    LoginUtil loginUtil = new LoginUtil();
//                    loginUtil.execute(user);
//                }else {
//                    Intent intent = new Intent(WelcomeActivity.this,LRLoginActivity.class);
//                    startActivity(intent);
//                    WelcomeActivity.this.finish();
//                }
//            }
//        };
//        timer.schedule(timerTask,1000*2);
    }
    public void findViews(){
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
    }

    public void startAnimators(){
        //1-->2
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(image1,"alpha",1.0f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(image2,"alpha",0f,1.0f);
        ObjectAnimator animatorScale1 = ObjectAnimator.ofFloat(image1,"scaleX",1.0f,1.3f);
        ObjectAnimator animatorScale2 = ObjectAnimator.ofFloat(image1,"scaleY",1.0f,1.3f);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.setDuration(6000);
        animatorSet1.play(animator1).with(animator2).with(animatorScale1).with(animatorScale2);

        //2-->3
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(image2,"alpha",1.0f,0f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(image3,"alpha",0f,1.0f);
        ObjectAnimator animatorScale3 = ObjectAnimator.ofFloat(image2,"scaleX",1.0f,1.3f);
        ObjectAnimator animatorScale4 = ObjectAnimator.ofFloat(image2,"scaleY",1.0f,1.3f);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(5000);
        animatorSet2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                image1.setScaleX(1.0f);
                image1.setScaleY(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet2.play(animator3).with(animator4).with(animatorScale3).with(animatorScale4);

        //3-->1
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(image3,"alpha",1.0f,0f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(image1,"alpha",0f,1.0f);
        ObjectAnimator animatorScale5 = ObjectAnimator.ofFloat(image3,"scaleX",1.0f,1.3f);
        ObjectAnimator animatorScale6 = ObjectAnimator.ofFloat(image3,"scaleY",1.0f,1.3f);
        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setDuration(5000);
        animatorSet3.play(animator5).with(animator6).with(animatorScale5).with(animatorScale6);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animatorSet1,animatorSet2,animatorSet3);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //将放大的view复位
                image2.setScaleX(1.0f);
                image2.setScaleY(1.0f);
                image3.setScaleX(1.0f);
                image3.setScaleY(1.0f);
                //循环播放
//                animation.start();
                Intent intent = new Intent(WelcomeActivity.this,LRLoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public void startAnimator(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(image1,"alpha",1.0f,0f);
        ObjectAnimator animatorScale1 = ObjectAnimator.ofFloat(image1,"scaleX",1.0f,1.3f);
        ObjectAnimator animatorScale2 = ObjectAnimator.ofFloat(image1,"scaleY",1.0f,1.3f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(5000);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                image1.setScaleX(1.0f);
                image1.setScaleY(1.0f);
                user.setUserPhone(phone);
                Log.e("userPhone",user.getUserPhone());
                LoginUtil loginUtil = new LoginUtil();
                loginUtil.execute(user);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.play(animator1).with(animatorScale1).with(animatorScale2);
        animatorSet.start();
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
            ArrayList<Integer> typeList=new ArrayList<>();
            List<Object> objectList = new ArrayList<>();
            String result = null;
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
                //判断 当前有没有用户登录环信
                if(!EMClient.getInstance().isLoggedInBefore()){
                    EMClient.getInstance().login(u.getUserId() + "", u.getUserPassword(), new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Log.e("Welcome-LoginUtil","登录成功");
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.e("Welcome-LoginUtil","登录失败");
                            Log.e(""+i,s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("user", u);
                intent.putIntegerArrayListExtra("type", typeList);
                startActivity(intent);
                WelcomeActivity.this.finish();
            } else {
                Looper.prepare();
                Toast.makeText(WelcomeActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }
}
