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
        viewHolder.chatContent.setText(chatList.get(position).getContent());

        return convertView;
    }

    private class ViewHolder{
        private ImageView userPhoto;//发消息的用户的头像
        private TextView chatContent;//消息的具体内容
    }
}
