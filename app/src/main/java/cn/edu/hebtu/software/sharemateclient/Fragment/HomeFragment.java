package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.edu.hebtu.software.sharemateclient.R;

/**
 * 首页Fragment
 * */
public class HomeFragment extends Fragment {
    private Map<String,View> TabspecViews = new HashMap<>();
    private Map<String,TextView> textViews = new HashMap<>();
    private View view;
    private FragmentTabHost tabHost;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home,null);
        initTabHost();
        setTabHostChanged();
        return view;
    }

    //初始化tabHost
    private void initTabHost(){
        //获取tabHost组件
        tabHost = view.findViewById(android.R.id.tabhost);
        //初始化TabHost容器
        tabHost.setup(getContext(),getChildFragmentManager(),android.R.id.tabcontent);
        tabHost.getTabWidget().setDividerDrawable(null);
        //创建选项卡对象
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tab1")
                .setIndicator(getTabSpecView("推荐","tab1"));
        //添加选项卡
        tabHost.addTab(tabSpec1,RecommendFragment.class,null);
        //创建选项卡对象
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator(getTabSpecView("附近","tab2"));
        //添加选项卡
        tabHost.addTab(tabSpec2,NearbyFragment.class,null);
        //设置默认选中某个选项卡
        tabHost.setCurrentTab(0);
        TextView textView = textViews.get("tab1");
        textView.setTextColor(getResources().
                getColor(R.color.white));
    }

    //给tabHost设置点击切换相关监听事件
    private void setTabHostChanged(){
        //切换
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Set<String> keys=TabspecViews.keySet();//返回所有key
                TextView text1 = textViews.get("tab1");
                TextView text2 = textViews.get("tab2");
                for(String str:keys){
                    if(str.equals(tabId)){
                        if(str.equals("tab1")){
                            text1.setTextColor(getResources().
                                    getColor(R.color.white));
                            text2.setTextColor(getResources().
                                    getColor(R.color.top1));
                        }else{
                            text1.setTextColor(getResources().
                                    getColor(R.color.top1));
                            text2.setTextColor(getResources().
                                    getColor(R.color.white));
                        }
                    }
                }
            }
        });
    }

    //获取首页fragment的tab视图并初始化tab标签
    private View getTabSpecView(String name, String tag){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.tabspesc_homefrag,null);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(name);
        textView.setTextSize(22);
        textView.setTextColor(getResources().
                getColor(R.color.top1));
        textViews.put(tag,textView);
        TabspecViews.put(tag,view);
        return view;
    }
}
