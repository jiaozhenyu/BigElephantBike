package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/4.
 * 是backTitleLayout的事件监听处理
 */
public class backTitleLayout extends LinearLayout{

    public backTitleLayout(Context context,AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_validate_phone,this);
        Button backButton =(Button)findViewById(R.id.backBtn);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
    }
}
