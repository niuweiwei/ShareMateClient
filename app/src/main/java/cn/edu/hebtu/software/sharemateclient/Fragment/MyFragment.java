package cn.edu.hebtu.software.sharemateclient.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebtu.software.sharemateclient.Activity.PersonalActivity;
import cn.edu.hebtu.software.sharemateclient.R;

/**
 * 我 Fragment
 * @author fengjiaxing
 * @date 2019/5/13
 * */
public class MyFragment extends Fragment {

    private GridView gridView;
    private TextView nameText;
    private TextView idText;
    private TextView introText;
    private TextView collection;
    private TextView note;
    private TextView followCount;
    private TextView fanCount;
    private TextView likeCount;
    private ImageView headImg;//头像
    private ImageView settingView;
    private Button btnPersonal;//个人资料
    private OnClickListener listener = new OnClickListener();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        //获取控件
        findView(view);
        setListener();
        return view;
    }

    /**
     * 获取控件
     * @param view
     */
    public void findView(View view){
        nameText = view.findViewById(R.id.userName);
        idText = view.findViewById(R.id.userId);
        introText = view.findViewById(R.id.userIntro);
        headImg = view.findViewById(R.id.userPhoto);
        gridView = view.findViewById(R.id.root);
        gridView.setEmptyView((view.findViewById(R.id.empty_view)));
        collection = view.findViewById(R.id.collection);
        note = view.findViewById(R.id.note);
        settingView = view.findViewById(R.id.setting);
        btnPersonal = view.findViewById(R.id.personal);
        followCount = view.findViewById(R.id.followCount);
        fanCount = view.findViewById(R.id.fanCount);
        likeCount = view.findViewById(R.id.likeCount);
    }

    public void setListener(){
        btnPersonal.setOnClickListener(listener);
    }
    /**
     * 监听器
     */
    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.personal:
                    Intent perIntent = new Intent(getContext(), PersonalActivity.class);
                    startActivity(perIntent);
                    break;
            }
        }
    }

}
