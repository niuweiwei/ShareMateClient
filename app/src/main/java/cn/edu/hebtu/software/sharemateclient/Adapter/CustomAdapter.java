package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.R;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private List<NoteBean> notes =new ArrayList<>();

    public CustomAdapter(Context context, int itemLaout,List<NoteBean> notes) {
        this.context = context;
        this.itemLayout = itemLaout;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(itemLayout,null);
        }
        ImageView ivphoto=convertView.findViewById(R.id.iv_photo);
        ivphoto.setImageResource(notes.get(0).getNoteImage());
        TextView ivtext=convertView.findViewById(R.id.note_alltext);
        ivtext.setText(notes.get(0).getNoteDetail());
        TextView z_count=convertView.findViewById(R.id.z_count);
        z_count.setText(String.valueOf(notes.get(0).getZancount()));
        TextView c_count=convertView.findViewById(R.id.c_count);
        c_count.setText(String.valueOf(notes.get(0).getCollectcount()));
        TextView p_count=convertView.findViewById(R.id.p_count);
        p_count.setText(String .valueOf(notes.get(0).getPingluncount()));
        return convertView;
    }



}
