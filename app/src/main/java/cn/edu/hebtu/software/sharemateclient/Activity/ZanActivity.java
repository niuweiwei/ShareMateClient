package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.ZanAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;
import cn.edu.hebtu.software.sharemateclient.R;

public class ZanActivity extends AppCompatActivity {

    private GridView gridView;
    private ZanAdapter zanAdapter;
    private ImageView ivBack;
    private OnClickListener listener;
    private List<NoteBean> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zan);
        findView();
        setListener();

        if(noteList.size()==0){
            gridView.setEmptyView((findViewById(R.id.empty_view)));
        }else{
            zanAdapter= new ZanAdapter(ZanActivity.this, R.layout.item_zan,noteList);
            gridView.setAdapter(zanAdapter);
        }
    }
    public void findView(){
        gridView = findViewById(R.id.root);
        ivBack = findViewById(R.id.back);
    }
    public void setListener(){
        listener = new OnClickListener();
        ivBack.setOnClickListener(listener);
    }
    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    Intent backIntent = new Intent(ZanActivity.this,SettingActivity.class);
                    startActivity(backIntent);
                    break;
            }
        }
    }
}
