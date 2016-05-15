package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

public class WelcomePageActivity extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome_page);
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                            Log.d("hh", "wait");
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    Intent intent =new Intent(WelcomePageActivity.this,MipcaActivityCapture.class);
                    intent.putExtra("name","welcome");
                    startActivity(intent);
                    Log.d("hh", "start 扫描");
                    finish();
                }
            }
        };
        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }
}
