package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;

public class PerNameActivity extends AppCompatActivity {
    private TextView msgText;
    private TextView finishText;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        msgText = findViewById(R.id.msg);
        msgText.setText("请输入2-8个字符");
        msgText.setTextColor(getResources().getColor(R.color.darkGray));
        editText = findViewById(R.id.content);
        finishText = findViewById(R.id.finish);
        Intent intent = getIntent();
        final UserBean user = (UserBean) intent.getSerializableExtra("user");
        editText.setText(user.getUserName());
        finishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.length()<2){
                    msgText.setText("您输入的小于2个字符");
                    msgText.setTextColor(getResources().getColor(R.color.warmRed));
                }else{
                    user.setUserName(editText.getText().toString());
                    Intent intent1 = new Intent();
                    intent1.putExtra("responseUser",user);
                    setResult(200,intent1);
                    PerNameActivity.this.finish();
                }
            }
        });
        ImageView imageView = findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerNameActivity.this.finish();
            }
        });
    }
}
