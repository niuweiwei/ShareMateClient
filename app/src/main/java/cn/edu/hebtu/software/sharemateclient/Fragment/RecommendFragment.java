package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.hebtu.software.sharemateclient.Adapter.ViewPagerAdapter;
import cn.edu.hebtu.software.sharemateclient.R;

public class RecommendFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter pagerAdapter;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_recommend,null);
        //设置首页推荐页的viewPager
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),getActivity());
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout  = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }
}
