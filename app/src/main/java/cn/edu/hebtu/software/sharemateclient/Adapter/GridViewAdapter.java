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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GridViewAdapter extends BaseAdapter{
    private Context context;
    private int itemLayout;
    private List<Note> notes;
    private String U ="http://10.7.89.23:8080/ShareMateServer/";
    private int userId = 17;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private PickTask pickTask;

    public GridViewAdapter(Context context, int itemLayout,List<Note> notes) {
        this.context = context;
        this.itemLayout = itemLayout;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(itemLayout,null);
            viewHolder= new ViewHolder();
            viewHolder.noteImg=convertView.findViewById(R.id.noteImage);
            viewHolder.noteTitle=convertView.findViewById(R.id.noteTitle);
            viewHolder.userIcon=convertView.findViewById(R.id.userIcon);
            viewHolder.userName = convertView.findViewById(R.id.userName);
            viewHolder.pick = convertView.findViewById(R.id.pick);
            viewHolder.likeCount = convertView.findViewById(R.id.likeCount);
            convertView.setTag(viewHolder);//缓存数据
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String noteImgUrl = U+notes.get(position).getNoteImage();
        String userIconUrl = U+notes.get(position).getUser().getUserPhoto();
        Glide.with(context)
                .load(noteImgUrl)
                .into(viewHolder.noteImg);
        Glide.with(context)
                .load(userIconUrl)
                .into(viewHolder.userIcon);
        viewHolder.noteTitle.setText(notes.get(position).getNoteTitle());
        viewHolder.userName.setText(notes.get(position).getUser().getUserName());
        viewHolder.likeCount.setText(notes.get(position).getLikeCount()+"");
        if(notes.get(position).isLike()){
            viewHolder.pick.setBackgroundResource(R.mipmap.picked);
        }else{
            viewHolder.pick.setBackgroundResource(R.mipmap.pick);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noteId = notes.get(position).getNoteId();
                boolean like = notes.get(position).isLike();
                int c = notes.get(position).getLikeCount();
                if(like){
                    v.setBackgroundResource(R.mipmap.pick);
                    notes.get(position).setLike(false);
                    c--;
                    notes.get(position).setLikeCount(c);
                    Toast.makeText(context,"取消赞啦",Toast.LENGTH_SHORT).show();
                    pickTask = new PickTask();
                    pickTask.execute(noteId,like);
                }else{
                    Toast.makeText(context,"赞啦",Toast.LENGTH_SHORT).show();
                    notes.get(position).setLike(true);
                    v.setBackgroundResource(R.mipmap.picked);
                    c++;
                    notes.get(position).setLikeCount(c);
                    pickTask = new PickTask();
                    pickTask.execute(noteId,like);
                }
            }
        });
        return convertView;
    }



    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView noteImg;
        ImageView userIcon;
        TextView noteTitle;
        TextView userName;
        Button pick;
        TextView likeCount;
    }

    //点赞和取消赞触发的进程
    private class PickTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            int noteId = (int)objects[0];
            boolean like = (boolean)objects[1];
            //1.创建OKHttpClient对象(已创建)
            // 2.创建Request对象
            Log.e("isLike",like+"");
            String url = U+"/note/pick/"+noteId+"?userId="+userId+"&like="+like;
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