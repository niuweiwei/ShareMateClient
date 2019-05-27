package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.NoteCommentListAdapter;
import cn.edu.hebtu.software.sharemateclient.Entity.Comment;
import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.Entity.User;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NoteDetailActivity extends AppCompatActivity {

    private ImageView userIcon,noteImage,contentUserIcon;
    private Button backBtn,followBtn,pickBtn,collectBtn,commentBtn;
    private TextView userName;
    private TextView noteTitle,noteDetail,noteDate;
    private TextView commentCount,pickCount,collectCount,comCount;
    private ButtonOnclickListener listener;
    private Note note;
    private String U;
    private User contentUser;
    private int userId,noteId;
    private OkHttpClient okHttpClient;
    private ListView commentListView;
    private NoteCommentListAdapter commentAdapter;
    private List<Comment> comments;
    private CommentListTask commentListTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        okHttpClient = new OkHttpClient();
        Intent intent  = getIntent();
        note= (Note) intent.getSerializableExtra("note");
        contentUser = (User)intent.getSerializableExtra("contentUser");
        userId = contentUser.getUserId();
        noteId = note.getNoteId();
        findViews();
        setViews();
        commentListTask = new CommentListTask();
        commentListTask.execute();
    }


    //初始化控件
    private void findViews(){
        userIcon = findViewById(R.id.userIcon);
        userName = findViewById(R.id.userName);
        backBtn = findViewById(R.id.backBtn);
        followBtn = findViewById(R.id.followBtn);
        pickBtn = findViewById(R.id.pickBtn);
        collectBtn = findViewById(R.id.collectBtn);
        commentBtn = findViewById(R.id.commentBtn);
        noteImage = findViewById(R.id.noteImage);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetail = findViewById(R.id.noteText);
        noteDate = findViewById(R.id.noteDate);
        contentUserIcon = findViewById(R.id.contentUserIcon);
        commentCount = findViewById(R.id.commentCount);
        pickCount = findViewById(R.id.pickCount);
        comCount = findViewById(R.id.comCount);
        collectCount = findViewById(R.id.collectCount);
        commentListView = findViewById(R.id.noteCommentList);
        listener = new ButtonOnclickListener();
        backBtn.setOnClickListener(listener);
        followBtn.setOnClickListener(listener);
        pickBtn.setOnClickListener(listener);
        collectBtn.setOnClickListener(listener);
        commentBtn.setOnClickListener(listener);
        U ="http://10.7.89.23:8080/ShareMateServer/";
    }

    // 初始化布局
    private void setViews(){
        userName.setText(note.getUser().getUserName());
        noteTitle.setText(note.getNoteTitle());
        noteDetail.setText(note.getNoteDetail());
        noteDate.setText(note.getNoteDate());
        collectCount.setText(note.getCollectCount()+"");
        comCount.setText(note.getCommentCount()+"");
        pickCount.setText(note.getLikeCount()+"");
        commentCount.setText("共 "+note.getCommentCount()+" 条评论");
        comments = new ArrayList<>();
        if(note.isLike()){
            pickBtn.setBackgroundResource(R.mipmap.picked);
        }else {
            pickBtn.setBackgroundResource(R.mipmap.pick);
        }
        if(note.isCollect()){
            collectBtn.setBackgroundResource(R.mipmap.collectedbtn);
        }else {
            collectBtn.setBackgroundResource(R.mipmap.collectbtn);
        }
        if(note.isFollow()){
            followBtn.setBackgroundResource(R.mipmap.followedbtn);
        }else {
            followBtn.setBackgroundResource(R.mipmap.followbtn);
        }

        String noteImgUrl = U+note.getNoteImage();
        String userIconUrl = U+note.getUser().getUserPhoto();
        String contentUserUrl = U+contentUser.getUserPhoto();
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this)
                .load(noteImgUrl)
                .into(noteImage);
        Glide.with(this)
                .load(userIconUrl)
                .apply(options)
                .into(userIcon);
        Glide.with(this)
                .load(contentUserUrl)
                .apply(options)
                .into(contentUserIcon);
    }

    //请求数据并初始化数组
    private class CommentListTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            String url = U+"/comment/getComment/"+noteId;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            // 3.创建Call对象
            Call call = okHttpClient.newCall(request);
            // 4.提交请求，返回响应
            try {
                Response response = call.execute();
                String rel = response.body().string();
                JSONObject noteObject = new JSONObject(rel);

                String array = noteObject.getJSONArray("commentList").toString();
                Log.e("alist",array);
                Gson gson = new Gson();
                Type noteListType = new TypeToken<ArrayList<Comment>>(){}.getType();
                comments = gson.fromJson(array, noteListType);
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setList();
        }
    }

    //设置commentListView
    private void setList(){
        commentAdapter = new NoteCommentListAdapter(this,
                R.layout.item_note_comment,comments);
        commentListView.setAdapter(commentAdapter);
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0,len = comments.size();i < len; i++) {
            View listItem = commentAdapter.getView(i, null,commentListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = commentListView.getLayoutParams();
        params.height = totalHeight + (commentListView.getDividerHeight() *  (commentAdapter.getCount() - 1));
        commentListView.setLayoutParams(params);
        commentAdapter.notifyDataSetChanged();
    }


    //设置按钮点击监听器
    private class ButtonOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //点击返回按钮返回上一页
                case R.id.backBtn:
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    break;
                //点击关注按钮关注
                case R.id.followBtn:
                    int followedId = note.getUser().getUserId();
                    boolean follow = note.isFollow();
                    if(note.isFollow()){
                        followBtn.setBackgroundResource(R.mipmap.followbtn);
                        note.setFollow(false);
                        followTask(followedId,follow);
                    }else {
                        followBtn.setBackgroundResource(R.mipmap.followedbtn);
                        note.setFollow(true);
                        followTask(followedId,follow);
                    }
                    break;
                //点赞
                case R.id.pickBtn:
                    int lc = note.getLikeCount();
                    int noteId = note.getNoteId();
                    boolean like = note.isLike();
                    if(note.isLike()){
                        lc--;
                        pickBtn.setBackgroundResource(R.mipmap.pick);
                        pickCount.setText(lc+"");
                        note.setLike(false);
                        note.setLikeCount(lc);
                        pickTask(noteId,like);
                    }else {
                        lc++;
                        pickCount.setText(lc+"");
                        pickBtn.setBackgroundResource(R.mipmap.picked);
                        note.setLike(true);
                        note.setLikeCount(lc);
                        pickTask(noteId,like);
                    }
                    break;
                //点击收藏按钮
                case R.id.collectBtn:
                    int cc=note.getCollectCount();
                    boolean collect = note.isCollect();
                    int nId = note.getNoteId();
                    if(note.isCollect()){
                        cc--;
                        collectCount.setText(cc+"");
                        collectBtn.setBackgroundResource(R.mipmap.collectbtn);
                        note.setCollect(false);
                        note.setCollectCount(cc);
                        collectTask(nId,collect);
                    }else {
                        cc++;
                        collectCount.setText(cc+"");
                        collectBtn.setBackgroundResource(R.mipmap.collectedbtn);
                        note.setCollect(true);
                        note.setCollectCount(cc);
                        collectTask(nId,collect);
                    }
                    break;
            }
        }
    }
    //点赞和取消赞触发的进程
    private void pickTask(final int noteId, final boolean like) {
        //1.创建OKHttpClient对象(已创建)
        // 2.创建Request对象
        new Thread(){
            @Override
            public void run() {
                String url = U + "/note/pick/" + noteId + "?userId=" + userId + "&like=" + like;
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                // 3.创建Call对象
                Call call = okHttpClient.newCall(request);
                // 4.提交请求，返回响应
                try {
                    call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //收藏和取消收藏触发的进程
    private void collectTask(final int noteId, final boolean collect){
            new Thread(){
                @Override
                public void run() {
                    String url = U+"/note/collect/"+noteId+"?userId="+userId+"&collect="+collect;
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    try {
                        call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
    }

    //关注和取消关注触发的进程
    private void followTask(final int followedUserId, final boolean follow){
        new Thread(){
            @Override
            public void run() {
                String url = U+"/note/follow/"+followedUserId+"?userId="+userId+"&follow="+follow;
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = okHttpClient.newCall(request);
                try {
                    call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
