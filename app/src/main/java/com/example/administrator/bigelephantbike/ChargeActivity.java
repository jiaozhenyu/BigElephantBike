package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import wheelview.ArrayWheelAdapter;
import wheelview.OnWheelChangedListener;
import wheelview.WheelView;

public class ChargeActivity extends Activity implements OnWheelChangedListener,View.OnClickListener{
    private TextView sumMoney;
    private TextView useTime;
    private TextView meetQuestion;
    private WheelView wheelFirst;
    private WheelView wheelSecond;
    private WheelView wheelThiid;
    private WheelView wheelForth;
    private TextView bikeId;
    private String key="1234";
    private boolean isKey1=false;
    private boolean isKey2=false;
    private boolean isKey3=false;
    private boolean isKey4=false;
    private Button returnBtn;
    private Button recoverBtn;
    private String passwordReturn="123456";
    private String passwordRecover="234567";

    private PasswordDialog myDialog;
    //密码输入是否正确
    private boolean pswResult;
    //实现MyJudgePasswordInterface
    private MyJudgePasswordInterface myJudgePasswordInterface;
    //1标识输入的是还车密码，2表示输入的是恢复密码
    private int flag=0;
    private String []nums={"0","1","2","3","4","5","6","7","8","9"};
    private MyAlertDialog myAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        sumMoney =(TextView)findViewById(R.id.sum_money);
        useTime =(TextView)findViewById(R.id.use_time);
        bikeId =(TextView)findViewById(R.id.bike_id);
        returnBtn =(Button)findViewById(R.id.return_btn);
        recoverBtn =(Button)findViewById(R.id.recover_btn);

        myJudgePasswordInterface=new MyJudgePasswordInterface() {
            @Override
            public void returnJudgePasswordResult(boolean result) {
                pswResult = result;
                if (flag == 1&&!result) {
                    myAlertDialog = new MyAlertDialog(ChargeActivity.this,
                            ChargeActivity.this.getResources().getString(R.string.error_password),
                            ChargeActivity.this.getResources().getString(R.string.ok));
                    myAlertDialog.show();
                    myAlertDialog.setClicklistener(new MyAlertDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            myAlertDialog.cancel();
                            myDialog = new PasswordDialog(ChargeActivity.this, ChargeActivity.this.getResources().getString(R.string.input_return_code), ChargeActivity.this.getResources().getString(R.string.return_tips), passwordReturn, myJudgePasswordInterface);
                            myDialog.show();
                            flag = 0;
                        }
                    });
                } else if (flag == 2&&!result) {
                    myAlertDialog = new MyAlertDialog(ChargeActivity.this,
                            ChargeActivity.this.getResources().getString(R.string.not_in_school),
                            ChargeActivity.this.getResources().getString(R.string.ok));
                    myAlertDialog.show();
                    myAlertDialog.setClicklistener(new MyAlertDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            myAlertDialog.cancel();
                        }
                    });
                }else if(flag==1&&result)
                    startActivity(new Intent(ChargeActivity.this,PaymentActivity.class));
            }
        };
        returnBtn.setOnClickListener(this);
        recoverBtn.setOnClickListener(this);

        meetQuestion=(TextView)findViewById(R.id.meet_qustion);
        meetQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChargeActivity.this,MeetQuestion.class));
            }
        });

        initWheelView();

    }
    public void initWheelView(){

        LinearLayout llContent = (LinearLayout)findViewById(R.id.wheelViewLinearLayout);
        //将创建的 LinearLayout 设置成横向的
        llContent.setOrientation(LinearLayout.HORIZONTAL);

        //创建 第一个WheelView 组件
        wheelFirst = new WheelView(this);
        //设置 WheelView 组件最多显示 5 个元素
        wheelFirst.setVisibleItems(1);
        //设置 WheelView 元素是否循环滚动
        wheelFirst.setCyclic(false);
        //设置 WheelView 适配器
        wheelFirst.setAdapter(new ArrayWheelAdapter<String>(nums));

        //第二个 WheelView
        wheelSecond = new WheelView(this);
        //设置右侧 WheelView 显示个数
        wheelSecond.setVisibleItems(1);
        //设置右侧 WheelView 元素是否循环滚动
        wheelSecond.setCyclic(false);
        //设置右侧 WheelView 的元素适配器
        wheelSecond.setAdapter(new ArrayWheelAdapter<String>(nums));

        //创建 第三个WheelView 组件
        wheelThiid = new WheelView(this);
        //设置 WheelView 组件最多显示 5 个元素
        wheelThiid.setVisibleItems(1);
        //设置 WheelView 元素是否循环滚动
        wheelThiid.setCyclic(false);
        //设置 WheelView 适配器
        wheelThiid.setAdapter(new ArrayWheelAdapter<String>(nums));

        //创建 第四个WheelView 组件
        wheelForth = new WheelView(this);
        //设置 WheelView 组件最多显示 5 个元素
        wheelForth.setVisibleItems(1);
        //设置 WheelView 元素是否循环滚动
        wheelForth.setCyclic(false);
        //设置 WheelView 适配器
        wheelForth.setAdapter(new ArrayWheelAdapter<String>(nums));


        llContent.addView(wheelFirst);
        llContent.addView(wheelSecond);
        llContent.addView(wheelThiid);
        llContent.addView(wheelForth);

        wheelFirst.addChangingListener(this);
        wheelSecond.addChangingListener(this);
        wheelThiid.addChangingListener(this);
        wheelForth.addChangingListener(this);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if(wheel.getId()==wheelFirst.getId()){
            if((nums[wheelFirst.getCurrentItem()].charAt(0)==(key.charAt(0)))){
                isKey1 =true;
                Log.d("hhh","1"+nums[wheel.getCurrentItem()]);
            }else{
                isKey1=false;
            }}
        if(wheel==wheelSecond){
            if((nums[wheelSecond.getCurrentItem()].charAt(0)==key.charAt(1))){
                isKey2 =true;
                Log.d("hhh","2"+nums[wheel.getCurrentItem()]);
            }else{
                isKey2 =false;
            }}
        if(wheel==wheelThiid){
            if((nums[wheelThiid.getCurrentItem()].charAt(0)==key.charAt(2))){
                isKey3 =true;
                Log.d("hhh","3"+nums[wheel.getCurrentItem()]);
            }else{
                isKey3 =false;
            }}
        if(wheel==wheelForth){
            if((nums[wheelForth.getCurrentItem()].charAt(0)==key.charAt(3))){
                isKey4 =true;
                Log.d("hhh","4"+nums[wheel.getCurrentItem()]);
            }else{
                isKey4 =false;
            }}
        if(isKey1&&isKey2&isKey3&&isKey4){
            Toast.makeText(this,"密码正确",Toast.LENGTH_SHORT).show();
            Log.d("hhh", "true");
        }
    }

    @Override
    public void onClick(View v) {
        //当点击还车按钮时
        if(v.getId()==R.id.return_btn){
            flag=1;
            myDialog =new PasswordDialog(ChargeActivity.this,this.getResources().getString(R.string.input_return_code),this.getResources().getString(R.string.return_tips),passwordReturn,myJudgePasswordInterface);
            myDialog.show();
        }
        if(v.getId()==R.id.recover_btn){
            flag=2;
            myDialog=new PasswordDialog(ChargeActivity.this,this.getResources().getString(R.string.input_recover_tips),this.getResources().getString(R.string.recover_tips),passwordRecover,myJudgePasswordInterface);
            myDialog.show();
        }
    }
}
