package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import android.os.Bundle;

import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import picimage.utils.adapter.MainGVAdapter;
import picimage.utils.ui.PhotoWallActivity;
import picimage.utils.utils.ScreenUtils;
import picimage.utils.utils.Utility;

public class MeetQuestion extends Activity implements View.OnClickListener{

    private static final int TAKE_PHOTO = 1;
    private MyTitle myTitle;
    private ListView questionList;
    private String[] itemsText = {"单车丢失", "锁车后无法还车结账", "车身损坏", "车锁损坏", "其他问题"};
    private int lastItem = -1;
    private View[] itemViews;
    private TextView questionDesTextView;
    private EditText questionDesEditText;
    private TextView locationTextView;
    private TextView voucher;

    private SelectPicPopupWindow myPopupWindow;
    private ImageView canmeraPic;
    private int picPath = 0;
    private String imageUri;
    private int picNum = 0;
    private String uri;

    private MainGVAdapter adapter;
    private ArrayList<String> imagePathList;

    //提交
    private Button subimt;
    //获得当前位置
    private MKSearch mkSearch =new MKSearch();
    private GeoPoint geoPoint;
    private BMapManager mapManager;
    private Location mlocation;
    private LocationManager locationManager;
    private String addr=null;

    private MyTitle.BtnClickListenerInterface btnClickListenerInterface = new MyTitle.BtnClickListenerInterface() {
        @Override
        public void clickBack() {
            startActivity(new Intent(MeetQuestion.this, ChargeActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mapManager=new BMapManager(getApplication());
        //mapManager.init("3xcnii7sPDhgG6ktegMXu6D2ureC4UhO", new MyMKGeneralListener());
        setContentView(R.layout.activity_meet_question);
        ScreenUtils.initScreen(this);

        /**
        //显示位置信息
        locationTextView = (TextView) findViewById(R.id.location);


        mapManager.getLocationManager().requestLocationUpdates(new MyLocationListener());
        mapManager.start();

        locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000, 0,locationListener);
            mlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
         }*/


        //提交
        subimt =(Button)findViewById(R.id.submit_btn);
        subimt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(lastItem+1){
                    case 0:startActivity(new Intent(MeetQuestion.this, PaymentActivity.class));break;
                    case 1:startActivity(new Intent(MeetQuestion.this, PaymentActivity.class));break;
                    default:startActivity(new Intent(MeetQuestion.this,ChargeActivity.class));
                }
            }
        });


        canmeraPic = (ImageView) findViewById(R.id.canmer_pic);
        voucher = (TextView) findViewById(R.id.voucher);
        GridView mainGV = (GridView) findViewById(R.id.pic);


        imagePathList = new ArrayList<String>();
        adapter = new MainGVAdapter(this, imagePathList);
        mainGV.setAdapter(adapter);

        //为凭证文本框添加点击事件

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPopupWindow = new SelectPicPopupWindow(MeetQuestion.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //为弹出窗口实现监听类
                        myPopupWindow.dismiss();
                        switch (v.getId()) {
                            case R.id.take_pic:
                                takePic();
                                break;
                            case R.id.choose_pic:
                                Log.d("read", "我被点了");
                                //跳转至最终的选择图片页面
                                Intent intent = new Intent(MeetQuestion.this, PhotoWallActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                });
                //显示窗口
                myPopupWindow.showAtLocation(MeetQuestion.this.findViewById(R.id.meet_qustion_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //
            }
        });

        questionDesTextView = (TextView) findViewById(R.id.question_des);
        SpannableStringBuilder builder = new SpannableStringBuilder(questionDesTextView.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
        ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);

        builder.setSpan(blackSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(graySpan, 4, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        questionDesTextView.setText(builder);

        //导航栏回退键的监听事件
        myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.init(R.drawable.back,
                this.getResources().getString(R.string.meet_question),
                btnClickListenerInterface
        );

        //问题列表
        questionList = (ListView) findViewById(R.id.question_list);
        questionList.setAdapter(new ListAdapter(itemsText));

        //处理条目的点击事件
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastItem >= 0) {
                    TextView ok =(TextView)itemViews[lastItem].findViewById(R.id.ok);
                    ok.setText("");
                    TextView question =(TextView)itemViews[lastItem].findViewById(R.id.question);
                    question.setTextColor(Color.BLACK);
                }
                    Log.d("view", "position=" + position + " id=" + id);
                    lastItem = (int) id;
                    TextView qusetionTextView = (TextView) view.findViewById(R.id.question);
                    qusetionTextView.setTextColor(Color.RED);
                    TextView okTextView = (TextView) view.findViewById(R.id.ok);
                    okTextView.setText("√");
                    switch(position){
                        case 0:final AlertDialog lostBikeDialog =new AlertDialog.Builder(MeetQuestion.this).create();
                               lostBikeDialog.show();
                               lostBikeDialog.getWindow().setContentView(R.layout.bike_lost_dialog);
                               lostBikeDialog.getWindow().findViewById(R.id.confirm_lost).setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       startActivity(new Intent(MeetQuestion.this,PaymentActivity.class));
                                       lostBikeDialog.cancel();
                                   }
                               });
                               lostBikeDialog.getWindow().findViewById(R.id.find_again).setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       lostBikeDialog.cancel();
                                   }
                               });break;
                        case 1:
                            final MyAlertDialog returnFailAlertDialog =new MyAlertDialog(MeetQuestion.this,getResources().getString(R.string.return_fail),getResources().getString(R.string.ok));
                               returnFailAlertDialog.show();
                               returnFailAlertDialog.setClicklistener(new MyAlertDialog.ClickListenerInterface() {
                                   @Override
                                   public void doConfirm() {
                                       startActivity(new Intent(MeetQuestion.this, PaymentActivity.class));
                                       returnFailAlertDialog.cancel();
                                   }
                               });break;
                        default:
                            final MyAlertDialog elseQuestionAlertDialog =new MyAlertDialog(MeetQuestion.this,getResources().getString(R.string.else_question),getResources().getString(R.string.ok));
                            elseQuestionAlertDialog.show();
                            elseQuestionAlertDialog.setClicklistener(new MyAlertDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {
                                    startActivity(new Intent(MeetQuestion.this,ChargeActivity.class));
                                    elseQuestionAlertDialog.cancel();
                                }
                            });

                    }
                    Log.d("view", okTextView.getText().toString());
                }
        });
    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            if (location != null) {
                geoPoint =new GeoPoint((int)(mlocation.getLatitude()*1000000),(int)(mlocation.getLongitude()*1000000));
                Log.d("loc","我的位置是"+geoPoint.toString());
                mkSearch.reverseGeocode(geoPoint);
                locationTextView.setText(addr);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    public void takePic() {
        //创建File对象，用于存储拍照后的图片
        //Environment.getExternalStorageDirectory()获取到的是手机SD卡的根目录
        long time = System.currentTimeMillis();
        File outputImage =new File("/storage/emulated/0/"+time+".jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        //这个URI对象标识这outputImage这张照片的唯一地址
        Uri imageUri =Uri.fromFile(outputImage);
        uri ="/storage/emulated/0/"+time+".jpg";
        Log.d("num",uri);
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        //指定图片的输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,TAKE_PHOTO);//启动相机程序
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
            if (resultCode == RESULT_OK) {
                Log.d("num", "result");
                Log.d("num",uri);
                boolean hasUpdate = false;
                if (!imagePathList.contains(uri)) {
                        Log.d("num", "没有这张");
                        //最多3张
                        if (imagePathList.size() == 3) {
                            Utility.showToast(this, "最多可添加3张图片。");
                            break;
                        }
                        Log.d("num", uri);
                        imagePathList.add(uri);
                        picNum = imagePathList.size();
                        hasUpdate = true;
                    }
                if (hasUpdate) {
                    adapter.notifyDataSetChanged();
                    Log.d("path", "update");
                }
            }
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("num","onNewIntent");
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        //添加，去重
        boolean hasUpdate = false;
        for (String path : paths) {
            if (!imagePathList.contains(path)) {
                //最多3张
                if (imagePathList.size() == 3) {
                    Utility.showToast(this, "最多可添加3张图片。");
                    break;
                }
                Log.d("num",path);
                imagePathList.add(path);
                picNum =imagePathList.size();
                hasUpdate = true;
            }
        }

        if (hasUpdate) {
            Log.d("path","update");
            adapter.notifyDataSetChanged();
        }
    }

    //点击的事件监听
    @Override
    public void onClick(View v) {
    }

    public class ListAdapter extends BaseAdapter{

        public ListAdapter(String []itemsText) {
            // TODO Auto-generated constructor stub
            itemViews = new View[itemsText.length];
            for(int i=0;i<itemsText.length;i++){
                String question=itemsText[i];    //获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i]=makeItemView(question);
            }
        }
        @Override
        public int getCount() {
            return itemViews.length;
        }

        @Override
        public Object getItem(int position) {
            return itemViews[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }
    }
    //绘制Item的函数
    private View makeItemView(String question) {
        LayoutInflater inflater = (LayoutInflater) MeetQuestion.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater.inflate(R.layout.list_item, null);

        // 通过findViewById()方法实例R.layout.item内各组件
        TextView content = (TextView) itemView.findViewById(R.id.question);
        content.setText(question);    //填入相应的值
        return itemView;
    }

    private class MyLocationListener implements com.baidu.mapapi.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            geoPoint =new GeoPoint((int)(mlocation.getLatitude()*1000000),(int)(mlocation.getLongitude()*1000000));
            Log.d("loc", "我的位置是" + geoPoint.toString());
            mkSearch.init(mapManager,new myMkSearchListener());
            mkSearch.reverseGeocode(geoPoint);
            locationTextView.setText(addr);
        }


    }
    private class myMkSearchListener implements MKSearchListener {
        @Override
        public void onGetPoiResult(MKPoiResult mkPoiResult, int i, int i1) {

        }

        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult mkTransitRouteResult, int i) {

        }

        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult mkDrivingRouteResult, int i) {

        }

        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult mkWalkingRouteResult, int i) {

        }

        @Override
        public void onGetAddrResult(MKAddrInfo mkAddrInfo, int i) {
           if(i==0){
               geoPoint =new GeoPoint((int)(mlocation.getLatitude()*1000000),(int)(mlocation.getLongitude()*1000000));
               Log.d("loc", "我的位置是" + geoPoint.toString());
               mkSearch.init(mapManager,new myMkSearchListener());
               mkSearch.reverseGeocode(geoPoint);
               locationTextView.setText(addr);
           }
        }

        @Override
        public void onGetBusDetailResult(MKBusLineResult mkBusLineResult, int i) {

        }

        @Override
        public void onGetSuggestionResult(MKSuggestionResult mkSuggestionResult, int i) {

        }
    }

    private class MyMKGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int i) {

        }

        @Override
        public void onGetPermissionState(int i) {

        }
    }
}
