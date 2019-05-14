package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.Entity.User;
import cn.edu.hebtu.software.sharemateclient.R;

public class NoteDetailActivity extends AppCompatActivity {

    private ImageView userIcon,noteImage,contentUserIcon;
    private Button backBtn,followBtn;
    private TextView userName;
    private TextView noteTitle,noteDetail,noteDate;
    private TextView commentCount;
    private ButtonOnclickListener listener;
    private String U;
    private User contentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Intent intent  = getIntent();
        Note note = (Note) intent.getSerializableExtra("note");

        contentUser = (User)intent.getSerializableExtra("contentUser");
        Log.e("notein",contentUser.getUserName());
        findViews();
        userName.setText(note.getUser().getUserName());
        noteTitle.setText(note.getNoteTitle());
        noteDetail.setText(note.getNoteDetail());
        noteDate.setText(note.getNoteDate());
        String noteImgUrl = U+note.getNoteImage();
        String userIconUrl = U+note.getUser().getUserPhoto();
        String contentUserUrl = U+contentUser.getUserPhoto();
        Glide.with(this)
                .load(noteImgUrl)
                .into(noteImage);
        Glide.with(this)
                .load(userIconUrl)
                .into(userIcon);
        Glide.with(this)
                .load(contentUserUrl)
                .into(contentUserIcon);
    }
    //初始化控件
    private void findViews(){
        userIcon = findViewById(R.id.userIcon);
        userName = findViewById(R.id.userName);
        backBtn = findViewById(R.id.backBtn);
        followBtn = findViewById(R.id.followBtn);
        noteImage = findViewById(R.id.noteImage);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetail = findViewById(R.id.noteText);
        noteDate = findViewById(R.id.noteDate);
        contentUserIcon = findViewById(R.id.contentUserIcon);
        commentCount = findViewById(R.id.commentCount);
        listener = new ButtonOnclickListener();
        backBtn.setOnClickListener(listener);
        followBtn.setOnClickListener(listener);
        U ="http://192.168.0.108:8080/ShareMateServer/";
    }

    private class ButtonOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //点击返回按钮返回上一页
                case R.id.backBtn:
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    break;
                //点击关注按钮关注
                case R.id.followBtn:
                    break;
            }
        }
    }
}
