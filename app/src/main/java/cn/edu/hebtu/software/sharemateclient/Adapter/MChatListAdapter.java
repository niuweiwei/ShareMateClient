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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.Chat;
import cn.edu.hebtu.software.sharemateclient.R;

public class MChatListAdapter extends BaseAdapter {

    private Context context;
    private int itemLayout;
    private List<Chat> chatList;
    private String serverPath;

    public MChatListAdapter(Context context, int itemLayout, List<Chat> chatList,String serverPath) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.chatList = chatList;
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            viewHolder.userPhoto = convertView.findViewById(R.id.iv_userPhoto);
            viewHolder.userName = convertView.findViewById(R.id.tv_name);
            viewHolder.content = convertView.findViewById(R.id.tv_chat);
            viewHolder.date = convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(serverPath+chatList.get(position).getUser().getUserPhoto())
                .apply(requestOptions)
                .into(viewHolder.userPhoto);
        viewHolder.userName.setText(chatList.get(position).getUser().getUserName());
        viewHolder.content.setText(chatList.get(position).getContent());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = chatList.get(position).getDate();
        String time = simpleDateFormat.format(date);
        viewHolder.date.setText(time);

        return convertView;
    }

    private class ViewHolder{

        private ImageView userPhoto;
        private TextView userName;
        private TextView content;
        private TextView date;
    }
}
