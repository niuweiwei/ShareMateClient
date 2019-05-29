package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import cn.edu.hebtu.software.sharemateclient.R;

public class LRSelectInterestActivity extends AppCompatActivity {
    private Button button;
    private CheckBox cbBeauty, cbTravel, cbSport, cbFood, cbScience, cbAnime;
    private int typeId;
    private int userId;
    private String remark;
    private ArrayList<Integer> type = new ArrayList<>();
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interest);
        path = getResources().getString(R.string.server_path);
        findViews();
        userId = getIntent().getIntExtra("userId", 0);
        button.setOnClickListener(new ButtonClickListener());
        cbBeauty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("isChecked3",isChecked+"");
                typeId = 3;
                if(isChecked){
                    remark = "add";
                    type.add(3);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }else{
                    remark = "delete";
                    int i = type.indexOf(3);
                    type.remove(i);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }
            }
        });
        cbTravel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("isChecked2",isChecked+"");
                typeId = 2;
                if(isChecked){
                    remark = "add";
                    type.add(2);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }else {
                    remark = "delete";
                    int i = type.indexOf(2);
                    type.remove(i);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }
            }
        });
        cbSport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("isChecked5",isChecked+"");
                typeId = 5;
                if(isChecked){
                    remark = "add";
                    type.add(5);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }else {
                    remark = "delete";
                    int i = type.indexOf(5);
                    type.remove(i);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }
            }
        });
        cbFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("isChecked1",isChecked+"");
                typeId = 1;
                if(isChecked){
                    remark = "add";
                    type.add(1);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }else {
                    remark = "delete";
                    int i = type.indexOf(1);
                    type.remove(i);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }
            }
        });
        cbScience.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("isChecked6",isChecked+"");
                typeId = 6;
                if(isChecked){
                    remark = "add";
                    type.add(6);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }else {
                    remark = "delete";
                    int i = type.indexOf(6);
                    type.remove(i);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }
            }
        });
        cbAnime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("isChecked4",isChecked+"");
                typeId = 4;
                if(isChecked){
                    remark = "add";
                    type.add(4);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }else {
                    remark = "delete";
                    int i = type.indexOf(4);
                    type.remove(i);
                    SelectInterestUtil selectInterest = new SelectInterestUtil();
                    selectInterest.execute(userId, typeId,remark);
                }
            }
        });
    }
    private void findViews() {
        button = findViewById(R.id.btn_next);
        cbBeauty = findViewById(R.id.cb_beauty);
        cbTravel = findViewById(R.id.cb_travel);
        cbSport = findViewById(R.id.cb_sport);
        cbFood = findViewById(R.id.cb_food);
        cbScience = findViewById(R.id.cb_science);
        cbAnime = findViewById(R.id.cb_anime);
    }

    /**
     * 点击下一步
     */
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e("type",type+"");
            Intent intent = new Intent(LRSelectInterestActivity.this,MainActivity.class);
            intent.putExtra("userId",userId);
            intent.putIntegerArrayListExtra("type",type);
            startActivity(intent);
        }
    }
    private class SelectInterestUtil extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }
    }
}
