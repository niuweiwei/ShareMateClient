package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.Like;
import cn.edu.hebtu.software.sharemateclient.R;

public class MLikeListAdapter extends BaseAdapter {

    private Context context;
    private  int itemLayout;
    private List<Like> likeList;
    private String serverPath;

    public MLikeListAdapter(Context context, int itemLayout, List<Like> likeList, String serverPath) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.likeList = likeList;
        this.serverPath = serverPath;
    }

    @Override
    public int getCount() {
        return likeList.size();
    }

    @Override
    public Object getItem(int position) {
        return likeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Like like = likeList.get(position);
        ViewHolder viewHolder = null;
        if( null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            viewHolder.portraitImage = convertView.findViewById(R.id.iv_portrait);
            viewHolder.nameText = convertView.findViewById(R.id.tv_name);
            viewHolder.dateText = convertView.findViewById(R.id.tv_date);
            viewHolder.noteImage = convertView.findViewById(R.id.iv_note);
            viewHolder.commentContent = convertView.findViewById(R.id.tv_commentContent);
            viewHolder.likeType = convertView.findViewById(R.id.tv_likeType);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //填充点赞的用户头像
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(serverPath+like.getUser().getUserPhoto())
                .apply(requestOptions)
                .into(viewHolder.portraitImage);
        //为用户头像绑定点击事件监听器
        viewHolder.portraitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击用户的头像跳转到该用户的个人页面
                Log.e("MLikeListAdapter","点击了用户的头像");
            }
        });
        viewHolder.nameText.setText(like.getUser().getUserName());
        Glide.with(context)
                .load(like.getNote().getNoteImage())
                .into(viewHolder.noteImage);
        //判断点赞的笔记还是评论
        if(like.getLikeType() == Like.NOTE) {
            viewHolder.likeType.setText("笔记");
            viewHolder.commentContent.setVisibility(View.GONE);
            Glide.with(context)
                    .load(serverPath+like.getNote().getNoteImage())
                    .into(viewHolder.noteImage);
        } else {
            viewHolder.likeType.setText("评论");
            viewHolder.commentContent.setVisibility(View.VISIBLE);
            if(like.getLikeType() == Like.COMMENT){
                viewHolder.commentContent.setText(like.getComment().getCommentDetail());
                Glide.with(context)
                        .load(serverPath+like.getComment().getNote().getNoteImage())
                        .into(viewHolder.noteImage);
            } else{
                viewHolder.commentContent.setText(like.getReply().getReplyDetail());
            }
        }
        //为笔记图片绑定点击事件监听器
        viewHolder.noteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击笔记图片跳转到笔记详情页面
                Log.e("MLikeListAdapter","点击了笔记的图片");
            }
        });
        viewHolder.dateText.setText(like.getLikeDate());

        return convertView;
    }

    private class ViewHolder{
        private ImageView portraitImage;
        private TextView nameText;
        private TextView dateText;
        private ImageView noteImage;
        private TextView likeType;
        private TextView commentContent;
    }
}
