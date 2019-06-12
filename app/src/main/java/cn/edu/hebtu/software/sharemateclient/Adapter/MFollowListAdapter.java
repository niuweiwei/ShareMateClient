package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Activity.PerFriendActivity;
import cn.edu.hebtu.software.sharemateclient.Bean.Follow;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MFollowListAdapter extends BaseAdapter {

    private Context context;
    private List<Follow> followList;
    private int itemLayout;
    private String serverPath;
    private OkHttpClient okHttpClient ;

    public MFollowListAdapter(Context context, List<Follow> followList, int itemLayout, String serverPath) {
        this.context = context;
        this.followList = followList;
        this.itemLayout = itemLayout;
        this.serverPath = serverPath;
    }

    @Override
    public int getCount() {
        return followList.size();
    }

    @Override
    public Object getItem(int position) {
        return followList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Follow follow = followList.get(position);
        final ViewHolder viewHolder;
        final OkHttpClient client = new OkHttpClient();
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            viewHolder.portraitImage = convertView.findViewById(R.id.iv_portrait);
            viewHolder.followName = convertView.findViewById(R.id.tv_name);
            viewHolder.followDate = convertView.findViewById(R.id.tv_date);
            viewHolder.followBtn = convertView.findViewById(R.id.btn_follow);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) convertView.getTag();

        //对控件进行填充
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(serverPath+"/"+follow.getFollowUser().getUserPhoto())
                .apply(requestOptions)
                .into(viewHolder.portraitImage);
        //为用户头像绑定事件监听器
        viewHolder.portraitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetFollowCount followTask = new GetFollowCount();
                followTask.execute(followList.get(position).getFollowUser(),position);


            }
        });
        viewHolder.followName.setText(follow.getFollowUser().getUserName());
        viewHolder.followDate.setText(follow.getFollowDate());


        //判断二者是否互关
        if(follow.isFollow()){
            viewHolder.followBtn.setText("已关注");
            viewHolder.followBtn.setTextColor(context.getResources().getColor(R.color.deepGray));
            viewHolder.followBtn.setBackground(context.getDrawable(R.drawable.cancelfollowedbutton_style));
        }else{
            viewHolder.followBtn.setText("回粉");
            viewHolder.followBtn.setTextColor(context.getResources().getColor(R.color.brightRed));
            viewHolder.followBtn.setBackground(context.getDrawable(R.drawable.followbutton_style));
        }
        //为关注按钮绑定事件监听器
        viewHolder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前是否为互关状态
                if(follow.isFollow()){
                    Log.e("MFollowListAdapter","取消关注");
                    //如果是互关状态 按钮的样式变为“回粉”状态 并开启线程服务器删除关注记录
                    viewHolder.followBtn.setText("回粉");
                    viewHolder.followBtn.setTextColor(context.getResources().getColor(R.color.brightRed));
                    viewHolder.followBtn.setBackground(context.getDrawable(R.drawable.followbutton_style));
                    follow.setFollow(false);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Request request = new Request.Builder()
                                    .url(serverPath+"/follow/deleteFollow/"+
                                            follow.getFollowedUser().getUserId()+"/"+
                                            follow.getFollowUser().getUserId())
                                    .build();
                            Call cancelFollow = client.newCall(request);
                            try {
                                cancelFollow.execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }else{
                    //如果是未互相关注 则关注按钮变为 "已关注"状态  并开启线程向服务器端添加关注记录
                    Log.e("MFollowListAdapter","关注");
                    viewHolder.followBtn.setText("已关注");
                    viewHolder.followBtn.setTextColor(context.getResources().getColor(R.color.deepGray));
                    viewHolder.followBtn.setBackground(context.getDrawable(R.drawable.cancelfollowedbutton_style));
                    follow.setFollow(true);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Request request = new Request.Builder()
                                    .url(serverPath+"/follow/addFollow/"+
                                            follow.getFollowedUser().getUserId()+"/"
                                            +follow.getFollowUser().getUserId())
                                    .build();
                            Call followCall = client.newCall(request);
                            try {
                                followCall.execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder{
        private ImageView portraitImage;
        private TextView followName;
        private TextView followDate;
        private Button followBtn;
    }

    /**
     * 异步任务——查询关注数量
     */
    public class GetFollowCount extends AsyncTask {

        private int position;
        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean u = (UserBean) objects[0];
            position = (int) objects[1];
            int uid = u.getUserId();
            String url = serverPath + "/follow/getFollowCount?userId="+uid;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("FollowCount---",result);
                JSONObject jsonObject = new JSONObject(result);
                int followCount = jsonObject.getInt("followCount");
//                Gson gson = new Gson();
//                int followCount = gson.fromJson(result,int.class);
                Log.e("followCount",followCount+"");
                u.setFollowCount(followCount);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return u;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            UserBean userBean = (UserBean) o;
            GetFanCount fanTask = new GetFanCount();
            fanTask.execute(userBean,position);
        }
    }
    /**
     * 异步任务——查询粉丝数量
     */
    public class GetFanCount extends AsyncTask{

        private int position;
        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean u = (UserBean) objects[0];
            position = (int) objects[1];
            int uid = u.getUserId();
            String url = serverPath + "/follow/getFanCount?userId="+uid;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("FanCount---",result);
                JSONObject jsonObject = new JSONObject(result);
                int fanCount = jsonObject.getInt("fanCount");
                Log.e("fanCount",fanCount+"");
                u.setFanCount(fanCount);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return u;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            UserBean userBean = (UserBean) o;
            GetLikeCount likeTask = new GetLikeCount();
            likeTask.execute(userBean,position);

        }
    }
    /**
     * 异步任务——查询收获的赞的数量
     */
    public class GetLikeCount extends AsyncTask{

        private int position;
        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean u = (UserBean) objects[0];
            position = (int) objects[1];
            int uid = u.getUserId();
            String url = serverPath + "/like/getLikesCount?userId="+uid;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("LikesCount---",result);
                JSONObject jsonObject = new JSONObject(result);
                int LikesCount = jsonObject.getInt("likesCount");
                Log.e("LikesCount",LikesCount+"");
                u.setLikeCount(LikesCount);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return u;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            UserBean userBean = (UserBean) o;
            Intent friendIntent = new Intent(context, PerFriendActivity.class);
            friendIntent.putExtra("friend",userBean);
            context.startActivity(friendIntent);
        }
    }

}
