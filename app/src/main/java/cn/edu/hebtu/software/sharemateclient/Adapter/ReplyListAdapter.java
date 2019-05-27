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
    private List<Reply> reReplys = new ArrayList<>();
    private ReReplyListAdapter reReplyListAdapter;
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
            viewHolder.replyDetail=convertView.findViewById(R.id.replyDetail);
            viewHolder.userIcon=convertView.findViewById(R.id.userIcon);
            viewHolder.userName = convertView.findViewById(R.id.userName);
            viewHolder.pick = convertView.findViewById(R.id.pick);
            viewHolder.replyTime = convertView.findViewById(R.id.replyTime);
            viewHolder.reReplyListView = convertView.findViewById(R.id.reReplyList);
            convertView.setTag(viewHolder);//缓存数据
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
        reReplys = replys.get(position).getReReplyList();
        reReplyListAdapter = new ReReplyListAdapter(context,R.layout.item_re_reply,reReplys);
        viewHolder.reReplyListView.setAdapter(reReplyListAdapter);
        setListHeight(viewHolder.reReplyListView);
        return convertView;
    }

    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView userIcon;
        TextView replyDetail;
        TextView userName;
        Button pick;
        TextView replyTime;
        ListView reReplyListView;
    }

    private void setListHeight( ListView view){
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0,len = reReplys.size();i < len; i++) {
            View listItem = reReplyListAdapter.getView(i, null,view);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = totalHeight-(view.getDividerHeight()*(reReplyListAdapter.getCount()-1));
        view.setLayoutParams(params);
        reReplyListAdapter.notifyDataSetChanged();
    }
}

