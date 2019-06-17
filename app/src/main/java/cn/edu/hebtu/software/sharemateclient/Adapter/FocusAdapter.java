package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Activity.PerFriendActivity;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FocusAdapter extends BaseAdapter {

    private int uid;
    private int id;
    private UserBean user;
    private String path;
    private int itemLayout;
    private Context context;
    private List<UserBean> userList = new ArrayList<>();
    private OkHttpClient okHttpClient;
//    private TextView focusText;

    public FocusAdapter(int itemLayout, Context context, List<UserBean> userList, String path, UserBean userBean) {
        this.itemLayout = itemLayout;
        this.context = context;
        this.userList = userList;
        this.path = path;
        this.user = userBean;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(itemLayout, null);
        }
        final ImageView imageView = convertView.findViewById(R.id.img_content);
        String photoPath = path + "/"+ userList.get(position).getUserPhoto();
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(context).load(photoPath).apply(mRequestOptions).into(imageView);
        TextView textView = convertView.findViewById(R.id.tv_note);
        textView.setText(userList.get(position).getUserName());
        final TextView focusText = convertView.findViewById(R.id.focus);
        focusText.setText("已关注");
        focusText.setTextColor(context.getResources().getColor(R.color.gray));
        //点击头像跳转到当前用户的详情页面
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, PerFriendActivity.class);
                intent.putExtra("friend", userList.get(position));
                context.startActivity(intent);
            }
        });
        //点击已关注选择是否取消关注
        focusText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focusText.getText().toString().equals("已关注")) {
                    Log.e("focusText", focusText.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("确认不再关注？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            focusText.setText("关注");
                            focusText.setTextColor(context.getResources().getColor(R.color.warmRed));
                            //把取消关注的用户从数据库中删除
                            uid = user.getUserId();
                            id = userList.get(position).getUserId();
                            DeleteFollow deleteFollow = new DeleteFollow();
                            deleteFollow.execute(user.getUserId(), userList.get(position).getUserId());
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                if (focusText.getText().toString().equals("关注")) {
                    Log.e("focusText", focusText.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("确认关注该用户？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            focusText.setText("已关注");
                            focusText.setTextColor(context.getResources().getColor(R.color.gray));
                            //插入关注的用户
                            InsertFollow insertFollow = new InsertFollow();
                            insertFollow.execute(uid, id);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        return convertView;
    }

    //把取消关注的用户从数据库中删除
    public class DeleteFollow extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            int followId = (Integer) objects[0];
            int userId = (Integer) objects[1];
            Log.e("delete", followId + "," + userId);
            String url = path + "/follow/deleteFollow?followId=" + followId + "&userId=" + userId;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                result = call.execute().body().string();
                Log.e("deleteFollow---", result);
                if (result.equals("删除成功")) {
                    Log.e("delete", "删除成功");
                } else {
                    Log.e("delete", "删除失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //把我关注的人插入数据库中
    public class InsertFollow extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            int followId = (Integer) objects[0];
            int userId = (Integer) objects[1];
            Log.e("insert", followId + "," + userId);
            String url = path + "/follow/insertFan?followId=" + followId + "&userId=" + userId;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                result = call.execute().body().string();
                Log.e("insertFan---", result);
                if (result.equals("插入成功")) {
                    Log.e("msg", "插入成功");
                } else {
                    Log.e("msg", "插入失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
