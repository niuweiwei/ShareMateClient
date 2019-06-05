package cn.edu.hebtu.software.sharemateclient.tools;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.hebtu.software.sharemateclient.R;

public class VerifyCodeView extends RelativeLayout {

    private EditText editText;
    private TextView[] textViews;
    private static int MAX = 4;
    private String inputContent;
    private InputCompleteListener inputCompleteListener;
    public VerifyCodeView(Context context) {
        super(context);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_verify_code,this);

        textViews = new TextView[MAX];
        textViews[0] = findViewById(R.id.tv_0);
        textViews[1] = findViewById(R.id.tv_1);
        textViews[2] = findViewById(R.id.tv_2);
        textViews[3] = findViewById(R.id.tv_3);
//        textViews[4] = findViewById(R.id.tv_4);
//        textViews[5] = findViewById(R.id.tv_5);
        editText = findViewById(R.id.edit_text_view);

        editText.setCursorVisible(false);//隐藏光标;
        setEditTextListener();
    }

    private void setEditTextListener(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputContent = editText.getText().toString();
                if(inputCompleteListener != null){
                    if(inputContent.length() >= MAX){
                        inputCompleteListener.inputComplete();
                    }else {
                        inputCompleteListener.invalidContent();
                    }
                }
                for (int i=0; i<MAX; i++){
                    if(i < inputContent.length()){
                        textViews[i].setText(String.valueOf(inputContent.charAt(i)));
                    }else {
                        textViews[i].setText("");
                    }
                }
            }
        });
    }
    public void setInputCompleteListener(InputCompleteListener inputCompleteListener){
        this.inputCompleteListener = inputCompleteListener;
    }
    public interface InputCompleteListener{
        void inputComplete();
        void invalidContent();
    }
    public String getEditContent(){
        return inputContent;
    }
}
