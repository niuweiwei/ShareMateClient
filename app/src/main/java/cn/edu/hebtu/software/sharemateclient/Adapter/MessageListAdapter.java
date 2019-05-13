package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.MessageFragmentTitle;
import cn.edu.hebtu.software.sharemateclient.R;

public class MessageListAdapter extends BaseAdapter {

    private List<MessageFragmentTitle> list;
    private Context context;
    private int itemLayout;

    public MessageListAdapter(List<MessageFragmentTitle> list, Context context, int itemLayout) {
        this.list = list;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if( null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            holder.titleImage = convertView.findViewById(R.id.iv_icon);
            holder.titleText = convertView.findViewById(R.id.tv_enter);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();
        holder.titleImage.setImageResource(list.get(position).getIcon());
        holder.titleText.setText(list.get(position).getTitle());
        return convertView;
    }

    private class ViewHolder{
        private ImageView titleImage;
        private TextView titleText;
    }
}
