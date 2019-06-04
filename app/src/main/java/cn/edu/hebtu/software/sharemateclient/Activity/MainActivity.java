package cn.edu.hebtu.software.sharemateclient.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.Fragment.FollowFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.HomeFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.MessageFragment;
import cn.edu.hebtu.software.sharemateclient.Fragment.MyFragment;
import cn.edu.hebtu.software.sharemateclient.R;

public class MainActivity extends AppCompatActivity {

    private TextView indexView;
    private TextView followView;
    private TextView messageView;
    private TextView myView;
    private HomeFragment indexFragment = new HomeFragment();
    private FollowFragment followFragment = new FollowFragment();
    private MessageFragment messageFragment = new MessageFragment();
    private MyFragment myFragment = new MyFragment();
    private FragmentManager manager ;
    private Fragment currentFragment = new Fragment();
    private List<TextView> views = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indexView = findViewById(R.id.tv_index);
        followView = findViewById(R.id.tv_follow);
        messageView = findViewById(R.id.tv_message);
        myView = findViewById(R.id.tv_my);

        manager = getSupportFragmentManager();
        //默认显示首页
        showFragment(indexFragment);
        //调用为每个选项绑定事件监听器的方法
        setClickListener();


    }

    //显示出指定的页面
    private void showFragment(Fragment fragment){
        //创建 fragment 事务
        FragmentTransaction transaction = manager.beginTransaction();
        //判断传入的fragment 是否是当前正在显示的fragment
        if(fragment != currentFragment)
            transaction.hide(currentFragment);
        //判断要展示的 fragment 是否被添加过
        if(!fragment.isAdded())
            transaction.add(R.id.content,fragment);
        transaction.show(fragment);
        //提交事务
        transaction.commit();
        currentFragment = fragment;
    }

    //为每一个 选项卡 (模拟的选项) 添加监听器
    private void setClickListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_index:
                        showFragment(indexFragment);
                        break;
                    case R.id.tv_follow:
                        showFragment(followFragment);
                        break;
                    case R.id.tv_message:
                        showFragment(messageFragment);
                        break;
                    case R.id.tv_my:
                        showFragment(myFragment);
                        break;
                }

                //点击改变效果
                for(TextView view : views){
                    TextView tmp = findViewById(v.getId());
                    if(tmp == view){
                        view.setTextColor(getResources().getColor(R.color.inkGray));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                    }else{
                        view.setTextColor(getResources().getColor(R.color.deepGray));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                    }
                }
            }

        };
        //每个选项绑定点击事件监听器
        indexView.setOnClickListener(listener);
        followView.setOnClickListener(listener);
        messageView.setOnClickListener(listener);
        myView.setOnClickListener(listener);

    }
}
