package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.Like;
import cn.edu.hebtu.software.sharemateclient.R;

public class LikeListAdapter extends BaseAdapter {

    private Context context;
    private  int itemLayout;
    private List<Like> likeList;

    public LikeListAdapter(Context context, int itemLayout, List<Like> likeList) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.likeList = likeList;
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

        ViewHolder viewHolder = null;
        if( null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            viewHolder.portraitImage = convertView.findViewById(R.id.iv_portrait);
            viewHolder.nameText = convertView.findViewById(R.id.tv_name);
            viewHolder.dateText = convertView.findViewById(R.id.tv_date);
            viewHolder.noteImage = convertView.findViewById(R.id.iv_note);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //填充点赞的用户头像
        Glide.with(context)
                .load(likeList.get(position).getUser().getUserPhoto())
                .into(viewHolder.portraitImage);
        viewHolder.nameText.setText(likeList.get(position).getUser().getUserName());
        viewHolder.dateText.setText(likeList.get(position).getDate());
        Glide.with(context)
                .load(likeList.get(position).getNote().getNoteImage())
                .into(viewHolder.noteImage);
        return convertView;
    }

    private class ViewHolder{
        private ImageView portraitImage;
        private TextView nameText;
        private TextView dateText;
        private ImageView noteImage;
    }
}
