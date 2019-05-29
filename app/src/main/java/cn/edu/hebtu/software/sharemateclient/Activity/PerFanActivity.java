package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.FanAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PerFanActivity extends AppCompatActivity {

    private UserBean user;
    private ImageView imageView;
    private ListView listView;
    private FanAdapter fanAdapter;
    private List<UserBean> userList = new ArrayList<>();
    private String path = null;
    private  ArrayList<Integer> type = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);
        path = getResources().getString(R.string.server_path);
        type = getIntent().getIntegerArrayListExtra("type");
        user = (UserBean) getIntent().getSerializableExtra("user");
        findViews();
        GetFan getFan = new GetFan();
        getFan.execute(user);
        //点击返回
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerFanActivity.this,MainActivity.class);
                intent.putExtra("back","my");
                intent.putIntegerArrayListExtra("type",type);
                intent.putExtra("userId",user.getUserId());
                startActivity(intent);
            }
        });
    }
    //从数据库中取出当前用户的粉丝
    public class GetFan extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean userBean = (UserBean) objects[0];
            int userId = userBean.getUserId();
            String url = path+"/follow/getFan?userId="+userId;
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Call call = okHttpClient.newCall(request);
                String result = call.execute().body().string();
                Log.e("FanResult---",result);
                //解析JSON
                JSONArray array = new JSONArray(result);
                for(int i=0 ; i<array.length() ; i++){
                    JSONObject userObject = array.getJSONObject(i);
                    UserBean fan = new UserBean();
                    fan.setUserId(userObject.getInt("userId"));
                    fan.setUserName(userObject.getString("userName"));
                    fan.setUserPhotoPath(path+"/"+userObject.getString("userPhoto"));
                    fan.setUserIntroduce(userObject.getString("userIntro"));
                    fan.setStatus(userObject.getBoolean("status"));
                    Log.e("status",userObject.getBoolean("status")+"");
                    fan.setNoteCount(userObject.getInt("noteCount"));
                    fan.setFanCount(userObject.getInt("fanCount"));
                    fan.setFollowCount(userObject.getInt("followCount"));
                    fan.setLikeCount(userObject.getInt("likesCount"));
                    userList.add(fan);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fanAdapter = new FanAdapter(PerFanActivity.this,R.layout.item_fan,userList,user,path);
            listView.setAdapter(fanAdapter);
        }
    }
    public void findViews(){
        listView = findViewById(R.id.root);
        listView.setEmptyView((findViewById(R.id.empty_view)));
        imageView = findViewById(R.id.back);
    }
}
