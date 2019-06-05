package cn.edu.hebtu.software.sharemateclient.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;
import cn.edu.hebtu.software.sharemateclient.R;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LRStartActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private Calendar calendar;
    private TextView tv_birth;
    private ImageView back;
    private Button btnNext;
    private String birth;
    private PopupWindow popupWindow;
    private RadioGroup radioGroup;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private String male;
    private String female;
    private UserBean user = new UserBean();
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        path = getResources().getString(R.string.server_path);
        findViews();
        btnNext.setOnClickListener(new ButtonClickListener());
        back.setOnClickListener(new backClickListener());
        tv_birth.setOnClickListener(new birthClickListener());
        radioGroup.setOnCheckedChangeListener(new CheckedChangeListener());
    }
    private void findViews(){
        linearLayout = findViewById(R.id.root);
        tv_birth = findViewById(R.id.tv_birth);
        btnNext = findViewById(R.id.btn_next);
        back = findViewById(R.id.iv_back);
        radioGroup = findViewById(R.id.radiogroup);
        rbMale = findViewById(R.id.male);
        rbFemale = findViewById(R.id.female);
    }

    /**
     * 选择男、女
     */
    private class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.male:
                    male = rbMale.getText().toString();
                    Log.e("male",male);
                    user.setUserSex(male);
                    break;
                case R.id.female:
                    female = rbFemale.getText().toString();
                    Log.e("female",female);
                    user.setUserSex(female);
                    break;
            }
        }
    }
    //选择生日
    private class birthClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            showBirthDialog();
        }
    }
    //生日日期选择器
    private void showBirthDialog(){
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        View view = getLayoutInflater().inflate(R.layout.select_birth,null);
        TextView tvOk = view.findViewById(R.id.tv_ok);
        final TextView tvCanale = view.findViewById(R.id.tv_cancle);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH) + 1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar = Calendar.getInstance();
                calendar.set(year,monthOfYear,dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                birth = format.format(calendar.getTime());
            }
        });
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(linearLayout, Gravity.BOTTOM,0,0);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_birth.setText(birth);
                Log.e("birth", birth);
                user.setUserBirth(birth);
                popupWindow.dismiss();
            }
        });
        tvCanale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    //点击返回
    private class backClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LRStartActivity.this,LRLoginActivity.class);
            startActivity(intent);
        }
    }
    //点击下一步按钮
    private class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //上传性别和生日
            SexUtil sexUtil = new SexUtil();
            sexUtil.execute(user);
        }
    }
    private class SexUtil extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            UserBean user = (UserBean) objects[0];
            Log.e("SexUtil",user.getUserSex());
            Log.e("SexUtil",user.getUserBirth());
            Gson gson = new Gson();
            String userStr = gson.toJson(user);
            MediaType type = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(type,userStr);
            String url = path +"/user/register2";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().post(body).url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                String result = call.execute().body().string();
                Log.e("Register2",result);
                JSONObject object = new JSONObject(result);
                String str = object.getString("user");
                Gson gson1 = new Gson();
                UserBean u = gson1.fromJson(str,UserBean.class);
                if(u != null){
                    Intent intent = new Intent(LRStartActivity.this,LRSelectInterestActivity.class);
                    intent.putExtra("user",u);
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
