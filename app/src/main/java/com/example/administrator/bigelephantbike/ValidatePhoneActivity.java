package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.graphics.Color.*;

public class ValidatePhoneActivity extends Activity implements TextWatcher {

    private Button validateBtn;
    private Button startBtn;
    private EditText phoneEditText;
    private MyEditText validateCodeEditText;
    private Timer timer = new Timer();
    private int recLen = 60;
    private boolean isValidate = false;
    private ImageView infoOperatingIV;
    private Animation operatingAnim;
    private ImageView imageView;
    private FrameLayout layout;
    private ImageView sendSucceed;
    private TextView protocal;
    private LinearInterpolator lin;

    //发送成功的提示一段时间后淡出
    final Handler handler = new Handler();
    int runCount = 0;// 全局变量，用于判断是否是第一次执行
    //发送验证码
    private IntentFilter sendFilter;
    private SendStausReceiver sendStausReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_phone);


        validateBtn = (Button) findViewById(R.id.validateBtn);
        startBtn = (Button) findViewById(R.id.start_btn);
        phoneEditText = (EditText) findViewById(R.id.phone);
        validateCodeEditText = (MyEditText) findViewById(R.id.validate_code);
        layout = (FrameLayout) findViewById(R.id.layout);
        protocal =(TextView)findViewById(R.id.protocol);

        protocal.setMovementMethod(LinkMovementMethod.getInstance());
        //protocal.setText(R.string.protical_link);
        //protocal.setMovementMethod(LinkMovementMethod.getInstance());

       // String htmlLinkText ="点击\"开始\"，即表示您同意" +"<a href=\"http://www.baidu.com\">《法律声明及隐私政策》</a>";
       // protocal.setText(Html.fromHtml(htmlLinkText));
        //protocal.setMovementMethod(LinkMovementMethod.getInstance());
        phoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneEditText.setHint(null);
            }
        });
        validateCodeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCodeEditText.setHint(null);
            }
        });
        validateCodeEditText.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            InputMethodManager imm = (InputMethodManager) validateCodeEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    imm.hideSoftInputFromWindow(validateCodeEditText.getWindowToken(), 0);
                }
            }
        });
        //为两个输入框添加内容改变时的监听函数
        phoneEditText.addTextChangedListener(this);
        validateCodeEditText.addTextChangedListener(this);

        //当点击验证按钮时
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneEditText.setFocusable(false);
                validateBtn.setBackgroundResource(R.drawable.corner_sharp_gray);
                validateBtn.setFocusable(false);
                validateBtn.setEnabled(false);

                validateCodeEditText.setEditTextDrawable();
                validateCodeEditText.setFocusable(false);
                Log.d("hh", "下面我要开始旋转啦");

                //添加等待背景图
                imageView = new ImageView(ValidatePhoneActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setImageResource(R.drawable.sending);
                layout.addView(imageView);

                //添加等待的图片
                operatingAnim = AnimationUtils.loadAnimation(ValidatePhoneActivity.this,R.anim.tips);
                lin = new LinearInterpolator();
                //​operatingAnim.setInterpolator(lin);//设置旋转速度为匀速​
                infoOperatingIV = new ImageView(ValidatePhoneActivity.this);
                infoOperatingIV.setImageResource(R.drawable.wait);
                infoOperatingIV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                layout.addView(infoOperatingIV);

                Log.d("hh", "----添加View成功----");
                //开始旋转
                if (operatingAnim != null) {
                    infoOperatingIV.startAnimation(operatingAnim);
                }
                //发送验证码倒计时
                try {
                    timer.schedule(task, 1000, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendValidateCode(phoneEditText.getText().toString(), "123456");
            }
        });

        //点击开始按钮时
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ValidatePhoneActivity.this,MipcaActivityCapture.class);
                intent.putExtra("name","phone");
                startActivity(intent);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String phone = phoneEditText.getText().toString();
        String validateCode = validateCodeEditText.getText().toString();

        //判断手机号是否符合格式
        Pattern pattern = Pattern
                .compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");
        Matcher matcher = pattern.matcher(phone);

        //判断验证码是否正确
        if (validateCode.equals("123456")) {
            startBtn.setEnabled(true);
            startBtn.setBackgroundResource(R.drawable.corner_sharp_green);
        } else {
            startBtn.setEnabled(false);
            startBtn.setBackgroundResource(R.drawable.corner_sharp_gray);
        }
        if (!isValidate) {
            if (matcher.matches()) {
                validateBtn.setEnabled(true);
                isValidate = true;
                validateBtn.setBackgroundResource(R.drawable.corner_sharp_green);
                Log.d("hh", "------------我变绿了----------");
            } else {
                validateBtn.setEnabled(false);
                validateBtn.setBackgroundResource(R.drawable.corner_sharp_gray);
            }
        }

    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    //发送验证码
    void sendValidateCode(String number, String message) {
        sendFilter = new IntentFilter();
        sendFilter.addAction("SENT_SMS_ACTION");
        sendStausReceiver = new SendStausReceiver();

        //注册广播接收器
        registerReceiver(sendStausReceiver, sendFilter);
        SmsManager smsManager = SmsManager.getDefault();

        //监控短信的发送状态
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent pi = PendingIntent.getBroadcast(ValidatePhoneActivity.this, 0, sentIntent, 0);
        smsManager.sendTextMessage(number, null, message, pi, null);
    }

    //发送短信成功之后
    class SendStausReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == RESULT_OK) {
                //短信发送成功
                //Toast.makeText(ValidatePhoneActivity.this, "send succeeded", Toast.LENGTH_SHORT).show();
                infoOperatingIV.clearAnimation();
                layout.removeView(infoOperatingIV);
                layout.removeView(imageView);

                sendSucceed = new ImageView(ValidatePhoneActivity.this);
                sendSucceed.setImageResource(R.drawable.send_succeed);
                sendSucceed.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                layout.addView(sendSucceed);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (runCount == 1) {// 第一次执行则关闭定时执行操作
                            // 在此处添加执行的代码
                            layout.removeView(sendSucceed);
                            validateCodeEditText.setFocusable(true);
                            validateCodeEditText.setFocusableInTouchMode(true);
                            validateCodeEditText.requestFocus();
                            validateCodeEditText.findFocus();
                            handler.removeCallbacks(this);
                        }
                        handler.postDelayed(this, 150);
                        runCount++;
                    }

                };
                handler.postDelayed(runnable, 1000);// 打开定时器，执行操作

            } else {
                //短信发送失败
                Toast.makeText(ValidatePhoneActivity.this, "send failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //倒计时
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {  // UI thread
                @Override
                public void run() {
                    recLen--;
                    validateBtn.setText(" " + recLen + "s ");
                    if (recLen < 0) {
                        timer.cancel();
                        validateBtn.setText("验证");
                    }
                }
            });
        }
    };

}
