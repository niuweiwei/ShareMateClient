package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.OkHttpClient;

public class NoteAdapter extends BaseAdapter {

    private String path;
    private TextView tv_name;
    private ImageView iv_head;
    private UserBean userBean;
    private Context context;
    private int itemLayout;
    private List<NoteBean> notes = new ArrayList<>();
//    private String U;
    private OkHttpClient okHttpClient = new OkHttpClient();

    public NoteAdapter(Context context, int itemLayout, List<NoteBean> noteList, UserBean userBean, String path) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.notes = noteList;
        this.userBean = userBean;
        this.path = path;
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
            viewHolder = new ViewHolder();
            viewHolder.noteImg=convertView.findViewById(R.id.noteImage);
            viewHolder.startBtn =convertView.findViewById(R.id.startBtn);
            viewHolder.noteVideo = convertView.findViewById(R.id.noteVideo);
            viewHolder.noteTitle=convertView.findViewById(R.id.noteTitle);
            viewHolder.userIcon=convertView.findViewById(R.id.userIcon);
            viewHolder.userName = convertView.findViewById(R.id.userName);
            convertView.setTag(viewHolder);//缓存数据
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        U = context.getResources().getString(R.string.url);
        //判断是图片笔记还是视频笔记
        final ViewHolder finalViewHolder1 = viewHolder;
        if(notes.get(position).getNoteImage()==null){
            viewHolder.noteVideo.setVisibility(View.VISIBLE);
            viewHolder.startBtn.setVisibility(View.VISIBLE);
            String noteVideoUrl = notes.get(position).getNoteVideo();
            final Uri videoUri = Uri.parse(noteVideoUrl);
            viewHolder.noteVideo.setLooping(false);
            viewHolder.noteVideo.setVideoURI(videoUri);
            viewHolder.noteVideo.setOnCompletionListener(new PLOnCompletionListener() {
                @Override
                public void onCompletion() {
                    finalViewHolder1.startBtn.setVisibility(View.VISIBLE);
                }
            });
            viewHolder.startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalViewHolder1.startBtn.setVisibility(View.INVISIBLE);
                    finalViewHolder1.noteVideo.start();
                }
            });
        }
        String noteImgUrl = path+"/"+notes.get(position).getNoteImage();
        Glide.with(context)
                .load(noteImgUrl)
                .into(viewHolder.noteImg);
        String userIconUrl = path+"/"+notes.get(position).getUser().getUserPhoto();
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(userIconUrl)
                .apply(options)
                .into(viewHolder.userIcon);
        viewHolder.noteTitle.setText(notes.get(position).getNoteTitle());
        viewHolder.userName.setText(notes.get(position).getUser().getUserName());

        return convertView;
    }

    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView noteImg;
        ImageView userIcon;
        TextView noteTitle;
        TextView userName;
        PLVideoView noteVideo;
        Button startBtn;
    }
    }