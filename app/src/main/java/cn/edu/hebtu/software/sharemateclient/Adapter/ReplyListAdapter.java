package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Entity.Comment;
import cn.edu.hebtu.software.sharemateclient.Entity.Reply;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReplyListAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private List<Reply> replys;
    private String U;
    private int userId = 17;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private PickTask pickTask;
    public ReplyListAdapter(Context context, int itemLayout, List<Reply> replys) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.replys = replys;
    }

    @Override
    public int getCount() {
        return replys.size();
    }

    @Override
    public Object getItem(int position) {
        return replys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(itemLayout,null);
            viewHolder= new ViewHolder();
            viewHolder.r = convertView.findViewById(R.id.r);
            viewHolder.repliedName = convertView.findViewById(R.id.repliedName);
            viewHolder.replyDetail=convertView.findViewById(R.id.replyDetail);
            viewHolder.userIcon=convertView.findViewById(R.id.userIcon);
            viewHolder.userName = convertView.findViewById(R.id.userName);
            viewHolder.pickBtn = convertView.findViewById(R.id.pickReply);
            viewHolder.replyTime = convertView.findViewById(R.id.replyTime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        U=context.getResources().getString(R.string.url);
        String userIconUrl = U+replys.get(position).getUser().getUserPhoto();
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(userIconUrl)
                .apply(options)
                .into(viewHolder.userIcon);
        viewHolder.replyTime.setText(replys.get(position).getReplyTime());
        viewHolder.userName.setText(replys.get(position).getUser().getUserName());
        viewHolder.replyDetail.setText(replys.get(position).getReplyDetail());
        if(replys.get(position).isLike()){
            viewHolder.pickBtn.setBackgroundResource(R.mipmap.picked);
        }else{
            viewHolder.pickBtn.setBackgroundResource(R.mipmap.pick);
        }
        if(replys.get(position).getCommentId()==0){
            viewHolder.r.setVisibility(View.VISIBLE);
            viewHolder.repliedName.setText(replys.get(position).getReplyedUser().getUserName());
            viewHolder.repliedName.setVisibility(View.VISIBLE);
        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int replyId = replys.get(position).getReplyId();
                boolean like = replys.get(position).isLike();
                if(like){
                    v.setBackgroundResource(R.mipmap.pick);
                    replys.get(position).setLike(false);
                    Toast.makeText(context,"取消赞啦",Toast.LENGTH_SHORT).show();
                    pickTask = new PickTask();
                    pickTask.execute(replyId,like);
                }else{
                    Toast.makeText(context,"赞啦",Toast.LENGTH_SHORT).show();
                    replys.get(position).setLike(true);
                    v.setBackgroundResource(R.mipmap.picked);
                    pickTask = new PickTask();
                    pickTask.execute(replyId,like);
                }
            }
        });
        return convertView;
    }

    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView userIcon;
        TextView replyDetail;
        TextView userName;
        Button pickBtn;
        TextView replyTime,repliedName,r;
    }

    //点赞和取消赞触发的进程
    private class PickTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            int replyId = (int)objects[0];
            boolean like = (boolean)objects[1];
            String url = U+"/comment/pickReply/"+replyId+"?userId="+userId+"&like="+like;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            // 3.创建Call对象
            Call call = okHttpClient.newCall(request);
            // 4.提交请求，返回响应
            try {
                Response response = call.execute();
                String rel = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            notifyDataSetChanged();
        }
    }
}

