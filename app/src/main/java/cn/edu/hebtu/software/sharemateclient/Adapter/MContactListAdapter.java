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

import cn.edu.hebtu.software.sharemateclient.Bean.User;
import cn.edu.hebtu.software.sharemateclient.R;

public class MContactListAdapter extends BaseAdapter {

    private Context context;
    private List<User> contactsList;
    private int itemLayout;
    private String serverPath;

    public MContactListAdapter(Context context, List<User> contactsList, int itemLayout,String serverPath) {
        this.context = context;
        this.contactsList = contactsList;
        this.itemLayout = itemLayout;
        this.serverPath = serverPath;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactsList.get(position);
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
            viewHolder.contactImage = convertView.findViewById(R.id.iv_contactImage);
            viewHolder.contactName = convertView.findViewById(R.id.tv_contactName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //填充联系人头像
        RequestOptions requestOptions = new RequestOptions()
                .circleCrop();
        Glide.with(context)
                .load(serverPath+contactsList.get(position).getUserPhoto())
                .apply(requestOptions)
                .into(viewHolder.contactImage);
        viewHolder.contactName.setText(contactsList.get(position).getUserName());
        return convertView;
    }

    private class ViewHolder{

        private ImageView contactImage;
        private TextView contactName;

    }
}
