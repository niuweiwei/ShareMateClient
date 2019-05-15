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

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.Follow;
import cn.edu.hebtu.software.sharemateclient.Bean.User;
import cn.edu.hebtu.software.sharemateclient.R;

public class MFollowListAdapter extends BaseAdapter {

    private Context context;
    private List<Follow> followList = new ArrayList<>();
    private int itemLayout;
    private String serverPath;

    public MFollowListAdapter(Context context, List<Follow> followList, int itemLayout, String serverPath) {
        this.context = context;
        this.followList = followList;
        this.itemLayout = itemLayout;
        this.serverPath = serverPath;
    }

    @Override
    public int getCount() {
        return followList.size();
    }

    @Override
    public Object getItem(int position) {
        return followList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Follow follow = followList.get(position);
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            viewHolder.portraitImage = convertView.findViewById(R.id.iv_portrait);
            viewHolder.followName = convertView.findViewById(R.id.tv_name);
            viewHolder.followDate = convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) convertView.getTag();

        //对控件进行填充
        RequestOptions requestOptions = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(serverPath+follow.getFollowUser().getUserPhoto())
                .apply(requestOptions)
                .into(viewHolder.portraitImage);
        viewHolder.followName.setText(follow.getFollowUser().getUserName());
        viewHolder.followDate.setText(follow.getFollowDate());
        return convertView;
    }

    private class ViewHolder{
        private ImageView portraitImage;
        private TextView followName;
        private TextView followDate;
    }
}
