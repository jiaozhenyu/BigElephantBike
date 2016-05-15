package com.example.administrator.bigelephantbike;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/27.
 */
public class LeftMenuLinearLayout extends LinearLayout {

    private TextView userName;
    private TextView isPass;
    private ImageView headPic;
    private final String defultName ="游客";
    private final String defultPass ="未认证";
    private final int defaultPic =R.drawable.head_pic;

    public LeftMenuLinearLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.left_menu, this);
        userName =(TextView)findViewById(R.id.user_name);
        isPass =(TextView)findViewById(R.id.is_pass);
        headPic =(ImageView)findViewById(R.id.head_pic);

        userName.setText(defultName);
        isPass.setText(defultPass);
        headPic.setImageResource(defaultPic);
    }

    public LeftMenuLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.left_menu, this);
        userName =(TextView)findViewById(R.id.user_name);
        isPass =(TextView)findViewById(R.id.is_pass);
        headPic =(ImageView)findViewById(R.id.head_pic);

        userName.setText(defultName);
        isPass.setText(defultPass);
        headPic.setImageResource(defaultPic);
    }

    public LeftMenuLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.left_menu, this);
        userName =(TextView)findViewById(R.id.user_name);
        isPass =(TextView)findViewById(R.id.is_pass);
        headPic =(ImageView)findViewById(R.id.head_pic);

        userName.setText(defultName);
        isPass.setText(defultPass);
        headPic.setImageResource(defaultPic);
    }

    public LeftMenuLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.left_menu, this);
        userName =(TextView)findViewById(R.id.user_name);
        isPass =(TextView)findViewById(R.id.is_pass);
        headPic =(ImageView)findViewById(R.id.head_pic);

        userName.setText(defultName);
        isPass.setText(defultPass);
        headPic.setImageResource(defaultPic);

    }

    public void init(String name,String pass,int id){
        userName.setText(name);
        isPass.setText(pass);
        headPic.setImageResource(id);
    }
}
