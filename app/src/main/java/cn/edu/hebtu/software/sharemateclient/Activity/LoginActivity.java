package cn.edu.hebtu.software.sharemateclient.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;


import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.HashMap;

import cn.edu.hebtu.software.sharemateclient.BuildConfig;
import cn.edu.hebtu.software.sharemateclient.Entity.User;
import cn.edu.hebtu.software.sharemateclient.R;
import cn.edu.hebtu.software.sharemateclient.util.MediaController;

public class LoginActivity extends AppCompatActivity {
    private PLVideoView vVideo;
    private Button startBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.login);
        startBtn = findViewById(R.id.start);
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
        vVideo = findViewById(R.id.video);
        final String mVideoPath = "http://panm32w98.bkt.clouddn.com/Fpf1M01BS9pcOE9kGdjxBkUESiWK";
        vVideo.setVideoURI(Uri.parse(mVideoPath));
        vVideo.setLooping(false);
        View v= LayoutInflater.from(this).inflate(R.layout.load_view,null);
        vVideo.setCoverView(v);
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
    }

}