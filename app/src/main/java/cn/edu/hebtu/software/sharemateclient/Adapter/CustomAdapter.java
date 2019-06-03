package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.tools.ImageTask;
import cn.edu.hebtu.software.sharemateclient.tools.RoundImgView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private List<NoteBean> notes =new ArrayList<>();
    private  AlertDialog.Builder builder;
    private String U= "http://192.168.0.109:8080/ShareMateServer";
    private OkHttpClient okHttpClient;

    public CustomAdapter(Context context, int itemLaout,List<NoteBean> notes) {
        this.context = context;
        this.itemLayout = itemLaout;
        this.notes = notes;
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
        ImageView iv_photo=convertView.findViewById(R.id.iv_photo);
        String photoPath = U + "/"+notes.get(position).getNoteImage();
        Log.e("photoPath",photoPath);
        Glide.with(context).load(photoPath).into(iv_photo);
        ImageView user_icon=convertView.findViewById(R.id.user_icon);
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
        TextView ivtext=convertView.findViewById(R.id.note_alltext);
        ivtext.setText(notes.get(position).getNoteDetail());
        TextView z_count=convertView.findViewById(R.id.z_count);
        z_count.setText(String.valueOf(notes.get(position).getZancount()));
        final Button dianzan=convertView.findViewById(R.id.dianzan);
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dianzan.setBackgroundResource(R.drawable.like);
                //更新数据库
               new Thread(){
                   @Override
                   public void run() {
                       super.run();
                       int noteid=notes.get(position).getNoId();
                       int userid=1;
                       Log.e("noteid",String.valueOf(noteid));
                       okHttpClient = new OkHttpClient();
                       Request request=new Request.Builder()
                               .url(U+"/note/zancount?noteid="+noteid+"&userid="+userid)
                               .build();
                       Call call=okHttpClient.newCall(request);
                       try {
                           call.execute();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               };


            }
        });
        TextView c_count=convertView.findViewById(R.id.c_count);
        c_count.setText(String.valueOf(notes.get(position).getCollectcount()));
        final Button shoucang=convertView.findViewById(R.id.shoucang);
        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoucang.setBackgroundResource(R.drawable.xingxing2);
                //更行数据库
              new Thread(){
                  @Override
                  public void run() {
                      super.run();
                      int noteid=notes.get(position).getNoId();
                      int userid=1;
                      okHttpClient = new OkHttpClient();
                      Request request=new Request.Builder()
                              .url(U+"/note/collectcount?noteid="+noteid+"&userid="+userid)
                              .build();
                      Call call=okHttpClient.newCall(request);
                      try {
                          call.execute();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }
              };
            }
        });
        TextView p_count=convertView.findViewById(R.id.p_count);
        p_count.setText(String .valueOf(notes.get(position).getPingluncount()));
        final Button pinglun=convertView.findViewById(R.id.pinglun);
       pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }



}
