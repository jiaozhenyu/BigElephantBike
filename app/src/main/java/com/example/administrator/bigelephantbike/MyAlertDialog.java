package com.example.administrator.bigelephantbike;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MyAlertDialog extends Dialog {
    private Context context;
    private String title;
    private String confirmButtonText;
    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {
             public void doConfirm();
             }

    public MyAlertDialog(Context context, String title, String confirmButtonText) {
        super(context,R.style.add_dialog);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
           // TODO Auto-generated method stub
           super.onCreate(savedInstanceState);
          init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_alert_dialog, null);
        setContentView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        Button tvConfirm = (Button) view.findViewById(R.id.ok);
        tvTitle.setText(title);
        tvConfirm.setText(confirmButtonText);

        tvConfirm.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        }
    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
                 this.clickListenerInterface = clickListenerInterface;
    }
    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            clickListenerInterface.doConfirm();
        }
    }
}
