package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import cn.edu.hebtu.software.sharemateclient.Activity.PersonalActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.SettingActivity;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 我 Fragment
 * @author fengjiaxing
 * @date 2019/5/13
 * */
public class MyFragment extends Fragment {

    private String path = null;
    private int userId = 1;
    private int typeId = 1;
    private String url;
    private GridView gridView;
    private TextView nameText;
    private TextView idText;
    private TextView introText;
    private TextView collection;
    private TextView note;
    private TextView followCount;
    private TextView fanCount;
    private TextView likeCount;
    private ImageView headImg;//头像
    private ImageView settingView;
    private Button btnPersonal;//个人资料
    private OnClickListener listener;
    private UserBean user = new UserBean();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        //获取控件
        findView(view);
        //监听器绑定
        setListener();
        path = getResources().getString(R.string.server_path);
        //得到user的详情
        GetUserDetail getUserDetail = new GetUserDetail();
        getUserDetail.execute(userId);
        return view;
    }

    /**
     * 获取控件
     * @param view
     */
    public void findView(View view){
        nameText = view.findViewById(R.id.userName);
        idText = view.findViewById(R.id.userId);
        introText = view.findViewById(R.id.userIntro);
        headImg = view.findViewById(R.id.userPhoto);
        gridView = view.findViewById(R.id.root);
        gridView.setEmptyView((view.findViewById(R.id.empty_view)));
        collection = view.findViewById(R.id.collection);
        note = view.findViewById(R.id.note);
        settingView = view.findViewById(R.id.setting);
        btnPersonal = view.findViewById(R.id.personal);
        followCount = view.findViewById(R.id.followCount);
        fanCount = view.findViewById(R.id.fanCount);
        likeCount = view.findViewById(R.id.likeCount);
    }
    /**
     * 监听器绑定
     */
    public void setListener(){
        listener = new OnClickListener();
        btnPersonal.setOnClickListener(listener);
        settingView.setOnClickListener(listener);
    }
    /**
     * 监听器类
     */
    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.personal://个人资料
                    Intent perIntent = new Intent(getActivity(), PersonalActivity.class);
                    perIntent.putExtra("person","my");
                    startActivity(perIntent);
                    break;
                case R.id.setting://设置
                    Intent setIntent = new Intent(getActivity(), SettingActivity.class);
                    startActivity(setIntent);
                    break;
            }
        }
    }
    /**
     * 异步任务——获取UserBean对象
     */
    class GetUserDetail extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            url = path + "/user/findUserByUserId?userId="+objects[0];
            //1.创建OKHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.创建Request对象
            Request request = new Request.Builder().url(url).build();
            //3.创建Call对象
            Call call = okHttpClient.newCall(request);
            //4.提交请求并返回相应
            try {
                String result = call.execute().body().string();
                Log.e("UserResult---",result);
                Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                UserBean userJson = gson.fromJson(result,UserBean.class);
                user.setUserId(userJson.getUserId());
                user.setUserName(userJson.getUserName());
                user.setUserPassword(userJson.getUserPassword());
                user.setUserSex(userJson.getUserSex());
                user.setUserPhoto(userJson.getUserPhoto());
                user.setUserPhone(userJson.getUserPhone());
                user.setUserAddress(userJson.getUserAddress());
                user.setUserBirth(userJson.getUserBirth());
                user.setUserIntroduce(userJson.getUserIntroduce());
//                user.setFollowCount(userJson.getInt("followCount"));
//                user.setFanCount(userJson.getInt("fanCount"));
//                user.setLikeCount(userJson.getInt("likeCount"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            nameText.setText(user.getUserName());
            String userId = String.format("%06d",user.getUserId());//格式化为至少6位十进制整数
            idText.setText("ShareMate号:" + userId);
            if (user.getUserIntroduce() == null || user.getUserIntroduce().length() < 20) {
                introText.setText(user.getUserIntroduce());
            } else {
                introText.setText(user.getUserIntroduce().substring(0, 20) + ".....");//Substring(截取子串的起始位置,子串长度)
            }
            Log.e("photoPath---",path+user.getUserPhoto());
            String photoPath = path+"/"+user.getUserPhoto();
            RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            Glide.with(getActivity()).load(photoPath).apply(mRequestOptions).into(headImg);
//            followCount.setText(""+user.getFollowCount());
//            fanCount.setText(""+user.getFanCount());
//            likeCount.setText(""+user.getLikeCount());
        }
    }

}
