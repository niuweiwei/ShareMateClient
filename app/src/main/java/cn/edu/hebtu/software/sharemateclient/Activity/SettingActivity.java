package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.edu.hebtu.software.sharemateclient.Fragment.MyFragment;
import cn.edu.hebtu.software.sharemateclient.R;

/**
 * 设置页
 * @author fengjiaxing
 * @date 2019/5/14
 */
public class SettingActivity extends AppCompatActivity {

    private ImageView ivBack;
    private LinearLayout setPerson;
    private LinearLayout zanText;
    private LinearLayout about;
    private OnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findView();
        setListener();
    }

    /**
     * 获取控件
     */
    public void findView(){
        ivBack = findViewById(R.id.back);
        setPerson = findViewById(R.id.setPer);
        zanText = findViewById(R.id.zan);
        about = findViewById(R.id.about);
    }

    /**
     * 监听器绑定
     */
    public void setListener(){
        listener = new OnClickListener();
        ivBack.setOnClickListener(listener);
        setPerson.setOnClickListener(listener);
        zanText.setOnClickListener(listener);
        about.setOnClickListener(listener);
    }
    /**
     * 监听器类
     */
    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://返回
                    Intent backIntent = new Intent(SettingActivity.this,MainActivity.class);
                    backIntent.putExtra("back","my");
                    startActivity(backIntent);
                    break;
                case R.id.setPer://个人资料
                    Intent perIntent = new Intent(SettingActivity.this,PersonalActivity.class);
                    perIntent.putExtra("person","set");
                    startActivity(perIntent);
                    break;
                case R.id.zan://我赞过的
                    Intent zanIntent = new Intent(SettingActivity.this,ZanActivity.class);
                    startActivity(zanIntent);
                    break;
                case R.id.about://关于share mate
                    Intent abIntent = new Intent(SettingActivity.this,ShareMateActivity.class);
                    startActivity(abIntent);
                    break;
            }
        }
    }
}
