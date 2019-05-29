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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Activity.PerFanActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.PerFollowActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.MainActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.PerPersonalActivity;
import cn.edu.hebtu.software.sharemateclient.Adapter.NoteAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
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

    private OkHttpClient okHttpClient;
    private String path = null;
    private int userId = 1;
    private ArrayList<Integer> type = new ArrayList<>();
    private GridView gridView;
    private TextView tvFan;
    private TextView tvFollow;
    private TextView nameText;
    private TextView idText;
    private TextView introText;
    private TextView collection;
    private TextView note;
    private TextView followCount;
    private TextView fanCount;
    private TextView likeCount;
    private ImageView headImg;//头像
    private ImageView logout;//退出登录
    private Button btnPersonal;//个人资料
    private OnClickListener listener;
    private UserBean user = new UserBean();
    private NoteAdapter noteAdapter;
    private List<NoteBean> collectionList = new ArrayList<>();
    private List<NoteBean> noteList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        //获取控件
        findView(view);
        //监听器绑定
        setListener();
        path = getResources().getString(R.string.server_path);
        type.add(1);
        type.add(2);
        //得到user的详情
        GetUserDetail getUserDetail = new GetUserDetail();
        getUserDetail.execute(userId);
        //取出Note笔记
        GetNote getNote = new GetNote();
        getNote.execute(user);
        //取出收藏
        GetCollection getCollection = new GetCollection();
        getCollection.execute(user);
        //查询关注数量
        GetFollowCount getFollowCount = new GetFollowCount();
        getFollowCount.execute(user);
        //查询粉丝数量
        GetFanCount getFanCount = new GetFanCount();
        getFanCount.execute(user);
        //查询收获的赞的数量
        GetLikeCount getLikeCount = new GetLikeCount();
        getLikeCount.execute(user);
        return view;
    }

    /**
     * 获取控件
     * @param view
     */
    public void findView(View view){
        tvFan = view.findViewById(R.id.tv_fan);
        tvFollow = view.findViewById(R.id.tv_follow);
        nameText = view.findViewById(R.id.userName);
        idText = view.findViewById(R.id.userId);
        introText = view.findViewById(R.id.userIntro);
        headImg = view.findViewById(R.id.userPhoto);
        gridView = view.findViewById(R.id.root);
        gridView.setEmptyView((view.findViewById(R.id.empty_view)));
        collection = view.findViewById(R.id.collection);
        note = view.findViewById(R.id.note);
        btnPersonal = view.findViewById(R.id.personal);
        followCount = view.findViewById(R.id.followCount);
        fanCount = view.findViewById(R.id.fanCount);
        likeCount = view.findViewById(R.id.likeCount);
        logout = view.findViewById(R.id.logout);
    }
    /**
     * 监听器绑定
     */
    public void setListener(){
        listener = new OnClickListener();
        tvFan.setOnClickListener(listener);
        tvFollow.setOnClickListener(listener);
        btnPersonal.setOnClickListener(listener);
        note.setOnClickListener(listener);
        collection.setOnClickListener(listener);
        followCount.setOnClickListener(listener);
        fanCount.setOnClickListener(listener);
        likeCount.setOnClickListener(listener);
        logout.setOnClickListener(listener);
        headImg.setOnClickListener(listener);
    }
    /**
     * 监听器类
     */
    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.personal://个人资料
                    Intent perIntent = new Intent(getActivity(), PerPersonalActivity.class);
                    perIntent.putExtra("person","my");
                    perIntent.putExtra("user",user);
                    perIntent.putIntegerArrayListExtra("type",type);
                    startActivityForResult(perIntent,1);
                    break;
                case R.id.userPhoto://头像
                    Intent imgIntent = new Intent(getActivity(), PerPersonalActivity.class);
                    imgIntent.putExtra("person","my");
                    imgIntent.putExtra("user",user);
                    imgIntent.putIntegerArrayListExtra("type",type);
                    startActivityForResult(imgIntent,1);
                    break;
                case R.id.note://笔记
                    note.setTextColor(getResources().getColor(R.color.warmRed));
                    collection.setTextColor(getResources().getColor(R.color.darkGray));
                    noteAdapter = new NoteAdapter(getActivity(), R.layout.item_note,noteList,user,path);
                    gridView.setAdapter(noteAdapter);
//                    setNoteGridView(gridView);
                    break;
                case R.id.collection://收藏
                    collection.setTextColor(getResources().getColor(R.color.warmRed));
                    note.setTextColor(getResources().getColor(R.color.darkGray));
                    noteAdapter = new NoteAdapter(getActivity(), R.layout.item_note,collectionList,user,path);
                    gridView.setAdapter(noteAdapter);
//                    setCollectGridView(gridView);
                    break;
                case R.id.followCount://关注
                    Intent focusIntent = new Intent();
                    focusIntent.setClass(getActivity(), PerFollowActivity.class);
                    focusIntent.putExtra("user",user);
                    focusIntent.putIntegerArrayListExtra("type",type);
                    startActivity(focusIntent);
                    break;
                case R.id.tv_follow://关注
                    Intent focusIntent2 = new Intent();
                    focusIntent2.setClass(getActivity(), PerFollowActivity.class);
                    focusIntent2.putExtra("user",user);
                    focusIntent2.putIntegerArrayListExtra("type",type);
                    startActivity(focusIntent2);
                    break;
                case R.id.fanCount://粉丝
                    Intent fanIntent = new Intent();
                    fanIntent.setClass(getActivity(), PerFanActivity.class);
                    fanIntent.putExtra("user",user);
                    fanIntent.putIntegerArrayListExtra("type",type);
                    startActivity(fanIntent);
                    break;
                case R.id.tv_fan://粉丝
                    Intent fanIntent2 = new Intent();
                    fanIntent2.setClass(getActivity(), PerFanActivity.class);
                    fanIntent2.putExtra("user",user);
                    fanIntent2.putIntegerArrayListExtra("type",type);
                    startActivity(fanIntent2);
                    break;
                case R.id.logout://退出登录
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MainActivity.class);
                    intent.putExtra("back","my");
                    intent.putIntegerArrayListExtra("type",type);
                    intent.putExtra("userId",user.getUserId());
                    startActivity(intent);
                    break;
            }
        }
    }
//    public void setNoteGridView(GridView gridView){
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
//                intent.putExtra("noteId",noteList.get(position).getNoteId());
//                Log.e("noteId",noteList.get(position).getNoteId()+"");
//                intent.putExtra("userId",user.getUserId());
//                intent.putIntegerArrayListExtra("type",type);
//                startActivity(intent);
//            }
//        });
//    }
//    public void setCollectGridView(GridView gridView){
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
//                intent.putExtra("noteId",collectionList.get(position).getNoteId());
//                intent.putExtra("userId",user.getUserId());
//                intent.putIntegerArrayListExtra("type",type);
//                startActivity(intent);
//            }
//        });
//    }
    /**
     * 异步任务——获取UserBean对象
     */
    public class GetUserDetail extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String url = path + "/user/findUserByUserId?userId="+objects[0];
            //1.创建OKHttpClient对象
            okHttpClient = new OkHttpClient();
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
        }
    }

    /**
     * 异步任务——从数据库取笔记
     */
    public class GetNote extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean user = (UserBean) objects[0];
            int uId = user.getUserId();
            String url = path + "/note/findNoteByUserId?userId="+uId;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("NoteResult---",result);
                JSONObject noteObject = new JSONObject(result);
                JSONArray noteArray = noteObject.getJSONArray("noteList");
                String noteStr = noteArray.toString();
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                Type type = new TypeToken<List<NoteBean>>(){}.getType();
                List<NoteBean> notes = gson.fromJson(noteStr,type);
                Log.e("notes---",notes.toString());
                for (int i = 0; i < notes.size(); i++){
                    NoteBean n = new NoteBean();
                    n.setNoteId(notes.get(i).getNoteId());
                    n.setNoteTitle(notes.get(i).getNoteTitle());
                    n.setNoteDetail(notes.get(i).getNoteDetail());
                    n.setNoteImage(notes.get(i).getNoteImage());
                    n.setNoteDate(notes.get(i).getNoteDate());
                    n.setUser(user);
                    noteList.add(n);
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
            note.setTextColor(getResources().getColor(R.color.warmRed));
            collection.setTextColor(getResources().getColor(R.color.darkGray));
            noteAdapter = new NoteAdapter(getActivity(), R.layout.item_note,noteList,user,path);
            gridView.setAdapter(noteAdapter);
//            setNoteGridView(gridView);
        }
    }
    /**
     * 异步任务——从数据库中取收藏
     */
    public class GetCollection extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean user = (UserBean) objects[0];
            int uId = user.getUserId();
            String url = path + "/collect/findCollectNote?userId="+uId;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("CollectResult---",result);
                JSONObject collectJson = new JSONObject(result);
                JSONArray collectArray = collectJson.getJSONArray("collectList");
                String collectStr = collectArray.toString();
                Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                Type type = new TypeToken<List<NoteBean>>(){}.getType();
                List<NoteBean> notes = gson.fromJson(collectStr,type);
                for (int i = 0; i < notes.size(); i++){
                    NoteBean n = new NoteBean();
                    n.setNoteId(notes.get(i).getNoteId());
                    n.setNoteTitle(notes.get(i).getNoteTitle());
                    n.setNoteDetail(notes.get(i).getNoteDetail());
                    n.setNoteImage(notes.get(i).getNoteImage());
                    n.setNoteDate(notes.get(i).getNoteDate());
                    n.setUser(user);
                    collectionList.add(n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /**
     * 异步任务——查询关注数量
     */
    public class GetFollowCount extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean u = (UserBean) objects[0];
            int uid = u.getUserId();
            String url = path + "/follow/getFollowCount?userId="+uid;
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
                user.setFollowCount(followCount);
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
            followCount.setText(""+user.getFollowCount());
        }
    }
    /**
     * 异步任务——查询粉丝数量
     */
    public class GetFanCount extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean u = (UserBean) objects[0];
            int uid = u.getUserId();
            String url = path + "/follow/getFanCount?userId="+uid;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("FanCount---",result);
                JSONObject jsonObject = new JSONObject(result);
                int fanCount = jsonObject.getInt("fanCount");
                Log.e("fanCount",fanCount+"");
                user.setFanCount(fanCount);
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
            fanCount.setText(""+user.getFanCount());
        }
    }
    /**
     * 异步任务——查询收获的赞的数量
     */
    public class GetLikeCount extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean u = (UserBean) objects[0];
            int uid = u.getUserId();
            String url = path + "/likes/getLikesCount?userId="+uid;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("LikesCount---",result);
                JSONObject jsonObject = new JSONObject(result);
                int LikesCount = jsonObject.getInt("likesCount");
                Log.e("LikesCount",LikesCount+"");
                user.setLikeCount(LikesCount);
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
            likeCount.setText(""+user.getLikeCount());
        }
    }
}
