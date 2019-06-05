package cn.edu.hebtu.software.sharemateclient.Activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import cn.edu.hebtu.software.sharemateclient.Adapter.NoteAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PerFriendActivity extends AppCompatActivity {
    private UserBean user = new UserBean() ;
    private TextView nameView;
    private TextView introView;
    private TextView idView;
    private ImageView photoView;
    private ImageView backView;
    private TextView collection;
    private TextView note;
    private TextView followCount;
    private TextView fanCount;
    private TextView likeCount;
    private GridView gridView;
    private NoteAdapter noteAdapter;
    private List<NoteBean> collectionList = new ArrayList<>();
    private List<NoteBean> noteList = new ArrayList<>();
    private String path = null;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        path = getResources().getString(R.string.server_path);
        user = (UserBean) getIntent().getSerializableExtra("friend");
        findView();
        setListener();
        setContent();
        GetNote getNote = new GetNote();
        getNote.execute(user);
        GetCollection getCollection = new GetCollection();
        getCollection.execute(user);
    }
    public void findView(){
        nameView = findViewById(R.id.userName);
        idView = findViewById(R.id.userId);
        photoView = findViewById(R.id.userPhoto);
        introView = findViewById(R.id.userIntro);
        backView = findViewById(R.id.back);
        collection = findViewById(R.id.collection);
        note = findViewById(R.id.note);
        gridView = findViewById(R.id.root);
        followCount = findViewById(R.id.followCount);
        fanCount = findViewById(R.id.fanCount);
        likeCount = findViewById(R.id.likeCount);
    }
    public void setContent(){
        gridView.setEmptyView((findViewById(R.id.empty_view)));
        String userId = String.format("%06d",user.getUserId());
        idView.setText("ShareMate号:"+userId);
        nameView.setText(user.getUserName());
        followCount.setText(""+user.getFollowCount());
        fanCount.setText(""+user.getFanCount());
        likeCount.setText(""+user.getLikeCount());
        if (user.getUserIntro() == null || user.getUserIntro().length() < 20) {
            introView.setText(user.getUserIntro());
        } else {
            introView.setText(user.getUserIntro().substring(0, 20) + ".....");
        }
        String photoPath = user.getUserPhotoPath();
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(this).load(photoPath).apply(mRequestOptions).into(photoView);

    }
    public void setListener(){
        SetOnClickListener listener = new SetOnClickListener();
        backView.setOnClickListener(listener);
        note.setOnClickListener(listener);
        collection.setOnClickListener(listener);
//        fanCount.setOnClickListener(listener);
//        followCount.setOnClickListener(listener);
    }
//    public void setNoteGridView(GridView gridView){
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("111","111");
//                Intent intent = new Intent(PerFriendActivity.this, NoteDetailActivity.class);
//                intent.putExtra("noteId",noteList.get(position).getNoteId());
//                Log.e("noteId",noteList.get(position).getNoteId()+"");
//                intent.putExtra("userId",user.getUserId());
//                startActivity(intent);
//            }
//        });
//    }
//    public void setCollectGridView(GridView gridView){
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("111","111");
//                Intent intent = new Intent(PerFriendActivity.this, NoteDetailActivity.class);
//                intent.putExtra("noteId",collectionList.get(position).getNoteId());
//                intent.putExtra("userId",user.getUserId());
//                startActivity(intent);
//            }
//        });
//    }

    public class SetOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    PerFriendActivity.this.finish();
                    break;
//                case R.id.followCount:
//                    Intent focusIntent = new Intent();
//                    focusIntent.setClass(PerFriendActivity.this, PerFollowActivity.class);
//                    focusIntent.putExtra("user",user);
//                    startActivity(focusIntent);
//                    break;
//                case R.id.fanCount:
//                    Intent fanIntent = new Intent();
//                    fanIntent.setClass(PerFriendActivity.this, PerFanActivity.class);
//                    fanIntent.putExtra("user",user);
//                    startActivity(fanIntent);
//                    break;
                case R.id.note:
                    note.setTextColor(getResources().getColor(R.color.warmRed));
                    collection.setTextColor(getResources().getColor(R.color.darkGray));
                    noteAdapter = new NoteAdapter(PerFriendActivity.this, R.layout.item_note,noteList,user,path);
                    gridView.setAdapter(noteAdapter);
//                    setNoteGridView(gridView);
                    break;
                case R.id.collection:
                    collection.setTextColor(getResources().getColor(R.color.warmRed));
                    note.setTextColor(getResources().getColor(R.color.darkGray));
                    noteAdapter = new NoteAdapter(PerFriendActivity.this, R.layout.item_note,collectionList,user,path);
                    gridView.setAdapter(noteAdapter);
//                    setCollectGridView(gridView);
                    break;
            }
        }
    }
    //从数据库中取出来笔记
    public class GetNote extends AsyncTask {
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
            noteAdapter = new NoteAdapter(getApplicationContext(), R.layout.item_note,noteList,user,path);
            gridView.setAdapter(noteAdapter);
//            setNoteGridView(gridView);
        }
    }

    //从数据库中取出收藏
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
}
