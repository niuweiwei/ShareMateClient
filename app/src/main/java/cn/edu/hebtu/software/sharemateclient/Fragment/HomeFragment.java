package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.hebtu.software.sharemateclient.R;

/**
 * 首页Fragment
 * */
public class HomeFragment extends Fragment {
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,null);
        return view;
    }

}
