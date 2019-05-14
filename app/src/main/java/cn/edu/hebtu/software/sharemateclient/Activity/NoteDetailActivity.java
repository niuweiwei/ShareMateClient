package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.edu.hebtu.software.sharemateclient.Entity.Note;
import cn.edu.hebtu.software.sharemateclient.R;

public class NoteDetailActivity extends AppCompatActivity {

    private ImageView userIcon,noteImage;
    private Button backBtn,followBtn;
    private TextView userName;
    private TextView noteTitle,noteDetail;
    private String U;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Intent intent  = getIntent();
        Note note = (Note) intent.getSerializableExtra("note");
        Log.e("notein",note.getNoteImage());
        findViews();
        userName.setText(note.getUser().getUserName());
        noteTitle.setText(note.getNoteTitle());
        noteDetail.setText(note.getNoteDetail());
        String noteImgUrl = U+note.getNoteImage();
        String userIconUrl = U+note.getUser().getUserPhoto();
        Glide.with(this)
                .load(noteImgUrl)
                .into(noteImage);
        Glide.with(this)
                .load(userIconUrl)
                .into(userIcon);

    }
    //初始化控件
    public void findViews(){
        userIcon = findViewById(R.id.userIcon);
        userName = findViewById(R.id.userName);
        backBtn = findViewById(R.id.backBtn);
        followBtn = findViewById(R.id.followBtn);
        noteImage = findViewById(R.id.noteImage);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetail = findViewById(R.id.noteText);
        U ="http://10.7.89.23:8080/ShareMateServer/";
    }
}
