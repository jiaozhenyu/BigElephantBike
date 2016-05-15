package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/3/21.
 */
public class backTitleLogoLayout extends LinearLayout {
    private DrawerLayout drawerLayout;
    public backTitleLogoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.titke_log,this);
        Button menuButton =(Button)findViewById(R.id.menu);
        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    public void init(DrawerLayout drawerLayout){
        this.drawerLayout =drawerLayout;
    }
}
