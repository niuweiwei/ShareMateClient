package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.sharemateclient.Adapter.CustomAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FollowFragment extends Fragment{
    private List<NoteBean> notes=new ArrayList<NoteBean>();
    private ListView listView;
    private CustomAdapter customAdapter = null;
    private UserBean user;
    private OkHttpClient okHttpClient;
    private String U;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.note_fragment, container, false);
        listView = view.findViewById(R.id.list);
        U=getResources().getString(R.string.server_path);
        okHttpClient = new OkHttpClient();
        user= (UserBean) getActivity().getIntent().getSerializableExtra("user");
        Log.e("okhttp","okhttp");
        listView=view.findViewById(R.id.list);
        guanzhuAsyncTask guanzhuAsyncTask=new guanzhuAsyncTask();
        guanzhuAsyncTask.execute();


        return view;
    }
    class guanzhuAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
           int userid=user.getUserId();
            Log.e("userid",String.valueOf(userid));
            String u=U+"/note/allnotelist?userid1="+userid;
            Log.e("URL",u);
            Request request=new Request.Builder()
                    .url(u)
                    .build();
            Call call=okHttpClient.newCall(request);
            try {
                String notejson=call.execute().body().string();
                Log.e("notelist" ,notejson);
                JSONObject noteObject=new JSONObject(notejson);
                JSONArray jsonArray=noteObject.getJSONArray("notelist");
                String notestr=jsonArray.toString();
                Gson gson=new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                Type type=new TypeToken<List<NoteBean>>(){}.getType();
                List<NoteBean> noteList=gson.fromJson(notestr,type);
                Log.e("noteList",noteList.get(0).getNoteTitle());
                for(int i=0;i<noteList.size();i++){
                    NoteBean noteBean=new NoteBean();
                    noteBean.setNoteId(noteList.get(i).getNoteId());
                    noteBean.setNoteImage(noteList.get(i).getNoteImage());
                    noteBean.setTitle(noteList.get(i).getTitle());
                    noteBean.setNoteDetail(noteList.get(i).getNoteDetail());
                    noteBean.setNoteLikeCount(noteList.get(i).getNoteLikeCount());
                    noteBean.setNoteCollectionCount(noteList.get(i).getNoteCollectionCount());
                    noteBean.setNoteCommentCount(noteList.get(i).getNoteCommentCount());
                    noteBean.setCommentdetial(noteList.get(i).getCommentdetial());
                    noteBean.setUserImage(noteList.get(i).getUserImage());
                    noteBean.setUserName(noteList.get(i).getUserName());
                    notes.add(noteBean);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("size",String.valueOf(notes.size()));
            for(int i=0;i<notes.size();i++)
            {
                Log.e("zan",String.valueOf(notes.get(i).getNoteLikeCount()));
                Log.e("id",String.valueOf(notes.get(i).getNoteId()));
                Log.e("colloct",String.valueOf(notes.get(i).getNoteCollectionCount()));

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            customAdapter=new CustomAdapter(getContext(),R.layout.list_item,notes,user);
            listView.setAdapter(customAdapter);
            Log.e("listview","liatview");
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //转跳至详情页面
                }
            });
        }
    }

}
