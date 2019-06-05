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

public class FanAdapter extends BaseAdapter {
    private String path = null;
    private UserBean user;
    private Context context;
    private int itemLayout;
    private List<UserBean> fanList = new ArrayList<>();
    private OkHttpClient okHttpClient;
    private boolean isStatus;

    public FanAdapter(Context context, int itemLayout, List<UserBean> fanList, UserBean userBean, String path) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.fanList = fanList;
        this.user = userBean;
        this.path = path;
    }

    @Override
    public int getCount() {
        return fanList.size();
    }

    @Override
    public Object getItem(int position) {
        return fanList.get(position);
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
        String photoPath = fanList.get(position).getUserPhotoPath();
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(context).load(photoPath).apply(mRequestOptions).into(imageView);
        TextView textView = convertView.findViewById(R.id.tv_note);
        textView.setText(fanList.get(position).getUserName());
        final TextView followView = convertView.findViewById(R.id.follow);
        isStatus = fanList.get(position).isStatus();
        if (isStatus) {
            followView.setText(" 互相关注 ");
            followView.setTextColor(context.getResources().getColor(R.color.gray));
            followView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (followView.getText().toString().equals(" 互相关注 ")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("确认不再关注？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //把取消关注的用户从数据库中删除
                                Log.e("id", fanList.get(position).getUserId() + " " + user.getUserId());
                                DeleteFollow deleteFollow = new DeleteFollow();
                                deleteFollow.execute(fanList.get(position).getUserId(), user.getUserId(), followView);
                                isStatus = false;
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
                    if (followView.getText().toString().equals(" 回粉 ")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("确认关注用户？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                InsertFollow insertFollow = new InsertFollow();
                                insertFollow.execute(fanList.get(position).getUserId(), user.getUserId(), followView);
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
        } else {
            followView.setText(" 回粉 ");
            followView.setTextColor(context.getResources().getColor(R.color.warmRed));
            followView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("确认关注用户？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InsertFollow insertFollow = new InsertFollow();
                            insertFollow.execute(fanList.get(position).getUserId(), user.getUserId(), followView);
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
            });
        }
        TextView noteText = convertView.findViewById(R.id.noteCount);
        noteText.setText("笔记." + fanList.get(position).getNoteCount());
        TextView fanText = convertView.findViewById(R.id.fanCount);
        fanText.setText("粉丝." + fanList.get(position).getFanCount());
        //点击头像跳转到当前用户的详情页面
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PerFriendActivity.class);
                intent.putExtra("friend", fanList.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    //把粉丝加入我的关注表里
    public class InsertFollow extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            int followId = (Integer) objects[0];
            int userId = (Integer) objects[1];
            TextView followView = (TextView) objects[2];
            String url = path + "/follow/insertFan?followId=" + followId + "&userId=" + userId;
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            String result = null;
            try {
                result = call.execute().body().string();
                Log.e("insertFan---", result);
                if (result.equals("插入成功")) {
                    Log.e("msg","插入成功");
                } else {
                    Log.e("msg","插入失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return followView;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            TextView followView = (TextView) o;
            followView.setText("互相关注");
            followView.setTextColor(context.getResources().getColor(R.color.gray));
        }
    }

    //把取消关注的用户从数据库中删除
    public class DeleteFollow extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            int followId = (Integer) objects[0];
            int userId = (Integer) objects[1];
            TextView followView = (TextView) objects[2];
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
            return followView;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            TextView followView = (TextView) o;
            followView.setText(" 回粉 ");
            followView.setTextColor(context.getResources().getColor(R.color.warmRed));
        }
    }
}
