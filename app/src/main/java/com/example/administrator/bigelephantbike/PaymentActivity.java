package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import payradio.PayRadioGroup;
import payradio.PayRadioPurified;

public class PaymentActivity extends Activity {
    private RadioButton radioButton;
    private Button pay;
    private boolean isEnough = false;
    private boolean isSucceed = false;
    private backTitleLogoLayout myTitle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        //点击按钮打开DrawerLayout
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        myTitle =(backTitleLogoLayout)findViewById(R.id.my_title);
        myTitle.init(drawerLayout);
        PayRadioGroup group = (PayRadioGroup) findViewById(R.id.genderGroup);
        pay = (Button) findViewById(R.id.pay);

        group.setOnCheckedChangeListener(new PayRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int radioButtonId = group.getCheckedRadioButtonId();
//				PayRadioButton rb = (PayRadioButton)MainActivity.this.findViewById(radioButtonId);
//				Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();

                PayRadioPurified rl = (PayRadioPurified) PaymentActivity.this.findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((PayRadioPurified) group.getChildAt(i)).setChangeImg(checkedId);
                }
                Toast.makeText(PaymentActivity.this, rl.getTextTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        //确认支付的事件监听
        pay.setOnClickListener(new View.OnClickListener() {
            Dialog myDialog;

            @Override
            public void onClick(View v) {
                if (!isEnough) {
                    myDialog = initDialog(R.layout.not_enough_dialog);
                    myDialog.getWindow().findViewById(R.id.ok_not_enough).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.cancel();
                        }
                    });

                } else if (!isSucceed) {
                    myDialog = initDialog(R.layout.pay_fail_dialog);
                    myDialog.getWindow().findViewById(R.id.ok_pay_fail).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.cancel();
                        }
                    });
                }
                else {
                       Intent intent =new Intent(PaymentActivity.this,MipcaActivityCapture.class);
                       intent.putExtra("name","payment");
                       startActivity(intent);
                }
            }
        });
    }
    Dialog initDialog(int view) {
        Dialog myDialog = new AlertDialog.Builder(PaymentActivity.this).create();
        myDialog.show();
        myDialog.getWindow().setContentView(view);
        return myDialog;
    }
}

