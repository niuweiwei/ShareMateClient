package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.MessageListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.MessageFragmentTitle;
import cn.edu.hebtu.software.sharemateclient.R;

/**
 * 消息Fragment
 * */
public class MessageFragment extends Fragment {

    private List<MessageFragmentTitle> titles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_message,container,false);
        //准备数据源
        titles.add(new MessageFragmentTitle(R.drawable.like,"收到的赞"));
        titles.add(new MessageFragmentTitle(R.drawable.comment,"收到的评论"));
        titles.add(new MessageFragmentTitle(R.drawable.followed,"新增关注"));
        titles.add(new MessageFragmentTitle(R.drawable.message,"收到的私信"));
        //初始化监听器
        MessageListAdapter listAdapter = new MessageListAdapter(titles,getActivity(),R.layout.message_list_item_layout);
        ListView listView = view.findViewById(R.id.lv_message);
        //绑定监听器
        listView.setAdapter(listAdapter);
        return view;
    }
}
