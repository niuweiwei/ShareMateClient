package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.widget.PLVideoView;
import cn.edu.hebtu.software.sharemateclient.Entity.User;
import cn.edu.hebtu.software.sharemateclient.R;

public class LoginActivity extends AppCompatActivity {
    private PLVideoView vVideo;
    private Button startBtn,button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.login);
        vVideo = findViewById(R.id.video);
        startBtn = findViewById(R.id.start);
        String mVideoURL = "http://panm32w98.bkt.clouddn.com/Fpf1M01BS9pcOE9kGdjxBkUESiWK";
        vVideo.setVideoURI(Uri.parse(mVideoURL));
        vVideo.setLooping(false);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setVisibility(View.INVISIBLE);
                vVideo.start();
            }
        });
        vVideo.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                startBtn.setVisibility(View.VISIBLE);
            }
        });

        //登录跳转 将当前user的信息传入主页的activity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User contentUser = new User(17,"狗蛋","gou",
                        "images/userPhotos/17.jpg","女","15852160982",
                        "上海市", "1985-09-04","做梦都想发家致富");
                Intent intent = new Intent();
                intent.putExtra("user",contentUser);
                intent.setClass(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

}