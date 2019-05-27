package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Entity.Comment;
import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.Entity.Reply;
import cn.edu.hebtu.software.sharemateclient.R;

public class NoteCommentListAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private List<Comment> comments;
    private ReplyListAdapter replyListAdapter;
    private List<Reply> replys = new ArrayList<>();
    private String U;
    public NoteCommentListAdapter(Context context, int itemLayout, List<Comment> comments) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.comments = comments;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(itemLayout,null);
            viewHolder= new ViewHolder();
            viewHolder.noteComment=convertView.findViewById(R.id.noteComment);
            viewHolder.userIcon=convertView.findViewById(R.id.userIcon);
            viewHolder.userName = convertView.findViewById(R.id.userName);
            viewHolder.pick = convertView.findViewById(R.id.pick);
            viewHolder.noteCommentDate = convertView.findViewById(R.id.noteCommentDate);
            viewHolder.replyListView = convertView.findViewById(R.id.replyList);
            convertView.setTag(viewHolder);//缓存数据
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        U=context.getResources().getString(R.string.url);
        String userIconUrl = U+comments.get(position).getUser().getUserPhoto();
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(userIconUrl)
                .apply(options)
                .into(viewHolder.userIcon);
        viewHolder.noteCommentDate.setText(comments.get(position).getCommentDate());
        viewHolder.userName.setText(comments.get(position).getUser().getUserName());
        viewHolder.noteComment.setText(comments.get(position).getCommentDetail());
        replys = comments.get(position).getReplyList();
        Log.e("rerePly",replys.size()+"");
        replyListAdapter = new ReplyListAdapter(context,R.layout.item_reply,replys);
        viewHolder.replyListView.setAdapter(replyListAdapter);
        replyListAdapter.notifyDataSetChanged();
        setListHeight(viewHolder.replyListView);
        return convertView;
    }

    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView userIcon;
        TextView noteComment;
        TextView userName;
        Button pick;
        TextView noteCommentDate;
        ListView replyListView;
    }
    private void setListHeight( ListView view) {
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
//        for (int i = 0,len = replys.size();i < len; i++) {
//            View listItem = replyListAdapter.getView(i, null,view);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        params.height = totalHeight-(view.getDividerHeight()*(replyListAdapter.getCount()-1));
//        view.setLayoutParams(params);
//        replyListAdapter.notifyDataSetChanged();

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
}

