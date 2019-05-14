package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.R;

public class ZanAdapter extends BaseAdapter {

    private Context context;
    private int itemLayout;
    private List<NoteBean> list = new ArrayList();

    public ZanAdapter(Context context, int itemLayout, List<NoteBean> list) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.list = list;
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
        if(null == convertView){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(itemLayout,null);
        }
        ImageView view = convertView.findViewById(R.id.zan);
        Glide.with(context).load(list.get(position).getNoteImagePath()).into(view);
        TextView textView = convertView.findViewById(R.id.note);
        if(list.get(position).getNoteTitle() != null && list.get(position).getNoteTitle().length()<6){
            textView.setText(list.get(position).getNoteTitle());
        }else {
            textView.setText(list.get(position).getNoteTitle().substring(0, 6) + "..");
        }
        return convertView;
    }
}
