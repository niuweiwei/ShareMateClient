package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Entity.Goods;
import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private List<Note> notes;
    private String U ="http://10.7.89.23:8080/ShareMateServer/";
    public GridViewAdapter(Context context, int itemLayout, List<Note> notes){
        this.context=context;
        this.itemLayout=itemLayout;
        this.notes=notes;
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
        ViewHolder viewHolder = null;
        if (convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(itemLayout,null);
            viewHolder= new ViewHolder();
            viewHolder.goodImg=convertView.findViewById(R.id.goodImage);
            viewHolder.goodName=convertView.findViewById(R.id.goodName);
            viewHolder.goodPrice=convertView.findViewById(R.id.goodPrice);
            viewHolder.shopCart = convertView.findViewById(R.id.shopCart);
            viewHolder.goodDetail = convertView.findViewById(R.id.goodDetail);
            convertView.setTag(viewHolder);//缓存数据
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.goodImg.setImageBitmap(goods.get(position).getBitmap());
        String imageUrl = U+notes.get(position).getNoteImage();
        Glide.with(context)
                .load(imageUrl)
                .into(viewHolder.goodImg);
        viewHolder.goodName.setText(notes.get(position).getNoteTitle());
        viewHolder.goodPrice.setText(notes.get(position).getNoteDate());
        viewHolder.goodDetail.setText(notes.get(position).getNoteDetail());
        viewHolder.shopCart.setBackgroundResource(R.mipmap.shopcart);
        return convertView;
    }
    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView goodImg;
        TextView goodName;
        TextView goodPrice;
        Button shopCart;
        TextView goodDetail;
    }
}
