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
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.Chat;
import cn.edu.hebtu.software.sharemateclient.R;

public class MChatContentAdapter extends BaseAdapter {

    private Context context;
    private List<Chat> chatList;
    private int leftItemLayout;
    private int rightItemLayout;
    private int currentUserId;
    private String serverPath;

    public MChatContentAdapter(Context context, List<Chat> chatList, int leftItemLayout, int rightItemLayout,int currentUserId,String serverPath) {
        this.context = context;
        this.chatList = chatList;
        this.leftItemLayout = leftItemLayout;
        this.rightItemLayout = rightItemLayout;
        this.currentUserId = currentUserId;
        this.serverPath = serverPath;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            //判断发消息的用户是否为当前登录用户 以适应不同的布局
            if(chatList.get(position).getUser().getUserId() == currentUserId )
                convertView = LayoutInflater.from(context).inflate(rightItemLayout,null);
            else
                convertView = LayoutInflater.from(context).inflate(leftItemLayout,null);
            viewHolder = new ViewHolder();
            //处理每条消息的时间是否显示
            viewHolder.timestamp = convertView.findViewById(R.id.tv_timestamp);
            if(position == 0){
                viewHolder.timestamp.setText(DateUtils.getTimestampString(chatList.get(position).getDate()));
                viewHolder.timestamp.setVisibility(View.VISIBLE);
            }else{
                Chat nowChat = chatList.get(position);
                Chat preChat = chatList.get(position-1);
                if (preChat != null && DateUtils.isCloseEnough(nowChat.getDate().getTime(), preChat.getDate().getTime())) {
                    viewHolder.timestamp.setVisibility(View.GONE);
                } else {
                    viewHolder.timestamp.setText(DateUtils.getTimestampString(nowChat.getDate()));
                    viewHolder.timestamp.setVisibility(View.VISIBLE);
                }
            }
            viewHolder.userPhoto = convertView.findViewById(R.id.iv_portrait);
            viewHolder.chatContent = convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(serverPath+chatList.get(position).getUser().getUserPhoto())
                .apply(requestOptions)
                .into(viewHolder.userPhoto);
        //点击用户头像 跳转到用户主页
        viewHolder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到指定用户的主页
            }
        });
        viewHolder.chatContent.setText(chatList.get(position).getContent());

        return convertView;
    }

    private class ViewHolder{
        private TextView timestamp;
        private ImageView userPhoto;//发消息的用户的头像
        private TextView chatContent;//消息的具体内容
    }
}
