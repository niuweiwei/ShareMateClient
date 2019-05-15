package cn.edu.hebtu.software.sharemateclient.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Adapter.MFollowListAdapter;
import cn.edu.hebtu.software.sharemateclient.Bean.Follow;
import cn.edu.hebtu.software.sharemateclient.R;

//"消息"模块中 新增关注的页面
public class MFanActivity extends AppCompatActivity {

    private List<Follow> followList = new ArrayList<>();
    private ListView followListView;
    private MFollowListAdapter followListAdapter;
    private String serverPath;
    private int currentUserId = 3;//当前用户id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfan);

        followListView = findViewById(R.id.lv_followed);

        //获取服务器端的URL
        serverPath = getResources().getString(R.string.server_path);
    }
}
