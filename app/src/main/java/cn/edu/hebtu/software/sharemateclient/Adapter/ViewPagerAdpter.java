package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import cn.edu.hebtu.software.sharemateclient.Fragment.RecommendIndexFrag;

public class ViewPagerAdpter extends FragmentStatePagerAdapter {
    private Context context;
    private String tabTitles[] = {"推荐" ,"美食","旅行","美妆","动漫","运动","科技"};
    private RecommendIndexFrag frag;
    public FragmentManager fm;
    public ViewPagerAdpter(FragmentManager fm, Context context) {
        super(fm);
        this.fm=fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("position",position+"点击了"+tabTitles[position]);
        return RecommendIndexFrag.newInstance(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        fm.beginTransaction().hide(fragment).commit();
    }
}
