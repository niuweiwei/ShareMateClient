package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.CommentAndReply;
import cn.edu.hebtu.software.sharemateclient.R;

public class MCommentListAdapter extends BaseAdapter {

    public Context context;
    public List<CommentAndReply> commentAndReplyList;
    public int itemLayout;
    public String serverPath;
    public int userId;//表明为当前的user

    public MCommentListAdapter(Context context, List<CommentAndReply> commentAndReplyList, int itemLayout,String serverPath,int userId) {
        this.context = context;
        this.commentAndReplyList = commentAndReplyList;
        this.itemLayout = itemLayout;
        this.serverPath = serverPath;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return commentAndReplyList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentAndReplyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            viewHolder.userPhoto = convertView.findViewById(R.id.iv_userPhoto);
            viewHolder.notePhoto = convertView.findViewById(R.id.iv_notePhoto);
            viewHolder.userName = convertView.findViewById(R.id.tv_userName);
            viewHolder.commentType = convertView.findViewById(R.id.tv_commentType);
            viewHolder.commentContent = convertView.findViewById(R.id.tv_commentContent);
            viewHolder.commented = convertView.findViewById(R.id.tv_commented);
            viewHolder.commentDate = convertView.findViewById(R.id.tv_commentDate);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommentAndReply commentAndReply = commentAndReplyList.get(position);

        //填充布局
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(serverPath+commentAndReply.getUser().getUserPhoto())
                .apply(requestOptions)
                .into(viewHolder.userPhoto);
        Glide.with(context)
                .load(serverPath+commentAndReply.getNote().getNoteImage())
                .into(viewHolder.notePhoto);
        viewHolder.userName.setText(commentAndReply.getUser().getUserName());
        viewHolder.commentContent.setText(commentAndReply.getCommentContent());
        viewHolder.commentDate.setText(commentAndReply.getCommentDate());
        if(commentAndReply.getTag() == CommentAndReply.COMMENT_TYPE){
            //表明为对笔记的评论
            viewHolder.commentType.setText("评论你的笔记");
            viewHolder.commented.setVisibility(View.GONE);
        }else {
            //表明为对评论的回复或者是对回复的回复
            viewHolder.commented.setVisibility(View.VISIBLE);
            //判断被回复的用户是否是当前用户
            if(commentAndReply.getArguedUser().getUserId() == userId ){
                viewHolder.commentType.setText("回复了我的评论");
                viewHolder.commented.setText("我的评论:"+commentAndReply.getCommentedContent());
            }else{
                viewHolder.commentType.setText("回复了"+commentAndReply.getArguedUser().getUserName()+"的评论");
                viewHolder.commented.setText(commentAndReply.getArguedUser().getUserName()+"的评论:"+commentAndReply.getCommentedContent());
            }
        }
        return convertView;
    }

    public class ViewHolder {

        public ImageView userPhoto;//发布评论或回复的用户头像
        public ImageView notePhoto;//相关笔记的图片
        public TextView userName;//发布评论或回复的用户名
        public TextView commentType;//发布的种类 是对笔记的评论还是对评论的回复
        public TextView commentContent;//发布的内容
        public TextView commented;//被回复的内容
        public TextView commentDate;//发布的时间
    }
}
