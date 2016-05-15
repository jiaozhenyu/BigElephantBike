package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/3/3.
 */
public class LogoTitleLayout extends RelativeLayout {
    public LogoTitleLayout(Context context,AttributeSet attrs){
            super(context, attrs);
            LayoutInflater.from(context).inflate(R.layout.titke_log, this);
            Button menuButton =(Button)findViewById(R.id.menu);
            menuButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)getContext()).finish();
                }
            });
    }
}
