package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Entity.Comment;
import cn.edu.hebtu.software.sharemateclient.Entity.Reply;
import cn.edu.hebtu.software.sharemateclient.R;

public class ReplyListAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private List<Reply> replys;
    private String U;
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
            viewHolder.pick = convertView.findViewById(R.id.pick);
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
        if(replys.get(position).getCommentId()==0){
            viewHolder.r.setVisibility(View.VISIBLE);
            viewHolder.repliedName.setText(replys.get(position).getReplyedUser().getUserName());
            viewHolder.repliedName.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView userIcon;
        TextView replyDetail;
        TextView userName;
        Button pick;
        TextView replyTime,repliedName,r;
    }
}

