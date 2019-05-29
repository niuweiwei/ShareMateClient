package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;

public class NoteAdapter extends BaseAdapter {

    private String path;
    private TextView tv_name;
    private ImageView iv_head;
    private UserBean userBean;
    private Context context;
    private int itemLayout;
    private List<NoteBean> noteList = new ArrayList<>();

    public NoteAdapter(Context context, int itemLayout, List<NoteBean> noteList, UserBean userBean, String path) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.noteList = noteList;
        this.userBean = userBean;
        this.path = path;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
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
        ImageView imageview = convertView.findViewById(R.id.img_content);
//        String photoPath = noteList.get(position).getNoteImagePath();
        String photoPath = path + "/" + noteList.get(position).getNoteImage();
        Glide.with(context).load(photoPath).into(imageview);
        TextView textView = convertView.findViewById(R.id.tv_note);
        if(noteList.get(position).getNoteTitle()== null || noteList.get(position).getNoteTitle().length()<8){
            textView.setText(noteList.get(position).getNoteTitle());
        }else{
            textView.setText(noteList.get(position).getNoteTitle().substring(0,8)+"...");
        }
        iv_head = convertView.findViewById(R.id.userHead);
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        String photo = path + "/" + noteList.get(position).getUser().getUserPhoto();
//        String photo = noteList.get(position).getUser().getUserPhotoPath();
        Glide.with(context).load(photo).apply(mRequestOptions).into(iv_head);
        tv_name = convertView.findViewById(R.id.userName);
        tv_name.setText(noteList.get(position).getUser().getUserName());
        return convertView;
    }
}
