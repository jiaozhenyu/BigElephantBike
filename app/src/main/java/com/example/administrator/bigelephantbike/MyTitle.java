package com.example.administrator.bigelephantbike;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MyTitle extends RelativeLayout{
    private Button backBtn;
    private TextView textView;
    private BtnClickListenerInterface btnClickListenerInterface;

    public interface BtnClickListenerInterface{
        void clickBack();
    }

    public MyTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void init(int pic, String text,BtnClickListenerInterface btnClickListenerInterface){
        //backBtn.setBackgroundResource(pic);
        textView.setText(text);
        this.btnClickListenerInterface =btnClickListenerInterface;
        backBtn.setOnClickListener(new myListener());
    }
    public void setBtnClicklistener(BtnClickListenerInterface clickListenerInterface)
    {
        this.btnClickListenerInterface = clickListenerInterface;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        backBtn = (Button) findViewById(R.id.backBtn);
        textView = (TextView) findViewById(R.id.text);
    }
    public class myListener implements OnClickListener{
        @Override
        public void onClick(View v) {
           btnClickListenerInterface.clickBack();
        }
    }
}