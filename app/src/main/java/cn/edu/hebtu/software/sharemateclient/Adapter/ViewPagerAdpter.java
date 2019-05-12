package cn.edu.hebtu.software.sharemateclient.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.edu.hebtu.software.sharemateclient.Fragment.GridFrag;

public class ViewPagerAdpter extends FragmentStatePagerAdapter {
    private Context context;
    private String tabTitles[] = {"推荐" ,"美妆","旅游","动漫","美食","运动","科技"};
    public ViewPagerAdpter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new GridFrag();
            case 1:
                return new GridFrag();
            case 2:
                return new GridFrag();
            case 3:
                return new GridFrag();
            case 4:
                return new GridFrag();
            case 5:
                return new GridFrag();
            case 6:
                return new GridFrag();

        }
        return null;
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
}
