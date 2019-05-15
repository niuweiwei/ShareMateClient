package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.sharemateclient.Adapter.CustomAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.R;


public class FollowFragment extends Fragment{
    private List<NoteBean> notes=new ArrayList<NoteBean>();
    private ListView listView;
    private CustomAdapter customAdapter = null;
    private int userId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.note_fragment, container, false);
        listView = view.findViewById(R.id.list);
        NoteBean note1=new NoteBean();
        note1.setNoteImage(R.drawable.a1);
        note1.setNoteDetail("12345789");
        note1.setZancount(1);
        note1.setCollectcount(2);
        note1.setPingluncount(5);
        notes.add(note1);
        NoteBean note2=new NoteBean();
        note2.setNoteImage(R.drawable.a1);
        note2.setNoteDetail("12345789");
        note2.setZancount(1);
        note2.setCollectcount(2);
        note2.setPingluncount(5);
        notes.add(note2);
        listView=view.findViewById(R.id.list);
        customAdapter=new CustomAdapter(getContext(),R.layout.list_item,notes);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //转跳至详情页面
            }
        });
        return view;
    }
}
