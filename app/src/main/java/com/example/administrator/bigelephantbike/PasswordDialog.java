package com.example.administrator.bigelephantbike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/3.
 */
public class PasswordDialog extends Dialog implements TextWatcher,View.OnKeyListener{

    private Context context;
    private String title;
    private String content;
    private TextView titleTextView;
    private TextView contentTextVIew;
    private EditText eidtText1;
    private EditText eidtText2;
    private EditText eidtText3;
    private EditText eidtText4;
    private EditText eidtText5;
    private EditText eidtText6;
    private String password;
    private int current=0;
    private String[]passwordInput =new String[6];
    private int maxLen =1;//限制EditText的输入长度
    private MyJudgePasswordInterface myJudgePasswordInterface;

    public PasswordDialog(Context context) {
        super(context);
    }
    public PasswordDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    public PasswordDialog(Context context,String title,String content,String password,MyJudgePasswordInterface myJudgePasswordInterface){
        super(context,R.style.add_dialog);
        this.context =context;
        this.title=title;
        this.content =content;
        this.password =password;
        this.myJudgePasswordInterface =myJudgePasswordInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.return_code_dialog, null);
        setContentView(view);

        titleTextView =(TextView)findViewById(R.id.title);
        contentTextVIew=(TextView)findViewById(R.id.content);
        eidtText1 =(EditText)findViewById(R.id.psw_1);
        eidtText2 =(EditText)findViewById(R.id.psw_2);
        eidtText3 =(EditText)findViewById(R.id.psw_3);
        eidtText4 =(EditText)findViewById(R.id.psw_4);
        eidtText5 =(EditText)findViewById(R.id.psw_5);
        eidtText6 =(EditText)findViewById(R.id.psw_6);

        titleTextView.setText(title);
        contentTextVIew.setText(content);

        //锁定数字键盘
        eidtText1.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        eidtText2.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        eidtText3.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        eidtText4.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        eidtText5.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        eidtText6.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        //添加内容改变的事件监听
        eidtText1.addTextChangedListener(this);
        eidtText2.addTextChangedListener(this);
        eidtText3.addTextChangedListener(this);
        eidtText4.addTextChangedListener(this);
        eidtText5.addTextChangedListener(this);
        eidtText6.addTextChangedListener(this);


        eidtText1.setOnKeyListener(this);
        eidtText2.setOnKeyListener(this);
        eidtText3.setOnKeyListener(this);
        eidtText4.setOnKeyListener(this);
        eidtText4.setOnKeyListener(this);
        eidtText5.setOnKeyListener(this);
        eidtText6.setOnKeyListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = 0;
        //Log.d("test","current="+current+"我变了");
        switch (current) {
            case 0:
                length = eidtText1.getText().length();
                changeFocus(length, eidtText2);
                passwordInput[0] = eidtText1.getText().toString();
                break;
            case 1:
                length = eidtText2.getText().length();
                changeFocus(length, eidtText3);
                passwordInput[1] = eidtText2.getText().toString();
                break;
            case 2:
                length = eidtText3.getText().length();
                changeFocus(length, eidtText4);
                passwordInput[2] = eidtText3.getText().toString();
                break;
            case 3:
                length = eidtText4.getText().length();
                changeFocus(length, eidtText5);
                passwordInput[3] = eidtText4.getText().toString();
                break;
            case 4:
                length = eidtText5.getText().length();
                changeFocus(length, eidtText6);
                passwordInput[4] = eidtText5.getText().toString();
                break;
            case 5:
                length = eidtText6.getText().length();
                if (length >= 1) {
                    passwordInput[5] = eidtText6.getText().toString();
                    myJudgePasswordInterface.returnJudgePasswordResult(validatePassword());
                    this.cancel();
                }
        }
    }
    @Override
    public void afterTextChanged(Editable s) {

    }
    void changeFocus(int l,EditText editText){
        if(l >= maxLen){
            current++;
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            editText.findFocus();
        }
    }
    boolean validatePassword(){
        current=0;
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<passwordInput.length;i++){
            stringBuffer.append(passwordInput[i]);
        }
        String psw =stringBuffer.toString();
        return psw.equals(password);
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            current--;
            switch(current){
                case 0:eidtText1.setText("");requestF(eidtText1);break;
                case 1:eidtText2.setText("");requestF(eidtText2);break;
                case 2:eidtText3.setText("");requestF(eidtText3);break;
                case 3:eidtText4.setText("");requestF(eidtText4);break;
                case 4:eidtText5.setText("");requestF(eidtText5);break;
                case 5:eidtText6.setText("");requestF(eidtText6);break;
            }
        }
        return false;
    }
    void requestF(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.findFocus();
    }
}
