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

public class PerAddressActivity extends AppCompatActivity {
    private EditText editText;
    private TextView finishText;
    private TextView titleText;
    private TextView alterText;
    private UserBean userBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        userBean = (UserBean)getIntent().getSerializableExtra("user");
        editText = findViewById(R.id.content);
        finishText = findViewById(R.id.finish);
        titleText = findViewById(R.id.title);
        alterText = findViewById(R.id.alter);
        alterText.setText("请输入2-40个字符");
        titleText.setText(getIntent().getStringExtra("msg"));
        if(titleText.getText().equals("常住地")){
            editText.setText(userBean.getUserAddress());
        }else{
            editText.setText(userBean.getUserIntroduce());
        }

        ImageView imageView = findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerAddressActivity.this.finish();
            }
        });
        finishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.length()<2){
                    alterText.setText("您输入的小于2个字符");
                    alterText.setTextColor(getResources().getColor(R.color.warmRed));
                }else{
                    if(titleText.getText().equals("常住地")){
                        userBean.setUserAddress(editText.getText().toString());
                    }else{
                        userBean.setUserIntroduce(editText.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("responseUser",userBean);
                    setResult(200,intent);
                    PerAddressActivity.this.finish();
                }
            }
        });
    }
}
