package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import cn.edu.hebtu.software.sharemateclient.Bean.CommentBean;
import cn.edu.hebtu.software.sharemateclient.Bean.Reply;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NoteCommentListAdapter extends BaseAdapter implements View.OnClickListener,
        AdapterView.OnItemClickListener{
    private Context context;
    private int itemLayout;
    private List<CommentBean> comments;
    private ReplyListAdapter replyListAdapter;
    private List<Reply> replys = new ArrayList<>();
    private String U;
    private Callback mCallback;
    private int userId;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private PickTask pickTask;

    public NoteCommentListAdapter(Context context, int itemLayout,
                      List<CommentBean> comments, Callback mCallback,int userId) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.comments = comments;
        this.mCallback = mCallback;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
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
            viewHolder.noteComment=convertView.findViewById(R.id.noteComment);
            viewHolder.userIcon=convertView.findViewById(R.id.userIcon);
            viewHolder.userName = convertView.findViewById(R.id.userName);
            viewHolder.pickBtn = convertView.findViewById(R.id.pickBtn);
            viewHolder.noteCommentDate = convertView.findViewById(R.id.noteCommentDate);
            viewHolder.replyListView = convertView.findViewById(R.id.replyList);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        U=context.getResources().getString(R.string.url);
        String userIconUrl = U+comments.get(position).getUser().getUserPhoto();
        RequestOptions options = new RequestOptions().circleCrop();
        //加载用户头像的图片
        Glide.with(context)
                .load(userIconUrl)
                .apply(options)
                .into(viewHolder.userIcon);
        viewHolder.noteCommentDate.setText((CharSequence) comments.get(position).getCommentDate());
        viewHolder.userName.setText(comments.get(position).getUser().getUserName());
        viewHolder.noteComment.setText(comments.get(position).getCommentDetail());
        if(comments.get(position).isLike()){
            viewHolder.pickBtn.setBackgroundResource(R.mipmap.picked);
        }else{
            viewHolder.pickBtn.setBackgroundResource(R.mipmap.pick);
        }
        replys = comments.get(position).getReplyList();
        replyListAdapter = new ReplyListAdapter(context,R.layout.item_reply,replys,userId);
        viewHolder.replyListView.setAdapter(replyListAdapter);
        replyListAdapter.notifyDataSetChanged();
        //设置ListView的自定义高度
        setListHeight(viewHolder.replyListView);
        //ListView绑定监听器在Activity响应事件
        viewHolder.replyListView.setOnItemClickListener(this);
        viewHolder.replyListView.setTag(position);
        viewHolder.pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int commentId = comments.get(position).getCommentId();
                boolean like = comments.get(position).isLike();
                if(like){
                    v.setBackgroundResource(R.mipmap.pick);
                    comments.get(position).setLike(false);
                    Toast.makeText(context,"取消赞啦",Toast.LENGTH_SHORT).show();
                    pickTask = new PickTask();
                    pickTask.execute(commentId,like);
                }else{
                    Toast.makeText(context,"赞啦",Toast.LENGTH_SHORT).show();
                    comments.get(position).setLike(true);
                    v.setBackgroundResource(R.mipmap.picked);
                    pickTask = new PickTask();
                    pickTask.execute(commentId,like);
                }
            }
        });
        return convertView;
    }

    //ListView 的item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.click(view,position,(int)parent.getTag());
    }
    //回调接口
    public interface Callback {
        void click(View v, int p, int c);
        void click(View v);
    }
    //回调方法
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView userIcon;
        TextView noteComment;
        TextView userName;
        Button pickBtn;
        TextView noteCommentDate;
        ListView replyListView;
    }

    //设置自定义ListView高度
    private void setListHeight( ListView view) {
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int  listViewWidth  = context.getResources().getDisplayMetrics().widthPixels;
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth,
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i <replyListAdapter.getCount(); i++) {
            View listItem = replyListAdapter.getView(i, null,view);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        params.height = totalHeight +
                (view.getDividerHeight() * (replyListAdapter.getCount() + 1));
        view.setLayoutParams(params);
        }

    //点赞和取消赞触发的进程
    private class PickTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            int commentId = (int)objects[0];
            boolean like = (boolean)objects[1];
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            String url = U+"/comment/pick/"+commentId+"?userId="+userId+"&like="+like;
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

