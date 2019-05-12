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
import java.io.IOException;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Entity.Goods;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private int itemLayout;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private List<Goods> goods;
    private String U ="http://10.7.89.23:8080";
    public GridViewAdapter(Context context, int itemLayout, List<Goods>goods){
        this.context=context;
        this.itemLayout=itemLayout;
        this.goods=goods;
    }
    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int position) {
        return goods.get(position);
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
        viewHolder.goodImg.setImageBitmap(goods.get(position).getBitmap());
        viewHolder.goodName.setText(goods.get(position).getCakeName());
        viewHolder.goodPrice.setText("￥"+goods.get(position).getCakePrice());
        viewHolder.goodDetail.setText(goods.get(position).getCakeDetail());
        viewHolder.shopCart.setBackgroundResource(R.mipmap.shopcart);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.shopCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(goods.get(position).getCakeId());
                Toast.makeText(context,
                        "加入购物车成功！",Toast.LENGTH_SHORT).show();
                finalViewHolder.shopCart.setBackgroundResource(R.mipmap.shopcart2);
            }
        });
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

    public void addCart(final int cakeId){
        new Thread(){
            @Override
            public void run() {
                String url =U+"/cakeshop/order/addCart/"+cakeId;
                Request request2 = new Request.Builder()
                        .url(url)
                        .build();
                // 3.创建Call对象
                Call call = okHttpClient.newCall(request2);
                Response response1 = null;
                try {
                    response1 = call.execute();
                    Log.e("cart","加购成功！"+response1.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
