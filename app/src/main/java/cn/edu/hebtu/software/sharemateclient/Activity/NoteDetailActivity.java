package cn.edu.hebtu.software.sharemateclient.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.sharemateclient.Adapter.NoteCommentListAdapter;
import cn.edu.hebtu.software.sharemateclient.Adapter.ReplyListAdapter;
import cn.edu.hebtu.software.sharemateclient.Entity.Comment;
import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.Entity.Reply;
import cn.edu.hebtu.software.sharemateclient.Entity.User;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class NoteDetailActivity extends AppCompatActivity implements NoteCommentListAdapter.Callback{

    private ImageView userIcon,noteImage;
    private Button backBtn,followBtn,pickBtn,collectBtn,commentBtn;
    private Button send;
    private TextView userName;
    private TextView noteTitle,noteDetail,noteDate;
    private TextView commentCount,pickCount,collectCount,comCount;
    private EditText commentEdit;
    private ButtonOnclickListener listener;
    private Note note;
    private String U;
    private User contentUser;
    private int userId;
    private int noteId,commentId,reReplyId;
    private OkHttpClient okHttpClient;
    private ListView commentListView;
    private NoteCommentListAdapter commentAdapter;
    private List<Comment> comments;
    private CommentListTask commentListTask;
    private int commentType,commentPosition,commentPosition2,replyPostion;
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
        send = findViewById(R.id.send);
        noteImage = findViewById(R.id.noteImage);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetail = findViewById(R.id.noteText);
        noteDate = findViewById(R.id.noteDate);
        commentCount = findViewById(R.id.commentCount);
        pickCount = findViewById(R.id.pickCount);
        comCount = findViewById(R.id.comCount);
        collectCount = findViewById(R.id.collectCount);
        commentEdit = findViewById(R.id.editSay);
        commentListView = findViewById(R.id.noteCommentList);
        listener = new ButtonOnclickListener();
        backBtn.setOnClickListener(listener);
        followBtn.setOnClickListener(listener);
        pickBtn.setOnClickListener(listener);
        collectBtn.setOnClickListener(listener);
        commentBtn.setOnClickListener(listener);
        send.setOnClickListener(listener);
        commentType = 0;
        commentEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentType =0;
                Log.e("点击了评论2","editText");
                Button button = findViewById(R.id.send);
                button.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(commentEdit);
                Toast.makeText(getApplicationContext(),"点击评论",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this)
                .load(noteImgUrl)
                .into(noteImage);
        Glide.with(this)
                .load(userIconUrl)
                .apply(options)
                .into(userIcon);
    }

    public void showSoftInputFromWindow(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.findFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void click(View v, int p,int c) {
        commentType=2;commentPosition2= c;replyPostion = p;
        reReplyId = comments.get(c).getReplyList().get(p).getReplyId();
        Button button = findViewById(R.id.send);
        button.setVisibility(View.VISIBLE);
        showSoftInputFromWindow(commentEdit);
    }

    @Override
    public void click(View v) {
    }


    //请求数据并初始化数组
    private class CommentListTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String url = U+"/comment/getComment/"+noteId;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String rel = response.body().string();
                JSONObject noteObject = new JSONObject(rel);
                String array = noteObject.getJSONArray("commentList").toString();
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
                R.layout.item_note_comment,comments,this);
        commentListView.setAdapter(commentAdapter);
        setListViewHeight();
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                commentType =1;
                commentPosition= position;
                commentId = comments.get(position).getCommentId();
                Button button = findViewById(R.id.send);
                button.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(commentEdit);
            }
        });
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
                case R.id.send:
                    //获取评论或回复的内容
                    String text = commentEdit.getText().toString().trim();
                    if(text.equals("")||text==null){
                        Toast.makeText(NoteDetailActivity.this,"评论不能为空哦",Toast.LENGTH_LONG).show();
                    }else {
                       if(commentType==0) { //type=0表示评论
                        //执行异步进程添加评论
                        commentTask(userId,text,note.getNoteId());
                        //更新主布局
                        updateComment(text);
                       }else if(commentType == 1){//type=1表示评论的回复
                           //添加评论的回复
                           addReplyForCommentTask(userId,commentId,text);
                           //更新主布局
                           updateReply(text);
                       }else{//type==其他表示回复的回复
                           //添加回复的回复
                           addReReplyTask(userId,reReplyId,text);
                           //更新主布局
                           updateReReply(text);
                       }
                    }
                    //隐藏发送按钮，软键盘 ，清空editText
                    send.setVisibility(View.INVISIBLE);
                    commentEdit.setText("");
                    commentEdit.clearFocus();
                    InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    break;
            }
        }
    }

    //添加评论点击发送的进程
    private void commentTask(int contentUserId,String comment,int noteId){
        Map<String,String> map = new HashMap<>();
        map.put("userId", contentUserId+"");
        map.put("comment",comment);
        map.put("noteId",noteId+"");
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            //追加表单信息
            builder.add(key,map.get(key));
        }
        String url = U + "/comment/addComment";
        RequestBody formBody=builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("yes","请求成功！");
            }
        });
    }

    //添加评论的回复的进程
    private void addReplyForCommentTask(int contentUserId,int commentId,String reply){
        Map<String,String> map = new HashMap<>();
        map.put("userId", contentUserId+"");
        map.put("reply",reply);
        map.put("commentId",commentId+"");
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            //追加表单信息
            builder.add(key,map.get(key));
        }
        String url = U + "/comment/addReply";
        RequestBody formBody=builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("yes","请求成功！");
            }
        });
    }

    //添加回复的回复的进程
    private void addReReplyTask(int contentUserId,int reReplyId,String reply){
        Map<String,String> map = new HashMap<>();
        map.put("userId", contentUserId+"");
        map.put("reply",reply);
        map.put("reReplyId",reReplyId+"");
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            //追加表单信息
            builder.add(key,map.get(key));
        }
        String url = U + "/comment/addReReply";
        RequestBody formBody=builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("yes","请求成功！");
            }
        });
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

    //主布局更新评论
    private void updateComment(String text){
        Comment comment = new Comment();
        comment.setCommentDetail(text);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date = sdf.format(d);
        comment.setCommentDate(date+"");
        comment.setUser(contentUser);
        comments.add(comment);
        comCount.setText(comments.size()+"");
        commentCount.setText("共 "+comments.size()+" 条评论");
        setList();
    }

    //主布局更新回复
    private void updateReply(String text){
        Reply reply = new Reply();
        reply.setReplyDetail(text);
        reply.setCommentId(commentId);
        reply.setUserId(userId);
        reply.setUser(contentUser);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date = sdf.format(d);
        reply.setReplyTime(date+"");
        comments.get(commentPosition).getReplyList().add(reply);
        setList();
    }

    //主布局更新回复的回复
    private void updateReReply(String text){
        Reply reply = new Reply();
        reply.setReplyDetail(text);
        reply.setReReplyId(reReplyId);
        reply.setUserId(userId);
        reply.setUser(contentUser);
        reply.setReplyedUser(comments.get(commentPosition2).getReplyList()
                .get(replyPostion).getUser());
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date = sdf.format(d);
        reply.setReplyTime(date+"");
        comments.get(commentPosition2).getReplyList().add(reply);
        setList();
    }

    //自定义listView的高度
    private void setListViewHeight(){
        int totalHeight = 0;
        for (int i = 0,len = comments.size();i < len; i++) {
            View listItem = commentAdapter.getView(i, null,commentListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = commentListView.getLayoutParams();
        params.height = totalHeight + (commentListView.getDividerHeight()
                * (commentAdapter.getCount() - 1));
        commentListView.setLayoutParams(params);
        commentAdapter.notifyDataSetChanged();
    }
}
