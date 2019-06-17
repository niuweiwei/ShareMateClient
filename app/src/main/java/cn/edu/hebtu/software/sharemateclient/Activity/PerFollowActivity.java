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
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.FocusAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author fengjiaxing
 * @date 2019/5/15
 */
public class PerFollowActivity extends AppCompatActivity {
    private UserBean user;
    private ImageView imageView;
    private ListView listView;
    private FocusAdapter focusAdapter;
    private List<UserBean> userList = new ArrayList<>();
    private String path;
    private ArrayList<Integer> type = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        type = getIntent().getIntegerArrayListExtra("type");
        user = (UserBean) getIntent().getSerializableExtra("user");
        findViews();
        path = getResources().getString(R.string.server_path);
        GetFriend getFriend = new GetFriend();
        getFriend.execute(user);
        //点击返回
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerFollowActivity.this,MainActivity.class);
                intent.putExtra("flag","my");
                intent.putIntegerArrayListExtra("type",type);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }
    //从数据库中取出当前用户关注的好友
    public class GetFriend extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean userBean = (UserBean) objects[0];
            int userId = userBean.getUserId();
            String url = path+"/follow/getFollowed?userId="+userId;
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                result = call.execute().body().string();
                Log.e("FollowedResult---",result);
                //解析JSON
                JSONArray array = new JSONArray(result);
                for(int i=0 ; i<array.length() ; i++){
                    JSONObject userObject = array.getJSONObject(i);
                    UserBean friend = new UserBean();
                    friend.setUserId(userObject.getInt("userId"));
                    friend.setUserName(userObject.getString("userName"));
                    friend.setUserPhoto(userObject.getString("userPhoto"));
                    friend.setUserPhotoPath(path+"/"+userObject.getString("userPhoto"));
                    friend.setUserIntro(userObject.getString("userIntro"));
                    friend.setFanCount(userObject.getInt("fanCount"));
                    friend.setLikeCount(userObject.getInt("likesCount"));
                    friend.setFollowCount(userObject.getInt("followCount"));
                    friend.setStatus(userObject.getBoolean("status"));
                    userList.add(friend);
                }
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
            focusAdapter = new FocusAdapter(R.layout.item_focus, PerFollowActivity.this, userList,path,user);
            listView.setAdapter(focusAdapter);
        }
    }

    //设置布局控件
    public void findViews(){
        listView = findViewById(R.id.root);
        listView.setEmptyView((findViewById(R.id.empty_view)));
        imageView = findViewById(R.id.back);
    }
}
