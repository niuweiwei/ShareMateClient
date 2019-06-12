package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.qiniu.pili.droid.shortvideo.PLTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.ImageTask;
import cn.edu.hebtu.software.sharemateclient.tools.RoundImgView;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private List<NoteBean> notes =new ArrayList<>();
    private  AlertDialog.Builder builder;
    private String U;
    private UserBean user;
    private OkHttpClient okHttpClient;

    public CustomAdapter(Context context, int itemLaout,List<NoteBean> notes,String U,UserBean user) {
        this.context = context;
        this.itemLayout = itemLaout;
        this.notes = notes;
        this.U = U;
        this.user = user;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(itemLayout,null);
        }
        PLVideoView noteVideo = convertView.findViewById(R.id.noteVideo);
        Button startBtn =convertView.findViewById(R.id.startBtn);
        TextView user_name=convertView.findViewById(R.id.user_name);
        user_name.setText(notes.get(position).getUserName());
        ImageView iv_photo=convertView.findViewById(R.id.iv_photo);
        ImageView usericon2 = convertView.findViewById(R.id.user_icon2);
        //视频
        if(notes.get(position).getNoteImage()==null){
            noteVideo.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.VISIBLE);
            String noteVideoUrl = notes.get(position).getNoteVideo();
            Log.e("videopath",noteVideoUrl);
            final Uri videoUri = Uri.parse(noteVideoUrl);
            noteVideo.setLooping(false);
            noteVideo.setVideoURI(videoUri);
            noteVideo.setOnCompletionListener(new PLOnCompletionListener() {
                @Override
                public void onCompletion() {
                    startBtn.setVisibility(View.VISIBLE);
                }
            });
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startBtn.setVisibility(View.INVISIBLE);
                    noteVideo.start();
                }
            });
        }

        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        String url2 = U + "/"+user.getUserPhoto();
        Glide.with(context).load(url2).apply(mRequestOptions).into(usericon2);
        String photoPath = U + "/"+notes.get(position).getNoteImage();
        Log.e("photoPath",photoPath);
        Glide.with(context).load(photoPath).into(iv_photo);
        ImageView user_icon=convertView.findViewById(R.id.user_icon);
        String userphoto=U + "/"+notes.get(position).getUserImage();
        Log.e("userphoto",userphoto);
        Glide.with(context).load(userphoto).apply(mRequestOptions).into(user_icon);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转跳至个人首页
            }
        });
        final Button guannzhu=convertView.findViewById(R.id.guannzhu);
        guannzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(context);
                builder.setMessage("确认取消关注？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing to do
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取数据库取消关注
                        guannzhu.setBackgroundResource(R.drawable.a8);
                    }
                });
            }
        });
//        final AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        ImageView comment_icon=convertView.findViewById(R.id.comment_icon);
        String commentuserphoto=U + "/"+notes.get(position).getCommentUserImage();
        Glide.with(context).load(commentuserphoto).apply(mRequestOptions).into(comment_icon);
        TextView ivtext=convertView.findViewById(R.id.note_alltext);
        ivtext.setText(notes.get(position).getNoteDetail());
        final  TextView z_count=convertView.findViewById(R.id.z_count);
        z_count.setText(String.valueOf(notes.get(position).getNoteLikeCount()));
        final Button dianzan=convertView.findViewById(R.id.dianzan);
        Log.e("tag",notes.get(position).getZanTag()+"");
        if(notes.get(position).getZanTag()==1){
            Log.e("tag",notes.get(position).getZanTag()+"");
            dianzan.setBackgroundResource(R.drawable.like);
        }else{
            dianzan.setBackgroundResource(R.drawable.xin);
        }
            dianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notes.get(position).getZanTag()==1){
                        dianzan.setBackgroundResource(R.drawable.xin);
                        z_count.setText(String.valueOf(notes.get(position).getNoteLikeCount()-1));
                        notes.get(position).setNoteLikeCount(notes.get(position).getNoteLikeCount()-1);
                        notes.get(position).setZanTag(0);
                    }
                    else{

                        dianzan.setBackgroundResource(R.drawable.like);
                        z_count.setText(String.valueOf(notes.get(position).getNoteLikeCount()+1));
                        notes.get(position).setNoteLikeCount(notes.get(position).getNoteLikeCount()+1);
                        notes.get(position).setZanTag(1);

                    }
                    //                    //更新数据库
//                    new Thread(){
//                        @Override
//                        public void run() {
//                            super.run();
//                            int noteid=notes.get(position).getNoteId();
//                            int userid=1;
//                            Log.e("noteid",String.valueOf(noteid));
//                            String url=U+"/note/zancount?noteid="+noteid+"&userid="+userid;
//                            Log.e("fr",url);
//                            okHttpClient = new OkHttpClient();
//                            Request request=new Request.Builder()
//                                    .url(url)
//                                    .build();
//                            Call call=okHttpClient.newCall(request);
//                            try {
//                                call.execute();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();


                }
            });




        final TextView c_count=convertView.findViewById(R.id.c_count);
        c_count.setText(String.valueOf(notes.get(position).getNoteCollectionCount()));
        final Button shoucang=convertView.findViewById(R.id.shoucang);
        if(notes.get(position).getCollectTag()==1){
            shoucang.setBackgroundResource(R.drawable.xingxing2);
        }else{
            shoucang.setBackgroundResource(R.drawable.xingxing);}
            shoucang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notes.get(position).getCollectTag()==1){
                        shoucang.setBackgroundResource(R.drawable.xingxing);
                        c_count.setText(String.valueOf(notes.get(position).getNoteCollectionCount()-1));
                        notes.get(position).setNoteCollectionCount(notes.get(position).getNoteCollectionCount()-1);
                        notes.get(position).setCollectTag(0);
                    }else{
                        shoucang.setBackgroundResource(R.drawable.xingxing2);
                        c_count.setText(String.valueOf(notes.get(position).getNoteCollectionCount()+1));
                        notes.get(position).setNoteCollectionCount(notes.get(position).getNoteCollectionCount()+1);
                        notes.get(position).setCollectTag(1);
//
                    }


//                    //更行数据库
//                    new Thread(){
//                        @Override
//                        public void run() {
//                            super.run();
//                            int noteid=notes.get(position).getNoteId();
//                            int userid=1;
//                            okHttpClient = new OkHttpClient();
//                            Request request=new Request.Builder()
//                                    .url(U+"/note/collectcount?noteid="+noteid+"&userid="+userid)
//                                    .build();
//                            Call call=okHttpClient.newCall(request);
//                            try {
//                                call.execute();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
                }
            });


        final TextView p_count=convertView.findViewById(R.id.p_count);
        p_count.setText(String .valueOf(notes.get(position).getNoteCommentCount()));
        final Button pinglun=convertView.findViewById(R.id.pinglun);
       pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       final TextView p_count2=convertView.findViewById(R.id.all_count);
       p_count2.setText("共"+String .valueOf(notes.get(position).getNoteCommentCount())+"条评论");
       final TextView pinglun1=convertView.findViewById(R.id.pinglun1);
       pinglun1.setText(notes.get(position).getCommentdetial());
       //发布评论
        final EditText fb=convertView.findViewById(R.id.fb);

        Button fabu=convertView.findViewById(R.id.fabu);
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pintlunfabu= (String) fb.getText().toString();
                p_count2.setText("共"+String .valueOf(notes.get(position).getNoteCommentCount()+1)+"条评论");
                p_count.setText(String .valueOf(notes.get(position).getNoteCommentCount()+1));
                final int userId=user.getUserId();
                pinglun1.setText(pintlunfabu);
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("noteid", String.valueOf(notes.get(position).getNoteId()));
                    jsonObject.put("userid", String.valueOf(userId));
                    jsonObject.put("pinglunfabu",pintlunfabu);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("fr", jsonObject.toString());
                okHttpClient = new OkHttpClient();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String url=U+"/note/addcomment";
                        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),
                                jsonObject.toString());
                        Log.e("fr",jsonObject.toString());
                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(url).build();
                        Call call = okHttpClient.newCall(request);
                        Log.e("fr","开始执行");
                        try {
                            call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });
        return convertView;
    }



}
