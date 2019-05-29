package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;
import cn.edu.hebtu.software.sharemateclient.Entity.Comment;
import cn.edu.hebtu.software.sharemateclient.Entity.Reply;
import cn.edu.hebtu.software.sharemateclient.R;


public class NoteCommentListAdapter extends BaseAdapter implements View.OnClickListener,
        AdapterView.OnItemClickListener{
    private Context context;
    private int itemLayout;
    private List<Comment> comments;
    private ReplyListAdapter replyListAdapter;
    private List<Reply> replys = new ArrayList<>();
    private String U;
    private Callback mCallback;

    public NoteCommentListAdapter(Context context, int itemLayout,
                                  List<Comment> comments, Callback mCallback) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.comments = comments;
        this.mCallback = mCallback;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(itemLayout,null);
            viewHolder= new ViewHolder();
            viewHolder.noteComment=convertView.findViewById(R.id.noteComment);
            viewHolder.userIcon=convertView.findViewById(R.id.userIcon);
            viewHolder.userName = convertView.findViewById(R.id.userName);
            viewHolder.pick = convertView.findViewById(R.id.pick);
            viewHolder.noteCommentDate = convertView.findViewById(R.id.noteCommentDate);
            viewHolder.replyListView = convertView.findViewById(R.id.replyList);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        U=context.getResources().getString(R.string.url);
        String userIconUrl = U+comments.get(position).getUser().getUserPhoto();
        RequestOptions options = new RequestOptions().circleCrop();
        //加载用户头像的图片
        Glide.with(context)
                .load(userIconUrl)
                .apply(options)
                .into(viewHolder.userIcon);
        viewHolder.noteCommentDate.setText(comments.get(position).getCommentDate());
        viewHolder.userName.setText(comments.get(position).getUser().getUserName());
        viewHolder.noteComment.setText(comments.get(position).getCommentDetail());
        replys = comments.get(position).getReplyList();
        replyListAdapter = new ReplyListAdapter(context,R.layout.item_reply,replys);
        viewHolder.replyListView.setAdapter(replyListAdapter);
        replyListAdapter.notifyDataSetChanged();
        //设置ListView的自定义高度
        setListHeight(viewHolder.replyListView);
        //ListView绑定监听器在Activity响应事件
        viewHolder.replyListView.setOnItemClickListener(this);
        viewHolder.replyListView.setTag(position);
        return convertView;
    }

    //ListView 的item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.click(view,position,(int)parent.getTag());
    }
    //回调接口
    public interface Callback {
        void click(View v,int p,int c);
        void click(View v);
    }
    //回调方法
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    //定义为内部类VIewHolder
    class ViewHolder{
        ImageView userIcon;
        TextView noteComment;
        TextView userName;
        Button pick;
        TextView noteCommentDate;
        ListView replyListView;
    }

    //设置自定义ListView高度
    private void setListHeight( ListView view) {
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int  listViewWidth  = context.getResources().getDisplayMetrics().widthPixels;
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth,
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i <replyListAdapter.getCount(); i++) {
            View listItem = replyListAdapter.getView(i, null,view);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        params.height = totalHeight +
                (view.getDividerHeight() * (replyListAdapter.getCount() + 1));
        view.setLayoutParams(params);
        }
}

