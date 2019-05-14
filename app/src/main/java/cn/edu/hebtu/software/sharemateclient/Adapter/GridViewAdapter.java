package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.R;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private List<Note> notes;
    private String U ="http://10.7.89.23:8080/ShareMateServer/";
    public GridViewAdapter(Context context, int itemLayout, List<Note> notes){
        this.context=context;
        this.itemLayout=itemLayout;
        this.notes=notes;
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
        viewHolder.pick.setBackgroundResource(R.mipmap.pick);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.pick.setBackgroundResource(R.mipmap.picked);
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
}
