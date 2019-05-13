package cn.edu.hebtu.software.sharemateclient.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Bean.Like;
import cn.edu.hebtu.software.sharemateclient.R;

public class LikeActivity extends AppCompatActivity {

    //数据源 当前登录用户获得的所有赞
    private List<Like> likeList = new ArrayList<>();
    private ListView likeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        likeListView = findViewById(R.id.lv_like);

    }
}
